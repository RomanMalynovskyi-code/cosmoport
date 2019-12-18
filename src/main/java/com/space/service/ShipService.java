package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

public interface ShipService {

    List<Ship> getAllShips();

    Ship getShipById(Long id);

    void deleteShipById(Long id);

    Ship updateShip(Ship ship, Long id);

    Ship createShip(Ship ship);

    Integer getShipCount();
}