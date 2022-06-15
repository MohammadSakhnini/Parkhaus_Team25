package de.hbrs.team25.se1_starter_repo.interfaces;

import de.hbrs.team25.se1_starter_repo.classes.CarType;

public interface CarIF {
    int nr();

    long begin();

    long end();

    int duration();

    int price();

    CarType getCarType();
}
