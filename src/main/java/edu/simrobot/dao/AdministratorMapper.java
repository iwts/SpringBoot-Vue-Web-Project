package edu.simrobot.dao;

import edu.simrobot.pojo.Administrator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdministratorMapper {
    @Select("select password from administrator")
    Administrator getPassword() throws Exception;
}
