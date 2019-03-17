package edu.simrobot.Schedule;

import edu.simrobot.dao.WorkMapper;
import edu.simrobot.pojo.Work;
import edu.simrobot.service.WorkService;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Schedule
 * 每天凌晨5秒的时候获取全部工作，一一遍历查看是否某工作已经到达截止日期，如果到达
 * 则在 service 层，先做 complete 处理，再使用 delete 删除。
 * */

@Component
@Configurable
@EnableScheduling
public class DeadLineSchedule {
    @Resource
    WorkMapper workMapper;
    @Resource
    WorkService workService;

    // 仅测试了有步长的情况下，每隔1s会运行，没有测试每天定时运行
    @Scheduled(cron = "0 5 0 * * ?")
    public void deleteExpiredWork(){
        Date nowDate = new Date();
        List<Work> workList = null;
        try{
            workList = workMapper.getWorkList();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(workList == null || workList.isEmpty()) return;
        for(Work work : workList){
            Date dateLine = work.getDeadLine();
            if(dateLine.before(nowDate)){
                workService.complete(work);
                workService.delete(work);
            }
        }
    }
}
