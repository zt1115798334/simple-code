package com.zt.utils;

import java.io.File;
import java.io.FileNotFoundException;
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

    /**
     * 删除某个文件夹下的所有文件夹和文件
     * @return boolean
     */
    public static boolean deleteFile(String path) throws Exception {
        try {

            File file = new File(path);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] fileList = file.list();
                for (String s : fileList) {
                    File delFile = new File(path + "\\" + s);
                    if (!delFile.isDirectory()) {
                        delFile.delete();

                    } else if (delFile.isDirectory()) {
                        deleteFile(path + "\\" + s);
                    }
                }
                file.delete();
            }

        } catch (FileNotFoundException e) {
            System.out.println("deleteFile() Exception:" + e.getMessage());
        }
        return true;
    }

}
