package com.zt.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date:  2020/10/16 16:21
 * description:
 */
public class YmlUtils {
    public static final String BOOTSTRAP_FILE = "target.yml";

    private static Map<String, String> result = new LinkedHashMap<>();

    public static Map<String, String> getYmlByFileName(String ymlContext) {
        result = new LinkedHashMap<>();
        Yaml props = new Yaml();
        Map<String, Object> param = props.loadAs(ymlContext, Map.class);
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (val instanceof Map) {
                forEachYaml(key, (Map<String, Object>) val);
            } else {
                result.put(key, val.toString());
            }
        }
        return result;
    }

    public static void forEachYaml(String keyStr, Map<String, Object> obj) {
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            String strNew = "";
            if (keyStr != null && !"".equals(keyStr)) {
                strNew = keyStr + "." + key;
            } else {
                strNew = key;
            }
            if (val instanceof Map) {
                forEachYaml(strNew, (Map<String, Object>) val);
            } else {
                result.put(strNew, String.valueOf(val));
            }
        }
    }



}
