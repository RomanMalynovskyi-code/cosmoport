package com.space.model;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Table(name = "ship")
@Entity
public class Ship{
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
    @Temporal(TemporalType.TIMESTAMP)
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

    public void setName(String name) {
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
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(prodDate.getTime());
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        this.prodDate = calendar.getTime();
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        if (used == null) {
            isUsed = false;
        }
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = Math.floor(speed * 100.0) / 100.0;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    // logic
    private Double calcRating() {
        double k = isUsed ? 0.5 : 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(prodDate.getTime());
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        this.rating = (80 * speed * k) / ((CURRENT_YEAR - calendar.get(Calendar.YEAR)) + 1);
        return Math.round(rating * 100.0) / 100.0;

    }

    public void setRating() {
        this.rating = calcRating();
    }
}
