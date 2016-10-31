package cz.cvut.aos.airline.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
@Entity
@Table(name = "reservations")
public class Reservation extends AbstractEntity {

    private static final long serialVersionUID = 5607060844626805976L;

    @Column(nullable = false)
    @NotNull
    private Integer seats;

    //heslo je pri persistenci vygenerovano jako UUID.randomUUID.toString()
    //neni dale hashovano, v databazi je tedy ulozeno primo, coz pro ucely teto aplikace vzhledem ke kontextu povazujeme za spravne.
    @Column(nullable = false)
    @NotNull
    private String password;

    @Column(nullable = false)
    @NotNull
    @Type(type = "org.hibernate.type.ZonedDateTimeType")
    private ZonedDateTime created;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StateChoices state;

    //schvalne eager, s rezervaci budu chtit vzdy vytahnout i let (pro ucely teto aplikace)
    //soucasne neni definovano inverzni mapovani v Flight (mapped_by), tedy pred ulozenim Reservation je nutne persistovat tento Flight
    //to je ale ocekavane workflow
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public StateChoices getState() {
        return state;
    }

    public void setState(StateChoices state) {
        this.state = state;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
