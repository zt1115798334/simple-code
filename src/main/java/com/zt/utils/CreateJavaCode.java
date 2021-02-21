package com.zt.utils;

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

    public static String createFieldId() {
        return createTab(1) + "@Id\n" +
                createTab(1) + "@GeneratedValue(strategy = GenerationType.IDENTITY)\n";
    }

    public static String createRemarks(String remarks) {
        return createTab(1) + "/**\n" +
                createTab(1) + "* " + remarks + "\n" +
                createTab(1) + "*/";
    }

    public static String createField(String columnType, String columnName) {
        return createTab(1) + "private " + columnType + " " + columnName + ";";
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
        return createTab(3) + entityNameStatementDb + ".set" + toUpperCaseFirstOne(columnTran.getColumnName()) + "(" + entityNameStatement + ".get" + toUpperCaseFirstOne(columnTran.getColumnName()) + "());";
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
            entitySaveLogic.append(CreateJavaCode.createSaveLogic(entityNameStatement, entityNameStatementDb, columnTran)).append(CreateJavaCode.createChangeLine());
        }
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
                createTab(3) + "" + entityNameStatement + ".setCreatedTime(DateUtils.currentDateTime());\n" +
                createTab(3) + "" + entityNameStatement + ".setDeleteState(UN_DELETED);\n" +
                createTab(3) + "return " + repositoryNameStatement + ".save(" + entityNameStatement + ");\n" +
                createTab(2) + "}\n" +
                createTab(1) + "}\n";
    }

    public static String createBaseInterfaceOfDelete(String entityName, String entityNameStatement, String entityNameStatementOptional,
                                                     String repositoryNameStatement) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "@Transactional(rollbackFor = RuntimeException.class)\n" +
                createTab(1) + "public void deleteById(Long id) {\n" +
                createTab(2) + "Optional<" + entityName + "> " + entityNameStatementOptional + " = this.findById(id);\n" +
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
                createTab(2) + "return " + repositoryNameStatement + ".findByIdAndDeleteState(id, UN_DELETED));\n" +
                createTab(1) + "}\n";
    }

    public static String createBaseInterfaceOfFindPage(String entityName, String entityNameStatement,
                                                       String repositoryNameStatement) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)\n" +
                createTab(1) + "public Page<" + entityName + "> findPageByEntity(" + entityName + " " + entityNameStatement + ") {\n" +
                createTab(2) + "Specification<" + entityName + "> specification = this.getAllSpecification(" + entityNameStatement + ");\n" +
                createTab(2) + "Pageable pageable = PageUtils.buildPageRequest(" + entityName + ");\n" +
                createTab(2) + "return " + repositoryNameStatement + ".findAll(specification, pageable);\n" +
                createTab(1) + "}\n";
    }

    public static String createAllSpecification(String entityName, String entityNameStatement) {
        return createTab(1) + "private Specification<" + entityName + "> getAllSpecification(" + entityName + " " + entityNameStatement + ") {\n" +
                createTab(2) + "return Specifications.<${" + entityName + "}>and()\n" +
                createTab(2) + ".equal(\"deleteState\", UN_DELETED)\n" +
                createTab(2) + ".equal(\"userId\", " + entityNameStatement + ".getUserId())\n" +
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
                                                              String entityName, String entityNameStatement) {
        return createTab(1) + "@Override\n" +
                createTab(1) + "public Page<" + entityName + "> " + findEntityNamePage + "(" + entityName + " " + entityNameStatement + "){\n" +
                createTab(2) + "return this.findPageByEntity(" + entityNameStatement + ");\n" +
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
                                                          String entityName, String entityNameStatement) {
        return createTab(1) + "Page<" + entityName + "> " + findEntityNamePage + "(" + entityName + " " + entityNameStatement + ");\n";
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
                createTab(2) + "return " + entityNameListStatement + ".stream().map(e -> " + entityDtoName + ".entityChangeDto(e, subjectList)).collect(Collectors.toList());\n" +
                createTab(1) + "}\n";
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
                createTab(2) + "return success(" + entityDtoName + ".entityChangeDto(" + entityDtoNameStatement + "));\n" +
                createTab(1) + "}\n";
    }

    public static String createControllerInterfaceOfFindPage(String findEntityNamePage,
                                                             String entityDtoName, String entityDtoNameStatement,
                                                             String serviceNameStatement,
                                                             String entityName, String entityNameStatement) {
        return createTab(1) + " @PostMapping(value = \"" + findEntityNamePage + "\")\n" +
                createTab(1) + "public ResultMessage " + findEntityNamePage + "(@RequestBody " + entityDtoName + " " + entityDtoNameStatement + ") {\n" +
                createTab(2) + "Page<" + entityName + "> " + entityNameStatement + "Page = " + serviceNameStatement + "." + findEntityNamePage + "(" + entityDtoNameStatement + ");\n" +
                createTab(2) + "return success(searchProjectDto.getPageNumber(), searchProjectDto.getPageSize(), " + entityNameStatement + "Page.getTotalElements(),\n" +
                createTab(2) + "" + entityDtoName + ".entityChangeListDto(" + entityNameStatement + "Page.getContent()));\n" +
                createTab(1) + "}\n";
    }


    public static String createEntityAnnotation(String tableName) {
        return "@NoArgsConstructor\n" +
                "@AllArgsConstructor\n" +
                "@EqualsAndHashCode(callSuper = true)\n" +
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
                "@Data\n" +
                "@JsonIgnoreProperties(ignoreUnknown = true)";
    }

    public static String createControllerAnnotation(String entityNameStatement) {
        return "@Validated\n" +
                "@AllArgsConstructor\n" +
                "@RestController\n" +
                "@RequestMapping(\"api/" + entityNameStatement + "\")";
    }


    public static void main(String[] args) {
        String columnType = "String";
        String columnName = "name";
        String get = createSetFun(columnType, columnName);
        System.out.println(get);
    }
}
