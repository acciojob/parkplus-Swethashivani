package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        try {
            User user = userRepository3.findById(userId).orElse(null);
            if (Objects.isNull(user)) {
                throw new Exception("Cannot make reservation");
            }
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).orElse(null);
            if (Objects.isNull(parkingLot)) {
                throw new Exception("Cannot make reservation");
            }
            int minPrice = Integer.MAX_VALUE;
            Spot requiredSpot = null;
            for (Spot spot : parkingLot.getSpotList()) {
                int NoOfWheelsPossible = Integer.MAX_VALUE;
                if (spot.getSpotType() == SpotType.TWO_WHEELER)
                    NoOfWheelsPossible = 2;
                else if (spot.getSpotType() == SpotType.FOUR_WHEELER)
                    NoOfWheelsPossible = 4;

                if (numberOfWheels <= NoOfWheelsPossible && !spot.getOccupied()) {
                    if ((spot.getPricePerHour() * timeInHours) < minPrice) {
                        minPrice = (spot.getPricePerHour() * timeInHours);
                        requiredSpot = spot;
                    }
                }
            }
            if (Objects.isNull(requiredSpot)) {
                throw new Exception("Cannot make reservation");
            }
            requiredSpot.setOccupied(true);
            spotRepository3.save(requiredSpot);
            Reservation reservation = new Reservation(timeInHours, user, requiredSpot);
            List<Reservation> userReservations = user.getReservationList();
            if (Objects.isNull(userReservations)) {
                userReservations = new ArrayList<>();
            }
            userReservations.add(reservation);
            userRepository3.save(user);
            return reservation;
        }catch (Exception e) {
            throw new Exception("Cannot make reservation");
        }
    }
}
