package com.ntu.cmq.web;

import com.ntu.cmq.model.*;
import com.ntu.cmq.model.dto.*;
import com.ntu.cmq.service.*;
import com.sun.deploy.net.HttpResponse;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cmq
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
    UserService userService;
    @Autowired
    SignInService signInService;
    @Autowired
    TestService testService;
    @Autowired
    StuTestService stuTestService;
    @Autowired
    MeanService meanService;
    @Autowired
    WorkService workService;
    @Autowired
    StuWorkService stuWorkService;
    @Autowired
    CoursewareService coursewareService;

    /**
     * 通过id获取teach
     * */
    @GetMapping("/teach")
    public Result getCourse(Long id){
        Teach teach = teachService.getById(id);
        if (null==teach) return Result.fail().setMsg("teach不存在");
        Course course = courseService.getById(teach.getCourseId());
        if (null!=teach&&null!=course){return Result.ok().setMsg("成功").setData("teachId",id).setData("course",course);}
        else {return Result.fail().setMsg("失败");}
    }

    @GetMapping("/delTeach/{id}")
    public Result delTeach(@PathVariable Long id){
        if (teachService.delTeach(id)>0) return Result.ok();
        else return Result.fail().setMsg("删除失败");
    }

    @GetMapping("/course/content")
    public Result getContent(Long id){
        Course course = courseService.getById(id);
        if (null!=course){ return Result.ok().setMsg("成功").setData("content",course.getContent());}
        else {return Result.fail().setMsg("失败");}
    }

    @PostMapping("/course/add")
    public Result addCourse(@RequestParam Long teacherId,@RequestParam Long courseId){
        Teach teach = new Teach();
        teach.setCourseId(courseId);
        teach.setTeacherId(teacherId);
        if (teachService.insertTeach(teach)>0){return Result.ok().setMsg("成功");}
        else {return Result.fail().setMsg("失败");}
    }

    @GetMapping("/course/allCourses")
    public Result showCourses(){
        List<Teach> teaches = teachService.getAllTeach();
        List<TeachDto> teachDtos = new ArrayList<>();
       try {
           for (Teach teach: teaches){
               TeachDto teachDto = new TeachDto();
               teachDto.setId(teach.getId());
               teachDto.setTeacherName(userService.getById(teach.getTeacherId()).getName());
               teachDto.setCourseName(courseService.getById(teach.getCourseId()).getName());
               teachDtos.add(teachDto);
           }
           return Result.ok().setMsg("成功").setData("teachList",teachDtos);
       }catch (Exception e){
           e.printStackTrace();
           return Result.fail().setMsg("失败");
       }
    }

    @GetMapping("/teach/add")
    public Result addStu(@RequestParam Long teachId,@RequestParam Long userId){
        Teach teach = teachService.getById(teachId);
        User user = userService.getById(userId);
        if (null == teach){return Result.fail().setMsg("teachId不存在");}
        if (null == user){return Result.fail().setMsg("userId不存在");}
        String stuIds = teach.getStudentIds();
        if ( stuIds == "" || stuIds==null){ teach.setStudentIds(userId.toString()); }
        else { teach.setStudentIds( teach.getStudentIds()+","+userId);}
        String teachIds = user.getTeachIds();
        if (teachIds == "" || teachIds==null ){ user.setTeachIds(teachId.toString());}
        else {user.setTeachIds(user.getTeachIds()+","+teachId);}
        if ( teachService.updateTeach(teach)>0 && userService.updateUser(user)>0){return Result.ok().setMsg("添加成功");}
        else {return Result.fail().setMsg("添加失败");}
    }

    @GetMapping("/teach/top")
    public Result top(@RequestParam Long teachId,@RequestParam Long userId){
        User user = userService.getById(userId);
        if (null==user) return Result.fail().setMsg("userId不存在");
        String techIds = user.getTeachIds();
        if (techIds==null || techIds==""){ user.setTeachIds(userId.toString());}
        else { user.setTeachIds(user.getTeachIds()+","+userId);}
        if (userService.updateUser(user)>0){ return Result.ok().setMsg("成功");}
        else { return Result.fail().setMsg("失败");}
    }

    @GetMapping("/signIn/add")
    public Result signInAdd(SignIn signIn){
        SignIn newSignIn = new SignIn();
        newSignIn.setPre(signIn.getPre());
        newSignIn.setTeachId(signIn.getTeachId());
        newSignIn.setStartTime(signIn.getStartTime());
        newSignIn.setDuringTime(signIn.getDuringTime());
        newSignIn.setStatus(signIn.getStatus());
        if (signInService.insertSignIn(newSignIn)>0){return Result.ok().setMsg("成功");}
        else { return Result.fail().setMsg("失败");}
    }

    @GetMapping("/signIn/signIn")
    public Result signIn(@RequestParam Long signInId,@RequestParam Long studentId){
        SignIn signIn = signInService.getById(signInId);
        if (signIn==null)return Result.fail().setMsg("签到不存在");
        String stuIds = signIn.getStudentIds();
        if (stuIds==null || stuIds==""){ signIn.setStudentIds(studentId.toString());}
         else {signIn.setStudentIds(signIn.getStudentIds()+","+studentId);}
         if (signInService.updateSignIn(signIn)>0)return Result.ok().setMsg("成功");
         else return Result.fail().setMsg("失败");
    }

    @GetMapping("/signIn/get")
    public Result signInGet(@RequestParam Long studentId,@RequestParam Long teachId){
        List<SignInDto> signInDtos = signInService.getByTeach(teachId);
        for (SignInDto signInDto:signInDtos){
            if (signInDto.getStudentIds()!=null) {
                String[] stuStrs = signInDto.getStudentIds().split(",");
                for (String stuStr : stuStrs) {
                    if (studentId == Long.parseLong(stuStr)) {
                        signInDto.setInStuIds(1);
                        break;
                    } else {
                        signInDto.setInStuIds(0);
                    }
                }
            }else {
                signInDto.setInStuIds(0);
            }
        }
        return Result.ok().setMsg("").setData("signIns",signInDtos);
    }

    @GetMapping("/delSignIn")
    public Result delSignIn(Long id){
        if(signInService.delSignIn(id)>0)
        return Result.ok();
        else return Result.fail().setMsg("删除失败");
    }

    @GetMapping("/getTest")
    public Result getTest(@RequestParam Long teachId,@RequestParam Long studentId){
        List<Test> testsT = testService.getByTeach(teachId);
        List<TestDto> tests1 = new ArrayList<>();
        for (Test test:testsT){
            TestDto testDto = new TestDto();
            testDto.setId(test.getId());
            testDto.setTeachId(teachId);
            testDto.setTime(test.getTime());
            if (testDto.getContent()!=null)testDto.setContent(test.getContent().split(","));
            tests1.add(testDto);
        }
        List<Test> testsTs= testService.getByTeachAndStu(teachId,studentId);
        List<TestDto> tests2 = new ArrayList<>();
        for (Test test:testsTs){
            TestDto testDto = new TestDto();
            testDto.setId(test.getId());
            testDto.setTeachId(teachId);
            testDto.setTime(test.getTime());
            if (testDto.getContent()!=null)  testDto.setContent(test.getContent().split(","));
            tests2.add(testDto);
        }
         return Result.ok().setMsg("ok").setData("testsByTeach",tests1).setData("testsByTeachAndStu",tests2);
    }

    @GetMapping("/addTest")
    public Result addTest(TestDto testDto){
        Test test = new Test();
        test.setTeachId(testDto.getTeachId());
        test.setTime(testDto.getTime());
        test.setContent(String.join(",",testDto.getContent()));
        if (testService.addTest(test)>0) return Result.ok();
        else return Result.fail().setMsg("add fail");
    }

    @GetMapping("/doTest")
    public Result doTest(Long testId){
        Test test = testService.getById(testId);
        return Result.ok().setMsg("ok").setData("test",test);
    }

    @GetMapping("/finishTest")
    public Result finishTest(StuTestDto stuTestDto){
        StuTest stuTest = new StuTest();
        stuTest.setTestId(stuTestDto.getTestId());
        stuTest.setStudentId(stuTestDto.getStuId());
        stuTest.setTeachId(stuTestDto.getTeachId());
        if (stuTestDto.getContent()!=null)stuTest.setContent(String.join(",",stuTestDto.getContent()));
        if(stuTestService.addStuTest(stuTest)>0) return Result.ok();
        else return Result.fail().setMsg("fail add");
    }

    @GetMapping("/score")
    public Result score(@RequestParam Long id,@RequestParam int score){
        if (stuTestService.updateScore(id,score)>0) return Result.ok();
        else return Result.fail().setMsg("fail score");
    }

    @GetMapping("/updateTest")
    public Result changeTest(TestDto testDto){
        Test test = testService.getById(testDto.getId());
        test.setContent(String.join(",",testDto.getContent()));
        if (testService.updateTest(test)>0)return Result.ok();
        else return Result.fail().setMsg("fail change");
    }

    @GetMapping("/delTest")
    public Result delTest(Long id){
        if (testService.delTest(id)>0)return Result.ok();
        else return Result.fail().setMsg("fail del");
    }

    @GetMapping("/getMean")
    public Result getMean(Long teachId){
        return Result.ok().setData("means",meanService.getByTeach(teachId));
    }

    //上传文件
    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file,@RequestParam String name,@RequestParam Long teachId){
        String originalFileName=file.getOriginalFilename();
        String filePath = path+"\\"+originalFileName;
        File dest =new File(filePath);
        if (!dest.getParentFile().exists()){dest.getParentFile().mkdir();}
        try{
            file.transferTo(dest);
            Mean mean = new Mean();
            mean.setName(name);
            mean.setAddress(filePath);
            mean.setTeachId(teachId);
            if (meanService.addMean(mean)>0)return Result.ok();
            else return Result.fail().setMsg("upload ok ,fail save");
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail().setMsg("fail upload");
        }
    }

    //下载文件
    @PostMapping("/download")
    public Result download(@RequestParam Long id, HttpServletResponse response,@RequestParam boolean isOnline)throws IOException{
       if (meanService.getById(id)!=null)
       {
           String address = meanService.getById(id).getAddress();
           File file = new File(address);
           int len = 0;
           if (file.exists()){
               String filename = file.getName();
               filename = new String(filename.getBytes(StandardCharsets.UTF_8),"ISO-8859-1");
               BufferedInputStream bi =new BufferedInputStream(new FileInputStream(file));
               byte[] buf = new byte[5*1024];
               response.reset();
               if (isOnline){ //在线打开
                   URL url = new URL("file:///"+address);
                   response.setContentType(url.openConnection().getContentType());
                   response.setHeader("Content-Disposition","inline;filename="+filename);
               }else {//下载
                   response.setContentType("application/x-msdownload");
                   response.setHeader("Content-Disposition", "attachment; filename=" + filename);
               }
               OutputStream out = response.getOutputStream();
               while ((len = bi.read(buf)) > 0)
                   out.write(buf, 0, len);
               bi.close();
               out.close();
           }
           return Result.ok();
       }
       else return Result.fail().setMsg("id wrong");
    }

    @GetMapping("/upWork")
    public Result upWork(Work work){
        WorkDto workDto = new WorkDto();
        workDto.setName(work.getName());
        workDto.setTeachId(work.getTeachId());
        workDto.setContent(String.join(",",work.getContent()));
        if (workService.addWork(workDto)>0)return Result.ok();
        else return Result.fail().setMsg("fail add");
    }

    @GetMapping("/getWork")
    public Result getWork(Long teachId){
        List<Work> works = new ArrayList<>();
        for (WorkDto workDto:workService.getByTeach(teachId)){
            Work work = new Work();
            work.setId(workDto.getId());
            work.setTeachId(teachId);
            if (workDto.getContent()!=null){ work.setContent(workDto.getContent().split(","));}
            works.add(work);
        }
        return Result.ok().setData("works",works);
    }

    @GetMapping("/correctWord")
    public Result correctWork(StuWork stuWork){
        if (stuWorkService.addStuWork(stuWork)>0)return Result.ok();
        else return Result.fail();
    }

    @GetMapping("/getScore")
     public Result getScore(@RequestParam Long workId,@RequestParam Long stuId){
        return Result.ok().setData("score",stuWorkService.getScore(workId,stuId));
    }

    @GetMapping("/upCourseware")
    public Result upCourseware(CoursewareDto coursewareDto){
        Courseware courseware = new Courseware();
        courseware.setTeachId(coursewareDto.getTeachId());
       if (coursewareDto.getContent()!=null)courseware.setContent(String.join(",",coursewareDto.getContent()));
        if (coursewareService.addCourseware(courseware)>0)return Result.ok();
        else return Result.fail();
    }

    @GetMapping("/getCourseware")
    public Result getCourseware(Long teachId){
        List<Courseware> coursewares = coursewareService.getByTeach(teachId);
        List<CoursewareDto> coursewareDtos = new ArrayList<>();
        for (Courseware courseware:coursewares){
            CoursewareDto coursewareDto = new CoursewareDto();
            coursewareDto.setId(courseware.getId());
            coursewareDto.setTeachId(courseware.getTeachId());
          if (courseware.getContent()!=null) coursewareDto.setContent(courseware.getContent().split(","));
          coursewareDtos.add(coursewareDto);
        }
        return Result.ok().setData("coursewares",coursewareDtos);
    }

    @GetMapping("/delCourseware")
    public Result delCourseware(Long teachId){
        if (coursewareService.delByTeach(teachId)>0)return Result.ok();
        else return Result.fail();
    }

}
