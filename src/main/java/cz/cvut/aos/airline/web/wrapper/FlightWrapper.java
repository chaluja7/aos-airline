package cz.cvut.aos.airline.web.wrapper;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
public class FlightWrapper extends CreateFlightWrapper implements CommonWrappable {

    private Long id;

    private String url;

    private String fromUrl;

    private String toUrl;

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

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public String getToUrl() {
        return toUrl;
    }

    public void setToUrl(String toUrl) {
        this.toUrl = toUrl;
    }
}
