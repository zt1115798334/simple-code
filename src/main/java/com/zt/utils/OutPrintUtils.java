package com.zt.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OutPrintUtils {

    public static void print(List<String> needZh) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        String outPropertiesPath = resourceLoader.getResource("classpath:out_properties.properties").getFile().getPath();
        String outYmlPath = resourceLoader.getResource("classpath:out_yml.yml").getFile().getPath();
        String outEnvPath = resourceLoader.getResource("classpath:out_evn.evn").getFile().getPath();
        System.out.println("path = " + outPropertiesPath);
        System.out.println("outYmlPath = " + outYmlPath);
        System.out.println("outEnvPath = " + outEnvPath);

        String target = resourceLoader.getResource("classpath:target.yml").getFile().getPath();
        String targetContent = String.join("\n", Files.readAllLines(Paths.get(target)));

        Map<String, String> ymlByFileName = YmlUtils.getYmlByFileName(targetContent);

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outPropertiesPath))) {
            writer.write(properties(ymlByFileName, needZh));
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outYmlPath))) {
            writer.write(yml(ymlByFileName, needZh));
        }

        Path ePath = Paths.get(outEnvPath);
        try (BufferedWriter writer = Files.newBufferedWriter(ePath)) {
            writer.write(env(ymlByFileName, needZh));
        }
    }

    public static JSONObject ymlExtract(String ymlContext, List<String> needZh) {
        JSONObject result = new JSONObject();
        Map<String, String> ymlByFileName = YmlUtils.getYmlByFileName(ymlContext);
        result.put("properties", properties(ymlByFileName, needZh));
        result.put("yml", yml(ymlByFileName, needZh));
        result.put("env", env(ymlByFileName, needZh));
        return result;
    }

    public static String ymlExtractProperties(String ymlContext, List<String> needZh) {
        Map<String, String> ymlByFileName = YmlUtils.getYmlByFileName(ymlContext);
        return properties(ymlByFileName, needZh);
    }

    public static String ymlExtractYml(String ymlContext, List<String> needZh) {
        Map<String, String> ymlByFileName = YmlUtils.getYmlByFileName(ymlContext);
        return yml(ymlByFileName, needZh);
    }
    public static String ymlExtractEnv(String ymlContext, List<String> needZh) {
        Map<String, String> ymlByFileName = YmlUtils.getYmlByFileName(ymlContext);
        return env(ymlByFileName, needZh);
    }

    public static String properties(Map<String, String> ymlByFileName, List<String> needZh) {
        StringBuilder properties = new StringBuilder();
        for (Map.Entry<String, String> entry : ymlByFileName.entrySet()) {
            if (needZh.contains(entry.getKey())) {
                String keyEd = entry.getKey().toUpperCase().replace(".", "_").replace("-", "_");
                properties.append(entry.getKey()).append("=").append("${").append(keyEd).append("}").append("\n");
            } else {
                properties.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
            }
        }
        return properties.toString();
    }

    public static String yml(Map<String, String> ymlByFileName, List<String> needZh) {
        Map<String, Object> ymlMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : ymlByFileName.entrySet()) {
            String v;
            if (needZh.contains(entry.getKey())) {
                String keyEd = entry.getKey().toUpperCase().replace(".", "_").replace("-", "_");
                v = "${" + keyEd + "}";
            } else {
                v = entry.getValue();
            }
            ymlMap.put(entry.getKey(), v);
        }
        return YamlParser.flattenedMapToYaml(ymlMap);
    }

    public static String env(Map<String, String> ymlByFileName, List<String> needZh) {
        StringBuilder env = new StringBuilder();
        for (Map.Entry<String, String> entry : ymlByFileName.entrySet()) {
            if (needZh.contains(entry.getKey())) {
                String key = entry.getKey().toUpperCase().replace(".", "_").replace("-", "_");
                env.append(key).append("=").append(entry.getValue()).append("\n");
            }
        }
        return env.toString();
    }
}
