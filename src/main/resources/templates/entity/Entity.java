package ${entityPackageName};

import javax.persistence.*;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/7 11:42
 * description:
 */
@Entity
@Table(name = "${tableName}")
public class ${entityName} {
    ${fieldCode}

    ${fieldGetSetCode}
}
