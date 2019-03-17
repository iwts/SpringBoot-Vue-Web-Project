package edu.simrobot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.simrobot.pojo.Student;
import edu.simrobot.pojo.dto.MyWork;
import edu.simrobot.service.StudentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生相关操作的 Controller
 * */

@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    StudentService studentService;
    @Resource
    ObjectMapper objectMapper;

    /**
     * 标准注册算法。
     * 直接调用 service 层方法，Controller 只需要利用 JSON 返回
     * 状态码即可。
     * */
    @RequestMapping("register")
    @ResponseBody
    public String register(@RequestBody Student student){
        String res = studentService.register(student);
        String message = null;
        Map<String,String> json = new HashMap<>();
        json.put("message",res);
        try{
            message = objectMapper.writeValueAsString(json);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return message;
    }

    /**
     * 登录
     * 解耦至 service 层，但是状态码的设定留在了 Controller 返回码
     * 已描述。
     * 利用 session 保存状态，同时检测重复登录。
     * 可以尝试 token
     * */
    @RequestMapping("login")
    @ResponseBody
    public String login(@RequestBody Student student, HttpSession session){
        // 返回码：0-已登录，1-账号密码错误,2-成功
        Object isLogin = session.getAttribute("isLogin");
        String message = null;
        Map<String,Object> json = new HashMap<>();
        if(isLogin != null && ((boolean) isLogin)){
            json.put("message","0");
        }else{
            Student res = studentService.login(student);
            if(res == null){
                json.put("message","1");
            }else{
                json.put("user",res);
                json.put("message","2");
            }
        }
        try{
            message = objectMapper.writeValueAsString(json);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return message;
    }

    /**
     * 注销
     * session 技术下就是删除session信息
     * 解耦至 service 层，但当前版本下没有注销时的额外操作
     * */
    @RequestMapping("logout")
    public void logout(HttpSession session){
        session.removeAttribute("user");
        session.setAttribute("isLogin",false);
        studentService.logout();
    }

    /**
     * 获取该学生当前参与的工作
     * 利用路径变量获取工作 ID 从而调用 service 方法
     * 返回 List ，在前端是 vue 的情况下可以获取 JSON 后利用 v-for 渲染
     * */
    @RequestMapping(value = "works/{studentId}",method = RequestMethod.GET)
    @ResponseBody
    public List<MyWork> getWorks(@PathVariable String studentId){
        return studentService.getMyWork(studentId);
    }

    /**
     * 学生对某个工作进行提交
     * 认为每个工作，最终学生需要提交一个文件（可压缩），提交之后认为完成工作
     * 该方法实现了文件上传。因为前端使用了 form 表单，所以没有研究 Ajax 异
     * 步传输 form 的情况，所以返回值是 MVC 风格的 ModelAndView。使用重定
     * 向直接跳转到具体的前端页面。
     * service 处理上传的文件
     * */
    @RequestMapping(value = "apply/done",method = RequestMethod.POST)
    public ModelAndView applyDone(@RequestParam(value = "studentId") String studentId,
                                  @RequestParam(value = "workId") String workId,
                                  @RequestParam(value = "file") MultipartFile multipartFile){
        if(multipartFile.isEmpty()){
            String url = "apply.html?studentId="+studentId+"&workId="+workId+"&message=noFile";
            return new ModelAndView("redirect:/"+url);
        }
        if(studentId == null || workId == null){
            return new ModelAndView("redirect:/apply.html?message=error");
        }
        studentService.applyDone(studentId,workId,multipartFile);

        return new ModelAndView("redirect:/student.html");
    }

    /**
     * 学生申请暂离
     * */
    @RequestMapping(value = "apply/leave/{workId}/{studentId}",method = RequestMethod.PUT)
    public void applyLeave(@PathVariable("workId") String workId,
                             @PathVariable("studentId") String studentId){
        studentService.applyLeave(workId,studentId);
    }

    /**
     * 学生申请回归
     * */
    @RequestMapping(value = "apply/return/{workId}/{studentId}",method = RequestMethod.PUT)
    public void applyReturn(@PathVariable("workId") String workId,
                           @PathVariable("studentId") String studentId){
        studentService.applyReturn(workId,studentId);
    }

    /**
     * 获取该学生参与的已经结束的工作。因为不在一张表（不推荐使用一张表储存，虽然查询方便
     * 但是完成以后处理历史工作时比较麻烦，正在进行的工作会比较多使用，历史数据太多影响数
     * 据库操作效率）
     * */
    @RequestMapping(value = "completeWorks/{studentId}",method = RequestMethod.GET)
    @ResponseBody
    public List<MyWork> getCompleteWorks(@PathVariable String studentId){
        return studentService.getCompleteWork(studentId);
    }
}
