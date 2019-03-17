package edu.simrobot.controller;

import edu.simrobot.pojo.Work;
import edu.simrobot.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Test Controller
 * */

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    WorkService workService;

    @RequestMapping(value = "method",method = RequestMethod.GET)
    @ResponseBody
    public void test(){
        List<Work> workList = workService.getWorkList();
        for(Work work : workList){
            if(work.getWorkId().compareTo("1") == 0){
                workService.complete(work);
                workService.delete(work);
                break;
            }
        }
    }

    @RequestMapping("linuxTest")
    public ModelAndView linuxTest(){
        return new ModelAndView("test.html");
    }
}
