package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import com.space.service.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/ships")
public class ShipController {
    @Autowired
    ShipService shipService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Ship>> getAllShips() {
        List<Ship> shipList = this.shipService.getAllShips();
        if (shipList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shipList, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> getShipById(@PathVariable("id") Long shipId) {
        if (shipId == null || shipId <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Ship ship = this.shipService.getShipById(shipId);
        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Ship> deleteShipById(@PathVariable("id") Long shipId) {
        if (shipId == null || shipId <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Ship ship = this.shipService.getShipById(shipId);
        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.shipService.deleteShipById(shipId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Integer getShipCount() {
        return this.shipService.getShipCount();
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        if (ShipServiceImpl.isDataValidForCreateShip(ship)) {
            ship.setUsed(ship.getUsed());
            ship.setSpeed(ship.getSpeed());
            ship.setRating(ship.getRating());
            return new ResponseEntity<>(shipService.createShip(ship), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateShip(@RequestBody Ship ship, @PathVariable("id") Long id) {
        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Ship entity = shipService.getShipById(id);
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (ShipServiceImpl.isDataValidForCreateShip(ship)) {
            entity.setName(ship.getName());
            entity.setPlanet(ship.getPlanet());
            entity.setShipType(ship.getShipType());
            entity.setProdDate(ship.getProdDate());
            entity.setUsed(ship.getUsed());
            entity.setSpeed(ship.getSpeed());
            entity.setCrewSize(ship.getCrewSize());
            entity.setRating(ship.getRating());
            shipService.updateShip(entity, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (!ShipServiceImpl.isDataValidForCreateShip(ship)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

