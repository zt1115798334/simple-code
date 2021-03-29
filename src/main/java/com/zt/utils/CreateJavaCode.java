package com.zt.utils;

import com.google.common.base.Objects;
import com.zt.entity.ColumnTrans;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 10:07
 * description:
 */
public class CreateJavaCode {

    public static String createTab(int number) {
        return IntStream.range(0, number).boxed().map(i -> "\t").collect(Collectors.joining());
    }

    public static String createEndBrackets() {
        return "}";
    }

    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);

    }

    public static String createPackagePath(String projectName) {
        return SysCon.PACKAG_PREFIXE + "." + projectName;
    }

    public static String createFieldId() {
        return createTab(1) + "@Id\n" +
                createTab(1) + "@GeneratedValue(strategy = GenerationType.IDENTITY)\n";
    }

    public static String createRemarks(String remarks) {
        return createTab(1) + "/**\n" +
                createTab(1) + "* " + remarks + "\n" +
                createTab(1) + "*/";
    }

    public static String createField(String typeNameTrans, String columnName) {
        return createTab(1) + "private " + typeNameTrans + " " + columnName + ";";
    }

    public static String createGetFun(String columnType, String columnName) {
        String columnNameUpperCase = toUpperCaseFirstOne(columnName);
        return createTab(1) + "public " + columnType + " get" + columnNameUpperCase + "() {\n" +
                createTab(2) + "return " + columnName + ";\n" +
                createTab(1) + "}";
    }

    public static String createSetFun(String columnType, String columnName) {
        String columnNameUpperCase = toUpperCaseFirstOne(columnName);
        return createTab(1) + "public void set" + columnNameUpperCase + "(" + columnType + " " + columnName + ") {\n" +
                createTab(2) + "this." + columnName + " = " + columnName + ";\n" +
                createTab(1) + "}";
    }

    public static String createGet(String columnName) {
        return ".get" + toUpperCaseFirstOne(columnName) + "()";
    }

    public static String createSet(String columnName, String value) {
        return ".get" + toUpperCaseFirstOne(columnName) + "(" + value + ")";
    }

    public static String createChangeLine() {
        return "\n";
    }

    public static String createSaveLogic(String entityNameStatement, String entityNameStatementDb, ColumnTrans columnTran) {
        return createTab(3) + entityNameStatementDb + ".set" + toUpperCaseFirstOne(columnTran.getColumnNameTrans()) + "(" + entityNameStatement + ".get" + toUpperCaseFirstOne(columnTran.getColumnNameTrans()) + "());";
    }

    public static String createBuilder(String entityName) {
        return createTab(2) + "return " + entityName + ".builder()\n";
    }

    public static String createBuild() {
        return createTab(3) + ".build();\n";
    }

    public static String createServiceStatementVariable(String repositoryName, String repositoryNameStatement) {
        return createTab(1) + "private final " + repositoryName + " " + repositoryNameStatement + ";\n";
    }

    public static String createControllerStatementVariable(String serviceName, String serviceNameStatement) {
        return createTab(1) + "private final " + serviceName + " " + serviceNameStatement + ";\n";
    }

    public static String createBaseInterfaceOfSave(String entityName, String entityNameStatement, String entityNameStatementOptional, String entityNameStatementDb,
                                                   String repositoryNameStatement, List<ColumnTrans> columnTrans) {
        StringBuilder entitySaveLogic = new StringBuilder();

        for (ColumnTrans columnTran : columnTrans) {
            if (!Objects.equal(columnTran.getColumnNameTrans(), "id") &&
                    !Objects.equal(columnTran.getColumnNameTrans(), "created_time") &&
                    !Objects.equal(columnTran.getColumnNameTrans(), "delete_state")) {
                entitySaveLogic.append(CreateJavaCode.createSaveLogic(entityNameStatement, entityNameStatementDb, columnTran)).append(CreateJavaCode.createChangeLine());
            }
        }
        String setCreatedTime = columnTrans.stream().anyMatch(ct -> Objects.equal(ct.getColumnNameTrans(), "createdTime")) ?
                createTab(3) + "" + entityNameStatement + ".setCreatedTime(DateUtils.currentDateTime());\n" : "";
        String setDeleteState = columnTrans.stream().anyMatch(ct -> Objects.equal(ct.getColumnNameTrans(), "deleteState")) ?
                createTab(3) + "" + entityNameStatement + ".setDeleteState(UN_DELETED);\n" : "";
        return createTab(1) + "@Override\n" +
                createTab(1) + "@Transactional(rollbackFor = RuntimeException.class)\n" +
                createTab(1) + "public " + entityName + " save(" + entityName + " " + entityNameStatement + ") {\n" +
                createTab(2) + "Long id = " + entityNameStatement + ".getId();\n" +
                createTab(2) + "if (id != null && id != 0L) {\n" +
                createTab(3) + "Optional<" + entityName + "> " + entityNameStatementOptional + " = " + repositoryNameStatement + ".findById(id);\n" +
                createTab(3) + "" + entityName + " " + entityNameStatementDb + " = " + entityNameStatementOptional + ".orElseThrow(() -> new OperationException(\"已删除\"));\n" +
                entitySaveLogic +
                createTab(3) + "return " + repositoryNameStatement + ".save(" + entityNameStatementDb + ");\n" +
                createTab(2) + "} else {\n" +
                setCreatedTime +
                setDeleteState +
                createTab(3) + "return " + repositoryNameStatement + ".save(" + entityNameStatement + ");\n" +
                createTab(2) + "}\n" +
                createTab(1) + "}\n";
    }

    public static String createBaseInterfaceOfDelete(String entityName, String entityNameStatement, String entityNameStatementOptional,
                                                     String repositoryNameStatement) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "@Transactional(rollbackFor = RuntimeException.class)\n" +
                createTab(1) + "public void deleteById(Long id) {\n" +
                createTab(2) + "Optional<" + entityName + "> " + entityNameStatementOptional + " = " + repositoryNameStatement + ".findById(id);\n" +
                createTab(2) + "" + entityNameStatementOptional + ".ifPresent(" + entityNameStatement + " -> {\n" +
                createTab(3) + "" + entityNameStatement + ".setDeleteState(DELETED);\n" +
                createTab(3) + "" + repositoryNameStatement + ".save(" + entityNameStatement + ");\n" +
                createTab(2) + "});\n" +
                createTab(1) + "}\n";
    }

    public static String createBaseInterfaceOfFindNotDelete(String entityName, String repositoryNameStatement) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)\n" +
                createTab(1) + "public Optional<" + entityName + "> findByIdNotDelete(Long id) {\n" +
                createTab(2) + "return " + repositoryNameStatement + ".findByIdAndDeleteState(id, UN_DELETED);\n" +
                createTab(1) + "}\n";
    }

    public static String createBaseInterfaceOfFindPage(String entityName, String entityNameStatement,
                                                       String searchDtoName, String searchDtoNameStatement,
                                                       String repositoryNameStatement) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)\n" +
                createTab(1) + "public Page<" + entityName + "> findPageByEntity(" + searchDtoName + " " + searchDtoNameStatement + ") {\n" +
                createTab(2) + "Specification<" + entityName + "> specification = this.getAllSpecification(" + searchDtoNameStatement + ");\n" +
                createTab(2) + "Pageable pageable = PageUtils.buildPageRequest(" + searchDtoNameStatement + ");\n" +
                createTab(2) + "return " + repositoryNameStatement + ".findAll(specification, pageable);\n" +
                createTab(1) + "}\n";
    }

    public static String createAllSpecification(String entityName, String entityNameStatement, String searchDtoName, String searchDtoNameStatement, List<ColumnTrans> columnTrans) {
        StringBuilder sb = new StringBuilder();
        for (ColumnTrans columnTran : columnTrans) {
            String columnNameTrans = columnTran.getColumnNameTrans();
            if (columnTran.getColumnEqualSearch()) {
                sb.append(".equal(\"").append(columnNameTrans).append("\", ").append(searchDtoNameStatement).append(createGet(columnNameTrans)).append(")\n");
            }
            if (columnTran.getColumnRangeSearch()) {
//                sb.append();
            }
        }
        return createTab(1) + "private Specification<" + entityName + "> getAllSpecification(" + searchDtoName + " " + searchDtoNameStatement + ") {\n" +
                createTab(2) + "return Specifications.<" + entityName + ">and()\n" +
                sb.toString() +
                createTab(2) + ".equal(\"deleteState\", UN_DELETED)\n" +
                createTab(2) + ".equal(\"userId\", " + searchDtoNameStatement + ".getUserId())\n" +
                createTab(2) + ".build();\n" +
                createTab(1) + "}\n";
    }

    public static String createServiceImplInterfaceOfSave(String saveEntityName, String entityName, String entityNameStatement) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "public " + entityName + " " + saveEntityName + "(" + entityName + " " + entityNameStatement + "){\n" +
                createTab(2) + "return this.save(" + entityNameStatement + ");\n" +
                createTab(1) + "}\n";
    }

    public static String createServiceImplInterfaceOfDelete(String deleteEntityName) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "public void " + deleteEntityName + "(Long id){\n" +
                createTab(2) + "this.deleteById(id);\n" +
                createTab(1) + "}\n";
    }

    public static String createServiceImplInterfaceOfFind(String findEntityName, String entityName) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "public " + entityName + " " + findEntityName + "(Long id){\n" +
                createTab(2) + "return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException(\"已删除\"));\n" +
                createTab(1) + "}\n";
    }

    public static String createServiceImplInterfaceOfFindPage(String findEntityNamePage,
                                                              String entityName, String entityNameStatement,
                                                              String searchDtoName, String searchDtoNameStatement) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "public Page<" + entityName + "> " + findEntityNamePage + "(" + searchDtoName + " " + searchDtoNameStatement + "){\n" +
                createTab(2) + "return this.findPageByEntity(" + searchDtoNameStatement + ");\n" +
                createTab(1) + "}\n";
    }

    public static String createServiceInterfaceOfSave(String saveEntityName, String entityName, String entityNameStatement) {
        return createTab(1) + entityName + " " + saveEntityName + "(" + entityName + " " + entityNameStatement + ");\n";
    }

    public static String createServiceInterfaceOfDelete(String deleteEntityName) {
        return createTab(1) + "void " + deleteEntityName + "(Long id);\n";
    }

    public static String createServiceInterfaceOfFind(String findEntityName, String entityName) {
        return createTab(1) + entityName + " " + findEntityName + "(Long id);\n";
    }

    public static String createServiceInterfaceOfFindPage(String findEntityNamePage,
                                                          String entityName,
                                                          String searchDtoName, String searchDtoNameStatement) {
        return createTab(1) + "Page<" + entityName + "> " + findEntityNamePage + "(" + searchDtoName + " " + searchDtoNameStatement + ");\n";
    }

    public static String createDtoChangeEntity(String entityDtoName, String entityDtoNameStatement, String entityName, String dtoChangeEntity) {
        return createTab(1) + "public static " + entityName + " dtoChangeEntity(" + entityDtoName + " " + entityDtoNameStatement + ") {\n" +
                CreateJavaCode.createBuilder(entityName) + dtoChangeEntity + CreateJavaCode.createBuild() + createTab(1) + "}\n";
    }

    public static String createEntityChangeDto(String entityDtoName, String entityName, String entityNameStatement, String entityChangeDto) {
        return createTab(1) + "public static " + entityDtoName + " entityChangeDto(" + entityName + " " + entityNameStatement + ") {\n" +
                CreateJavaCode.createBuilder(entityDtoName) + entityChangeDto + CreateJavaCode.createBuild() + createTab(1) + "}\n";
    }

    public static String createEntityChangeListDto(String entityDtoName, String entityName, String entityNameListStatement) {
        return createTab(1) + "public static List<" + entityDtoName + "> entityChangeListDto(List<" + entityName + "> " + entityNameListStatement + ") {\n" +
                createTab(2) + "return " + entityNameListStatement + ".stream().map(e -> " + entityDtoName + ".entityChangeDto(e)).collect(Collectors.toList());\n" +
                createTab(1) + "}\n";
    }

    public static String createSearchRangeSearchDto(String columnName, String columnType, String columnRemarks) {
        return CreateJavaCode.createRemarks(columnRemarks + "--开始") + "\n" +
                createTab(1) + "private " + columnType + " " + columnName + "Start;\n" +
                CreateJavaCode.createRemarks(columnRemarks + "--结束") + "\n" +
                createTab(1) + "private " + columnType + " " + columnName + "End;\n";
    }

    public static String createSearchEqualSearchDto(String columnName, String columnType, String columnRemarks) {
        return CreateJavaCode.createRemarks(columnRemarks) + "\n" +
                createTab(1) + "private " + columnType + " " + columnName + ";\n";
    }

    public static String createControllerInterfaceOfSave(String saveEntityName,
                                                         String entityDtoName, String entityDtoNameStatement,
                                                         String serviceNameStatement,
                                                         String entityName, String entityNameStatement) {
        return createTab(1) + "@SaveLog(desc = \"\")\n" +
                createTab(1) + "@PostMapping(value = \"" + saveEntityName + "\")\n" +
                createTab(1) + "public ResultMessage " + saveEntityName + "(@RequestBody " + entityDtoName + " " + entityDtoNameStatement + ") {\n" +
                createTab(2) + "" + entityName + " " + entityNameStatement + " = " + serviceNameStatement + "." + saveEntityName + "(" + entityDtoName + ".dtoChangeEntity(" + entityDtoNameStatement + "));\n" +
                createTab(2) + "return success(" + entityDtoName + ".entityChangeDto(" + entityNameStatement + "));\n" +
                createTab(1) + "}\n";
    }

    public static String createControllerInterfaceOfDelete(String deleteEntityName,
                                                           String serviceNameStatement) {
        return createTab(1) + "@SaveLog(desc = \"\")\n" +
                createTab(1) + "@PostMapping(value = \"" + deleteEntityName + "\")\n" +
                createTab(1) + "public ResultMessage " + deleteEntityName + "(@RequestParam Long id) {\n" +
                createTab(2) + "" + serviceNameStatement + "." + deleteEntityName + "(id);\n" +
                createTab(2) + "return success();\n" +
                createTab(1) + "}\n";
    }

    public static String createControllerInterfaceOfFind(String findEntityName,
                                                         String entityDtoName, String entityDtoNameStatement,
                                                         String serviceNameStatement,
                                                         String entityName, String entityNameStatement) {
        return createTab(1) + "@PostMapping(value = \"" + findEntityName + "\")\n" +
                createTab(1) + "public ResultMessage " + findEntityName + "(@RequestParam Long id) {\n" +
                createTab(2) + "" + entityName + " " + entityNameStatement + " = " + serviceNameStatement + "." + findEntityName + "(id);\n" +
                createTab(2) + "return success(" + entityDtoName + ".entityChangeDto(" + entityNameStatement + "));\n" +
                createTab(1) + "}\n";
    }

    public static String createControllerInterfaceOfFindPage(String findEntityNamePage,
                                                             String entityDtoName, String entityDtoNameStatement,
                                                             String serviceNameStatement,
                                                             String entityName, String entityNameStatement,
                                                             String searchDtoName, String searchDtoNameStatement) {
        return createTab(1) + " @PostMapping(value = \"" + findEntityNamePage + "\")\n" +
                createTab(1) + "public ResultMessage " + findEntityNamePage + "(@RequestBody " + searchDtoName + " " + searchDtoNameStatement + ") {\n" +
                createTab(2) + "Page<" + entityName + "> " + entityNameStatement + "Page = " + serviceNameStatement + "." + findEntityNamePage + "(" + searchDtoNameStatement + ");\n" +
                createTab(2) + "return success(" + searchDtoNameStatement + ".getPageNumber(), " + searchDtoNameStatement + ".getPageSize(), " + entityNameStatement + "Page.getTotalElements(),\n" +
                createTab(2) + "" + entityDtoName + ".entityChangeListDto(" + entityNameStatement + "Page.getContent()));\n" +
                createTab(1) + "}\n";
    }


    public static String createEntityAnnotation(String tableName) {
        return "@NoArgsConstructor\n" +
                "@AllArgsConstructor\n" +
                "@Builder\n" +
                "@Data\n" +
                "@Entity\n" +
                "@Table(name = \"" + tableName + "\")";
    }

    public static String createServiceImplAnnotation() {
        return "@AllArgsConstructor\n" +
                "@Service\n";
    }

    public static String createDtoAnnotation() {
        return "@NoArgsConstructor\n" +
                "@AllArgsConstructor\n" +
                "@Builder\n" +
                "@Data\n" +
                "@JsonIgnoreProperties(ignoreUnknown = true)";
    }

    public static String createSearchDtoAnnotation() {
        return
//                "@NoArgsConstructor\n" +
//                "@AllArgsConstructor\n" +
                "@Builder\n" +
                        "@EqualsAndHashCode(callSuper = true)\n" +
                        "@Data\n" +
                        "@JsonIgnoreProperties(ignoreUnknown = true)";
    }

    public static String createControllerAnnotation(String entityNameStatement) {
        return "@Validated\n" +
                "@AllArgsConstructor\n" +
                "@RestController\n" +
                "@RequestMapping(\"api/" + entityNameStatement + "\")";
    }

    public static String createImportJavaEntity(String entityPackageName, String entityName) {
        return "import lombok.AllArgsConstructor;\n" +
                "import lombok.Builder;\n" +
                "import lombok.Data;\n" +
                "import lombok.EqualsAndHashCode;\n" +
                "import lombok.NoArgsConstructor;\n" +
                "\n" +
                "import javax.persistence.*;\n" +
                "import java.time.LocalDateTime;";
    }

    public static String createImportJavaRepository(String entityPackageName, String entityName) {
        return "import " + entityPackageName + "." + entityName + ";\n" +
                "import org.springframework.data.jpa.repository.JpaSpecificationExecutor;\n" +
                "import org.springframework.data.repository.CrudRepository;\n" +
                "\n" +
                "import java.util.Optional;";
    }

    public static String createImportJavaService(String packagePath,
                                                 String entityPackageName, String entityName,
                                                 String searchDtoPackageName, String searchDtoName) {
        return "import " + packagePath + ".base.service.BaseService;\n" +
                "import " + searchDtoPackageName + "." + searchDtoName + ";\n" +
                "import " + entityPackageName + "." + entityName + ";\n" +
                "import org.springframework.data.domain.Page;\n" +
                "\n" +
                "import java.util.List;";
    }

    public static String createImportJavaServiceImpl(String packagePath,
                                                     String entityPackageName, String entityName,
                                                     String searchDtoPackageName, String searchDtoName,
                                                     String repositoryPackageName, String repositoryName) {
        return "\n" +
                "import " + packagePath + ".base.service.PageUtils;\n" +
                "import " + entityPackageName + "." + entityName + ";\n" +
                "import " + searchDtoPackageName + "." + searchDtoName + ";\n" +
                "import " + packagePath + ".exception.OperationException;\n" +
                "import " + repositoryPackageName + "." + repositoryName + ";\n" +
                "import " + packagePath + ".service.*;\n" +
                "import " + packagePath + ".specification.Specifications;\n" +
                "import " + packagePath + ".utils.DateUtils;\n" +
                "import lombok.AllArgsConstructor;\n" +
                "import org.springframework.data.domain.Page;\n" +
                "import org.springframework.data.domain.Pageable;\n" +
                "import org.springframework.data.jpa.domain.Specification;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import org.springframework.transaction.annotation.Propagation;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "\n" +
                "import java.util.Optional;";
    }

    public static String createImportJavaDto(String packagePath,
                                             String entityPackageName, String entityName) {
        return "import com.alibaba.fastjson.JSONObject;\n" +
                "import " + entityPackageName + "." + entityName + ";\n" +
                "import " + packagePath + ".utils.DateUtils;\n" +
                "import com.fasterxml.jackson.annotation.JsonFormat;\n" +
                "import com.fasterxml.jackson.annotation.JsonIgnoreProperties;\n" +
                "import com.google.common.collect.Lists;\n" +
                "import io.swagger.annotations.ApiModelProperty;\n" +
                "import lombok.*;\n" +
                "import org.springframework.format.annotation.DateTimeFormat;\n" +
                "\n" +
                "import java.io.Serializable;\n" +
                "import java.time.LocalDateTime;\n" +
                "import java.util.Collections;\n" +
                "import java.util.List;\n" +
                "import java.util.Optional;\n" +
                "import java.util.stream.Collectors;";
    }


    public static String createImportJavaSearchDto(String packagePath) {
        return "import " + packagePath + ".base.dto.PageDto;\n" +
                "import com.fasterxml.jackson.annotation.JsonIgnoreProperties;\n" +
                "import lombok.*;\n" +
                "\n" +
                "import java.time.LocalDateTime;\n" +
                "import java.util.List;";
    }

    public static String createImportJavaController(String packagePath,
                                                    String entityPackageName, String entityName,
                                                    String searchDtoPackageName, String searchDtoName,
                                                    String servicePackageName, String serviceName) {
        return "import " + packagePath + ".aop.SaveLog;\n" +
                "import " + packagePath + ".base.controller.BaseResultMessage;\n" +
                "import " + packagePath + ".base.controller.CurrentUser;\n" +
                "import " + packagePath + ".base.controller.ResultMessage;\n" +
                "import " + entityPackageName + ".*;\n" +
                "import " + searchDtoPackageName + ".*;\n" +
                "import " + servicePackageName + ".*;\n" +
                "import lombok.AllArgsConstructor;\n" +
                "import org.springframework.data.domain.Page;\n" +
                "import org.springframework.validation.annotation.Validated;\n" +
                "import org.springframework.web.bind.annotation.*;\n";
    }

    public static void main(String[] args) {
        String columnType = "String";
        String columnName = "name";
        String get = createSetFun(columnType, columnName);
        System.out.println(get);
    }
}
