package com.zt.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.zt.controller.base.BaseResultMessage;
import com.zt.controller.base.ResultMessage;
import com.zt.entity.ColumnTrans;
import com.zt.entity.DatabaseTrans;
import com.zt.entity.Table;
import com.zt.entity.TableTrans;
import com.zt.properties.TemplatesProperties;
import com.zt.service.ColumnService;
import com.zt.service.TableService;
import com.zt.utils.CamelCaseUtils;
import com.zt.utils.CreateJavaCode;
import com.zt.utils.DateUtils;
import com.zt.utils.FileUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/11 0:06
 * description:
 */
@RestController
@RequestMapping(value = "api")
public class Controller extends BaseResultMessage {

    private final TableService tableService;

    private final ColumnService columnService;

    private final TemplatesProperties templatesProperties;

    private final FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    public Controller(TableService tableService, ColumnService columnService, TemplatesProperties templatesProperties, FreeMarkerConfigurer freeMarkerConfigurer) {
        this.tableService = tableService;
        this.columnService = columnService;
        this.templatesProperties = templatesProperties;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    @PostMapping("showTable")
    public ResultMessage showTable() {
        List<Table> tableAll;
        try {
            tableAll = tableService.findTableAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return failure();
        }
        return success(tableAll);
    }

    @PostMapping("showColumn")
    public ResultMessage showColumn(@RequestParam List<String> tableNames) {
        List<Table> columnAll;
        try {
            columnAll = columnService.findColumnAll(tableNames);
        } catch (SQLException e) {
            e.printStackTrace();
            return failure();
        }
        return success(columnAll);
    }

    @PostMapping("getTableTrans")
    public ResultMessage getTableTrans(HttpServletResponse response,
                                       @RequestBody DatabaseTrans databaseTrans) throws IOException, TemplateException {
        System.out.println("databaseTrans = " + databaseTrans);
        String projectName = databaseTrans.getProjectName();
        List<TableTrans> tableTrans = databaseTrans.getTableTrans();
        Template templateEntity = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getEntityTemplate());
        Template templateRepository = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRepositoryTemplate());
        Template templateService = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getServiceTemplate());
        Template templateServiceImpl = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getServiceImplTemplate());
        Template templateDto = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getDtoTemplate());
        Template templateSearchDto = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSearchDtoTemplate());
        Template templateController = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getControllerTemplate());
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        String createdTime = DateUtils.formatDateTime(currentDateTime);

        //存储指定公共类芦荟胶
        JSONObject publicJavaPathCode = new JSONObject();
        String packagePath = CreateJavaCode.createPackagePath(projectName);
        String packagePathZ = packagePath.replace(".", "/");
        //类路径
        publicJavaPathCode.put("createdTime", createdTime);
        publicJavaPathCode.put("packagePath", packagePath);
        if (databaseTrans.isCreateBasics()) {
            createBase(packagePathZ, publicJavaPathCode);
            createSpecification(packagePathZ, publicJavaPathCode);
            createEnums(packagePathZ, publicJavaPathCode);
            createException(packagePathZ, publicJavaPathCode);
            createProperties(packagePathZ, publicJavaPathCode);
            createUtils(packagePathZ, publicJavaPathCode);
            createSecurity(packagePathZ, publicJavaPathCode);
            createRedis(packagePathZ, publicJavaPathCode);
            createAop(packagePathZ, publicJavaPathCode);
            createCache(packagePathZ, publicJavaPathCode);
            createXss(packagePathZ, publicJavaPathCode);
        }

        for (TableTrans tableTran : tableTrans) {
            createEntity(templateEntity, tableTran, createdTime, projectName);
            createRepository(templateRepository, tableTran, createdTime, projectName);
            createService(templateService, tableTran, createdTime, projectName);
            createServiceImpl(templateServiceImpl, tableTran, createdTime, projectName);
            createDto(templateDto, tableTran, createdTime, projectName);
            createSearchDto(templateSearchDto, tableTran, createdTime, projectName);
            createController(templateController, tableTran, createdTime, projectName);
        }
        String downFileName = "test";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/ms-word;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; fileName=" + downFileName);
        return success();
    }

    private void createBase(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {
        Template baseBaseResultMessageTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseBaseResultMessageTemplate());
        Template baseResultMessageTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseResultMessageTemplate());
        Template baseCurrentUserTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseCurrentUserTemplate());
        Template basePageEntityTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBasePageEntityTemplate());
        Template baseBaseServiceTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseBaseServiceTemplate());
        Template baseConstantServiceTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseConstantServiceTemplate());
        Template basePageUtilsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBasePageUtilsTemplate());
        Template basePageDtoTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBasePageDtoTemplate());

        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileBaseBaseResultMessageTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getBaseBaseResultMessageTemplate());
        String targetFileBaseResultMessageTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getBaseResultMessageTemplate());
        String targetFileBaseCurrentUserTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getBaseCurrentUserTemplate());
        String targetFileBasePageEntityTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getBasePageEntityTemplate());
        String targetFileBaseBaseServiceTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getBaseBaseServiceTemplate());
        String targetFileBaseConstantServiceTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getBaseConstantServiceTemplate());
        String targetFileBasePageUtilsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getBasePageUtilsTemplate());
        String targetFileBasePageDtoTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getBasePageDtoTemplate());

        writerOut(baseBaseResultMessageTemplate, targetFileBaseBaseResultMessageTemplate, javaCode);
        writerOut(baseResultMessageTemplate, targetFileBaseResultMessageTemplate, javaCode);
        writerOut(baseCurrentUserTemplate, targetFileBaseCurrentUserTemplate, javaCode);
        writerOut(basePageEntityTemplate, targetFileBasePageEntityTemplate, javaCode);
        writerOut(baseBaseServiceTemplate, targetFileBaseBaseServiceTemplate, javaCode);
        writerOut(baseConstantServiceTemplate, targetFileBaseConstantServiceTemplate, javaCode);
        writerOut(basePageUtilsTemplate, targetFileBasePageUtilsTemplate, javaCode);
        writerOut(basePageDtoTemplate, targetFileBasePageDtoTemplate, javaCode);

        System.out.println("生成代码完毕哦!!");
    }

    private void createSpecification(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {
        Template specificationBooleanOperatorTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSpecificationBooleanOperatorTemplate());
        Template specificationMatchTypeTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSpecificationMatchTypeTemplate());
        Template specificationSpecificationsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSpecificationSpecificationsTemplate());
        Template specificationSpecificationUtilsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSpecificationSpecificationUtilsTemplate());

        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileSpecificationBooleanOperatorTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSpecificationBooleanOperatorTemplate());
        String targetFileSpecificationMatchTypeTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSpecificationMatchTypeTemplate());
        String targetFileSpecificationSpecificationsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSpecificationSpecificationsTemplate());
        String targetFileSpecificationSpecificationUtilsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSpecificationSpecificationUtilsTemplate());

        writerOut(specificationBooleanOperatorTemplate, targetFileSpecificationBooleanOperatorTemplate, javaCode);
        writerOut(specificationMatchTypeTemplate, targetFileSpecificationMatchTypeTemplate, javaCode);
        writerOut(specificationSpecificationsTemplate, targetFileSpecificationSpecificationsTemplate, javaCode);
        writerOut(specificationSpecificationUtilsTemplate, targetFileSpecificationSpecificationUtilsTemplate, javaCode);
    }

    private void createEnums(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {
        Template enumsDeleteStateTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getEnumsDeleteStateTemplate());
        Template enumsSystemStatusCodeTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getEnumsSystemStatusCodeTemplate());
        Template enumsTimeUnitsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getEnumsTimeUnitsTemplate());
        Template enumsLoginTypeTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getEnumsLoginTypeTemplate());
        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileEnumsDeleteStateTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getEnumsDeleteStateTemplate());
        String targetFileEnumsSystemStatusCodeTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getEnumsSystemStatusCodeTemplate());
        String targetFileEnumsTimeUnitsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getEnumsTimeUnitsTemplate());
        String targetFileEnumsLoginTypeTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getEnumsLoginTypeTemplate());

        writerOut(enumsDeleteStateTemplate, targetFileEnumsDeleteStateTemplate, javaCode);
        writerOut(enumsSystemStatusCodeTemplate, targetFileEnumsSystemStatusCodeTemplate, javaCode);
        writerOut(enumsTimeUnitsTemplate, targetFileEnumsTimeUnitsTemplate, javaCode);
        writerOut(enumsLoginTypeTemplate, targetFileEnumsLoginTypeTemplate, javaCode);
    }

    private void createException(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {
        Template exceptionGlobalExceptionTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getExceptionGlobalExceptionTemplate());
        Template exceptionOperationExceptionTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getExceptionOperationExceptionTemplate());

        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileExceptionGlobalExceptionTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getExceptionGlobalExceptionTemplate());
        String targetFileExceptionOperationExceptionTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getExceptionOperationExceptionTemplate());

        writerOut(exceptionGlobalExceptionTemplate, targetFileExceptionGlobalExceptionTemplate, javaCode);
        writerOut(exceptionOperationExceptionTemplate, targetFileExceptionOperationExceptionTemplate, javaCode);

    }

    private void createProperties(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {
        Template propertiesAccountPropertiesTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getPropertiesAccountPropertiesTemplate());
        Template propertiesJwtPropertiesTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getPropertiesJwtPropertiesTemplate());

        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFilePropertiesAccountPropertiesTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getPropertiesAccountPropertiesTemplate());
        String targetFilePropertiesJwtPropertiesTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getPropertiesJwtPropertiesTemplate());

        writerOut(propertiesAccountPropertiesTemplate, targetFilePropertiesAccountPropertiesTemplate, javaCode);
        writerOut(propertiesJwtPropertiesTemplate, targetFilePropertiesJwtPropertiesTemplate, javaCode);
    }

    private void createUtils(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {
        Template utilsDateUtilsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getUtilsDateUtilsTemplate());
        Template utilsJwtUtilsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getUtilsJwtUtilsTemplate());
        Template utilsNetworkUtilTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getUtilsNetworkUtilTemplate());
        Template utilsRequestResponseUtilTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getUtilsRequestResponseUtilTemplate());
        Template utilsUuidUtilTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getUtilsUuidUtilTemplate());
        Template utilsUserUtilsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getUtilsUserUtilsTemplate());
        Template utilsDigestsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getUtilsDigestsTemplate());
        Template utilsEncodesTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getUtilsEncodesTemplate());

        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileUtilsDateUtilsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getUtilsDateUtilsTemplate());
        String targetFileUtilsJwtUtilsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getUtilsJwtUtilsTemplate());
        String targetFileUtilsNetworkUtilTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getUtilsNetworkUtilTemplate());
        String targetFileUtilsRequestResponseUtilTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getUtilsRequestResponseUtilTemplate());
        String targetFileUtilsUuidUtilTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getUtilsUuidUtilTemplate());
        String targetFileUtilsUserUtilsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getUtilsUserUtilsTemplate());
        String targetFileUtilsDigestsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getUtilsDigestsTemplate());
        String targetFileUtilsEncodesTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getUtilsEncodesTemplate());

        writerOut(utilsDateUtilsTemplate, targetFileUtilsDateUtilsTemplate, javaCode);
        writerOut(utilsJwtUtilsTemplate, targetFileUtilsJwtUtilsTemplate, javaCode);
        writerOut(utilsNetworkUtilTemplate, targetFileUtilsNetworkUtilTemplate, javaCode);
        writerOut(utilsRequestResponseUtilTemplate, targetFileUtilsRequestResponseUtilTemplate, javaCode);
        writerOut(utilsUuidUtilTemplate, targetFileUtilsUuidUtilTemplate, javaCode);
        writerOut(utilsUserUtilsTemplate, targetFileUtilsUserUtilsTemplate, javaCode);
        writerOut(utilsDigestsTemplate, targetFileUtilsDigestsTemplate, javaCode);
        writerOut(utilsEncodesTemplate, targetFileUtilsEncodesTemplate, javaCode);
    }

    private void createSecurity(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {
        Template securityShiroBaseConfigTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityShiroBaseConfigTemplate());
        Template securityStatelessWebSubjectFactoryTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityStatelessWebSubjectFactoryTemplate());
        Template securityJwtFilterTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityJwtFilterTemplate());
        Template securityShiroFilterChainManagerTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityShiroFilterChainManagerTemplate());
        Template securityAModularRealmAuthenticatorTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityAModularRealmAuthenticatorTemplate());
        Template securityJwtRealmTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityJwtRealmTemplate());
        Template securityPasswordRealmTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityPasswordRealmTemplate());
        Template securityRealmManagerTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityRealmManagerTemplate());
        Template securityJwtTokenTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityJwtTokenTemplate());
        Template securityPasswordTokenTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSecurityPasswordTokenTemplate());

        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileSecurityShiroBaseConfigTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityShiroBaseConfigTemplate());
        String targetFileSecurityStatelessWebSubjectFactoryTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityStatelessWebSubjectFactoryTemplate());
        String targetFileSecurityJwtFilterTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityJwtFilterTemplate());
        String targetFileSecurityShiroFilterChainManagerTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityShiroFilterChainManagerTemplate());
        String targetFileSecurityAModularRealmAuthenticatorTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityAModularRealmAuthenticatorTemplate());
        String targetFileSecurityJwtRealmTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityJwtRealmTemplate());
        String targetFileSecurityPasswordRealmTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityPasswordRealmTemplate());
        String targetFileSecurityRealmManagerTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityRealmManagerTemplate());
        String targetFileSecurityJwtTokenTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityJwtTokenTemplate());
        String targetFileSecurityPasswordTokenTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getSecurityPasswordTokenTemplate());

        writerOut(securityShiroBaseConfigTemplate, targetFileSecurityShiroBaseConfigTemplate, javaCode);
        writerOut(securityStatelessWebSubjectFactoryTemplate, targetFileSecurityStatelessWebSubjectFactoryTemplate, javaCode);
        writerOut(securityJwtFilterTemplate, targetFileSecurityJwtFilterTemplate, javaCode);
        writerOut(securityShiroFilterChainManagerTemplate, targetFileSecurityShiroFilterChainManagerTemplate, javaCode);
        writerOut(securityAModularRealmAuthenticatorTemplate, targetFileSecurityAModularRealmAuthenticatorTemplate, javaCode);
        writerOut(securityJwtRealmTemplate, targetFileSecurityJwtRealmTemplate, javaCode);
        writerOut(securityPasswordRealmTemplate, targetFileSecurityPasswordRealmTemplate, javaCode);
        writerOut(securityRealmManagerTemplate, targetFileSecurityRealmManagerTemplate, javaCode);
        writerOut(securityJwtTokenTemplate, targetFileSecurityJwtTokenTemplate, javaCode);
        writerOut(securityPasswordTokenTemplate, targetFileSecurityPasswordTokenTemplate, javaCode);
    }

    private void createRedis(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {

        Template redisRedisConfigMainTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRedisRedisConfigMainTemplate());
        Template redisRedisServiceImplTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRedisRedisServiceImplTemplate());
        Template redisStringRedisServiceImplTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRedisStringRedisServiceImplTemplate());
        Template redisJsonArrayRedisTemplateTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRedisJsonArrayRedisTemplateTemplate());
        Template redisJsonObjectRedisTemplateTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRedisJsonObjectRedisTemplateTemplate());
        Template redisRedisDatabaseTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRedisRedisDatabaseTemplate());
        Template redisRedisServiceTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRedisRedisServiceTemplate());
        Template redisStringRedisServiceTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRedisStringRedisServiceTemplate());

        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileRedisRedisConfigMainTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getRedisRedisConfigMainTemplate());
        String targetFileRedisRedisServiceImplTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getRedisRedisServiceImplTemplate());
        String targetFileRedisStringRedisServiceImplTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getRedisStringRedisServiceImplTemplate());
        String targetFileRedisJsonArrayRedisTemplateTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getRedisJsonArrayRedisTemplateTemplate());
        String targetFileRedisJsonObjectRedisTemplateTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getRedisJsonObjectRedisTemplateTemplate());
        String targetFileRedisRedisDatabaseTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getRedisRedisDatabaseTemplate());
        String targetFileRedisRedisServiceTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getRedisRedisServiceTemplate());
        String targetFileRedisStringRedisServiceTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getRedisStringRedisServiceTemplate());

        writerOut(redisRedisConfigMainTemplate, targetFileRedisRedisConfigMainTemplate, javaCode);
        writerOut(redisRedisServiceImplTemplate, targetFileRedisRedisServiceImplTemplate, javaCode);
        writerOut(redisStringRedisServiceImplTemplate, targetFileRedisStringRedisServiceImplTemplate, javaCode);
        writerOut(redisJsonArrayRedisTemplateTemplate, targetFileRedisJsonArrayRedisTemplateTemplate, javaCode);
        writerOut(redisJsonObjectRedisTemplateTemplate, targetFileRedisJsonObjectRedisTemplateTemplate, javaCode);
        writerOut(redisRedisDatabaseTemplate, targetFileRedisRedisDatabaseTemplate, javaCode);
        writerOut(redisRedisServiceTemplate, targetFileRedisRedisServiceTemplate, javaCode);
        writerOut(redisStringRedisServiceTemplate, targetFileRedisStringRedisServiceTemplate, javaCode);
    }

    private void createAop(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {
        Template aopAopUtilsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getAopAopUtilsTemplate());
        Template aopAbsHttpAspectTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getAopAbsHttpAspectTemplate());
        Template aopAbsHttpAspectSaveTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getAopAbsHttpAspectSaveTemplate());
        Template aopSaveLogTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getAopSaveLogTemplate());

        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileAopAopUtilsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getAopAopUtilsTemplate());
        String targetFileAopAbsHttpAspectTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getAopAbsHttpAspectTemplate());
        String targetFileAopAbsHttpAspectSaveTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getAopAbsHttpAspectSaveTemplate());
        String targetFileAopSaveLogTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getAopSaveLogTemplate());

        writerOut(aopAopUtilsTemplate, targetFileAopAopUtilsTemplate, javaCode);
        writerOut(aopAbsHttpAspectTemplate, targetFileAopAbsHttpAspectTemplate, javaCode);
        writerOut(aopAbsHttpAspectSaveTemplate, targetFileAopAbsHttpAspectSaveTemplate, javaCode);
        writerOut(aopSaveLogTemplate, targetFileAopSaveLogTemplate, javaCode);
    }

    private void createCache(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {

        Template cacheCacheKeysTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getCacheCacheKeysTemplate());

        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileCacheCacheKeysTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getCacheCacheKeysTemplate());

        writerOut(cacheCacheKeysTemplate, targetFileCacheCacheKeysTemplate, javaCode);
    }

    private void createXss(String packagePathZ, JSONObject publicJavaPathCode) throws IOException, TemplateException {

        Template xssXssFilterTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getXssXssFilterTemplate());
        Template xssXssHttpServletRequestWrapperTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getXssXssHttpServletRequestWrapperTemplate());
        Template xssXssUtilsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getXssXssUtilsTemplate());

        //类路径
        JSONObject javaCode = new JSONObject();
        javaCode.putAll(publicJavaPathCode);

        String targetFileXssXssFilterTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getXssXssFilterTemplate());
        String targetFileXssXssHttpServletRequestWrapperTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getXssXssHttpServletRequestWrapperTemplate());
        String targetFileXssXssUtilsTemplate = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.getXssXssUtilsTemplate());

        writerOut(xssXssFilterTemplate, targetFileXssXssFilterTemplate, javaCode);
        writerOut(xssXssHttpServletRequestWrapperTemplate, targetFileXssXssHttpServletRequestWrapperTemplate, javaCode);
        writerOut(xssXssUtilsTemplate, targetFileXssXssUtilsTemplate, javaCode);

    }

    private void createEntity(Template templateEntity, TableTrans tableTran, String createdTime, String projectName) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(projectName);
        String tableName = tableTran.getTableName();
        String entityName = tableTran.getTableNameTrans();
        //类路径
        String entityPackageName = packagePath + ".entity";

        //需要引入jar包
        String importJava = CreateJavaCode.createImportJavaEntity(entityPackageName, entityName);

        //注解
        String lombokAnnotation = CreateJavaCode.createEntityAnnotation(tableName);

        //构建变量
        List<ColumnTrans> columnTrans = tableTran.getColumnTrans();
        StringBuilder fieldCode = new StringBuilder();

        for (ColumnTrans columnTran : columnTrans) {
            String columnName = columnTran.getColumnName();
            String columnType = columnTran.getColumnType();
            String columnRemarks = columnTran.getColumnRemarks();

            fieldCode.append(CreateJavaCode.createRemarks(columnRemarks)).append(CreateJavaCode.createChangeLine());
            if (Objects.equal("id", columnName)) {
                fieldCode.append(CreateJavaCode.createFieldId());
            }
            fieldCode.append(CreateJavaCode.createField(columnType, columnName)).append(CreateJavaCode.createChangeLine());
        }

        JSONObject javaCode = new JSONObject();
        javaCode.put("entityPackageName", entityPackageName);
        javaCode.put("importJava", importJava);
        javaCode.put("entityName", entityName);
        javaCode.put("fieldCode", fieldCode.toString());
        javaCode.put("lombokAnnotation", lombokAnnotation);
        javaCode.put("createdTime", createdTime);

        String targetFile = FileUtils.createFiles(entityPackageName.replace(".", "/") + File.separator + entityName + ".java");
        writerOut(templateEntity, targetFile, javaCode);
    }

    private void createRepository(Template templateEntity, TableTrans tableTran, String createdTime, String projectName) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(projectName);
        String entityName = tableTran.getTableNameTrans();
        //类路径
        String repositoryPackageName = packagePath + ".repo";
        //定义引入jar包前缀
        String entityPackageName = packagePath + ".entity";
        //类名
        String repositoryName = entityName + "Repository";

        //需要引入jar包
        String importJava = CreateJavaCode.createImportJavaRepository(entityPackageName, entityName);

        JSONObject javaCode = new JSONObject();
        javaCode.put("repositoryPackageName", repositoryPackageName);
        javaCode.put("importJava", importJava);
        javaCode.put("repositoryName", repositoryName);
        javaCode.put("entityName", entityName);
        javaCode.put("createdTime", createdTime);


        String targetFile = FileUtils.createFiles(repositoryPackageName.replace(".", "/") + File.separator + repositoryName + ".java");
        writerOut(templateEntity, targetFile, javaCode);
    }

    private void createService(Template templateEntity, TableTrans tableTran, String createdTime, String projectName) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(projectName);
        String entityName = tableTran.getTableNameTrans();
        String entityNameStatement = CamelCaseUtils.underlineToHump(entityName);
        //类路径
        String servicePackageName = packagePath + ".service";
        //定义引入jar包前缀
        String entityPackageName = packagePath + ".entity";
        String searchDtoPackageName = packagePath + ".dto";
        //声明引用类
        String searchDtoName = "Search" + entityName + "Dto";
        String searchDtoNameStatement = CreateJavaCode.toLowerCaseFirstOne(searchDtoName);
        //类名
        String serviceName = entityName + "Service";
        //定义业务方法
        String saveEntityName = "save" + entityName;
        String deleteEntityName = "delete" + entityName;
        String findEntityName = "find" + entityName;
        String findEntityNamePage = "find" + entityName + "Page";

        //需要引入jar包前缀
        String importJava = CreateJavaCode.createImportJavaService(packagePath,
                entityPackageName, entityName,
                searchDtoPackageName, searchDtoName);

        //接口方法
        String serviceInterface = CreateJavaCode.createServiceInterfaceOfSave(saveEntityName, entityName, entityNameStatement) + "\n" +
                CreateJavaCode.createServiceInterfaceOfDelete(deleteEntityName) + "\n" +
                CreateJavaCode.createServiceInterfaceOfFind(findEntityName, entityName) + "\n" +
                CreateJavaCode.createServiceInterfaceOfFindPage(findEntityNamePage, entityName, searchDtoName, searchDtoNameStatement) + "\n";

        JSONObject javaCode = new JSONObject();
        javaCode.put("servicePackageName", servicePackageName);
        javaCode.put("importJava", importJava);
        javaCode.put("serviceName", serviceName);
        javaCode.put("entityId", "Long");
        javaCode.put("entityName", entityName);
        javaCode.put("searchDtoName", searchDtoName);
        javaCode.put("serviceInterface", serviceInterface);
        javaCode.put("createdTime", createdTime);


        String targetFile = FileUtils.createFiles(servicePackageName.replace(".", "/") + File.separator + serviceName + ".java");
        writerOut(templateEntity, targetFile, javaCode);
    }

    private void createServiceImpl(Template templateEntity, TableTrans tableTran, String createdTime, String projectName) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(projectName);
        String entityName = tableTran.getTableNameTrans();
        String entityNameStatement = CreateJavaCode.toLowerCaseFirstOne(entityName);
        //类路径
        String serviceImplPackageName = packagePath + ".service.impl";
        //定义引入jar包前缀
        String entityPackageName = packagePath + ".entity";
        String repositoryPackageName = packagePath + ".repo";
//        String servicePackageName = packagePath + ".service";
        String searchDtoPackageName = packagePath + ".dto";
        //声明引用类
        String repositoryName = entityName + "Repository";
        String repositoryNameStatement = CreateJavaCode.toLowerCaseFirstOne(repositoryName);
        String searchDtoName = "Search" + entityName + "Dto";
        String searchDtoNameStatement = CreateJavaCode.toLowerCaseFirstOne(searchDtoName);
        //类名
        String serviceImplName = entityName + "ServiceImpl";

        //父级类名称
        String serviceName = entityName + "Service";

        //定义方法
        String saveEntityName = "save" + entityName;
        String deleteEntityName = "delete" + entityName;
        String findEntityName = "find" + entityName;
        String findEntityNamePage = "find" + entityName + "Page";
        //定义变量
        String entityNameStatementDb = entityNameStatement + "Db";
        String entityNameStatementOptional = entityNameStatement + "Optional";
        //需要引入jar包前缀
        String importJava = CreateJavaCode.createImportJavaServiceImpl(packagePath,
                entityPackageName, entityName,
                searchDtoPackageName, searchDtoName,
                repositoryPackageName, repositoryName);
        //注解
        String lombokAnnotation = CreateJavaCode.createServiceImplAnnotation();
        //声明接口类变量
        String serviceStatementVariable = CreateJavaCode.createServiceStatementVariable(repositoryName, repositoryNameStatement);
        //接口方法
        String serviceImplInterface = CreateJavaCode.createBaseInterfaceOfSave(entityName, entityNameStatement, entityNameStatementOptional, entityNameStatementDb, repositoryNameStatement, tableTran.getColumnTrans()) + "\n" +
                CreateJavaCode.createBaseInterfaceOfDelete(entityName, entityNameStatement, entityNameStatementOptional, repositoryNameStatement) + "\n" +
                CreateJavaCode.createBaseInterfaceOfFindNotDelete(entityName, repositoryNameStatement) + "\n" +
                CreateJavaCode.createBaseInterfaceOfFindPage(entityName, entityNameStatement, searchDtoName, searchDtoNameStatement, repositoryNameStatement) + "\n" +
                CreateJavaCode.createServiceImplInterfaceOfSave(saveEntityName, entityName, repositoryNameStatement) + "\n" +
                CreateJavaCode.createServiceImplInterfaceOfDelete(deleteEntityName) + "\n" +
                CreateJavaCode.createServiceImplInterfaceOfFind(findEntityName, entityName) + "\n" +
                CreateJavaCode.createServiceImplInterfaceOfFindPage(findEntityNamePage, entityName, entityNameStatement, searchDtoName, searchDtoNameStatement) + "\n" +
                CreateJavaCode.createAllSpecification(entityName, entityNameStatement, searchDtoName, searchDtoNameStatement) + "\n";

        JSONObject javaCode = new JSONObject();
        javaCode.put("serviceImplPackageName", serviceImplPackageName);
        javaCode.put("importJava", importJava);
        javaCode.put("lombokAnnotation", lombokAnnotation);
        javaCode.put("serviceName", serviceName);
        javaCode.put("serviceImplName", serviceImplName);
        javaCode.put("serviceStatementVariable", serviceStatementVariable);
        javaCode.put("serviceImplInterface", serviceImplInterface);
        javaCode.put("createdTime", createdTime);

        String targetFile = FileUtils.createFiles(serviceImplPackageName.replace(".", "/") + File.separator + serviceImplName + ".java");
        writerOut(templateEntity, targetFile, javaCode);
    }

    private void createDto(Template templateEntity, TableTrans tableTran, String createdTime, String projectName) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(projectName);
        String entityName = tableTran.getTableNameTrans();
        String entityNameStatement = CamelCaseUtils.underlineToHump(entityName);
        //类路径
        String dtoPackageName = packagePath + ".dto";
        //定义引入jar包前缀
        String entityPackageName = packagePath + ".entity";
        //类名
        String dtoName = entityName + "Dto";
        //需要引入jar包前缀
        String importJava = CreateJavaCode.createImportJavaDto(packagePath, entityPackageName, entityName);

        //注解
        String lombokAnnotation = CreateJavaCode.createDtoAnnotation();
        //定义变量
        List<ColumnTrans> columnTrans = tableTran.getColumnTrans();
        String dtoNameStatement = CamelCaseUtils.underlineToHump(dtoName);
        StringBuilder fieldCode = new StringBuilder();
        StringBuilder dtoChangeEntity = new StringBuilder();
        StringBuilder entityChangeDto = new StringBuilder();
        for (ColumnTrans columnTran : columnTrans) {
            String columnName = columnTran.getColumnName();
            String columnType = columnTran.getColumnType();
            String columnRemarks = columnTran.getColumnRemarks();
            if (!Objects.equal("userId", columnName) && !Objects.equal("deleteState", columnName)) {
                fieldCode.append(CreateJavaCode.createRemarks(columnRemarks)).append(CreateJavaCode.createChangeLine());
                fieldCode.append(CreateJavaCode.createField(columnType, columnName)).append(CreateJavaCode.createChangeLine());
                dtoChangeEntity.append(CreateJavaCode.createTab(3)).append(".").append(columnName).append("(").append(dtoNameStatement).append(CreateJavaCode.createGet(columnName)).append(")").append("\n");
                entityChangeDto.append(CreateJavaCode.createTab(3)).append(".").append(columnName).append("(").append(entityNameStatement).append(CreateJavaCode.createGet(columnName)).append(")").append("\n");
            }
        }
        String entityNameListStatement = entityNameStatement + "List";
        //方法
        String dtoFun = CreateJavaCode.createDtoChangeEntity(dtoName, dtoNameStatement, entityName, dtoChangeEntity.toString()) + "\n" +
                CreateJavaCode.createEntityChangeDto(dtoName, entityName, entityNameStatement, entityChangeDto.toString()) + "\n" +
                CreateJavaCode.createEntityChangeListDto(dtoName, entityName, entityNameListStatement) + "\n";

        JSONObject javaCode = new JSONObject();
        javaCode.put("dtoPackageName", dtoPackageName);
        javaCode.put("importJava", importJava);
        javaCode.put("lombokAnnotation", lombokAnnotation);
        javaCode.put("dtoName", dtoName);
        javaCode.put("fieldCode", fieldCode.toString());
        javaCode.put("dtoFun", dtoFun);
        javaCode.put("createdTime", createdTime);
        String targetFile = FileUtils.createFiles(dtoPackageName.replace(".", "/") + File.separator + dtoName + ".java");
        writerOut(templateEntity, targetFile, javaCode);
    }

    private void createSearchDto(Template templateEntity, TableTrans tableTran, String createdTime, String projectName) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(projectName);
        String entityName = tableTran.getTableNameTrans();
        //类路径
        String searchDtoPackageName = packagePath + ".dto";
        //定义引入jar包前缀
//        String entityPackageName = packagePath + ".entity";
        //类名
        String searchDtoName = "Search" + entityName + "Dto";
        //需要引入jar包前缀
        String importJava = CreateJavaCode.createImportJavaSearchDto(packagePath);
        //注解
        String lombokAnnotation = CreateJavaCode.createSearchDtoAnnotation();
        //定义变量
        List<ColumnTrans> columnTrans = tableTran.getColumnTrans();
        StringBuilder fieldCode = new StringBuilder();
        for (ColumnTrans columnTran : columnTrans) {
            String columnName = columnTran.getColumnName();
            String columnType = columnTran.getColumnType();
            String columnRemarks = columnTran.getColumnRemarks();
            if (!Objects.equal("userId", columnName) && !Objects.equal("deleteState", columnName)) {
                if (columnTran.getColumnEqualSearch()) {
                    fieldCode.append(CreateJavaCode.createSearchEqualSearchDto(columnName, columnType, columnRemarks));
                }
                if (columnTran.getColumnRangeSearch()) {
                    fieldCode.append(CreateJavaCode.createSearchRangeSearchDto(columnName, columnType, columnRemarks));
                }
            }
        }

        JSONObject javaCode = new JSONObject();
        javaCode.put("searchDtoPackageName", searchDtoPackageName);
        javaCode.put("importJava", importJava);
        javaCode.put("lombokAnnotation", lombokAnnotation);
        javaCode.put("searchDtoName", searchDtoName);
        javaCode.put("fieldCode", fieldCode.toString());
        javaCode.put("createdTime", createdTime);
        String targetFile = FileUtils.createFiles(searchDtoPackageName.replace(".", "/") + File.separator + searchDtoName + ".java");
        writerOut(templateEntity, targetFile, javaCode);
    }

    private void createController(Template templateEntity, TableTrans tableTran, String createdTime, String projectName) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(projectName);
        String entityName = tableTran.getTableNameTrans();
        String entityNameStatement = CamelCaseUtils.underlineToHump(entityName);
        //类路径
        String controllerPackageName = packagePath + ".controller";
        //定义引入jar包前缀
        String entityPackageName = packagePath + ".entity";
        String servicePackageName = packagePath + ".service";
        String searchDtoPackageName = packagePath + ".dto";
        //声明引用类
        String serviceName = entityName + "Service";
        String serviceNameStatement = CamelCaseUtils.underlineToHump(serviceName);
        String searchDtoName = "Search" + entityName + "Dto";
        String searchDtoNameStatement = CreateJavaCode.toLowerCaseFirstOne(searchDtoName);
        //类名
        String controllerName = entityName + "Controller";

        //定义方法
        String saveEntityName = "save" + entityName;
        String deleteEntityName = "delete" + entityName;
        String findEntityName = "find" + entityName;
        String findEntityNamePage = "find" + entityName + "Page";
        //定义变量
        String entityDtoName = entityName + "Dto";
        String entityDtoNameStatement = CamelCaseUtils.underlineToHump(entityDtoName);

        //需要引入jar包前缀
        String importJava = CreateJavaCode.createImportJavaController(packagePath,
                entityPackageName, entityName,
                searchDtoPackageName, searchDtoName,
                servicePackageName, serviceName);

        //注解
        String lombokAnnotation = CreateJavaCode.createControllerAnnotation(entityNameStatement);

        //声明接口类变量
        String controllerStatementVariable = CreateJavaCode.createControllerStatementVariable(serviceName, serviceNameStatement);

        //接口方法
        String controllerInterface = CreateJavaCode.createControllerInterfaceOfSave(saveEntityName, entityDtoName, entityDtoNameStatement, serviceNameStatement, entityName, entityNameStatement) + "\n" +
                CreateJavaCode.createControllerInterfaceOfDelete(deleteEntityName, serviceNameStatement) + "\n" +
                CreateJavaCode.createControllerInterfaceOfFind(findEntityName, entityDtoName, entityDtoNameStatement, serviceNameStatement, entityName, entityNameStatement) + "\n" +
                CreateJavaCode.createControllerInterfaceOfFindPage(findEntityNamePage, entityDtoName, entityDtoNameStatement, serviceNameStatement, entityName, entityNameStatement, searchDtoName, searchDtoNameStatement) + "\n";

        JSONObject javaCode = new JSONObject();
        javaCode.put("controllerPackageName", controllerPackageName);
        javaCode.put("importJava", importJava);
        javaCode.put("lombokAnnotation", lombokAnnotation);
        javaCode.put("controllerName", controllerName);
        javaCode.put("controllerStatementVariable", controllerStatementVariable);
        javaCode.put("controllerInterface", controllerInterface);
        javaCode.put("createdTime", createdTime);

        String targetFile = FileUtils.createFiles(controllerPackageName.replace(".", "/") + File.separator + controllerName + ".java");
        writerOut(templateEntity, targetFile, javaCode);
    }

    public void writerOut(Template targetTemplate, String targetFile, JSONObject javaCode) throws IOException, TemplateException {
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8));
        targetTemplate.process(javaCode, out);
        out.flush();
        out.close();
    }


    public static void main(String[] args) {
        System.out.println();
        List<String> strings = new ArrayList<>();
        strings.add("utilsEncodesTemplate");
        for (String s : strings) {
            System.out.println(" Template " + s + " = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.get" + CreateJavaCode.toUpperCaseFirstOne(s) + "());");
        }
        System.out.println();
        System.out.println("   //类路径\n" +
                "JSONObject javaCode = new JSONObject();\n" +
                "javaCode.putAll(publicJavaPathCode);");
        System.out.println();
        for (String s : strings) {
            System.out.println(" String targetFile" + CreateJavaCode.toUpperCaseFirstOne(s) + " = FileUtils.createFiles(packagePathZ + File.separator + templatesProperties.get" + CreateJavaCode.toUpperCaseFirstOne(s) + "());");
        }
        System.out.println();
        for (String s : strings) {
            System.out.println("writerOut(" + s + ", targetFile" + CreateJavaCode.toUpperCaseFirstOne(s) + ", javaCode);");
        }
    }

}
