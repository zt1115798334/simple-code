package com.zt.utils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedWriter;
import java.io.IOException;
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

        Map<String, String> ymlByFileName = YmlUtils.getYmlByFileName(YmlUtils.BOOTSTRAP_FILE);
        Path tpPath = Paths.get(outPropertiesPath);
        Map<String, Object> map = new LinkedHashMap<>();

        try (BufferedWriter writer = Files.newBufferedWriter(tpPath)) {
            for (Map.Entry<String, String> entry : ymlByFileName.entrySet()) {
                String v;

                if (needZh.contains(entry.getKey())) {
                    String keyEd = entry.getKey().toUpperCase().replace(".", "_").replace("-", "_");
                    v = "${" + keyEd + "}";
                    writer.write(entry.getKey() + "=" + "${" + keyEd + "}" + "\n");
                } else {
                    v = entry.getValue();
                    writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
                }
                map.put(entry.getKey(), v);

            }
        }
        String d = YamlParser.flattenedMapToYaml(map);
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outYmlPath))) {
            writer.write(d);
        }
//        TransferUtils.properties2Yaml(outPropertiesPath, outYmlPath);

        Path ePath = Paths.get(outEnvPath);
        try (BufferedWriter writer = Files.newBufferedWriter(ePath)) {
            for (Map.Entry<String, String> entry : ymlByFileName.entrySet()) {
                if (needZh.contains(entry.getKey())) {
                    String key = entry.getKey().toUpperCase().replace(".", "_").replace("-", "_");
                    writer.write(key + "=" + entry.getValue() + "\n");
                }

            }
        }
    }
}
