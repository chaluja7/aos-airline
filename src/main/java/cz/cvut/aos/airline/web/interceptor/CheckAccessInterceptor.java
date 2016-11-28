package cz.cvut.aos.airline.web.interceptor;

import cz.cvut.aos.airline.entity.Reservation;
import cz.cvut.aos.airline.service.ReservationService;
import cz.cvut.aos.airline.web.exception.UnauthorizedException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Interceptor, ktery zkontroluje na metodach controleru oanotovanych @CheckXPasswordHeader pritomnost spravne
 * hlavicky X-Password, ktera dava pristup k detailu rezervace a jejimu stornovani
 *
 * @author jakubchalupa
 * @since 28.11.16
 */
@Component
public class CheckAccessInterceptor implements MethodInterceptor {

    @Autowired
    protected ReservationService reservationService;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        CheckAccess currentAnnotation = getCheckAccessAnnotation(invocation.getMethod());
        if(currentAnnotation == null) {
            //tohle invoke by se dle CheckAccessAdvisoru musi zavolat jen nad metodou, ktera je oanotovana CheckAccess
            throw new RuntimeException();
        }

        //z parametru metody controlleru ziskam ID resourcu, ke kteremu pristupuji. Ten musi byt oanotovan @CheckedResourceId
        int indexOfCurrentResourceId = getCheckedEntityIdAnnotatedArgumentIndex(invocation.getMethod(), CheckedResourceId.class);
        if(indexOfCurrentResourceId < 0) {
            throw new RuntimeException("@CheckAccess annotated method " + invocation.getMethod().getName() + " has no argument annotated by @CheckedResourceId");
        }

        //z parametru metody controlleru ziskam hlavicku X-Password. Ta musi by oanotovana @XPassword
        int indexOfPasswordHeader = getCheckedEntityIdAnnotatedArgumentIndex(invocation.getMethod(), XPasswordHeader.class);
        if(indexOfPasswordHeader < 0) {
            throw new RuntimeException("@CheckAccess annotated method " + invocation.getMethod().getName() + " has no argument annotated by @XPassword");
        }

        //vytahnu resourceId a passwordHeader
        String resourceIdString = getStringArgumentValueOnIndex(indexOfCurrentResourceId, invocation.getArguments());
        Long resourceId;
        try {
            resourceId = Long.parseLong(resourceIdString);
        } catch (NumberFormatException e) {
            throw new RuntimeException("@CheckedResourceId must be Long type");
        }

        String passwordHeader = getStringArgumentValueOnIndex(indexOfPasswordHeader, invocation.getArguments());
        if(StringUtils.isEmpty(passwordHeader)) {
            throw new UnauthorizedException();
        }

        //a zjistim, jestli je password spravne k dane rezervaci
        Reservation byIdAndPassword = reservationService.findByIdAndPassword(resourceId, passwordHeader);
        if(byIdAndPassword == null) {
            throw new UnauthorizedException();
        }

        return invocation.proceed();
    }

    /**
     * Vrati aktualni platnou CheckAccess anotaci pro danou metodu
     * @param method metoda s definovanou CheckAccess anotaci
     * @return aktualni platnou anotaci
     * @throws java.lang.RuntimeException
     */
    private CheckAccess getCheckAccessAnnotation(Method method) {
        CheckAccess secured = method.getAnnotation(CheckAccess.class);
        if (secured == null) {
            secured = method.getDeclaringClass().getAnnotation(CheckAccess.class);
            if (secured == null) {
                throw new RuntimeException("@CheckAccess not found on method " + method.getName() + " or its declaring class " + method.getClass().getName());
            }
        }

        return secured;
    }

    /**
     * Vrati index argumentu metody, ktery ma anotaci clazz (napr. CheckedResourceId.class)
     * @param method metoda s definovanou CheckAccess anotaci
     * @param clazz - trida anotace, kteorou hledam. Anotace bohuzel neumi dedicnost, cili to nejde vice specifikovat
     * @return index argumentu
     */
    private int getCheckedEntityIdAnnotatedArgumentIndex(Method method, Class clazz) {
        int indexOfCurrentEntityId = -1;
        int currentIndex = 0;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(Annotation[] annotations : parameterAnnotations) {
            for(Annotation annotation : annotations) {
                if(annotation.annotationType().equals(clazz)) {
                    indexOfCurrentEntityId = currentIndex;
                    break;
                }
            }

            if(indexOfCurrentEntityId == currentIndex) {
                break;
            }

            currentIndex++;
        }

        return indexOfCurrentEntityId;
    }

    /**
     * Vytahne string argument z pole na danem indexu
     * @param index index do pole
     * @param arguments pole argumentu
     * @return hodnotu daneho argumentu
     * @throws java.lang.RuntimeException
     */
    private String getStringArgumentValueOnIndex(int index, Object[] arguments) {
        if(arguments.length > index) {
            Object arg = arguments[index];
            return arg != null ? arg.toString() : null;
        } else {
            throw new RuntimeException("@CheckedResourceId arguments exception");
        }
    }

}
