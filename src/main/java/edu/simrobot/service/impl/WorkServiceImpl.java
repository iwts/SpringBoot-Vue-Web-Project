package edu.simrobot.service.impl;

import edu.simrobot.dao.*;
import edu.simrobot.pojo.Student;
import edu.simrobot.pojo.StudentForWork;
import edu.simrobot.pojo.Work;
import edu.simrobot.pojo.dto.Participant;
import edu.simrobot.service.WorkService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("workService")
public class WorkServiceImpl implements WorkService {
    @Resource
    WorkMapper workMapper;
    @Resource
    StudentForWorkMapper studentForWorkMapper;
    @Resource
    CompleteWorkMapper completeWorkMapper;
    @Resource
    ScwMapper scwMapper;
    @Resource
    StudentMapper studentMapper;

    @Override
    public List<Work> getWorkList() {
        List<Work> workList = null;
        try{
            workList = workMapper.getWorkList();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return workList;
    }

    @Override
    public List<Work> getCompleteWorkList() {
        List<Work> workList = null;
        try{
            workList = completeWorkMapper.getWorkList();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return workList;
    }

    @Override
    public String join(StudentForWork apply) {
        List<String> workIdList = null;
        try{
            workIdList = studentForWorkMapper.getWorkIdByStudentId(apply.getStudentId());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        int flag = 1;
        // 判定不能重复加入
        if(workIdList != null){
            for(String id : workIdList){
                if(id.compareTo(apply.getWorkId()) == 0){
                    flag = 0;
                    break;
                }
            }
        }
        // 状态码：0-已加入，1-成功
        if(flag == 1){
            try{
                studentForWorkMapper.insertApply(apply);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return ""+flag;
    }

    @Override
    public void create(Work work) {
        try{
            workMapper.insertWork(work);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(Work work){
        try{
            workMapper.deleteWork(work);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void complete(Work work) {
        // 备份的过程，注意是备份两张表
        try{
            completeWorkMapper.insertWork(work);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        List<StudentForWork> studentForWorkList = null;
        try {
            studentForWorkList = studentForWorkMapper.getAllByWorkId(work.getWorkId());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(studentForWorkList != null && !studentForWorkList.isEmpty()){
            for(StudentForWork studentForWork : studentForWorkList){
                try{
                    scwMapper.insertScw(studentForWork);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public Map<String,Object> getParticipants(String workId,boolean isComplete) {
        List<StudentForWork> studentForWorkList = null;
        // 根据 isComplete 选择不同的表进行操作
        try{
            if(isComplete){
                studentForWorkList = scwMapper.getAllByWorkId(workId);
            }else{
                studentForWorkList = studentForWorkMapper.getAllByWorkId(workId);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(studentForWorkList == null || studentForWorkList.isEmpty()) return null;

        Map<String,Object> json = new HashMap<>();
        List<Participant> participantList = new ArrayList<>();
        int completeNumber = 0;
        for(StudentForWork sfw : studentForWorkList){
            Participant participant = new Participant();
            Student student = null;
            try{
                student = studentMapper.selectStudentById(sfw.getStudentId());
            }catch (Exception ex){
                ex.printStackTrace();
            }
            if(student != null) participant.setName(student.getName());
            participant.setStatus(sfw.getStatus());
            participantList.add(participant);
            if(sfw.getStatus() == StudentForWork.Status.COMPLETE)
                completeNumber++;
        }
        int allNumber = studentForWorkList.size();
        double rate = (double) (completeNumber / allNumber);

        json.put("participant",participantList);
        json.put("complete_number",completeNumber);
        json.put("all_number",allNumber);
        json.put("rate",rate);
        return json;
    }

}
