package com.ntu.cmq.web;

import com.ntu.cmq.model.Result;
import com.ntu.cmq.model.User;
import com.ntu.cmq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author cmq
 */
@RestController
public class LoginController {

    @Autowired
    UserService userService;


    /**
     * 登录
     * */
    @PostMapping("/login")
    public Result login(String username, String password, HttpSession session){
        User user = userService.getByUsername(username);
        if (null==user){ return Result.fail().setMsg("输入用户名不正确");}
        else {
            if (password != user.getPassword()){ return Result.fail().setMsg("输入密码不正确");}
            else {
                session.setAttribute("user",user);
                return Result.ok().setMsg("用户名,密码正确").setData("身份",user.getStatus());
            }
        }
    }

    /**
     * 登出
     * */
    @GetMapping("/logout")
    public Result logout(HttpSession session){
        if (session.getAttribute("user")!=null){
            session.removeAttribute("user");
            if (session.getAttribute("user")==null){return Result.ok().setMsg("登出成功");}
            else { return Result.fail().setMsg("登出失败");}
        }
        else {
            return Result.fail().setMsg("登出失败");
        }
    }

    /**
     * 注册
     * */
    @PostMapping("/register")
    public Result register(String username,String password,Integer status){
        if (null != userService.getByUsername(username)){ return Result.fail().setMsg("用户名重复，换一个");}
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setStatus(status);
        userService.insertUser(user);
        if (user.getId()>0){ return Result.ok().setMsg("注册成功");}
        else {return Result.fail().setMsg("注册失败");}
    }


}
