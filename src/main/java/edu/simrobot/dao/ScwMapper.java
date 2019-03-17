package edu.simrobot.dao;

import edu.simrobot.pojo.StudentForWork;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ScwMapper {
    @Insert("insert into scw values(#{workId},#{studentId},#{status})")
    void insertScw(StudentForWork studentForWork) throws Exception;

    @Select("select * from scw where studentId=#{studentId}")
    List<StudentForWork> getAllByStudentId(String studentId) throws Exception;

    @Select("select * from scw where workId=#{workId}")
    List<StudentForWork> getAllByWorkId(String workId) throws Exception;
}
