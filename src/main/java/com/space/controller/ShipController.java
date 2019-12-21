package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.service.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("rest/ships")
public class ShipController {
    @Autowired
    ShipService shipService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Ship>> getAllShips(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
            @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder shipOrder) {
        Page<Ship> shipList = this.shipService.getAllShips(name, planet, shipType, after, before,
                isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating,
                pageNumber, pageSize, Sort.by(Sort.Direction.ASC, shipOrder.getFieldName()));
        return new ResponseEntity<>(shipList.getContent(), HttpStatus.OK);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Integer getShipCount(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String planet,
            @RequestParam(required = false) ShipType shipType,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean isUsed,
            @RequestParam(required = false) Double minSpeed,
            @RequestParam(required = false) Double maxSpeed,
            @RequestParam(required = false) Integer minCrewSize,
            @RequestParam(required = false) Integer maxCrewSize,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating) {
        return this.shipService.getShipCount(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating);
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

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        if (ShipServiceImpl.isDataValidForCreateShip(ship)) {
            ship.setUsed(ship.getUsed());
            ship.setSpeed(ship.getSpeed());
            ship.setRating();
            return new ResponseEntity<>(shipService.createShip(ship), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> updateShip(@RequestBody Ship ship, @PathVariable("id") Long id) {
        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Ship entity = shipService.getShipById(id);
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (ship != null) {
            if (ShipServiceImpl.nameCheckAndUpdate(ship, entity)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (ShipServiceImpl.planetCheckAndUpdate(ship, entity)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (ShipServiceImpl.prodDateCheckAndUpdate(ship, entity))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (ShipServiceImpl.speedCheckAndUpdate(ship, entity)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (ShipServiceImpl.crewSizeCheckAndUpdate(ship, entity))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (ship.getShipType() != null) {
                entity.setShipType(ship.getShipType());
            }
            if (ship.getUsed() != null) {
                entity.setUsed(ship.getUsed());
            }
            entity.setRating();
            return ResponseEntity.of(Optional.of(shipService.updateShip(entity, id)));
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}

