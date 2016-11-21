package cz.cvut.aos.airline.web.wrapper;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
public class ReservationWrapper extends CreateReservationWrapper implements CommonWrappable {

    private Long entityId;

    private String created;

    private String state;

    //heslo je nutne v odpovedi mit, protoze se ho uzivatel musi nejak po vytvoreni rezeravce dozvedet
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
