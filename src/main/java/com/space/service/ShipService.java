package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ShipService {

    // List<Ship> getAllShips();

    Ship getShipById(Long id);

    void deleteShipById(Long id);

    Ship updateShip(Ship ship, Long id);

    Ship createShip(Ship ship);

    Integer getShipCount();

    Page<Ship> getAllShips(Integer pageNumber, Integer pageSize, Sort sort);
}