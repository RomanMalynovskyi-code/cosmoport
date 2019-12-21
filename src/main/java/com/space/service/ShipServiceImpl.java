package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    public Integer getShipCount(String name, String planet, ShipType shipType, Long after, Long before,
                                Boolean isUsed, Double minSpeed, Double maxSpeed,
                                Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {
        return Math.toIntExact(shipRepository.count(Specification.where(new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (!StringUtils.isEmpty(name)) {
                    predicate = cb.and(predicate, cb.like(root.get("name"), "%" + name + "%"));
                }
                if (!StringUtils.isEmpty(planet)) {
                    predicate = cb.and(predicate, cb.like(root.get("planet"), "%" + planet + "%"));
                }
                if (Objects.nonNull(shipType)) {
                    predicate = cb.and(predicate, cb.equal(root.get("shipType"), shipType));
                }
                if (Objects.nonNull(isUsed)) {
                    predicate = cb.and(predicate, cb.equal(root.get("isUsed"), isUsed));
                }
                predicate = getPredicateDateAfterAndDateBefore(after, before, root, cb, predicate);
                predicate = getPredicateMinAndMaxSpeed(minSpeed, maxSpeed, root, cb, predicate);
                predicate = getPredicateMinAndMaxCrewSize(minCrewSize, maxCrewSize, root, cb, predicate);
                predicate = getPredicateMinAndMaxRating(minRating, maxRating, root, cb, predicate);
                return predicate;
            }
        })));
    }

    @Override
    public Page<Ship> getAllShips(String name, String planet, ShipType shipType, Long after, Long before,
                                  Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                  Double minRating, Double maxRating,
                                  Integer pageNumber, Integer pageSize, Sort sort) {
        return shipRepository.findAll(Specification.where((Specification<Ship>) (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (!StringUtils.isEmpty(name)) {
                predicate = cb.and(predicate, cb.like(root.get("name"), "%" + name + "%"));
            }
            if (!StringUtils.isEmpty(planet)) {
                predicate = cb.and(predicate, cb.like(root.get("planet"), "%" + planet + "%"));
            }
            if (Objects.nonNull(shipType)) {
                predicate = cb.and(predicate, cb.equal(root.get("shipType"), shipType));
            }
            if (Objects.nonNull(isUsed)) {
                predicate = cb.and(predicate, cb.equal(root.get("isUsed"), isUsed));
            }
            predicate = getPredicateDateAfterAndDateBefore(after, before, root, cb, predicate);
            predicate = getPredicateMinAndMaxSpeed(minSpeed, maxSpeed, root, cb, predicate);
            predicate = getPredicateMinAndMaxCrewSize(minCrewSize, maxCrewSize, root, cb, predicate);
            predicate = getPredicateMinAndMaxRating(minRating, maxRating, root, cb, predicate);
            return predicate;
        }), PageRequest.of(pageNumber, pageSize, sort));
    }

    public Predicate getPredicateDateAfterAndDateBefore(Long after, Long before, Root<Ship> root, CriteriaBuilder cb, Predicate predicate) {
        if (Objects.nonNull(after) && Objects.nonNull(before)) {
            Date afterDate = new Date(after);
            Date beforeDate = new Date(before);
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("prodDate"), afterDate),
                    cb.lessThan(root.get("prodDate"), beforeDate));
        } else if (Objects.isNull(after) && Objects.nonNull(before)) {
            Date beforeDate = new Date(before);
            predicate = cb.and(predicate, cb.lessThan(root.get("prodDate"), beforeDate));
        } else if (Objects.nonNull(after)) {
            Date afterDate = new Date(after);
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("prodDate"), afterDate));
        }
        return predicate;
    }

    public Predicate getPredicateMinAndMaxRating(Double minRating, Double maxRating, Root<Ship> root, CriteriaBuilder cb, Predicate predicate) {
        if (Objects.nonNull(minRating) && Objects.nonNull(maxRating)) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("rating"), minRating),
                    cb.lessThan(root.get("rating"), maxRating));
        } else if (Objects.isNull(minRating) && Objects.nonNull(maxRating)) {
            predicate = cb.and(predicate, cb.lessThan(root.get("rating"), maxRating));
        } else if (Objects.nonNull(minRating)) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("rating"), minRating));
        }
        return predicate;
    }

    public Predicate getPredicateMinAndMaxSpeed(Double minSpeed, Double maxSpeed, Root<Ship> root, CriteriaBuilder cb, Predicate predicate) {
        if (Objects.nonNull(minSpeed) && Objects.nonNull(maxSpeed)) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("speed"), minSpeed),
                    cb.lessThan(root.get("speed"), maxSpeed));
        } else if (Objects.isNull(minSpeed) && Objects.nonNull(maxSpeed)) {
            predicate = cb.and(predicate, cb.lessThan(root.get("speed"), maxSpeed));
        } else if (Objects.nonNull(minSpeed)) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("speed"), minSpeed));
        }
        return predicate;
    }

    public Predicate getPredicateMinAndMaxCrewSize(Integer minCrewSize, Integer maxCrewSize, Root<Ship> root, CriteriaBuilder cb, Predicate predicate) {
        if (Objects.nonNull(minCrewSize) && Objects.nonNull(maxCrewSize)) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize),
                    cb.lessThan(root.get("crewSize"), maxCrewSize));
        } else if (Objects.isNull(minCrewSize) && Objects.nonNull(maxCrewSize)) {
            predicate = cb.and(predicate, cb.lessThan(root.get("crewSize"), maxCrewSize));
        } else if (Objects.nonNull(minCrewSize)) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize));
        }
        return predicate;
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
