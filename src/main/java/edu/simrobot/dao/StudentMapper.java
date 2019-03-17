package edu.simrobot.dao;

import edu.simrobot.pojo.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentMapper {
    @Select("select * from student where studentId=#{studentId}")
    Student selectStudentById(String studentId) throws Exception;

    @Insert("insert into student(studentId,password,name,grade) values (#{studentId},#{password},#{name},#{grade})")
    void insertStudent(Student student) throws Exception;
}
