package edu.simrobot.dao;

import edu.simrobot.pojo.Work;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface WorkMapper {
    @Select("select * from work")
    List<Work> getWorkList() throws Exception;

    @Select("select * from work where workId=#{workId}")
    Work getWorkById(String workId) throws Exception;

    @Insert("insert into work values(#{workId},#{name},#{deadLine},#{description})")
    void insertWork(Work work) throws Exception;

    @Delete("delete from work where workId=#{workId}")
    void deleteWork(Work work) throws Exception;
}
