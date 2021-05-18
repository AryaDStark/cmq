package com.ntu.cmqq.web;

import com.baomidou.mybatisplus.core.conditions.Condition;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ntu.cmqq.dto.*;
import com.ntu.cmqq.entity.*;
import com.ntu.cmqq.service.*;
import com.ntu.cmqq.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Arya
 */
@RestController
public class Controller {

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
                TeachDto teachDto = new TeachDto(teach.getId(),teach.getTeacherId(),courseService.getById(teach.getCourseId()),teach.getIsTop(),teach.getDescription(),null,stuTeachService.count(wrapper1));
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
        if (teachService.save(teach))return Result.ok();
        else return Result.fail().setMsg("add fail");
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
            if (signins.isEmpty())return Result.fail().setMsg("teachId="+teachId+"无signin");
           for (Signin signin:signins) {
               StuSignin stuSignin = new StuSignin();
               stuSignin.setIsSignin(false);
               stuSignin.setSigninId(signin.getId());
               stuSignin.setStudentId(stuId);
               stuSigninService.save(stuSignin);
           }
            //保存test
            List<Test> tests=testService.list(wrapper);
            if (tests.isEmpty()) return Result.fail().setMsg("teachId="+teachId+"无test");
            for (Test test:tests) {
                StuTest stuTest = new StuTest();
                stuTest.setTestId(test.getId());
                stuTest.setStudentId(stuId);
                stuTestService.save(stuTest);
            }
            //保存work
            List<Work> works = workService.list(wrapper);
            if (works.isEmpty()) return Result.fail().setMsg("teachId="+teachId+"无work");
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
        if(stuTeachService.remove(wrapper)) return Result.ok();
        else return Result.fail();
    }

    @GetMapping("/deleteTeach")
    public Result deleteTeach(@RequestParam int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        if(!teachService.removeById(teachId)) return Result.fail().setMsg("wrong teachId , teach del fail");
        if(!stuTeachService.remove(wrapper)) return Result.fail().setMsg("wrong teachId , stuTeach del fail");
         for (Test test:(List<Test>)testService.list(wrapper)){
             QueryWrapper wrapper1 = new QueryWrapper();
             wrapper.eq("testId",test.getId());
            if(!stuTestService.remove(wrapper1)) return Result.fail().setMsg("stuTest.testId="+test.getId()+"del fail");
            if(!meansService.remove(wrapper1)) return Result.fail().setMsg("mean.testId="+test.getId()+"del fail");
         }
         if(!testService.remove(wrapper)) return Result.fail().setMsg("test del fail");

         for (Work work:(List<Work>)workService.list(wrapper)){
             QueryWrapper wrapper1 = new QueryWrapper();
             wrapper.eq("workId",work.getId());
             if(!stuWorkService.remove(wrapper1)) return Result.fail().setMsg("stuWork.workId="+work.getId()+"del fail");
         }
         if(!workService.remove(wrapper)) return Result.fail().setMsg("work del fail id");

         for (Signin signin:(List<Signin>) signinService.list(wrapper)){
             QueryWrapper wrapper1 = new QueryWrapper();
             wrapper.eq("signinId",signin.getId());
             if(!stuSigninService.remove(wrapper1)) return Result.fail().setMsg("stuSignin.signinId="+signin.getId()+"del fail");
         }
         if(!signinService.remove(wrapper)) return Result.fail().setMsg("signIn del fail");
         if(!achieveService.remove(wrapper)) return Result.fail().setMsg("achieve del fail");
         if(!coursewareService.remove(wrapper)) return Result.fail().setMsg("courseware del fail");
         return Result.ok();
    }

    @PostMapping("/createSignin")
    public Result createSignIn(@RequestBody Signin signin){
        if (signinService.save(signin)) return Result.ok();
        else return Result.fail();
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
        wrapper.eq("signin_id",signinId);
        if(!stuSigninService.remove(wrapper1)) return Result.fail().setMsg("stuSignin del fail");
        return Result.ok();
    }

    @GetMapping("/getSignin")
    public Result getSignIn(@RequestParam int signinId,@RequestParam int studentId){
        QueryWrapper wrapper = new QueryWrapper();
        Signin signin = signinService.getById(signinId);
        wrapper.eq("signin_id",signinId);
        wrapper.eq("student_id",studentId);
        StuSignin stuSignin = stuSigninService.getOne(wrapper);
        return Result.ok().setData("signIn",signin).setData("stuSignIN",stuSignin);
    }

    @GetMapping("/getSigninTeacher")
    public Result tGetSignIn(@RequestParam int signinId){
        SignInDto signInDto = new SignInDto();
        Signin signin = signinService.getById(signinId);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("signin_id",signin);
        List<StuSignin> stuSignins = stuSigninService.list(wrapper);
        signInDto.setId(signinId);
        signInDto.setPre(signin.getPre());
        signInDto.setTeachId(signin.getTeachId());
        signInDto.setStartTime(signin.getStartTime());
        signInDto.setStatus(signin.getStatus());
        signInDto.setStuSignins(stuSignins);
        return Result.ok().setData("signIn",signInDto);
    }

    @PostMapping("/createTest")
    public Result createTest(@RequestBody TestDto testDto){
        Test test = new Test();
        test.setTeachId(testDto.getTeachId());
        test.setTime(testDto.getTime());
        test.setName(testDto.getName());
        test.setContent(String.join("//", testDto.getContent()));
        if (testService.save(test)) return Result.ok();
        else return Result.fail();
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
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("test_id",test.getId());
            wrapper1.eq("student_id",studentId);
            stuTestDto.setStuTest(stuTestService.getOne(wrapper));
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
            StuDto stuDto = new StuDto(student.getId(),student.getNickname(),student.getSchool(),student.getDescription(),student.getStatus(),stuTestService.getOne(wrapper1));
            stuDtos.add(stuDto);
        }
        return Result.ok().setData("students",stuDtos);
    }

    @GetMapping("/getTestDetail")
    public Result getTestDetail(@RequestParam int testId,@RequestParam int studentId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("test_id",testId);
        wrapper.eq("student_id",studentId);
        return Result.ok().setData("test",testService.getById(testId)).setData("stuTest",stuTestService.getOne(wrapper));
    }

    @PostMapping("/giveTestScore")
    public Result giveTestScore(@RequestBody StuTest stuTestF){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("student_id",stuTestF.getStudentId());
        wrapper.eq("test_id",stuTestF.getTestId());
        StuTest stuTest = stuTestService.getOne(wrapper);
        stuTest.setScore(stuTestF.getScore());
        if (stuTestService.updateById(stuTest)) return Result.ok();
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
    public Result createWork(@RequestBody WorkDto workDto){
        Work work = new Work();
        work.setTeachId(workDto.getTeachId());
        work.setContent(String.join("//",workDto.getContent()));
        work.setName(workDto.getName());
        workService.save(work);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",workDto.getTeachId());
        for (StuTeach stuTeach:(List<StuTeach>)stuTeachService.list(wrapper)){
            StuWork stuWork = new StuWork();
            stuWork.setWorkId(work.getId());
            stuWork.setStudentId(stuTeach.getStudentId());
            stuWorkService.save(stuWork);
        }
        return Result.ok();
    }

    @PostMapping("/changeWork")
    public Result changeWork(@RequestBody WorkDto workDto){
        Work work = new Work();
        work.setId(workDto.getId());
        work.setName(workDto.getName());
        work.setContent(String.join("//",workDto.getContent()));
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
            workDto.setContent(work.getContent().split("//"));
            workDtos.add(workDto);
        }
        return Result.ok().setData("works",workDtos);
    }

    @GetMapping("/getStudentWorkDetailList")
    public Result getStuWorkDetail(@RequestParam int teachId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teach_id",teachId);
        List<Student> students = new ArrayList<>();
        for (StuTeach stuTeach:(List<StuTeach>)stuTeachService.list(wrapper)){
            Student student = studentService.getById(stuTeach.getStudentId());
            student.setPassword("");
            students.add(student);
        }
        return Result.ok().setData("students",students);
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

//    @PostMapping()

}

