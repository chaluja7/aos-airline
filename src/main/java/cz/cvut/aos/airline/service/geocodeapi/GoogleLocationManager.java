package cz.cvut.aos.airline.service.geocodeapi;

import cz.cvut.aos.airline.service.geocodeapi.resource.GoogleLocationResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by sange on 24/11/2016.
 */

@Service
public class GoogleLocationManager {

    // https://maps.googleapis.com/maps/api/geocode/json?address=prague&key=AAA

    private static final String API_KEY = "AIzaSyBakk9fY_69alLcwI9U6DQr1xvfKDKVG6k";

    private static final String GOOGLE_API_URL = "https://maps.googleapis.com";

    private static final String GOOGLE_MAP_JSON_URI = "maps/api/geocode/json";

    private static final String ADDRESS_PARAM = "address";

    private static final String API_KEY_PARAM = "key";


    /**
     *
     * @param addresss adresa
     * @return google adresa s lokaci
     */
    public GoogleLocationResource getGoogleLocation(String addresss) {
        HttpEntity<GoogleLocationResource> googleAddressResponse = getLatLonResponse(addresss, GoogleLocationResource.class);
        return googleAddressResponse.getBody();
    }

    /**
     *
     * @param address adresa
     * @param type
     * @param <T>
     * @return odpoved z google api s adresou a lokaci
     */
    private <T> HttpEntity<T> getLatLonResponse(String address, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(GOOGLE_API_URL)
                .path(GOOGLE_MAP_JSON_URI)
                .queryParam(ADDRESS_PARAM, address)
                .queryParam(API_KEY_PARAM, API_KEY);

        return new RestTemplate().exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, type);
    }
}
