package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;

    @Override
    public Ship getShipById(Long id) {
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteShipById(Long id) {
        shipRepository.deleteById(id);
    }

    @Override
    public Integer getShipCount() {
        return Math.toIntExact(shipRepository.count());
    }

    @Override
    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    @Override
    public Ship updateShip(Ship ship, Long id) {
        return shipRepository.save(ship);
    }

    @Override
    public Ship createShip(Ship ship) {
        return shipRepository.save(ship);
    }

    public static boolean isDataValidForCreateShip(Ship ship) {
        if (ship.getName() == null || ship.getName().isEmpty() || ship.getName().length() > 50) {
            return false;
        }
        if (ship.getPlanet() == null || ship.getPlanet().isEmpty() || ship.getPlanet().length() > 50) {
            return false;
        }
        if (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99) {
            return false;
        }
        if (ship.getCrewSize() == null || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999) {
            return false;
        }
        if (ship.getShipType() == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        if (ship.getProdDate() == null || ship.getProdDate().getTime() < 0 || calendar.get(Calendar.YEAR) < 2800 || calendar.get(Calendar.YEAR) > 3019) {
            return false;
        }
        return true;
    }

  /* *//* public static boolean isDataValidUpdateShip(Ship ship) {
        if (ship.getName() == null || ship.getName().isEmpty() || ship.getName().length() > 50) {
            return false;
        }
        if (ship.getPlanet() == null || ship.getPlanet().isEmpty() || ship.getPlanet().length() > 50) {
            return false;
        }
        if (ship.getSpeed() == null || ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99) {
            return false;
        }
        if (ship.getCrewSize() == null || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999) {
            return false;
        }
        if (ship.getShipType() == null) {
            return false;
        }
        if (ship.getProdDate() == null) {
            return false;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(ship.getProdDate());
        if (calendar.get(Calendar.YEAR) < 2800 || calendar.get(Calendar.YEAR) > 3019) {
            return false;
        }
        return true;*//*
    }*/
}
