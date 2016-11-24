package cz.cvut.aos.airline.service.geocodeapi.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Model to parse response from google maps api.
 *
 * @author jakubchalupa
 * @since 03.01.15
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class GoogleLocationResource {

    private List<GoogleResult> results;

    public List<GoogleResult> getResults() {
        return results;
    }

    public void setResults(List<GoogleResult> results) {
        this.results = results;
    }

}
