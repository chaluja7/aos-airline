package cz.cvut.aos.airline.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@RequestMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public abstract class AbstractController {

    protected static final String X_COUNT_HEADER = "X-Count-records";

    protected static final String X_BASE_HEADER = "X-Base";

    protected static final String X_OFFSET_HEADER = "X-Offset";

    protected static final String X_ORDER = "X-Order";

    protected static final String X_FILTER = "X-Filter";

    protected ResponseEntity<?> getResponseCreated(Object body, String location) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION, location);
        return new ResponseEntity<>(body, httpHeaders, HttpStatus.CREATED);
    }

}
