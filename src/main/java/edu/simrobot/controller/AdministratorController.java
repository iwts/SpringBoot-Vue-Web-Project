package edu.simrobot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.simrobot.pojo.Administrator;
import edu.simrobot.service.AdministratorService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员相关 Controller
 * */

@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    @Resource
    AdministratorService administratorService;
    @Resource
    ObjectMapper objectMapper;

    /**
     * 管理员登录功能，不使用session，而是每次登录必须输入密码才能进入 console 的模式
     * 返回 JSON 数据，前端分析是否跳转。
     *
     * 这样交给前端有比较重大的安全问题，即前端可以通过 URL 直接进入 console。安全问题
     * 整体没有考虑解决。主要是个人对此没有太深了解，利用 Filter 之类的可能不适合这样的
     * REST 架构。
     *
     * 如果需要改动，可以看一下 Spring Security 是否为此提供了解决方案。或者使用 token
     * 是否能获得比较好的解决方案。
     *
     * 或者想要直接删除大部分代码重新完成安全框架，可以咨询：
     *      Author: iwts
     *      version: 1.0
     *      E-mail: 1065077057@qq.com
     * QQ同上，可能不加，请写好备注
     * */
    @RequestMapping(value = "login",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String login(@RequestBody Administrator administrator, HttpSession session){
        boolean res = administratorService.login(administrator.getPassword());
        Map<String,String> json = new HashMap<>();
        String message = null;

        // 这样设定 JSON 信息，用于前端获取后选择跳转或者提示密码错误
        if (!res){
            json.put("admin_login","wrong");
        }else{
            json.put("admin_login","success");
            session.setAttribute("admin_login",true);
        }

        try{
            message = objectMapper.writeValueAsString(json);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return message;
    }
}
