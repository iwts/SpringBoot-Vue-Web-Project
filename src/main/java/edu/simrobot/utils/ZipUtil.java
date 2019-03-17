package edu.simrobot.utils;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

/**
 * 压缩工具类
 * 传入路径，制作成 zip 文件
 * 利用 apache ant
 * */

public class ZipUtil {
    private File zipFile;

    public ZipUtil(String pathName) {
        zipFile = new File(pathName);
    }

    public void compress(String srcPathName) {
        File srcDir = new File(srcPathName);
        Project project = new Project();
        Zip zip = new Zip();
        zip.setProject(project);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(project);
        fileSet.setDir(srcDir);
        zip.addFileset(fileSet);
        zip.execute();
    }
}
