package cz.cvut.aos.airline.web.interceptor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Advisor, ktery priradi provedeni checkAccessInterceptor k metodam oanotovanym CheckAccess anotaci.
 *
 * @author jakubchalupa
 * @since 03.03.15
 */
@Component
public class CheckAccessAdvisor extends AbstractPointcutAdvisor {

    private static final long serialVersionUID = 6183878645869006888L;

    @Autowired
    private CheckAccessInterceptor checkAccessInterceptor;

    private final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return method.isAnnotationPresent(CheckAccess.class);
        }
    };

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.checkAccessInterceptor;
    }

}
