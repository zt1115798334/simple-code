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
@Entity
@Table(name = "${tableName}")
public class ${entityName} {
    ${fieldCode}

    ${fieldGetSetCode}
}
