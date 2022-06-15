package de.hbrs.team25.se1_starter_repo.classes;

import de.hbrs.team25.se1_starter_repo.interfaces.CarIF;
import de.hbrs.team25.se1_starter_repo.interfaces.ITicket;

import java.util.Date;

public class Ticket implements ITicket {
    private String TicketNumber;
    private Date TicketDate;
    private CarIF car;

    public Ticket(String ticketNumber, CarIF car) {
        TicketNumber = ticketNumber;
        TicketDate = new Date();
        this.car = car;
    }

    @Override
    public String GetTicketNumber() {
        return TicketNumber;
    }

    @Override
    public Date GetTicketDate() {
        return TicketDate;
    }

    @Override
    public CarIF GetCar() {
        return car;
    }
}
