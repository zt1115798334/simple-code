package com.zt.fun;

import com.zt.utils.OutPrintUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date:  2020/10/16 16:00
 * description:
 */
public class YmlPompDemo {


    public static void main(String[] args) throws IOException {


        List<String> needZh = new ArrayList<>();
        needZh.add("server.port");

        OutPrintUtils.print(needZh);
        System.out.println("转换完毕");
    }

}
