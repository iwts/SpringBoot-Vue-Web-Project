package edu.simrobot.service;

public interface AdministratorService {

    /**
     * 管理员登录
     * @param password 仅需要密码即可
     * @return boolean
     * */
    boolean login(String password);
}
