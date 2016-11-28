package cz.cvut.aos.airline.web.interceptor;

import java.lang.annotation.*;

/**
 * Vlastni anotace pro kontrolu pristupu k metodam kde je vyzadovana X-Password hlavicka
 *
 * @author jakubchalupa
 * @since 03.03.15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
public @interface CheckAccess {

    //empty

}
