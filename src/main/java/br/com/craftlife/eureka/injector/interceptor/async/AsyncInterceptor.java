package br.com.craftlife.eureka.injector.interceptor.async;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AsyncInterceptor implements MethodInterceptor {

    private final ExecutorService executorService;

    public AsyncInterceptor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (!method.getReturnType().equals(Future.class)) {
            throw new RuntimeException("The return type for " + method.getName() + " must be of the type Future");
        }
        return executorService.submit(() -> {
            try {
                Future<Object> result = (Future<Object>) invocation.proceed();
                return result.get();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        });
    }

}
