package com.ntu.cmqq.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ntu.cmqq.dto.JoinTeach;
import com.ntu.cmqq.dto.TeachDto;
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

    @GetMapping("/getTeach")
    public Result getTeach(@RequestParam int id,@RequestParam int status){
        if (status==0){//老师
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("teacher_id",id);
            List<Teach> teachList = teachService.list(wrapper);
            List<TeachDto> teachDtos = new ArrayList<>();
            for (Teach teach:teachList){
                TeachDto teachDto = new TeachDto(teach.getId(),teach.getTeacherId(),courseService.getById(teach.getCourseId()),teach.getIsTop(),teach.getDescription());
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
                Teach teach = teachService.getById(stuTeach.getTeachId());
                TeachDto teachDto = new TeachDto(teach.getId(),teach.getTeacherId(),courseService.getById(teach.getCourseId()),teach.getIsTop(),teach.getDescription());
                teachDtos.add(teachDto);
            }
            return Result.ok().setData("teaches",teachDtos);
        }
        else return Result.fail().setMsg("wrong status");
    }

    @GetMapping("/getALLTeach")
    public Result getAllTeach(){
        return Result.ok().setData("teaches",teachService.list(null));
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
        return null;
    }
}
