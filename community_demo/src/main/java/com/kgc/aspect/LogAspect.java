package com.kgc.aspect;

import com.google.gson.Gson;
import com.kgc.annotation.LogAnnotation;
import com.kgc.entity.SysLog;
import com.kgc.entity.SysUser;
import com.kgc.service.SysLogService;
import com.kgc.util.HttpContextUtil;
import com.kgc.util.IPUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by jiang on 8/23/23 10:28 PM
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;
    public static SysUser sysUser;

    /**
     * 实现aop 日志流程
     * 1.创造一个annotation
     * 2.定义一个切面类
     * 3.在登陆的时候获取到用户
     *
     * 注意:只有增删改需要添加注解 查不需要
     */

    /**
     * 指定自定义注解
     */
    @Pointcut("@annotation(com.kgc.annotation.LogAnnotation)")
    public void logPointCut() {
        //这个方法是找到添加了注解的方法 定义的注解叫LogAnnotation 这个方法就会找添加了@logannotation的方法 例如 camera中的添加方法
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        saveSysLog(point, (int) time);
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, int time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = new SysLog();

        LogAnnotation log = method.getAnnotation(LogAnnotation.class);

        if (log != null) {
            //注解上的描述
            sysLog.setOperation(log.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = new Gson().toJson(args[0]);
            sysLog.setParams(params);
        } catch (Exception e) {

        }

        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        sysLog.setIp(IPUtil.getIpAddr(request));
        sysLog.setTime(time);
//        sysLog.setCreateTime(new Date());
        //登录用户信息
        if (sysUser != null) {
            sysLog.setUsername(sysUser.getUsername());
            this.sysLogService.save(sysLog);
        }
    }


}
