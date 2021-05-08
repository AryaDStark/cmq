package com.ntu.cmq.web;

import com.ntu.cmq.mapper.CourseMapper;
import com.ntu.cmq.model.*;
import com.ntu.cmq.model.dto.TeachDto;
import com.ntu.cmq.service.CourseService;
import com.ntu.cmq.service.SignInService;
import com.ntu.cmq.service.TeachService;
import com.ntu.cmq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cmq
 */
@RestController
public class TeachController {
    @Autowired
    TeachService teachService;
    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    SignInService signInService;

    @GetMapping("/teach")
    public Result getCourse(Long id){
        Teach teach = teachService.getById(id);
        Course course = courseService.getById(id);
        if (null!=teach&&null!=course){return Result.ok().setMsg("成功").setData("teachId",id).setData("course",course);}
        else {return Result.fail().setMsg("失败");}
    }

    @GetMapping("/course/content")
    public Result getContent(Long id){
        Course course = courseService.getById(id);
        if (null!=course){ return Result.ok().setMsg("成功").setData("content",course.getContent());}
        else {return Result.fail().setMsg("失败");}
    }

    @PostMapping("/course/add")
    public Result addCourse(Long teacherId,Long courseId){
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
        TeachDto teachDto = new TeachDto();
       try {
           for (Teach teach: teaches){
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
    public Result addStu(Long teachId,Long userId){
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
    public Result top(Long teachId,Long userId){
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
    public Result signIn(Long signInId,Long teachId,Long studentId){
        SignIn signIn = signInService.getById(signInId);
        if (signIn==null)return Result.fail().setMsg("签到不存在");
        signIn.setTeachId(teachId);
        String stuIds = signIn.getStudentIds();
        if (stuIds==null || stuIds==""){ signIn.setStudentIds(studentId.toString());}
         else {signIn.setStudentIds(signIn.getStudentIds()+","+studentId);}
         if (signInService.updateSignIn(signIn)>0)return Result.ok().setMsg("成功");
         else return Result.fail().setMsg("失败");
    }

    @GetMapping("/signIn/get")
    public Result signIn(String studentIds,Long teachId){
        return null;
    }
}
