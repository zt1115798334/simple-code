package com.zt.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HttpUtils {

    public static String readFileContext(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public static void downFile(HttpServletResponse response, String filename, String context) {
        response.reset(); // reset the response
        response.setContentType("application/octet-stream");//告诉浏览器输出内容为流
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        //NIO 实现
        int bufferSize = 131072;
        // 6x128 KB = 768KB byte buffer
        try (InputStream inputStream = new ByteArrayInputStream(context.getBytes())) {
            byte[] byteArr = new byte[bufferSize];
            for (int i; (i = inputStream.read(byteArr)) != -1; ) {
                response.getOutputStream().write(byteArr, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
