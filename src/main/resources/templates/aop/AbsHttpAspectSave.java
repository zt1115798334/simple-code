package ${packagePath}.aop;

import com.alibaba.fastjson.JSONObject;
import ${packagePath}.base.controller.CurrentUser;
import ${packagePath}.entity.User;
import ${packagePath}.entity.UserLog;
import ${packagePath}.service.UserLogService;
import ${packagePath}.utils.DateUtils;
import ${packagePath}.aop.utils.AopUtils;
import ${packagePath}.utils.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
 * date:${createdTime}
 * description:
 */
@Aspect
@Slf4j
@Component
public class AbsHttpAspectSave implements CurrentUser {
    private final ThreadLocal<String> className = new ThreadLocal<>();
    private final ThreadLocal<String> methodName = new ThreadLocal<>();
    private final ThreadLocal<String> paramVal = new ThreadLocal<>();
    private final ThreadLocal<String> ip = new ThreadLocal<>();

    private final UserLogService userLogService;

    public AbsHttpAspectSave(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    @Pointcut("execution( * ${packagePath}.controller..*.*(..)) && @annotation(logs)")
    public void aopPointCut(SaveLog logs) {

    }

    @Before(value = "aopPointCut(logs)", argNames = "joinPoint,logs")
    private void doBefore(JoinPoint joinPoint, SaveLog logs) {
        Signature signature = joinPoint.getSignature();
        className.set(signature.getDeclaringTypeName());
        methodName.set(signature.getName());
        MethodSignature methodSignature = (MethodSignature) signature;

        Object[] parameterValues = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();

        paramVal.set("[" + AopUtils.parseParams(parameterNames, parameterValues) + "]");
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        ip.set(NetworkUtil.getLocalIp(request));
    }

    @AfterReturning(returning = "object", pointcut = "aopPointCut(logs)", argNames = "object,logs")
    private void doAfterReturning(Object object, SaveLog logs) {
        //返回值
        String response = JSONObject.toJSONString(object);
        String desc = logs.desc();
        Long userId = null;
        String username = null;
        if (logs.containUser()) {
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                userId = currentUser.getId();
                username = currentUser.getUserName();
            }
        }
        UserLog userLog = UserLog.builder()
                .userId(userId).name(username).type(desc).content(paramVal.get()).ip(ip.get())
                .time(DateUtils.currentDateTime()).classify(className.get()).fun(methodName.get())
                .response(response).build();
        userLogService.save(userLog);
        className.remove();
        paramVal.remove();
        methodName.remove();
        ip.remove();
    }
}
