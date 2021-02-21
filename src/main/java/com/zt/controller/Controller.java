package com.zt.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;
import com.zt.controller.base.AbstractController;
import com.zt.entity.ColumnTrans;
import com.zt.entity.CommonModel;
import com.zt.entity.Table;
import com.zt.entity.TableTrans;
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

import javax.servlet.http.HttpServletRequest;
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

    private final CommonModel commonModel;

    private final FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    public Controller(TableService tableService, ColumnService columnService, CommonModel commonModel, FreeMarkerConfigurer freeMarkerConfigurer) {
        this.tableService = tableService;
        this.columnService = columnService;
        this.commonModel = commonModel;
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
    public JSONObject getTableTrans(HttpServletRequest request, HttpServletResponse response,
                                    @RequestBody List<TableTrans> tableTrans) throws IOException, TemplateException {
        System.out.println("tableTrans = " + tableTrans);
        Template templateEntity = freeMarkerConfigurer.getConfiguration().getTemplate(commonModel.getEntityTemplate());
        Template templateRepository = freeMarkerConfigurer.getConfiguration().getTemplate(commonModel.getRepositoryTemplate());
        Template templateService = freeMarkerConfigurer.getConfiguration().getTemplate(commonModel.getServiceTemplate());
        Template templateServiceImpl = freeMarkerConfigurer.getConfiguration().getTemplate(commonModel.getServiceImplTemplate());
        Template templateDto = freeMarkerConfigurer.getConfiguration().getTemplate(commonModel.getDtoTemplate());
        LocalDateTime currentDateTime = DateUtils.currentDateTime();
        String createdTime = DateUtils.formatDateTime(currentDateTime);
        for (TableTrans tableTran : tableTrans) {
            createEntity(templateEntity, tableTran, createdTime);
            createRepository(templateRepository, tableTran, createdTime);
            createService(templateService, tableTran, createdTime);
            createServiceImpl(templateServiceImpl, tableTran, createdTime);
            createDto(templateDto, tableTran, createdTime);
        }
        String downFileName = "test";
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/ms-word;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; fileName=" + downFileName);
        return success();
    }

    private void createEntity(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = tableTran.getPackagePath();
        boolean lombokState = tableTran.isLombokState();
        String tableName = tableTran.getTableName();
        String tableNameTrans = tableTran.getTableNameTrans();
        List<ColumnTrans> columnTrans = tableTran.getColumnTrans();
        StringBuilder fieldCode = new StringBuilder();
        StringBuilder fieldGetSetCode = new StringBuilder();
        StringBuilder lombokAnnotation = new StringBuilder();
        if (!lombokState) {
            for (ColumnTrans columnTran : columnTrans) {
                String columnName = columnTran.getColumnName();
                String columnType = columnTran.getColumnType();
                String columnRemarks = columnTran.getColumnRemarks();
                fieldCode.append(CreateJavaCode.createRemarks(columnRemarks)).append(CreateJavaCode.createChangeLine());
                fieldCode.append(CreateJavaCode.createField(columnType, columnName)).append(CreateJavaCode.createChangeLine());

                fieldGetSetCode.append(CreateJavaCode.createGetFun(columnType, columnName));
                fieldGetSetCode.append(CreateJavaCode.createChangeLine()).append(CreateJavaCode.createChangeLine());
                fieldGetSetCode.append(CreateJavaCode.createSetFun(columnType, columnName));
                fieldGetSetCode.append(CreateJavaCode.createChangeLine()).append(CreateJavaCode.createChangeLine());
            }
        } else {
            for (ColumnTrans columnTran : columnTrans) {
                String columnName = columnTran.getColumnName();
                String columnType = columnTran.getColumnType();
                String columnRemarks = columnTran.getColumnRemarks();


                fieldCode.append(CreateJavaCode.createRemarks(columnRemarks)).append(CreateJavaCode.createChangeLine());
                fieldCode.append(CreateJavaCode.createField(columnType, columnName)).append(CreateJavaCode.createChangeLine());

            }
            lombokAnnotation.append("@NoArgsConstructor");
            lombokAnnotation.append(CreateJavaCode.createChangeLine());
            lombokAnnotation.append("@AllArgsConstructor");
            lombokAnnotation.append(CreateJavaCode.createChangeLine());
            lombokAnnotation.append("@EqualsAndHashCode(callSuper = true)");
            lombokAnnotation.append(CreateJavaCode.createChangeLine());
            lombokAnnotation.append("@Data");
        }
        JSONObject javaCode = new JSONObject();
        String entityPackageName = packagePath + ".entity";
        javaCode.put("entityPackageName", entityPackageName);
        javaCode.put("tableName", tableName);
        javaCode.put("entityName", tableNameTrans);
        javaCode.put("fieldCode", fieldCode.toString());
        javaCode.put("fieldGetSetCode", fieldGetSetCode.toString());
        javaCode.put("lombokAnnotation", lombokAnnotation.toString());
        javaCode.put("createdTime", createdTime);


        String targetFile = FileUtils.createFiles(entityPackageName.replace(".", "/") + File.separator + tableNameTrans + ".java");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8));
        templateEntity.process(javaCode, out);
        out.flush();
        out.close();
    }

    private void createRepository(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = tableTran.getPackagePath();
        String entityName = tableTran.getTableNameTrans();

        boolean lombokState = tableTran.isLombokState();
        JSONObject javaCode = new JSONObject();

        String entityPackageName = packagePath + ".entity";
        String repositoryName = entityName + "Repository";
        String repositoryPackageName = packagePath + ".repo";

        javaCode.put("entityName", entityName);
        javaCode.put("entityPackageName", entityPackageName);
        javaCode.put("repositoryName", repositoryName);
        javaCode.put("repositoryPackageName", repositoryPackageName);
        javaCode.put("createdTime", createdTime);


        String targetFile = FileUtils.createFiles(repositoryPackageName.replace(".", "/") + File.separator + repositoryName + ".java");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
        templateEntity.process(javaCode, out);
        out.flush();
        out.close();
    }

    private void createService(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = tableTran.getPackagePath();
        String entityName = tableTran.getTableNameTrans();

        JSONObject javaCode = new JSONObject();

        String entityPackageName = packagePath + ".entity";
        String servicePackageName = packagePath + ".service";

        String serviceName = entityName + "Service";
        String entityNameStatement = CamelCaseUtils.underlineToHump(entityName);
        String saveEntityName = "save" + entityName;
        String deleteEntityName = "delete" + entityName;
        String findEntityName = "find" + entityName;
        String findEntityNamePage = "find" + entityName + "Page";

        javaCode.put("entityId", "Long");
        javaCode.put("entityName", entityName);
        javaCode.put("entityNameHump", CreateJavaCode.toLowerCaseFirstOne(entityName));
        javaCode.put("entityNameStatement", entityNameStatement);
        javaCode.put("saveEntityName", saveEntityName);
        javaCode.put("deleteEntityName", deleteEntityName);
        javaCode.put("findEntityName", findEntityName);
        javaCode.put("findEntityNamePage", findEntityNamePage);
        javaCode.put("entityPackageName", entityPackageName);
        javaCode.put("serviceName", serviceName);
        javaCode.put("servicePackageName", servicePackageName);
        javaCode.put("createdTime", createdTime);


        String targetFile = FileUtils.createFiles(servicePackageName.replace(".", "/") + File.separator + serviceName + ".java");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8));
        templateEntity.process(javaCode, out);
        out.flush();
        out.close();
    }

    private void createServiceImpl(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = tableTran.getPackagePath();
        String entityName = tableTran.getTableNameTrans();
        boolean lombokState = tableTran.isLombokState();
        List<ColumnTrans> columnTrans = tableTran.getColumnTrans();
        JSONObject javaCode = new JSONObject();

        StringBuilder entitySaveLogic = new StringBuilder();

        String entityPackageName = packagePath + ".entity";
        String repositoryPackageName = packagePath + ".repo";
        String servicePackageName = packagePath + ".service";
        String serviceImplPackageName = packagePath + ".service.impl";
        String repositoryName = entityName + "Repository";
        String serviceName = entityName + "Service";
        String serviceImplName = entityName + "ServiceImpl";
        String entityNameStatement = CamelCaseUtils.underlineToHump(entityName);
        String entityNameStatementDb = entityNameStatement + "Db";
        String entityNameStatementOptional = entityNameStatement + "Optional";
        String saveEntityName = "save" + entityName;
        String deleteEntityName = "delete" + entityName;
        String findEntityName = "find" + entityName;
        String findEntityNamePage = "find" + entityName + "Page";
        String repositoryNameStatement = CreateJavaCode.toLowerCaseFirstOne(repositoryName);
        String autowired = "";
        String finalStr = "";
        StringBuilder lombokAnnotation = new StringBuilder();
        if (!lombokState) {
            autowired = "@Autowired";
        } else {
            lombokAnnotation.append("@AllArgsConstructor");
            finalStr = "final";
        }

        javaCode.put("entityName", entityName);
        javaCode.put("entityNameHump", CreateJavaCode.toLowerCaseFirstOne(entityName));
        javaCode.put("entityNameStatement", entityNameStatement);
        javaCode.put("entityNameStatementDb", entityNameStatementDb);
        javaCode.put("entityNameStatementOptional", entityNameStatementOptional);
        javaCode.put("saveEntityName", saveEntityName);
        javaCode.put("deleteEntityName", deleteEntityName);
        javaCode.put("findEntityName", findEntityName);
        javaCode.put("findEntityNamePage", findEntityNamePage);
        javaCode.put("entityPackageName", entityPackageName);
        javaCode.put("repositoryName", repositoryName);
        javaCode.put("repositoryPackageName", repositoryPackageName);
        javaCode.put("serviceName", serviceName);
        javaCode.put("servicePackageName", servicePackageName);
        javaCode.put("serviceImplPackageName", serviceImplPackageName);
        javaCode.put("serviceImplName", serviceImplName);
        javaCode.put("autowired", autowired);
        javaCode.put("finalStr", finalStr);
        javaCode.put("repositoryNameStatement", repositoryNameStatement);
        javaCode.put("lombokAnnotation", lombokAnnotation.toString());
        javaCode.put("createdTime", createdTime);

        for (ColumnTrans columnTran : columnTrans) {
            entitySaveLogic.append(CreateJavaCode.createSaveLogic(entityNameStatement, entityNameStatementDb, columnTran)).append(CreateJavaCode.createChangeLine());
        }
        javaCode.put("entitySaveLogic", entitySaveLogic.toString());

        String targetFile = FileUtils.createFiles(serviceImplPackageName.replace(".", "/") + File.separator + serviceImplName + ".java");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8));
        templateEntity.process(javaCode, out);
        out.flush();
        out.close();
    }

    private void createDto(Template templateEntity, TableTrans tableTran, String createdTime) throws IOException, TemplateException {
        String packagePath = tableTran.getPackagePath();
        boolean lombokState = tableTran.isLombokState();
        List<ColumnTrans> columnTrans = tableTran.getColumnTrans();
        String entityName = tableTran.getTableNameTrans();
        String entityNameStatement = CamelCaseUtils.underlineToHump(entityName);
        String entityDtoName = entityName + "Dto";
        String entityDtoNameStatement = CamelCaseUtils.underlineToHump(entityDtoName);
        String entityNameListStatement = entityNameStatement + "List";
        StringBuilder fieldCode = new StringBuilder();
        StringBuilder fieldGetSetCode = new StringBuilder();
        StringBuilder lombokAnnotation = new StringBuilder();
        StringBuilder dtoChangeEntity = new StringBuilder();
        StringBuilder entityChangeDto = new StringBuilder();
        StringBuilder entityChangeListDto = new StringBuilder();

        if (!lombokState) {
            for (ColumnTrans columnTran : columnTrans) {
                String columnName = columnTran.getColumnName();
                String columnType = columnTran.getColumnType();
                String columnRemarks = columnTran.getColumnRemarks();
                fieldCode.append(CreateJavaCode.createRemarks(columnRemarks)).append(CreateJavaCode.createChangeLine());
                fieldCode.append(CreateJavaCode.createField(columnType, columnName)).append(CreateJavaCode.createChangeLine());

                fieldGetSetCode.append(CreateJavaCode.createGetFun(columnType, columnName));
                fieldGetSetCode.append(CreateJavaCode.createChangeLine()).append(CreateJavaCode.createChangeLine());
                fieldGetSetCode.append(CreateJavaCode.createSetFun(columnType, columnName));
                fieldGetSetCode.append(CreateJavaCode.createChangeLine()).append(CreateJavaCode.createChangeLine());
            }
        } else {
            dtoChangeEntity.append(CreateJavaCode.createDtoChangeEntity(entityDtoName, entityDtoNameStatement, entityName));
            entityChangeDto.append(CreateJavaCode.createEntityChangeDto(entityDtoName, entityName, entityNameStatement));
            dtoChangeEntity.append(CreateJavaCode.createBuilder(entityName));
            entityChangeDto.append(CreateJavaCode.createBuilder(entityDtoName));
            entityChangeListDto.append(CreateJavaCode.createEntityChangeListDto(entityDtoName, entityName, entityNameListStatement));
            for (ColumnTrans columnTran : columnTrans) {
                String columnName = columnTran.getColumnName();
                String columnType = columnTran.getColumnType();
                String columnRemarks = columnTran.getColumnRemarks();
                if (!Objects.equal("userId", columnName) && !Objects.equal("deleteState", columnName)) {
                    fieldCode.append(CreateJavaCode.createRemarks(columnRemarks)).append(CreateJavaCode.createChangeLine());
                    fieldCode.append(CreateJavaCode.createField(columnType, columnName)).append(CreateJavaCode.createChangeLine());
                    dtoChangeEntity.append(CreateJavaCode.createTab(3)).append(".").append(columnName).append("(").append(entityDtoNameStatement).append(CreateJavaCode.createGet(columnName)).append(")").append("\n");
                    entityChangeDto.append(CreateJavaCode.createTab(3)).append(".").append(columnName).append("(").append(entityDtoNameStatement).append(CreateJavaCode.createGet(columnName)).append(")").append("\n");
                }
            }
            dtoChangeEntity.append(CreateJavaCode.createBuild());
            entityChangeDto.append(CreateJavaCode.createBuild());
            dtoChangeEntity.append(CreateJavaCode.createTab(1)).append(CreateJavaCode.createEndBrackets());
            entityChangeDto.append(CreateJavaCode.createTab(1)).append(CreateJavaCode.createEndBrackets());


            lombokAnnotation.append("@Builder");
            lombokAnnotation.append(CreateJavaCode.createChangeLine());
            lombokAnnotation.append("@Getter");
            lombokAnnotation.append(CreateJavaCode.createChangeLine());
            lombokAnnotation.append("@Setter");
            lombokAnnotation.append(CreateJavaCode.createChangeLine());
            lombokAnnotation.append("@JsonIgnoreProperties(ignoreUnknown = true)");
        }
        JSONObject javaCode = new JSONObject();
        String entityPackageName = packagePath + ".dto";
        javaCode.put("entityPackageName", entityPackageName);
        javaCode.put("entityName", entityName);
        javaCode.put("fieldCode", fieldCode.toString());
        javaCode.put("lombokAnnotation", lombokAnnotation.toString());
        javaCode.put("dtoChangeEntity", dtoChangeEntity.toString());
        javaCode.put("entityChangeDto", entityChangeDto.toString());
        javaCode.put("entityChangeListDto", entityChangeListDto.toString());
        javaCode.put("createdTime", createdTime);


        String targetFile = FileUtils.createFiles(entityPackageName.replace(".", "/") + File.separator + entityDtoName + ".java");
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8));
        templateEntity.process(javaCode, out);
        out.flush();
        out.close();
    }

}
