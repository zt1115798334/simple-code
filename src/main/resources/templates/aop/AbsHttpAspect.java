package ${packagePath}.aop;

import com.alibaba.fastjson.JSONObject;
import ${packagePath}.aop.utils.AopUtils;
import ${packagePath}.utils.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: ${createdTime}
 * description:
 */
@Aspect
@Slf4j
@Component
public class AbsHttpAspect {

    private final ThreadLocal<String> methodName = new ThreadLocal<>();
    private final ThreadLocal<String> paramVal = new ThreadLocal<>();
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final ThreadLocal<String> token = new ThreadLocal<>();
    private final ThreadLocal<String> ip = new ThreadLocal<>();

    @Pointcut("execution( * ${packagePath}.controller..*.*(..))")
    public void aopPointCut() {

    }

    @Before("aopPointCut()")
    private void doBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        methodName.set(signature.getDeclaringTypeName() + "." + signature.getName());
        Object[] parameterValues = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();

        paramVal.set("[" + AopUtils.parseParams(parameterNames, parameterValues) + "]");
        startTime.set(System.currentTimeMillis());
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        token.set(request.getHeader("authorization"));
        ip.set(NetworkUtil.getLocalIp(request));

    }

    @After("aopPointCut()")
    private void doAfter() {
        long E_time = System.currentTimeMillis() - startTime.get();
        log.info("执行 {} 耗时为：{}ms, ip:{}, token信息：{}", methodName.get(), E_time, ip.get(), token.get());
        if (paramVal.get().length() < 1000) {
            log.info("参数信息：{}", paramVal.get());
        }
    }

    @AfterReturning(returning = "object", pointcut = "aopPointCut()")
    private void doAfterReturning(Object object) {
        if (object != null) {
            JSONObject result = JSONObject.parseObject(JSONObject.toJSONString(object));
            result.remove("data");
            log.info("执行 {} ，response：{}", methodName.get(), result.toJSONString());
        }
        methodName.remove();
        paramVal.remove();
        startTime.remove();
        token.remove();
        ip.remove();
    }

}
