package com.zt.fun;

import com.google.common.base.Objects;
import com.zt.utils.FormatXmlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

public class FormatWordXml {
    public static void main(String[] args) {
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            String document = resourceLoader.getResource("classpath:document.xml").getFile().getPath();
            String documentFormat = resourceLoader.getResource("classpath:documentFormat.xml").getFile().getPath();
            System.out.println("document = " + document);
            System.out.println("documentFormat = " + documentFormat);
            String documentContent = String.join("\n", Files.readAllLines(Paths.get(document)));
            StringBuilder result = FormatXmlUtils.getStringBuilder(documentContent);

//            System.out.println(result.toString());
            Files.write(Paths.get(documentFormat), result.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
