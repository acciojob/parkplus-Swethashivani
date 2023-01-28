package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;

    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot(name, address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        SpotType spotType = SpotType.OTHERS;
        if (numberOfWheels.equals(2))
            spotType = SpotType.TWO_WHEELER;
        else if (numberOfWheels.equals(4))
            spotType = SpotType.FOUR_WHEELER;

        Spot spot = new Spot(spotType, pricePerHour);
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);
        spot.setOccupied(false);
        spotRepository1.save(spot);
        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot = spotRepository1.findById(spotId).orElse(null);
        if (!Objects.isNull(spot)) {
            ParkingLot parkingLot = spot.getParkingLot();
            List<Spot> spotList= parkingLot.getSpotList();
            for (Spot s : spotList) {
                if(s == spot)
                    spotRepository1.delete(s);

            }
            spotRepository1.deleteById(spotId);
        }
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot = spotRepository1.findById(spotId).orElse(null);
        if (!Objects.isNull(spot)) {
            spot.setParkingLot(parkingLotRepository1.findById(parkingLotId).get());
            spot.setPricePerHour(pricePerHour);
            spotRepository1.save(spot);
            return spot;
        }
        return null;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).orElse(null);
        if (!Objects.isNull(parkingLot)) {
            List<Spot> spotList= parkingLot.getSpotList();
            for (Spot spot: spotList) {
                spotRepository1.delete(spot);
            }
            parkingLotRepository1.deleteById(parkingLotId);
        }
    }
}
