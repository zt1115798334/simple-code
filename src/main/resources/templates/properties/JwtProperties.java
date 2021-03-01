package ${packagePath}.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/3 14:21
 * description:
 */
@Data
@Component
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtProperties {

    private String header;

    private Long expiration;
    private String expirationUnit;

    private Long refreshExpiration;
    private String refreshExpirationUnit;

    private Long rememberMeExpiration;
    private String rememberMeExpirationUnit;

    private Long rememberMeRefreshExpiration;
    private String rememberMeRefreshExpirationUnit;

    private String tokenHead;
    private String secret;

}
