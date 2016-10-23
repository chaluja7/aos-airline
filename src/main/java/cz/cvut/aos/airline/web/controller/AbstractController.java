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

    protected ResponseEntity<String> getResponseCreated(String location) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", location);
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    protected abstract String getResourceDestination(Long id);

}
