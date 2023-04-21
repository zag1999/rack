package com.jm.rack.fl.common.aspectj;

import com.jm.rack.fl.common.annotation.LockByName;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Aspect
@Component
public class LockByNameAspect {
    private final Map<String, Lock> lockMap = new HashMap<>();

    @Pointcut("@annotation(com.jm.rack.fl.common.annotation.LockByName)")
    public void pointcut() {
    }

    @Around(value = "pointcut() && @annotation(lockByName)")
    public Object around(ProceedingJoinPoint joinPoint, LockByName lockByName) throws Throwable {
        Object res = null;
        String name = lockByName.name();
        Lock lock = lockMap.computeIfAbsent(name, n -> new ReentrantLock());
        try {
            lock.lock();
            res = joinPoint.proceed(joinPoint.getArgs());
        } finally {
            lock.unlock();
        }
        return res;
    }
}
