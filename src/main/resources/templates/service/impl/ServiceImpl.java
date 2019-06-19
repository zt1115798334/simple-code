package ${serviceImplPackageName};

import ${entityPackageName}.${entityName};
import ${repositoryPackageName}.${repositoryName};
import ${servicePackageName}.${serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: ${createdTime}
 * description:
 */
${lombokAnnotation}
@Service
@Transactional(rollbackOn = RuntimeException.class)
public class ${serviceImplName} implements ${serviceName} {

    ${autowired}
    private ${finalStr} ${repositoryName} ${repositoryNameStatement};

}
