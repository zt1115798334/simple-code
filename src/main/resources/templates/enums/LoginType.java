package ${packagePath}.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型
 */
@Getter
@AllArgsConstructor
public enum LoginType {
    AJAX("ajax", "ajax登陆");

    private final String type;
    private final String name;
}
