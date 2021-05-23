package com.ntu.cmqq.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ntu.cmqq.dto.*;
import com.ntu.cmqq.entity.*;
import com.ntu.cmqq.service.*;
import com.ntu.cmqq.util.Result;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Arya
 */
@RestController
public class Controller {

    @Value("${path.filePath}")
    private String path;
    @Autowired
    TeachService teachService;
    @Autowired
    CourseService courseService;
    @Autowired
    StuTeachService stuTeachService;
    @Autowired
    SigninService signinService;
    @Autowired
    StuSigninService stuSigninService;
    @Autowired
    TestService testService;
    @Autowired
    StuTestService stuTestService;
    @Autowired
    WorkService workService;
    @Autowired
    StuWorkService stuWorkService;
    @Autowired
    MeansService meansService;
    @Autowired
    CoursewareService coursewareService;
    @Autowired
    AchieveService achieveService;
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;


    @GetMapping("/getTeach")
    public Result getTeach(@RequestParam int id,@RequestParam int status){
        if (status==0){//老师
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("teacher_id",id);
            List<Teach> teachList = teachService.list(wrapper);
            List<TeachDto> teachDtos = new ArrayList<>();
            for (Teach teach:teachList){
                QueryWrapper wrapper1 = new QueryWrapper();
                wrapper1.eq("teach_id",teach.getId());
                TeachDto teachDto = new TeachDto(teach.getId(),teach.getTeacherId(),courseService.getById(teach.getCourseId()),teach.getIsTop(),teach.getDescription(),null,stuTeachService.count(wrapper1));
                teachDtos.add(teachDto);
            }
            return Result.ok().setData("teaches",teachDtos);
        }
        if (status==1){//学生
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("student_id",id);
            List<StuTeach> stuTeaches = stuTeachService.list(wrapper);
            List<TeachDto> teachDtos = new ArrayList<>();
            for (StuTeach stuTeach:stuTeaches){
                QueryWrapper wrapper1 = new QueryWrapper();
                wrapper1.eq("teach_id",stuTeach.getTeachId());
                Teach teach = teachService.getById(stuTeach.getTeachId());
                Teacher teacher = teacherService.getById(teach.getTeacherId());
                teacher.setPassword("");
                TeachDto teachDto = new TeachDto(teach.getId(),teach.getTeacherId(),courseService.getById(teach.getCourseId()),stuTeach.getIsTop(),teach.getDescription(),teacher,stuTeachService.count(wrapper1));
                teachDtos.add(teachDto);
            }
            return Result.ok().setData("teaches",teachDtos);
        }
        else return Result.fail().setMsg("wrong status");
    }

    @GetMapping("/getALLTeach")
    public Result getAllTeach(){
        List<TeachDto> teachDtos = new ArrayList<>();
        for (Teach teach:(List<Teach>)teachService.list(null)){
            Teacher teacher = teacherService.getById(teach.getTeacherId());
            teacher.setPassword("");
            TeachDto teachDto = new TeachDto(teach.getId(),teach.getTeacherId(),courseService.getById(teach.getCourseId()),teach.getIsTop(),teach.getDescription(),teacher,0);
            teachDtos.add(teachDto);
        }
        return Result.ok().setData("teaches",teachDtos);
    }

    @PostMapping("/createTeach")
    public Result createTeach(@RequestBody Teach teachF){
        Teach teach = new Teach();
        teach.setCourseId(teachF.getCourseId());
        teach.setDescription(teachF.getDescription());
        teach.setTeacherId(teachF.getTeacherId());
        teach.setIsTop(false);
        if (!teachService.save(teach))return Result.fail();
        Achieve achieve = new Achieve();
        achieve.setTeachId(teach.getId());
        achieve.setWork("0.5");
        achieve.setTest("0.5");
        if (!achieveService.save(achieve)) return Result.fail();
        else return Result.ok();
    }

    @GetMapping("/getAllCourse")
    public Result getAllCourse(){
        return Result.ok().setData("courses",courseService.list(null));
    }

    @PostMapping("/joinTeach")
    public Result joinTeach(@RequestBody JoinTeach joinTeach){
        int stuId = joinTeach.getStudentId();

        for (int teachId:Arrays.asList(joinTeach.getTeachIds())){
            System.out.println(teachId);
           //保存teach
            StuTeach stuTeach = new StuTeach();
            stuTeach.setStudentId(stuId);
            stuTeach.setTeachId(teachId);
            stuTeach.setIsTop(false);
            stuTeachService.save(stuTeach);

            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("teach_id",teachId);
            //保存signin
            List<Signin> signins = signinService.list(wrapper);
           for (Signin signin:signins) {
               StuSignin stuSignin = new StuSignin();
               stuSignin.setIsSignin(false);
               stuSignin.setSigninId(signin.getId());
               stuSignin.setStudentId(stuId);
               stuSigninService.save(stuSignin);
           }
            //保存test
            List<Test> tests=testService.list(wrapper);
            for (Test test:tests) {
                StuTest stuTest = new StuTest();
                stuTest.setTestId(test.getId());
                stuTest.setStudentId(stuId);
                stuTestService.save(stuTest);
            }
            //保存work
            List<Work> works = workService.list(wrapper);
            for (Work work:works) {
                StuWork stuWork = new StuWork();
                stuWork.setStudentId(stuId);
                stuWork.setWorkId(work.getId());
                stuWork.setScore(0);
                stuWorkService.save(stuWork);
            }
        }
        return Result.ok();
    }

    @PostMapping("/setTeachTop")
    public Result setTeachTop(@RequestBody TeachTop teachTop){
        if (teachTop.getStatus()==0) {//老师
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("id",teachTop.getTeachId());
            Teach teach = teachService.getById(teachTop.getTeachId());
            teach.setIsTop(teachTop.getIsTop());
           if(teachService.update(teach,wrapper)) return Result.ok();
           else return Result.fail().setMsg("teacher update fail");
        }
        if (teachTop.getStatus()==1){//学生
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("student_id",teachTop.getId());
            wrapper.eq("teach_id",teachTop.getTeachId());
            StuTeach stuTeach = stuTeachService.getOne(wrapper);
            stuTeach.setIsTop(teachTop.getIsTop());
            if (stuTeachService.update(stuTeach,wrapper))return Result.ok();
            else return Result.fail().setMsg("stu update fail");
        }
        else return Result.fail().setMsg("wrong status");
        }

    @GetMapping("/outTeach")
    public Result outTeach(@RequestParam int studentId,@RequestParam int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("student_id",studentId);
        wrapper.eq("teach_id",teachId);
        if(!stuTeachService.remove(wrapper)) return Result.fail();
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("teach_id",teachId);
        for (Signin signin:(List<Signin>)signinService.list(wrapper1)){
            QueryWrapper wrapper2 = new QueryWrapper();
            wrapper2.eq("signin_id",signin.getId());
            wrapper2.eq("student_id",studentId);
            stuSigninService.remove(wrapper2);
        }
        for (Test test:(List<Test>)testService.list(wrapper1)){
            QueryWrapper wrapper2 = new QueryWrapper();
            wrapper2.eq("test_id",test.getId());
            wrapper2.eq("student_id",studentId);
            stuTestService.remove(wrapper2);
        }
        for (Work work:(List<Work>)workService.list(wrapper1)){
            QueryWrapper wrapper2 = new QueryWrapper();
            wrapper2.eq("work_id",work.getId());
            wrapper2.eq("student_id",studentId);
            stuWorkService.remove(wrapper2);
        }
        return Result.ok();

    }

    @GetMapping("/deleteTeach")
    public Result deleteTeach(@RequestParam int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        teachService.removeById(teachId);
        stuTeachService.remove(wrapper);
         for (Test test:(List<Test>)testService.list(wrapper)){
             QueryWrapper wrapper1 = new QueryWrapper();
             wrapper1.eq("test_id",test.getId());
            stuTestService.remove(wrapper1);
            meansService.remove(wrapper1);
         }
         testService.remove(wrapper);

         for (Work work:(List<Work>)workService.list(wrapper)){
             QueryWrapper wrapper1 = new QueryWrapper();
             wrapper1.eq("work_id",work.getId());
             stuWorkService.remove(wrapper1);
         }
         workService.remove(wrapper);

         for (Signin signin:(List<Signin>) signinService.list(wrapper)){
             QueryWrapper wrapper1 = new QueryWrapper();
             wrapper1.eq("signin_id",signin.getId());
             stuSigninService.remove(wrapper1);
         }
        signinService.remove(wrapper);
        achieveService.remove(wrapper);
        coursewareService.remove(wrapper);
         return Result.ok();
    }

    @PostMapping("/createSignin")
    public Result createSignIn(@RequestBody Signin signin){
        if(!signinService.save(signin)) return Result.fail();
        int signId = signin.getId();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",signin.getTeachId());
         for (StuTeach stuTeach:(List<StuTeach>)stuTeachService.list(wrapper)){
             StuSignin stuSignin = new StuSignin();
             stuSignin.setSigninId(signId);
             stuSignin.setStudentId(stuTeach.getStudentId());
             stuSignin.setIsSignin(false);
             stuSigninService.save(stuSignin);
         }
         return Result.ok();
    }

    @GetMapping("/joinSignin")
    public Result jionSignIn(@RequestParam int studentId,@RequestParam int signinId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("student_id",studentId);
        wrapper.eq("signin_id",signinId);
        StuSignin stuSignin = stuSigninService.getOne(wrapper);
        stuSignin.setIsSignin(true);
        if (stuSigninService.update(stuSignin,wrapper))return Result.ok();
        else return Result.fail();
    }

    @GetMapping("/deleteSignin")
    public Result delSignIn(int signinId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",signinId);
        if(!signinService.remove(wrapper)) return Result.fail().setMsg("signin del fail");
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("signin_id",signinId);
        if(!stuSigninService.remove(wrapper1)) return Result.fail().setMsg("stuSignin del fail");
        return Result.ok();
    }

    @GetMapping("/getSignin")
    public Result getSignIn(@RequestParam int teachId,@RequestParam int studentId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        wrapper.orderByDesc("start_time");
        List<Signin> signins = signinService.list(wrapper);
        List<StuSignInDto> stuSignInDtos = new ArrayList<>();
        for (Signin signin:signins){
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("student_id",studentId);
            wrapper1.eq("signin_id",signin.getId());
            StuSignInDto stuSignInDto = new StuSignInDto(signin.getId(),signin.getPre(),signin.getTeachId(),signin.getStartTime(),signin.getDuringTime(),signin.getStatus(),stuSigninService.getOne(wrapper1));
            stuSignInDtos.add(stuSignInDto);
        }
        return Result.ok().setData("signIns",stuSignInDtos);
    }

    @GetMapping("/getSigninList")
    public  Result getSignInResult(@RequestParam int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        wrapper.orderByDesc("start_time");
        List<Signin> signins = signinService.list(wrapper);
        return Result.ok().setData("signIns",signins);
    }

    @GetMapping("/getSigninTeacher")
    public Result tGetSignIn(@RequestParam int signinId){
        SignInDto signInDto = new SignInDto();
        Signin signin = signinService.getById(signinId);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("signin_id",signinId);
        List<StuSignin> stuSignins = stuSigninService.list(wrapper);
        List<StuSignDto> stuSignDtos = new ArrayList<>();
        for (StuSignin stuSignin:stuSignins){
            StuSignDto stuSignDto = new StuSignDto(stuSignin.getStudentId(),stuSignin.getSigninId(),stuSignin.getIsSignin(),studentService.getById(stuSignin.getStudentId()).getNickname());
            stuSignDtos.add(stuSignDto);
        }
        signInDto.setId(signinId);
        signInDto.setPre(signin.getPre());
        signInDto.setTeachId(signin.getTeachId());
        signInDto.setStartTime(signin.getStartTime());
        signInDto.setStatus(signin.getStatus());
        signInDto.setStuSignDtos(stuSignDtos);
        return Result.ok().setData("signIn",signInDto);
    }

    @PostMapping("/createTest")
    public Result createTest(@RequestBody TestDto testDto){
        Test test = new Test();
        test.setTeachId(testDto.getTeachId());
        test.setTime(testDto.getTime());
        test.setName(testDto.getName());
        test.setContent(String.join("//", testDto.getContent()));
        if (!testService.save(test)) return Result.fail();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",testDto.getTeachId());
        for (StuTeach stuTeach:(List<StuTeach>)stuTeachService.list(wrapper)){
            StuTest stuTest = new StuTest();
            stuTest.setTestId(test.getId());
            stuTest.setStudentId(stuTeach.getStudentId());
            if (!stuTestService.save(stuTest)) return Result.fail();
        }
        return Result.ok();
    }

    @PostMapping("/changeTest")
    public Result changeTest(@RequestBody TestDto testDto){
        Test test = testService.getById(testDto.getId());
        test.setTime(testDto.getTime());
        test.setName(testDto.getName());
        test.setContent(String.join("//", testDto.getContent()));
        if (testService.updateById(test)) return Result.ok();
        else return Result.fail();
    }

    @GetMapping("/getTest")
    public Result getTest(int testId){
        TestDto testDto = new TestDto();
        Test test = testService.getById(testId);
        testDto.setId(testId);
        testDto.setName(test.getName());
        testDto.setTeachId(test.getTeachId());
        testDto.setTime(test.getTime());
        testDto.setContent(test.getContent().split("//"));
        return Result.ok().setData("test",testDto);
    }

    @GetMapping("/getStudentTestList")
    public Result gStuTest(@RequestParam int teachId,@RequestParam int studentId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        List<Test> tests = testService.list(wrapper);
        List<StuTestDto> stuTestDtos = new ArrayList<>();
        for (Test test:tests){
            StuTestDto stuTestDto = new StuTestDto();
            stuTestDto.setId(test.getId());
            stuTestDto.setTeachId(teachId);
            stuTestDto.setTime(test.getTime());
            stuTestDto.setName(test.getName());
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("test_id",test.getId());
            wrapper1.eq("student_id",studentId);
            stuTestDto.setStuTest(stuTestService.getOne(wrapper1));
            stuTestDtos.add(stuTestDto);
        }
        return Result.ok().setData("tests",stuTestDtos);
    }

    @GetMapping("/getTeacherTestList")
    public Result gTeacherTests(@RequestParam int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        return Result.ok().setData("tests",testService.list(wrapper));
    }

    @GetMapping("/getStudentTestDetailList")
    public Result getStuTest(@RequestParam int teachId,@RequestParam int testId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        Student student;
        List<StuDto> stuDtos = new ArrayList<>();
        for (StuTeach stuTeach:(List<StuTeach>)stuTeachService.list(wrapper)){
            student = studentService.getById(stuTeach.getStudentId());
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("test_id",testId);
            wrapper1.eq("student_id",stuTeach.getStudentId());
            StuDto stuDto = new StuDto(student.getId(),student.getNickname(),student.getSchool(),student.getDescription(),student.getStatus(),stuTestService.getOne(wrapper1),null);
            stuDtos.add(stuDto);
        }
        return Result.ok().setData("students",stuDtos);
    }

    @GetMapping("/getTestDetail")
    public Result getTestDetail(@RequestParam int testId,@RequestParam int studentId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("test_id",testId);
        wrapper.eq("student_id",studentId);
        Test test = testService.getById(testId);
        TestDto testDto = new TestDto(test.getId(),test.getTeachId(),test.getTime(),test.getName(),test.getContent().split("//"));
        StuTest stuTest = stuTestService.getOne(wrapper);
        TestStuDto testStuDto =null;
        if (stuTest.getContent()!=null) {
             testStuDto = new TestStuDto(stuTest.getTestId(), stuTest.getStudentId(), stuTest.getScore(), stuTest.getContent().split("//"), stuTest.getIsPush());
        }
        else {
             testStuDto = new TestStuDto(stuTest.getTestId(), stuTest.getStudentId(), stuTest.getScore(), null, stuTest.getIsPush());
        }
        return Result.ok().setData("test",testDto).setData("stuTest",testStuDto);
    }

    @PostMapping("/giveTestScore")
    public Result giveTestScore(@RequestBody StuTest stuTestF){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("student_id",stuTestF.getStudentId());
        wrapper.eq("test_id",stuTestF.getTestId());
        StuTest stuTest = stuTestService.getOne(wrapper);
        stuTest.setScore(stuTestF.getScore());
        if (stuTestService.update(stuTest,wrapper)) return Result.ok();
        else return Result.fail();
    }

    @PostMapping("/giveStudentTest")
    public Result giveStudentTest(@RequestBody TestStuDto testStuDto){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("test_id",testStuDto.getTestId());
        wrapper.eq("student_id",testStuDto.getStudentId());
        StuTest stuTestTemp = stuTestService.getOne(wrapper);
        StuTest stuTest = new StuTest();
        stuTest.setTestId(testStuDto.getTestId());
        stuTest.setStudentId(testStuDto.getStudentId());
        stuTest.setScore(stuTestTemp.getScore());
        stuTest.setIsPush(true);
        stuTest.setContent(String.join("//",testStuDto.getContent()));
        if (stuTestService.update(stuTest,wrapper)) return Result.ok();
        else return Result.fail();
    }

    @GetMapping("/deleteTest")
    public Result delTest(int testId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("test_id",testId);
        if (stuTestService.remove(wrapper)&&testService.removeById(testId)) return Result.ok();
        else return Result.fail();
    }

    @PostMapping("/createWork")
    public Result createWork(@RequestBody Work workF){
        Work work = new Work();
        work.setTeachId(workF.getTeachId());
        work.setContent(workF.getContent());
        work.setName(workF.getName());
        workService.save(work);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",workF.getTeachId());
        for (StuTeach stuTeach:(List<StuTeach>)stuTeachService.list(wrapper)){
            StuWork stuWork = new StuWork();
            stuWork.setWorkId(work.getId());
            stuWork.setStudentId(stuTeach.getStudentId());
            stuWorkService.save(stuWork);
        }
        return Result.ok();
    }

    @PostMapping("/changeWork")
    public Result changeWork(@RequestBody Work workF){
        Work work = new Work();
        work.setId(workF.getId());
        work.setName(workF.getName());
        work.setContent(workF.getContent());
        work.setTeachId(workService.getById(workF.getId()).getTeachId());
        if (workService.updateById(work)) return Result.ok();
        else return Result.fail();

    }

    @GetMapping("/getStudentWorkList")
    public Result getStuWork(@RequestParam int teachId,@RequestParam int studentId){
        List<WorkDto> workDtos = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        for (Work work:(List<Work>)workService.list(wrapper)){
            WorkDto workDto = new WorkDto();
            workDto.setId(work.getId());
            workDto.setTeachId(teachId);
            workDto.setName(work.getName());
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("work_id",work.getId());
            wrapper1.eq("student_id",studentId);
            workDto.setStuWork(stuWorkService.getOne(wrapper1));
            workDto.setContent(work.getContent());
            workDtos.add(workDto);
        }
        return Result.ok().setData("works",workDtos);
    }

    @GetMapping("/getStudentWorkDetailList")
    public Result getStuWorkDetail(@RequestParam int teachId,@RequestParam int workId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        List<StuDto> stuDtos = new ArrayList<>();
        for (StuTeach stuTeach:(List<StuTeach>)stuTeachService.list(wrapper)){
            Student student = studentService.getById(stuTeach.getStudentId());
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("student_id",student.getId());
            wrapper1.eq("work_id",workId);
            StuDto stuDto = new StuDto(student.getId(),student.getNickname(),student.getSchool(),student.getDescription(),student.getStatus(),null,stuWorkService.getOne(wrapper1));
            stuDtos.add(stuDto);
        }
        return Result.ok().setData("students",stuDtos);
    }

    @GetMapping("/deleteWork")
    public Result delWork(@RequestParam int workId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("work_id",workId);
        if (workService.removeById(workId)&&stuWorkService.remove(wrapper)) return Result.ok();
        else return Result.fail();
    }

    @GetMapping("/getTeacherWorkList")
    public Result getTeacherWorks(@RequestParam int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        return Result.ok().setData("works",workService.list(wrapper));
    }

    @PostMapping("/giveWorkScore")
    public Result giveScore(@RequestBody StuWork stuWork){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("student_id",stuWork.getStudentId());
        wrapper.eq("work_id",stuWork.getWorkId());
        if(stuWorkService.update(stuWork,wrapper)) return Result.ok();
        else  return Result.fail();
    }

    @PostMapping("/updateMeans")
    public Result upMeans(@RequestParam MultipartFile file,@RequestParam String fName,@RequestParam int teachId){
        if (file.isEmpty())return Result.fail().setMsg("file wrong");
        String fileName = file.getOriginalFilename();
       //存储路径
        String address = path+"\\"+fileName;
        File dest = new File(address);
        //如果目录不存在则创建
        if (!dest.getParentFile().exists()) dest.getParentFile().mkdir();
        try {
            file.transferTo(dest);
            Means means = new Means();
            means.setName(fName);
            means.setTeachId(teachId);
            means.setAddress(address);
            if (meansService.save(means)) return Result.ok();
            else return Result.fail().setMsg("save fail");

        }catch (IOException e){
            e.printStackTrace();
            return Result.fail().setMsg("up fail");
        }

    }

    @GetMapping("/downloadMeans")
    public Result download(int id, HttpServletResponse response)throws Exception{
        Means means = meansService.getById(id);
        if (means==null) return Result.fail().setMsg("wrong id");
        String address = means.getAddress();
        File file = new File(address);
        if (file.exists()){
            IOUtils.copy(FileUtils.openInputStream(file),response.getOutputStream());
            return Result.ok();
        }else return Result.fail().setMsg("file丢失");
    }

    @GetMapping("/getMeanList")
    public Result getMeans(int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        return Result.ok().setData("means",meansService.list(wrapper));
    }

    @GetMapping("/deleteMeans")
    public Result delMeans(int id){
        if (meansService.removeById(id)) return Result.ok();
        else return Result.fail();
    }

    @PostMapping("/createCourseware")
    public Result createCourseware(@RequestBody Courseware courseware){
        if (coursewareService.save(courseware)) return Result.ok();
        else return  Result.fail();
    }

    @GetMapping("/getCoursewareList")
    public Result getCourseList(int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        return Result.ok().setData("coursewares",coursewareService.list(wrapper));
    }

    @GetMapping("/deleteCourseware")
    public Result delCourseware(int id){
        if(coursewareService.removeById(id)) return Result.ok();
        else return Result.fail();
    }

    @PostMapping("/changeCourseware")
    public Result changeCourseware(@RequestBody Courseware courseware){
       if(coursewareService.updateById(courseware)) return Result.ok() ;
       else return Result.fail();
    }

    @GetMapping("/getAchieve")
    public Result getAchieve(int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        return Result.ok().setData("achieve",achieveService.getOne(wrapper));
    }

    @PostMapping("/changeAchieve")
    public Result changeAchieve(@RequestBody Achieve achieveF){
        Achieve achieve = achieveService.getById(achieveF.getId());
        achieve.setTest(achieveF.getTest());
        achieve.setWork(achieveF.getWork());
        if (achieveService.updateById(achieve)) return Result.ok();
        else return Result.fail();
    }

    @GetMapping("/getScore")
    public Result getScore(@RequestParam int teachId,@RequestParam int studentId){
        double testSum = 0;
        double testScore = 0;
        double workSum = 0;
        double workScore = 0;
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        List<Test> tests = testService.list(wrapper);
        if (!tests.isEmpty()){
        for (Test test:tests){
          QueryWrapper wrapper1 = new QueryWrapper();
          wrapper1.eq("test_id",test.getId());
          wrapper1.eq("student_id",studentId);
          Integer score =stuTestService.getOne(wrapper1).getScore();
          if (null==score) score=0;
          testSum = testSum + score;
        }
        testScore = testSum/(tests.size());
        }
        List<Work> works = workService.list(wrapper);
        if (!works.isEmpty()) {
            for (Work work : works) {
                QueryWrapper wrapper1 = new QueryWrapper();
                wrapper1.eq("work_id", work.getId());
                wrapper1.eq("student_id", studentId);
                Integer score =stuWorkService.getOne(wrapper1).getScore();
                if (null==score) score=0;
                workSum = workSum + score;
            }
            workScore = workSum/(works.size());
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("teach_id",teachId);
        Achieve achieve = achieveService.getOne(wrapper);
        double testPercent = Double.parseDouble(achieve.getTest());
        double workPercent = Double.parseDouble(achieve.getWork());
        return Result.ok().setData("score",testScore*testPercent+workScore*workPercent);
    }


}

