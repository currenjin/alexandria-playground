package com.tdd.weather;

import com.tdd.Observer;
import com.tdd.Subject;

import java.util.ArrayList;
import java.util.List;

public class Weather implements Subject {
    private final List<Observer> observers = new ArrayList<>();
    private WeatherData weatherData;

    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        this.observers.forEach(observer -> observer.update(
                this.weatherData.temperature,
                this.weatherData.humidity,
                this.weatherData.pressure
        ));
    }

    public WeatherData getWeatherData() {
        return this.weatherData;
    }
}
