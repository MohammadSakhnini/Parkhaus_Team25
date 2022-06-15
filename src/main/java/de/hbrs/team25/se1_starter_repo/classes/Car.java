package de.hbrs.team25.se1_starter_repo.classes;

import de.hbrs.team25.se1_starter_repo.interfaces.CarIF;

import java.util.Arrays;

// ToDo replace 0 by correct values read from this.params
public class Car implements CarIF {
    String[] params;
    private CarType carType;

    public Car(String[] params) {
        this.params = params;
        if (params[8].equals("E-Car")) {
            carType = CarType.Electric;
        } else {
            carType = CarType.Gas;
        }
    }

    @Override
    public int nr() {
        return 0;
    }

    @Override
    public long begin() {
        return 0;
    }

    @Override
    public long end() {
        return 0;
    }

    @Override
    public int duration() {
        return 0;
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(params);
    }

    public CarType getCarType() {
        return carType;
    }
}

