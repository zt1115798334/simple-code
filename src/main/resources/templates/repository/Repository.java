package ${repositoryPackageName};

${importJava}

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: ${createdTime}
 * description:
 */
public interface ${repositoryName} extends CrudRepository<${entityName},Long>, JpaSpecificationExecutor<${entityName}> {

        Optional<${entityName}> findByIdAndDeleteState(Long id, Integer deleteState);

}
