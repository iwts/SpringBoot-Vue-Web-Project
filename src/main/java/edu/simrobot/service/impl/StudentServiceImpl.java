package edu.simrobot.service.impl;

import edu.simrobot.dao.*;
import edu.simrobot.pojo.Student;
import edu.simrobot.pojo.StudentForWork;
import edu.simrobot.pojo.Work;
import edu.simrobot.pojo.dto.MyWork;
import edu.simrobot.service.StudentService;
import edu.simrobot.utils.PathUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service("studentService")
public class StudentServiceImpl implements StudentService {
    @Resource
    StudentMapper studentMapper;
    @Resource
    StudentForWorkMapper studentForWorkMapper;
    @Resource
    WorkMapper workMapper;
    @Resource
    ScwMapper scwMapper;
    @Resource
    CompleteWorkMapper completeWorkMapper;

    @Override
    public String register(Student student) {
        // 返回码：0-成功，1-不存在，2-输入不完整，3-学号错误，4-重复注册
        if(student == null) return "1";
        if(student.getStudentId() == null || student.getPassword() == null || student.getName() == null){
            return "2";
        }
        if(student.getStudentId().length() != 10) return "3";
        // 因为学号前4位是年级，所以获取以后作为 grade 值
        String grade = student.getStudentId().substring(0,4);
        int gradeTest = Integer.parseInt(grade);
        // 简单判定学号是否正确
        if(gradeTest < 2004) return "3";
        student.setGrade(grade);
        Student sqlStudent = null;
        try{
            sqlStudent = studentMapper.selectStudentById(student.getStudentId());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(sqlStudent != null){
            return "4";
        }
        try{
            studentMapper.insertStudent(student);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "0";
    }

    @Override
    public Student login(Student student) {
        if(student == null || student.getStudentId() == null || student.getPassword() == null) return null;
        Student sqlStudent = null;
        try{
            sqlStudent = studentMapper.selectStudentById(student.getStudentId());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(sqlStudent == null || student.getPassword().compareTo(sqlStudent.getPassword()) != 0) return null;
        return sqlStudent;
    }

    @Override
    public void logout(){
        // 目前没有需要操作的内容
    }

    @Override
    public List<MyWork> getMyWork(String studentId){
        List<StudentForWork> workIdList = null;
        List<MyWork> myWorkList = new ArrayList<>();
        try{
            workIdList = studentForWorkMapper.getAllByStudentId(studentId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(workIdList == null) return null;

        // 前端需要知道当前学生参与状态，所以还需要遍历工作，获取状态
        for(StudentForWork o : workIdList){
            String workId = o.getWorkId();
            StudentForWork.Status status = o.getStatus();
            Work work = null;
            try{
                work = workMapper.getWorkById(workId);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            MyWork temp = new MyWork();
            temp.setWork(work);
            temp.setStatus(status);
            myWorkList.add(temp);
        }
        return myWorkList;
    }

    @Override
    public void applyDone(String studentId, String workId, MultipartFile multipartFile) {
        String path = PathUtil.REALPATH;
        path += "/work-"+workId;
        // 没有归档文件则创建一个
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        // 这里用原始文件名，不确定是否强制更换成student.name
        String name = multipartFile.getOriginalFilename();
        if(name == null) name = "default.file";
        File file = new File(path,name);
        try{
            multipartFile.transferTo(file);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        StudentForWork studentForWork = new StudentForWork(studentId,workId, StudentForWork.Status.COMPLETE);
        try{
            studentForWorkMapper.updateStatus(studentForWork);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void applyLeave(String workId, String studentId) {
        StudentForWork studentForWork = new StudentForWork(studentId,workId, StudentForWork.Status.LEAVE);
        try{
            studentForWorkMapper.updateStatus(studentForWork);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void applyReturn(String workId, String studentId) {
        StudentForWork studentForWork = new StudentForWork(studentId,workId, StudentForWork.Status.UNDONE);
        try{
            studentForWorkMapper.updateStatus(studentForWork);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public List<MyWork> getCompleteWork(String studentId){
        // 基本与 getMyWork 一样，只是对不同的数据库操作
        List<StudentForWork> workIdList = null;
        List<MyWork> completeWorkList = new ArrayList<>();
        try{
            workIdList = scwMapper.getAllByStudentId(studentId);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(workIdList == null) return null;
        for(StudentForWork o : workIdList){
            String workId = o.getWorkId();
            StudentForWork.Status status = o.getStatus();
            Work work = null;
            try{
                work = completeWorkMapper.getWorkById(workId);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            MyWork temp = new MyWork();
            temp.setWork(work);
            temp.setStatus(status);
            completeWorkList.add(temp);
        }
        return completeWorkList;
    }
}
