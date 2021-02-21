package com.zt.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 9:21
 * description:
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "templates.file-path")
public class TemplatesProperties {

    private String entityTemplate;

    private String repositoryTemplate;

    private String serviceTemplate;

    private String serviceImplTemplate;

    private String dtoTemplate;

    private String searchDtoTemplate;

    private String controllerTemplate;

}