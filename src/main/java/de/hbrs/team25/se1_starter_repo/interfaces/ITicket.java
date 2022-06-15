package de.hbrs.team25.se1_starter_repo.interfaces;

import java.util.Date;

public interface ITicket {
    public String GetTicketNumber();

    public Date GetTicketDate();

    public CarIF GetCar();
}
