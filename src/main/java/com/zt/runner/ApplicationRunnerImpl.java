package com.zt.runner;

import com.zt.utils.FileUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String pathFile = System.getProperty("user.dir") + File.separator + "com";
        System.out.println("开始删除 pathFile = " + pathFile);
        FileUtils.deleteFile(pathFile);
        System.out.println("删除成功！！");
    }
}
