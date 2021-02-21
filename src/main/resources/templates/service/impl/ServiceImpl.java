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

    @Override
    public ${entityName} save(${entityName} ${entityNameStatement}) {
        Long id = ${entityNameStatement}.getId();
        if (id != null && id != 0L) {
            Optional<${entityName}> ${entityNameStatementOptional} = ${repositoryNameStatement}.findById(id);
            ${entityName} ${entityNameStatementDb} = ${entityNameStatementOptional}.orElseThrow(() -> new OperationException("已删除"));
${entitySaveLogic}
            return ${repositoryNameStatement}.save(${entityNameStatementDb});

        } else {
            ${entityNameStatement}.setCreatedTime(DateUtils.currentDateTime());
            ${entityNameStatement}.setDeleteState(UN_DELETED);
            return ${repositoryNameStatement}.save(${entityNameStatement});
        }
    }

    @Override
    public Optional<${entityName}> findByIdNotDelete(Long id) {
        return ${repositoryNameStatement}.findByIdAndDeleteState(id, UN_DELETED));
    }

    @Override
    public void deleteById(Long id) {
        Optional<${entityName}> ${entityNameStatementOptional} = this.findById(id);
        ${entityNameStatementOptional}.ifPresent(${entityNameHump} -> {
            ${entityNameStatement}.setDeleteState(DELETED);
            ${repositoryNameStatement}.save(${entityNameStatement});
        });
    }

    @Override
    public Page<${entityName}> findPageByEntity(${entityName} ${entityNameStatement}) {
        Specification<${entityName}> specification = this.getAllSpecification(${entityNameStatement});
        Pageable pageable = PageUtils.buildPageRequest(searchProjectDto);
        return ${repositoryNameStatement}.findAll(specification, pageable);
    }

    @Override
    public ${entityName} ${saveEntityName}(${entityName} ${entityNameStatement}){
        return this.save(${entityNameStatement});
    }

    @Override
    public void ${deleteEntityName}(Long id){
        this.deleteById(id);
    }

    @Override
    public ${entityName} ${findEntityName}(Long id){
        return this.findByIdNotDelete(id).orElseThrow(() -> new OperationException("已删除"));
    }

    @Override
    public Page<${entityName}> ${findEntityNamePage}(${entityName} ${entityNameStatement}){
        return this.findPageByEntity(${entityNameStatement});
    }

    private Specification<${entityName}> getAllSpecification(${entityName} ${entityNameStatement}) {
        return Specifications.<${entityName}>and()
        .equal("deleteState", UN_DELETED)
        .equal("userId", ${entityNameStatement}.getUserId())
        .build();
    }
}
