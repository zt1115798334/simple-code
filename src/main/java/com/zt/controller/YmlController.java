package com.zt.controller;

import com.alibaba.fastjson.JSONObject;
import com.zt.controller.base.BaseResultMessage;
import com.zt.controller.base.ResultMessage;
import com.zt.utils.HttpUtils;
import com.zt.utils.OutPrintUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("yml")
public class YmlController extends BaseResultMessage {


    @PostMapping(value = "ymlExtract")
    public ResultMessage ymlExtract(@RequestParam(name = "file") MultipartFile file,
                                    @RequestParam(name = "word") List<String> word) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        JSONObject jsonObject = OutPrintUtils.ymlExtract(documentContent, word);
        return success(jsonObject);
    }

    @PostMapping(value = "ymlExtractProperties")
    public String ymlExtractProperties(@RequestParam(name = "file") MultipartFile file,
                                       @RequestParam(name = "word") List<String> word) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        return OutPrintUtils.ymlExtractProperties(documentContent, word);
    }

    @PostMapping(value = "ymlExtractYml")
    public String ymlExtractYml(@RequestParam(name = "file") MultipartFile file,
                                @RequestParam(name = "word") List<String> word) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        return OutPrintUtils.ymlExtractYml(documentContent, word);
    }

    @PostMapping(value = "ymlExtractEnv")
    public String ymlExtractEnv(@RequestParam(name = "file") MultipartFile file,
                                @RequestParam(name = "word") List<String> word) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        return OutPrintUtils.ymlExtractEnv(documentContent, word);
    }
}
