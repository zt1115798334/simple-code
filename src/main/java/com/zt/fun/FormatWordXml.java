package com.zt.fun;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
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
            StringBuilder result = new StringBuilder();
            Stack<String> stack_m = new Stack<>();
            Stack<String> stack_n = new Stack<>();
            StringBuilder p = new StringBuilder();
            for (int i = 0; i < documentContent.length(); i++) {
                String charText = String.valueOf(documentContent.charAt(i));
                if (stack_m.isEmpty()) {
                    result.append(charText);
                    if (Objects.equal(charText, "$")) {
                        stack_m.push("$");
                    }
                } else {
                    if (Objects.equal(charText, "<")) {
                        stack_n.push("<");
                    }
                    if (Objects.equal(charText, ">")) {
                        stack_n.pop();

                    }
                    if ((!Objects.equal(charText, "<") && !Objects.equal(charText, ">"))
                            && stack_n.isEmpty()&& StringUtils.isNotBlank(charText)) {
                        result.append(charText);
                    }
                    if (Objects.equal(charText, "}")) {
                        stack_m.pop();
                    }
                }
            }
//            System.out.println(result.toString());
            Files.write(Paths.get(documentFormat), result.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
