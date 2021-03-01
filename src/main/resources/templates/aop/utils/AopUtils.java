package ${packagePath}.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: ${createdTime}
 * description:
 */
@Slf4j
public class AopUtils {

    /**
     * 请求参数格式转换 aop 接口使用
     *
     * @param parameterNames  parameterNames
     * @param parameterValues parameterValues
     * @return String
     */
    public static String parseParams(String[] parameterNames, Object[] parameterValues) {
        StringBuilder stringBuffer = new StringBuilder();
        int length = parameterNames.length;
        for (int i = 0; i < length; i++) {
            String parameterName = Optional.ofNullable(parameterNames[i]).orElse("unknown");
            Object parameterValueObj = Optional.ofNullable(parameterValues[i]).orElse("unknown");
            Class<?> parameterValueClazz = parameterValueObj.getClass();
            String parameterValue;
            if (parameterValueClazz.isPrimitive() ||
                    parameterValueClazz == String.class) {
                parameterValue = parameterValueObj.toString();
            } else if (parameterValueObj instanceof Serializable) {
                parameterValue = JSON.toJSONString(parameterValueObj);
            } else {
                parameterValue = parameterValueObj.toString();
            }
            stringBuffer.append(parameterName).append(":").append(parameterValue).append(" ");
        }
        return stringBuffer.toString();
    }

}
