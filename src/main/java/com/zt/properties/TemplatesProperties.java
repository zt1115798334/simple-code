package com.zt.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 9:21
 * description:
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "templates.file-path")
public class TemplatesProperties {

    private String entityTemplate;

    private String repositoryTemplate;

    private String serviceTemplate;

    private String serviceImplTemplate;

    private String dtoTemplate;

    private String searchDtoTemplate;

    private String controllerTemplate;

    private String baseBaseResultMessageTemplate;

    private String baseResultMessageTemplate;

    private String baseCurrentUserTemplate;

    private String basePageEntityTemplate;

    private String baseBaseServiceTemplate;

    private String baseConstantServiceTemplate;

    private String basePageUtilsTemplate;

    private String basePageDtoTemplate;

    private String specificationBooleanOperatorTemplate;

    private String specificationMatchTypeTemplate;

    private String specificationSpecificationsTemplate;

    private String specificationSpecificationUtilsTemplate;

    private String enumsDeleteStateTemplate;

    private String enumsSystemStatusCodeTemplate;

    private String enumsTimeUnitsTemplate;

    private String enumsLoginTypeTemplate;

    private String exceptionGlobalExceptionTemplate;

    private String exceptionOperationExceptionTemplate;

    private String propertiesAccountPropertiesTemplate;

    private String propertiesJwtPropertiesTemplate;

    private String utilsDateUtilsTemplate;

    private String utilsJwtUtilsTemplate;

    private String utilsNetworkUtilTemplate;

    private String utilsRequestResponseUtilTemplate;

    private String utilsUserUtilsTemplate;

    private String utilsUuidUtilTemplate;

    private String utilsDigestsTemplate;

    private String securityShiroBaseConfigTemplate;

    private String securityStatelessWebSubjectFactoryTemplate;

    private String securityJwtFilterTemplate;

    private String securityShiroFilterChainManagerTemplate;

    private String securityAModularRealmAuthenticatorTemplate;

    private String securityJwtRealmTemplate;

    private String securityPasswordRealmTemplate;

    private String securityRealmManagerTemplate;

    private String securityJwtTokenTemplate;

    private String securityPasswordTokenTemplate;

    private String redisRedisConfigMainTemplate;

    private String redisRedisServiceImplTemplate;

    private String redisStringRedisServiceImplTemplate;

    private String redisJsonArrayRedisTemplateTemplate;

    private String redisJsonObjectRedisTemplateTemplate;

    private String redisRedisDatabaseTemplate;

    private String redisRedisServiceTemplate;

    private String redisStringRedisServiceTemplate;

    private String aopAopUtilsTemplate;

    private String aopAbsHttpAspectTemplate;

    private String aopAbsHttpAspectSaveTemplate;

    private String aopSaveLogTemplate;

    private String cacheCacheKeysTemplate;

    private String xssXssFilterTemplate;

    private String xssXssHttpServletRequestWrapperTemplate;

    private String xssXssUtilsTemplate;
}
