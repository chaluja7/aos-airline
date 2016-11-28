package cz.cvut.aos.airline.web.interceptor;

import java.lang.annotation.*;

/**
 * Vlastni anotace pro predani hlavicky obsahujici X-Password
 *
 * @author jakubchalupa
 * @since 03.03.15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Inherited
@Documented
public @interface XPasswordHeader {

    //empty

}
