package cz.cvut.aos.airline.service.rome2rio;

import cz.cvut.aos.airline.service.geocodeapi.resource.Location;
import cz.cvut.aos.airline.service.rome2rio.resource.R2RResource;
import org.springframework.beans.factory.annotation.Autowired;
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
public class R2RManager {

    // https://www.rome2rio.com/api/1.4/json/Search?key=&oName=Bern&dName=Zurich&noRideshare

    private static final String API_KEY = "";

    private static final String R2R_API_URL = "https://www.rome2rio.com";

    private static final String R2R_JSON_URI = "api/1.4/json/Search";

    private static final String ORIGIN_PARAM = "oPos";

    private static final String DESTINATION_PARAM = "dPos";

    private static final String API_KEY_PARAM = "key";

    private static final String EXCLUDE_PARAM = "noRideshare&noBikeshare&noRail&noBus&noFerry&noCar&noSpecial&noTowncar&noCommuter";


    /**
     * @param origin      lokace: lat, lng
     * @param destination lokace: lat, lng
     * @return vzdalenost
     */
    public R2RResource getR2RDistance(Location origin, Location destination) {
        HttpEntity<R2RResource> googleAddressResponse = getDistanceResponse(origin, destination, R2RResource.class);
        return googleAddressResponse.getBody();
    }


    private <T> HttpEntity<T> getDistanceResponse(Location origin, Location destination, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(R2R_API_URL)
                .path(R2R_JSON_URI)
                .queryParam(ORIGIN_PARAM, origin)  // comma sep. toString
                .queryParam(DESTINATION_PARAM, destination)  // comma sep. toString
                .queryParam(API_KEY_PARAM, API_KEY)
                .queryParam(EXCLUDE_PARAM);

        return new RestTemplate().exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, type);
    }

    public double getR2RDistanceTest(Location origin, Location destination) {
        return (double) Math.round(DistanceCalculator.distance(origin.getLat(), origin.getLng(), destination.getLat(), destination.getLng(), "K") * 100.0) / 100.0;
    }
}
