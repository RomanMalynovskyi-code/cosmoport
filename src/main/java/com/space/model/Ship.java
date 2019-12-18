package com.space.model;

import org.springframework.lang.NonNull;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


@Table(name = "ship")
@Entity
public class Ship {
    public static final Integer CURRENT_YEAR = 3019;

    public Ship() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "name")
    private String name;


    @Column(name = "planet")
    private String planet;


    @Column(name = "shipType")
    @Enumerated(EnumType.STRING)
    private ShipType shipType;


    @Column(name = "prodDate")
    @Temporal(TemporalType.DATE)
    private Date prodDate;


    @Column(name = "isUsed")
    private Boolean isUsed;


    @Column(name = "speed", scale = 2)
    private Double speed;


    @Column(name = "crewSize", precision = 4)
    private Integer crewSize;


    @Column(name = "rating", scale = 2)
    private Double rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prodDate);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public void setProdDate(@NonNull Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        if (isUsed == null) {
            return false;
        }
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        if (speed == null) {
            this.speed = 0.0;
        }
        return Math.floor(speed * 100.0) / 100.0;
    }

    public void setSpeed(@NonNull Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return calcRating();
    }

    // logic
    private Double calcRating() {
        if (isUsed == null) {
            isUsed = false;
        }
        double k = isUsed ? 0.5 : 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prodDate);
        this.rating = (80 * speed * k) / ((CURRENT_YEAR - calendar.get(Calendar.YEAR)) + 1);
        return Math.round(rating * 100.0) / 100.0;
    }


    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship1 = (Ship) o;
        return id.equals(ship1.id) &&
                name.equals(ship1.name) &&
                planet.equals(ship1.planet) &&
                shipType == ship1.shipType &&
                prodDate.equals(ship1.prodDate) &&
                isUsed.equals(ship1.isUsed) &&
                speed.equals(ship1.speed) &&
                crewSize.equals(ship1.crewSize) &&
                rating.equals(ship1.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, planet, shipType, prodDate, isUsed, speed, crewSize, rating);
    }
}
