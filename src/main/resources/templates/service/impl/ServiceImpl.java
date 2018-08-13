package ${serviceImplPackageName};

import ${entityPackageName}.${entityName};
import ${repositoryPackageName}.${repositoryName};
import ${servicePackageName}.${serviceName};

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: ${createdTime}
 * description:
 */
@Service
@Transactional(rollbackOn = RuntimeException.class)
public class ${serviceImplName} implements ${serviceName} {

    @Autowired
    private ${repositoryName} ${repositoryNameStatement};

}
