package ${entityPackageName};

import javax.persistence.*;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: ${createdTime}
 * description:
 */
${lombokAnnotation}
public class ${entityName} implements Serializable {
    ${fieldCode}

${dtoChangeEntity}

${entityChangeDto}

${entityChangeListDto}
}
