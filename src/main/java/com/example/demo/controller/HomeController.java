package com.example.demo.controller;

import com.example.demo.entity.UserInfo;
import com.example.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping({"/", "/index"})
    public String index() {
        return "/index";
    }


    /**
     * 登录接口，获取subject，然后执行登录，根据shiro抛出的异常，来跳转到对应的页面
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping("/login")
    public String login(String username, String password) throws Exception {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            System.out.println("未知账号错误,不报错误，直接跳转到登录页面");
            return "/login";
        }
        UserInfo userInfo = userService.findByUserName(username);
        subject.getSession().setAttribute("user", userInfo);
        return "index";
    }

    @RequestMapping("/403")
    public String unauthorizedRole() {
        System.out.println("------没有权限-------");
        return "403";
    }

}