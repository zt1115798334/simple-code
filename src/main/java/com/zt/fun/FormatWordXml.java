package com.zt.fun;

import com.google.common.base.Objects;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

public class FormatWordXml {
    public static void main(String[] args) throws IOException {

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        String document = resourceLoader.getResource("classpath:document.xml").getFile().getPath();
        System.out.println("document = " + document);
        String documentContent = String.join("\n", Files.readAllLines(Paths.get(document)));
//        System.out.println(collect);
        StringBuilder result = new StringBuilder();
        Stack<String> stack = new Stack<>();
        int top = 0;
        int bottom = 0;
        StringBuilder p = new StringBuilder();
        for (int i = 0; i < documentContent.length(); i++) {
            String charText = String.valueOf(documentContent.charAt(i));
            System.out.println("charText: " + charText + " top: " + top + " buttom:" + bottom);
            if (Objects.equal(charText, "<")) {
                stack.push("<");
                bottom = i - 1;
                if ((top - 1) >= 0) {
                    result.append(documentContent.substring(top, bottom));
                }
            }
            if (Objects.equal(charText, ">")) {
                String pop = stack.pop();
            }
            //遍历到”>“元素
            if (stack.isEmpty()) {
                p = new StringBuilder();
            } else {
                top = i;
                p.append(charText);
            }

            if (Objects.equal(charText, "$")) {
                System.out.println("$ = " + i);
                bottom = i;
                result.append(documentContent.substring(top, bottom));
                top = i + 1;
            }
            if (Objects.equal(charText, "{")) {
                System.out.println("{ = " + i);
                top = i;
                bottom = i + 1;
                result.append(documentContent.substring(top, bottom));
            }
            if (Objects.equal(charText, "}")) {
                System.out.println("} = " + i);

            }
        }
        System.out.println(result.toString());
    }
}
