package com.zt.utils;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

import java.util.Stack;

public class FormatXmlUtils {
    public static StringBuilder getStringBuilder(String documentContent) {
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
                        && stack_n.isEmpty() && StringUtils.isNotBlank(charText)) {
                    result.append(charText);
                }
                if (Objects.equal(charText, "}")) {
                    stack_m.pop();
                }
            }
        }
        return result;
    }
}
