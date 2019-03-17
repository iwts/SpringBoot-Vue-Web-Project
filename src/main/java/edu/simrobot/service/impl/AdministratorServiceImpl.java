package edu.simrobot.service.impl;

import edu.simrobot.dao.AdministratorMapper;
import edu.simrobot.pojo.Administrator;
import edu.simrobot.service.AdministratorService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("administratorService")
public class AdministratorServiceImpl implements AdministratorService {
    @Resource
    AdministratorMapper administratorMapper;

    public boolean login(String password){
        if(password == null) return false;
        Administrator administrator = null;
        try{
            administrator = administratorMapper.getPassword();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(administrator != null && password.compareTo(administrator.getPassword()) == 0){
            return true;
        }
        return false;
    }
}
