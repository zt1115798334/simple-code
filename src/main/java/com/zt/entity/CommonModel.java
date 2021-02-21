package com.zt.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 9:21
 * description:
 */
@Data
@Component
public class CommonModel {

    @Value("${templates.file-path.entity}")
    private String entityTemplate;

    @Value("${templates.file-path.repository}")
    private String repositoryTemplate;

    @Value("${templates.file-path.service}")
    private String serviceTemplate;

    @Value("${templates.file-path.serviceImpl}")
    private String serviceImplTemplate;

    @Value("${templates.file-path.dto}")
    private String dtoTemplate;

}
