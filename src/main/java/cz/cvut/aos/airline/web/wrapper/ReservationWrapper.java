package cz.cvut.aos.airline.web.wrapper;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
public class ReservationWrapper extends CreateReservationWrapper implements CommonWrappable {

    private Long id;

    private String url;

    private String created;

    private String state;

    private String flightUrl;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFlightUrl() {
        return flightUrl;
    }

    public void setFlightUrl(String flightUrl) {
        this.flightUrl = flightUrl;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }
}
