package edu.simrobot.service;

import edu.simrobot.pojo.Student;
import edu.simrobot.pojo.dto.MyWork;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface StudentService {

    /**
     * 学生注册
     * @param student 学生实体类
     * @return String JSON返回状态码
     * */
    String register(Student student);

    /**
     * 学生登录，基于session
     * @param student 学生实体类
     * @return Student JSON返回该学生实体类
     * */
    Student login(Student student);

    /**
     * 学生注销，基于session
     * 不需要任何其他返回值，只需要消除session即可
     * */
    void logout();

    /**
     * 根据学生ID获取该学生参与的正在进行的工作，使用JSON返回
     * @param studentId 仅需要学生ID
     * @return List<MyWork> 返回List，泛型是专门用于传输的实体类
     * */
    List<MyWork> getMyWork(String studentId);

    /**
     * 对于学生参与的工作进行提交文件，
     * 学生工作显示上可以点击完成工作，此时前端需要向服务器传输上文件作为完成的内容
     * 同时应该在表中修改该学生参与的此工作的状态
     * 实现上使用 Spring 原生的 Multipart 技术
     *
     * @param studentId 用于SQL表上进行处理
     * @param workId 同上
     * @param multipartFile Multipart技术
     * */
    void applyDone(String studentId, String workId, MultipartFile multipartFile);

    /**
     * 对于学生参与的工作申请请假操作，只需要修改工作状态即可
     * @param workId SQL表上进行处理
     * @param studentId 同上
     * */
    void applyLeave(String workId,String studentId);

    /**
     * 只有在学生是请假状态下可以调用，回归工作，修改工作状态即可
     * @param workId 同上
     * @param studentId 同上
     * */
    void applyReturn(String workId,String studentId);

    /**
     * 返回该学生下已经完成的工作。在数据库上，有专门的表储存已完成工作信息
     * 基本与 {@link StudentService#getMyWork(String)} 类似，只是操作
     * 的数据库不同。
     *
     * @param studentId 仅需要学生ID
     * @return List<MyWork> 返回值同方法 {@link StudentService#getMyWork(String)}
     * */
    List<MyWork> getCompleteWork(String studentId);
}
