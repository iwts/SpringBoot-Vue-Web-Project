package edu.simrobot.dao;

import edu.simrobot.pojo.Work;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CompleteWorkMapper {
    @Insert("insert into completeWork values(#{workId},#{name},#{deadLine},#{description})")
    void insertWork(Work work) throws Exception;

    @Select("select * from completeWork where workId=#{workId}")
    Work getWorkById(String workId) throws Exception;

    @Select("select * from completeWork")
    List<Work> getWorkList() throws Exception;
}
