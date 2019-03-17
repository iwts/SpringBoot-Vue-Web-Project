package edu.simrobot.utils;

/**
 * 路径处理工具类
 * 但在该项目中似乎只能写一些静态常量了
 * 分为 Linux 版与 Windows 本地测试版
 * */

public class PathUtil {
    // Windows 本地测试
//    public static String REALPATH = "C:/Users/10650/Desktop/resource/file";
//    public static String DOWNLOAD_PATH = "C:/Users/10650/Desktop/resource/file";
    // Linux 服务器部署 -- 当前服务器为 DO
    public static String REALPATH = "/www/wwwroot/resource/file";
    public static String DOWNLOAD_PATH = "/www/wwwroot/resource/file";
}
