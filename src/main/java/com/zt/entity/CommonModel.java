package com.zt.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 9:21
 * description:
 */
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

    public String getEntityTemplate() {
        return entityTemplate;
    }

    public void setEntityTemplate(String entityTemplate) {
        this.entityTemplate = entityTemplate;
    }

    public String getRepositoryTemplate() {
        return repositoryTemplate;
    }

    public void setRepositoryTemplate(String repositoryTemplate) {
        this.repositoryTemplate = repositoryTemplate;
    }

    public String getServiceTemplate() {
        return serviceTemplate;
    }

    public void setServiceTemplate(String serviceTemplate) {
        this.serviceTemplate = serviceTemplate;
    }

    public String getServiceImplTemplate() {
        return serviceImplTemplate;
    }

    public void setServiceImplTemplate(String serviceImplTemplate) {
        this.serviceImplTemplate = serviceImplTemplate;
    }
}
