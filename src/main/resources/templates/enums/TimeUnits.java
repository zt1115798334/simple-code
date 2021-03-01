package ${packagePath}.enums;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public enum TimeUnits {
    MINUTES("minutes", TimeUnit.MINUTES),
    HOURS("hours", TimeUnit.HOURS),
    DAYS("days", TimeUnit.DAYS);

    private final String type;
    private final TimeUnit timeUnit;

    private static final Map<String, TimeUnits> enumMap = Maps.newHashMap();

    static {
        for (TimeUnits type : TimeUnits.values()) {
            enumMap.put(type.getType(), type);
        }
    }

    public static TimeUnits getEnum(String type) {
        return enumMap.get(type);
    }

}
