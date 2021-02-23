package com.zt.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.zt.controller.base.AbstractController;
import com.zt.entity.ColumnTrans;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/11 0:06
 * description:
 */
@RestController
public class Controller extends AbstractController {

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
    public JSONObject showTable() {
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
    public JSONObject showColumn(@RequestParam List<String> tableNames) {
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
    public JSONObject getTableTrans(HttpServletResponse response,
                                    @RequestBody List<TableTrans> tableTrans) throws IOException, TemplateException {
        System.out.println("tableTrans = " + tableTrans);
        Template templateEntity = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getEntityTemplate());
        Template templateRepository = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getRepositoryTemplate());
        Template templateService = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getServiceTemplate());
        Template templateServiceImpl = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getServiceImplTemplate());
        Template templateDto = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getDtoTemplate());
        Template templateSearchDto = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getSearchDtoTemplate());
        Template templateController = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getControllerTemplate());
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        String createdTime = DateUtils.formatDateTime(currentDateTime);
        for (TableTrans tableTran : tableTrans) {
            createBase(tableTran, createdTime);
            createEntity(templateEntity, tableTran, createdTime);
            createRepository(templateRepository, tableTran, createdTime);
            createService(templateService, tableTran, createdTime);
            createServiceImpl(templateServiceImpl, tableTran, createdTime);
            createDto(templateDto, tableTran, createdTime);
            createSearchDto(templateSearchDto, tableTran, createdTime);
            createController(templateController, tableTran, createdTime);
        }
        String downFileName = "test";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/ms-word;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; fileName=" + downFileName);
        return success();
    }

    private void createBase(TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        Template baseBaseResultMessageTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseBaseResultMessageTemplate());
        Template baseResultMessageTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseResultMessageTemplate());
        Template basePageEntityTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBasePageEntityTemplate());
        Template baseBaseServiceTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseBaseServiceTemplate());
        Template baseConstantServiceTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseConstantServiceTemplate());
        Template baseInterfaceBaseServiceTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBaseInterfaceBaseServiceTemplate());
        Template basePageUtilsTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(templatesProperties.getBasePageUtilsTemplate());

        String packagePath = CreateJavaCode.createPackagePath(tableTran.getProjectName());

        //类路径
        String baseControllerPackageName = packagePath + ".base.controller";
        String baseEntityPackageName = packagePath + ".base.entity";
        String baseServicePackageName = packagePath + ".base.service";
        JSONObject javaCode = new JSONObject();
        javaCode.put("createdTime", createdTime);
        javaCode.put("baseControllerPackageName", baseControllerPackageName);
        javaCode.put("baseEntityPackageName", baseEntityPackageName);
        javaCode.put("baseServicePackageName", baseServicePackageName);

        String targetFileBaseBaseResultMessageTemplate = FileUtils.createFiles(baseControllerPackageName.replace(".", "/") + File.separator + "BaseResultMessage.java");
        String targetFileBaseResultMessageTemplate = FileUtils.createFiles(baseControllerPackageName.replace(".", "/") + File.separator + "ResultMessage.java");
        String targetFileBasePageEntityTemplate = FileUtils.createFiles(baseEntityPackageName.replace(".", "/") + File.separator + "PageEntity.java");
        String targetFileBaseBaseServiceTemplate = FileUtils.createFiles(baseServicePackageName.replace(".", "/") + File.separator + "BaseService.java");
        String targetFileBaseConstantServiceTemplate = FileUtils.createFiles(baseServicePackageName.replace(".", "/") + File.separator + "ConstantService.java");
        String targetFileBaseInterfaceBaseServiceTemplate = FileUtils.createFiles(baseServicePackageName.replace(".", "/") + File.separator + "InterfaceBaseService.java");
        String targetFileBasePageUtilsTemplate = FileUtils.createFiles(baseServicePackageName.replace(".", "/") + File.separator + "PageUtils.java");

        writerOut(baseBaseResultMessageTemplate, targetFileBaseBaseResultMessageTemplate, javaCode);
        writerOut(baseResultMessageTemplate, targetFileBaseResultMessageTemplate, javaCode);
        writerOut(basePageEntityTemplate, targetFileBasePageEntityTemplate, javaCode);
        writerOut(baseBaseServiceTemplate, targetFileBaseBaseServiceTemplate, javaCode);
        writerOut(baseConstantServiceTemplate, targetFileBaseConstantServiceTemplate, javaCode);
        writerOut(baseInterfaceBaseServiceTemplate, targetFileBaseInterfaceBaseServiceTemplate, javaCode);
        writerOut(basePageUtilsTemplate, targetFileBasePageUtilsTemplate, javaCode);
    }

    private void createEntity(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(tableTran.getProjectName());
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

    private void createRepository(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(tableTran.getProjectName());
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

    private void createService(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(tableTran.getProjectName());
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
        javaCode.put("serviceInterface", serviceInterface);
        javaCode.put("createdTime", createdTime);


        String targetFile = FileUtils.createFiles(servicePackageName.replace(".", "/") + File.separator + serviceName + ".java");
        writerOut(templateEntity, targetFile, javaCode);
    }

    private void createServiceImpl(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(tableTran.getProjectName());
        String entityName = tableTran.getTableNameTrans();
        String entityNameStatement = CamelCaseUtils.underlineToHump(entityName);
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
                CreateJavaCode.createAllSpecification(entityName, entityNameStatement) + "\n";

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

    private void createDto(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(tableTran.getProjectName());
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
                entityChangeDto.append(CreateJavaCode.createTab(3)).append(".").append(columnName).append("(").append(dtoNameStatement).append(CreateJavaCode.createGet(columnName)).append(")").append("\n");
            }
        }
        String entityNameListStatement = entityNameStatement + "List";
        //方法
        String dtoFun = CreateJavaCode.createDtoChangeEntity(dtoName, dtoNameStatement, entityName, dtoChangeEntity.toString()) + "\n" +
                CreateJavaCode.createEntityChangeDto(dtoName, dtoNameStatement, entityName, entityChangeDto.toString()) + "\n" +
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

    private void createSearchDto(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(tableTran.getProjectName());
        String entityName = tableTran.getTableNameTrans();
        //类路径
        String searchDtoPackageName = packagePath + ".dto";
        //定义引入jar包前缀
//        String entityPackageName = packagePath + ".entity";
        //类名
        String searchDtoName = "Search" + entityName + "Dto";
        //需要引入jar包前缀
        String importJava = CreateJavaCode.createImportJavaSearchDto();
        //注解
        String lombokAnnotation = CreateJavaCode.createDtoAnnotation();
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

    private void createController(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = CreateJavaCode.createPackagePath(tableTran.getProjectName());
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
}
