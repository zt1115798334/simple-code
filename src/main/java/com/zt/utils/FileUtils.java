package com.zt.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 11:44
 * description:
 */
public class FileUtils {

    public static String createFiles(String path) throws IOException {
        String pathFile = System.getProperty("user.dir") + File.separator + path;
        File file = new File(pathFile);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return pathFile;
    }
}
