package com.ntu.cmqq.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ntu.cmqq.dto.User;
import com.ntu.cmqq.entity.Student;
import com.ntu.cmqq.entity.Teacher;
import com.ntu.cmqq.service.StudentService;
import com.ntu.cmqq.service.TeacherService;
import com.ntu.cmqq.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arya
 */
@RestController
public class LoginController {

    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;

    @PostMapping("/login")
    public Result checkStu(@RequestBody Student studentF){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",studentF.getUsername());
        Student student = studentService.getOne(wrapper);
        if (null==student)return Result.fail().setMsg("wrong username");
        if (!studentF.getPassword().equals(studentF.getPassword())) return Result.fail().setMsg("wrong pwd");
        else {
            student.setPassword("");
            return Result.ok().setData("student",student);
        }
    }

    @PostMapping("/teacherLogin")
    public Result checkTeacher(@RequestBody Teacher teacherF){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",teacherF.getUsername());
        Teacher teacher = teacherService.getOne(wrapper);
        if(null==teacher)return Result.fail().setMsg("WRONG USERNAME");
        if (!teacherF.getPassword().equals(teacher.getPassword()))return Result.fail().setMsg("wrong pwd");
        else {
            teacher.setPassword("");
            return Result.ok().setData("teacher",teacher);
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username",user.getUsername());
        if (studentService.getOne(wrapper)!=null||teacherService.getOne(wrapper)!=null) return Result.fail().setMsg("用户名已存在");
        if (user.getStatus()==1) {
            Student student =new Student();
            student.setUsername(user.getUsername());
            student.setPassword(user.getPassword());
            student.setStatus(true);
            student.setSchool(user.getSchool());
            student.setNickname(user.getNickname());
            student.setDescription("");
            if (studentService.save(student))return Result.ok().setMsg("stu add ok");
            else return Result.fail().setMsg("add stu fail");
        }
        if (user.getStatus()==0){
            Teacher teacher = new Teacher();
            teacher.setUsername(user.getUsername());
            teacher.setPassword(user.getPassword());
            teacher.setStatus(true);
            teacher.setSchool(user.getSchool());
            teacher.setNickname(user.getNickname());
            if (teacherService.save(teacher))return Result.ok().setMsg("teacher add ok");
            else return Result.fail().setMsg("add stu fail");
        }
        else return Result.fail().setMsg("wrong status");
    }

    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody User user){
       if (user.getStatus()==1){//学生
           Student student = studentService.getById(user.getId());
           if (student==null)return Result.fail().setMsg("wrong id");
           student.setPassword(user.getPassword());
           if (studentService.updateById(student)) return Result.ok();
           else return Result.fail().setMsg("stu.pwd修改失败");
       }
        if (user.getStatus()==0){//老师
            Teacher teacher =teacherService.getById(user.getId());
            if (teacher==null)return Result.fail().setMsg("wrong id");
            teacher.setPassword(user.getPassword());
            if (teacherService.updateById(teacher)) return Result.ok();
            else return Result.fail().setMsg("teacher.pwd修改失败");
        }
        else return Result.fail().setMsg("wrong status");
    }
}
