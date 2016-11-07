package cz.cvut.aos.airline.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
@Entity
@Table(name = "destinations")
public class Destination extends AbstractEntity {

    private static final long serialVersionUID = -3069267246928233125L;

    @Column(nullable = false, unique = true)
    @Size(max = 255)
    @NotNull
    private String name;

    @Column(nullable = false)
    @Min(-90)
    @Max(90)
    @NotNull
    private Double lat;

    @Column(nullable = false)
    @Min(-180)
    @Max(180)
    @NotNull
    private Double lon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
