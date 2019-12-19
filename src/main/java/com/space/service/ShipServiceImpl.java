package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;

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
    public Page<Ship> getAllShips(Integer pageNumber, Integer pageSize, Sort sort) {
        return shipRepository.findAll(PageRequest.of(pageNumber, pageSize, sort));
    }

   /* @Override
    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }*/

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
        if (ship.getSpeed() == null || ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99) {
            return false;
        }
        if (ship.getCrewSize() == null || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999) {
            return false;
        }
        if (ship.getShipType() == null) {
            return false;
        }
        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        if (ship.getProdDate() == null || ship.getProdDate().getTime() < 0 || calendar.get(Calendar.YEAR) < 2800 || calendar.get(Calendar.YEAR) > 3019) {
            return false;
        }
        return true;
    }

    public static boolean crewSizeCheckAndUpdate(Ship ship, Ship entity) {
        if (ship.getCrewSize() != null) {
            if (ship.getCrewSize() >= 1 && ship.getCrewSize() <= 9999) {
                entity.setCrewSize(ship.getCrewSize());
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean speedCheckAndUpdate(Ship ship, Ship entity) {
        if (ship.getSpeed() != null) {
            if (ship.getSpeed() >= 0.01 && ship.getSpeed() <= 0.99) {
                entity.setSpeed(ship.getSpeed());
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean prodDateCheckAndUpdate(Ship ship, Ship entity) {
        if (ship.getProdDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ship.getProdDate());
            if (ship.getProdDate().getTime() > 0 && calendar.get(Calendar.YEAR) >= 2800 && calendar.get(Calendar.YEAR) <= 3019) {
                entity.setProdDate(ship.getProdDate());
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean planetCheckAndUpdate(Ship ship, Ship entity) {
        if (ship.getPlanet() != null) {
            if (!ship.getPlanet().isEmpty() && ship.getPlanet().length() <= 50) {
                entity.setPlanet(ship.getPlanet());
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean nameCheckAndUpdate(Ship ship, Ship entity) {
        if (ship.getName() != null) {
            if (!ship.getName().isEmpty() && ship.getName().length() <= 50) {
                entity.setName(ship.getName());
            } else {
                return true;
            }
        }
        return false;
    }
}
