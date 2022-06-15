package de.hbrs.team25.se1_starter_repo.classes;

import de.hbrs.team25.se1_starter_repo.interfaces.IParkingSpot;

public class ParkingSpot implements IParkingSpot {


    private Boolean IsHandyCapSpot;
    private Boolean Occupied;
    private Boolean Reserved;


    public ParkingSpot(Boolean isHandyCapSpot, Boolean occupied, Boolean reservations) {
        IsHandyCapSpot = isHandyCapSpot;
        Occupied = occupied;
        Reserved = reservations;
    }

    @Override
    public Boolean getIsHandyCapSpot() {
        return IsHandyCapSpot;
    }

    @Override
    public Boolean getIsOccupied() {
        return Occupied;
    }

    @Override
    public Boolean getIsReserved() {
        return Reserved;
    }

    public void setHandyCapSpot(Boolean handyCapSpot) {
        IsHandyCapSpot = handyCapSpot;
    }

    public void setOccupied(Boolean occupied) {
        Occupied = occupied;
    }

    public void setReserved(Boolean reserved) {
        Reserved = reserved;
    }
}
