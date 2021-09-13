package com.zt.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zt.controller.base.BaseResultMessage;
import com.zt.controller.base.ResultMessage;
import com.zt.utils.HttpUtils;
import com.zt.utils.OutPrintUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/yml")
public class YmlController extends BaseResultMessage {

    private final ConcurrentHashMap<Integer,String> concurrentHashMap = new ConcurrentHashMap<>();


    @Builder
    @Data
    public static class Word {
        private final Integer key;
        private final String label;
    }

    public List<String> getLabel(List<Integer> keys) {
        return keys.stream()
                .map(concurrentHashMap::get)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "ymlAnalysis")
    public ResultMessage ymlAnalysis(@RequestParam(name = "file") MultipartFile file) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        LinkedList<String> words = OutPrintUtils.ymlAnalysis(documentContent);
        List<Word> wordList = Lists.newArrayList();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            concurrentHashMap.put(i, word);
            wordList.add(Word.builder().key(i).label(word).build());
        }

        return success(wordList);
    }

    @PostMapping(value = "ymlExtract")
    public ResultMessage ymlExtract(@RequestParam(name = "file") MultipartFile file,
                                    @RequestParam(name = "key") List<Integer> key) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        JSONObject jsonObject = OutPrintUtils.ymlExtract(documentContent, getLabel(key));
        return success(jsonObject);
    }

    @PostMapping(value = "ymlExtractProperties")
    public String ymlExtractProperties(@RequestParam(name = "file") MultipartFile file,
                                       @RequestParam(name = "key") List<Integer> key) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        return OutPrintUtils.ymlExtractProperties(documentContent, getLabel(key));
    }

    @PostMapping(value = "ymlExtractYml")
    public String ymlExtractYml(@RequestParam(name = "file") MultipartFile file,
                                @RequestParam(name = "key") List<Integer> key) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        return OutPrintUtils.ymlExtractYml(documentContent, getLabel(key));
    }


    @PostMapping(value = "ymlExtractYmlEnv")
    public String ymlExtractYmlEnv(@RequestParam(name = "file") MultipartFile file,
                                @RequestParam(name = "key") List<Integer> key) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        return OutPrintUtils.ymlExtractYmlEnv(documentContent, getLabel(key));
    }

    @PostMapping(value = "ymlExtractEnv")
    public String ymlExtractEnv(@RequestParam(name = "file") MultipartFile file,
                                @RequestParam(name = "key") List<Integer> key) throws IOException {
        String documentContent = HttpUtils.readFileContext(file);
        return OutPrintUtils.ymlExtractEnv(documentContent, getLabel(key));
    }
}
