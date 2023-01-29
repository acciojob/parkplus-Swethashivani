package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(value = EnumType.STRING)
    private SpotType spotType;

    private int pricePerHour;
    private boolean occupied;

    public Spot(SpotType spotType, int pricePerHour) {
        this.spotType = spotType;
        this.pricePerHour = pricePerHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public void setSpotType(SpotType spotType) {
        this.spotType = spotType;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }



    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<Reservation> reservationList = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private ParkingLot parkingLot;

    public Spot() {
    }
}
