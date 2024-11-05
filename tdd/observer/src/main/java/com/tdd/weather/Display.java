package com.tdd.weather;

import com.tdd.Observer;
import com.tdd.Subject;

public class Display implements Observer {

    @Override
    public void update(Float temperature, Float humidity, Float pressure) {
        System.out.println("Temperature: " + temperature);
        System.out.println("Humidity: " + humidity);
        System.out.println("Pressure: " + pressure);
    }
}
