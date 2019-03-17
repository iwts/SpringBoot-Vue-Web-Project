package edu.simrobot.dao;

import edu.simrobot.pojo.StudentForWork;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface StudentForWorkMapper {
    @Select("select workId from studentForWork where studentId=#{studentId}")
    List<String> getWorkIdByStudentId(String studentId) throws Exception;

    @Select("select * from studentForWork where studentId=#{studentId}")
    List<StudentForWork> getAllByStudentId(String studentId) throws Exception;

    @Insert("insert into studentForWork values(#{studentId},#{workId},#{status})")
    void insertApply(StudentForWork studentForWork) throws Exception;

    @Update("update studentForWork " +
            "set status=#{status} " +
            "where studentId=#{studentId} and workId=#{workId}")
    void updateStatus(StudentForWork studentForWork) throws Exception;

    @Select("select * from studentForWork where workId=#{workId}")
    List<StudentForWork> getAllByWorkId(String workId) throws Exception;
}
