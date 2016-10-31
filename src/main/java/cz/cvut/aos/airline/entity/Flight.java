package cz.cvut.aos.airline.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * @author jakubchalupa
 * @since 31.10.16
 */
@Entity
@Table(name = "flights")
public class Flight extends AbstractEntity {

    private static final long serialVersionUID = 882875570466556009L;

    @Column(nullable = false, unique = true)
    @Size(max = 255)
    @NotNull
    private String name;

    @Column
    @Type(type = "org.hibernate.type.ZonedDateTimeType")
    private ZonedDateTime dateOfDeparture;

    @Column
    private Double distance;

    @Column
    private Double price;

    @Column
    private Integer seats;

    //schvalne eager, s flight budu chtit vzdy i destinace (pro ucely teto aplikace)
    //soucasne neni definovano inverzni mapovani v Destination (mapped_by), tedy pred ulozenim Flight je nutne persistovat tuto Destinaci
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_from_id")
    private Destination from;

    //schvalne eager, s flight budu chtit vzdy i destinace (pro ucely teto aplikace)
    //soucasne neni definovano inverzni mapovani v Destination (mapped_by), tedy pred ulozenim Flight je nutne persistovat tuto Destinaci
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_to_id")
    private Destination to;

    //zde by mohl byt list napojenych rezervaci, ale pro ucely teto aplikace neni treba

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(ZonedDateTime dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Destination getFrom() {
        return from;
    }

    public void setFrom(Destination from) {
        this.from = from;
    }

    public Destination getTo() {
        return to;
    }

    public void setTo(Destination to) {
        this.to = to;
    }
}
