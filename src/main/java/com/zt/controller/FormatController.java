package com.zt.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.zt.utils.FormatXmlUtils;
import com.zt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.stream.Collectors;

@RestController
@RequestMapping("format")
public class FormatController {


    @PostMapping(value = "formatWordXml")
    public void formatWordXml(@RequestParam(name = "file") MultipartFile file,
                              HttpServletResponse response) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        StringBuilder result = FormatXmlUtils.getStringBuilder(documentContent);
        HttpUtils.downFile(response, "document.xml", response.toString());
    }


}
