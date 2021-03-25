package com.zt.controller;

import com.zt.utils.FormatXmlUtils;
import com.zt.utils.HttpUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("api/format")
public class FormatController {


    @PostMapping(value = "formatWordXml")
    public void formatWordXml(@RequestParam(name = "file") MultipartFile file,
                              HttpServletResponse response) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        StringBuilder result = FormatXmlUtils.getStringBuilder(documentContent);
        HttpUtils.downFile(response, "document.xml", result.toString());
    }
}
