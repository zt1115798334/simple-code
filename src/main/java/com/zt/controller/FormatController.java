package com.zt.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.zt.utils.FormatXmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

@RestController
@RequestMapping("format")
public class FormatController {


    @PostMapping(value = "formatWordXml")
    public void formatWordXml(@RequestParam(name = "file") MultipartFile file,
                                HttpServletResponse response) throws IOException {
        String tempFilePath = "D:/document";
        String tempFile = tempFilePath + File.separator + "document.xml";
        Files.createDirectories(Paths.get(tempFilePath));
        file.transferTo(new File(tempFile));

        String documentContent = String.join("\n", Files.readAllLines(Paths.get(tempFile)));
        StringBuilder result = FormatXmlUtils.getStringBuilder(documentContent);


        response.reset(); // reset the response
        response.setContentType("application/octet-stream");//告诉浏览器输出内容为流
        response.setHeader("Content-Disposition", "attachment; filename=\"document.xml\"");
        //NIO 实现
        int bufferSize = 131072;
        // 6x128 KB = 768KB byte buffer
        try (InputStream inputStream = new ByteArrayInputStream(result.toString().getBytes())) {
            byte[] byteArr = new byte[bufferSize];
            for (int i; (i = inputStream.read(byteArr)) != -1; ) {
                response.getOutputStream().write(byteArr, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
