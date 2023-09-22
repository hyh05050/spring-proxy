package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

@Slf4j
public class MultiAdvisorTest {


    @Test
    @DisplayName("1 proxy, 1 advisor")
    void multiAdvisorTest1() {
        //client -> proxy2 -> proxy1 - >target

        //프록시1 생성 및 target 호출
        ServiceInterface target = new ServiceImpl();
        ProxyFactory factory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advise1());
        factory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) factory.getProxy();

        //프록시2 생성 및 프록시1 호출
        ProxyFactory factory2 = new ProxyFactory(proxy);
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advise2());
        factory2.addAdvisor(advisor2);
        ServiceInterface proxy2 = (ServiceInterface) factory2.getProxy();

        proxy2.save();
        proxy2.find();
    }

    @Test
    @DisplayName("1 proxy, multi advisor")
    void multiAdvisorTest2() {

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advise1());
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advise2());

        ServiceInterface target = new ServiceImpl();
        ProxyFactory factory = new ProxyFactory(target);

        factory.addAdvisor(advisor2);
        factory.addAdvisor(advisor);

        ServiceInterface proxy = (ServiceInterface) factory.getProxy();

        proxy.save();
        proxy.find();
    }

    @Slf4j
    static class Advise1 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("Advise1 실행");
            Object result = invocation.proceed();
            return result;
        }
    }

    @Slf4j
    static class Advise2 implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("Advise2 실행");
            Object result = invocation.proceed();
            return result;
        }
    }
}
