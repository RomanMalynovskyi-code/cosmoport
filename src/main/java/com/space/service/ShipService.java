package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ShipService {

    Ship getShipById(Long id);

    void deleteShipById(Long id);

    Ship updateShip(Ship ship, Long id);

    Ship createShip(Ship ship);

    Integer getShipCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                         Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                         Double minRating, Double maxRating);

    Page<Ship> getAllShips(String name, String planet, ShipType shipType, Long after, Long before,
                           Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                           Double minRating, Double maxRating,
                           Integer pageNumber, Integer pageSize, Sort sort);
}