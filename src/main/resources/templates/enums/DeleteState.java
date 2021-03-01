package ${packagePath}.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除状态
 */
@Getter
@AllArgsConstructor
public enum DeleteState {
    UN_DELETED(0, "未删除"),
    DELETE(1, "删除");

    private final Integer code;
    private final String name;
}
