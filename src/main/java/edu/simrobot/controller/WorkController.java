package edu.simrobot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.simrobot.pojo.StudentForWork;
import edu.simrobot.pojo.Work;
import edu.simrobot.pojo.dto.JoinApply;
import edu.simrobot.service.WorkService;
import edu.simrobot.utils.PathUtil;
import edu.simrobot.utils.ZipUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作相关 Controller
 * */

@RestController
@RequestMapping("work")
public class WorkController {
    @Resource
    WorkService workService;
    @Resource
    ObjectMapper objectMapper;

    /**
     * 获取全部工作，作为 List 返回，JSON 传输，前端可以用数组接收
     * */
    @RequestMapping(value = "works",method = RequestMethod.GET)
    @ResponseBody
    public List<Work> getWorkList(){
        return workService.getWorkList();
    }

    /**
     * 获取已完成的工作
     * */
    @RequestMapping(value = "completeWorks",method = RequestMethod.GET)
    @ResponseBody
    public List<Work> getCompleteWorkList(){
        return workService.getCompleteWorkList();
    }

    /**
     * 学生加入某工作
     * */
    @RequestMapping(value = "join",method = RequestMethod.POST)
    @ResponseBody
    public String join(@RequestBody JoinApply joinApply){
        StudentForWork apply = new StudentForWork(joinApply.getStudentId(),
                                                joinApply.getWorkId(),
                                                StudentForWork.Status.UNDONE);
        // 状态码：0-已加入，1-成功
        String res = workService.join(apply);
        Map<String,Object> json = new HashMap<>();
        json.put("message",res);
        String message = "";
        try{
            message = objectMapper.writeValueAsString(json);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return message;
    }

    /**
     * 创建一个工作，任何人都可以创建。仍然前端使用 form 提交，返回值设置成了
     * ModelAndView，利用重定向
     * */
    @RequestMapping(value = "create",method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView create(@ModelAttribute Work work){
        Date date = new Date();
        // 工作ID由当前时间去掉年份生成
        SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmmss");
        work.setWorkId(sdf.format(date));
        workService.create(work);
        return new ModelAndView("redirect:/create.html?message=ok");
    }

    /**
     * 算是工作详细信息的一部分，获取参与该工作的全部人员。前端在传输的时候使用 URL 参数
     * 传进 complete 值。只要有这个值，认为请求的是已完成的工作，设定 flag 在调用
     * service 层方法的时候确定 isComplete 值，详情参考
     * {@link WorkService#getParticipants(String, boolean)}
     * */
    @RequestMapping(value = "participant/{workId}",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> participant(@PathVariable("workId") String workId, HttpServletRequest request){
        String complete = request.getParameter("complete");
        boolean flag = false;
        if(complete != null) flag = true;
        // flag作用看接口上解释
        return workService.getParticipants(workId,flag);
    }

    /**
     * 下载工作数据。直接浏览器下载，所以设定返回值是 ModelAndView 但 return null。
     * 前端页面处理，如果工作没有人完成，那么不会有下载按钮。后端会查看是否存在该工作
     * 对应的文件夹，如果没有重定向到 404 页面。
     * 使用工具类 ZipUtil 将工作对应文件夹压缩成为 zip 文件再下载。
     * 工作文件夹格式：work-{workId}
     * */
    @RequestMapping(value = "download/{workId}",method = RequestMethod.GET)
    public ModelAndView download(@PathVariable("workId") String workId, HttpServletResponse response){
        File testFile = new File(PathUtil.DOWNLOAD_PATH+"/work-"+workId);
        if(!testFile.exists()){
            return new ModelAndView("redirect:/file-404.html");
        }

        ZipUtil zu = new ZipUtil(PathUtil.DOWNLOAD_PATH+"/work-"+workId+".zip");
        zu.compress(PathUtil.DOWNLOAD_PATH+"/work-"+workId);

        String fileName = "/work-"+workId+".zip";
        String filePath = PathUtil.DOWNLOAD_PATH;
        File file = new File(filePath + "/" + fileName);
        if(file.exists()){
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;

            // IO流方式下载
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if(bis != null) bis.close();
                if(fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 最终删除该 zip 文件
        file.delete();

        return null;
    }
}
