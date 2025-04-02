package com.tdd.weather;

import com.tdd.Observer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherTest {
    private final Observer observer = new Display();
    private final Weather weather = new Weather();

    @Test
    void set_weather() {
        WeatherData weatherData = new WeatherData(1F, 1F, 1F);

        weather.setWeatherData(weatherData);
        WeatherData actual = weather.getWeatherData();

        assertEquals(weatherData, actual);
    }

    @Test
    void set_observer() {
        WeatherData weatherData = new WeatherData(1F, 1F, 1F);
        weather.setWeatherData(weatherData);

        assertDoesNotThrow(() -> weather.registerObserver(observer));
    }

    @Test
    void remove_observer() {
        WeatherData weatherData = new WeatherData(1F, 1F, 1F);
        weather.setWeatherData(weatherData);
        weather.registerObserver(observer);

        assertDoesNotThrow(() -> weather.unregisterObserver(observer));
    }

    @Test
    void notify_observer_one_call() {
        WeatherData weatherData = new WeatherData(2F, 1F, 1F);
        weather.setWeatherData(weatherData);

        weather.registerObserver(observer);
        System.out.println("-----------------------");

        weather.setWeatherData(weatherData);
    }

    @Test
    void notify_observer_three_call() {
        WeatherData weatherData = new WeatherData(2F, 1F, 1F);
        weather.registerObserver(observer);
        weather.setWeatherData(weatherData);

        WeatherData secondWeatherData = new WeatherData(3F, 1F, 1F);
        weather.setWeatherData(secondWeatherData);

        WeatherData thirdWeatherData = new WeatherData(4F, 1F, 1F);
        weather.setWeatherData(thirdWeatherData);
    }

    @Test
    void notify_both_observer_three_call() {
        WeatherData weatherData = new WeatherData(2F, 1F, 1F);
        weather.registerObserver(observer);
        weather.registerObserver(observer);
        weather.setWeatherData(weatherData);

        WeatherData secondWeatherData = new WeatherData(3F, 1F, 1F);
        weather.setWeatherData(secondWeatherData);

        WeatherData thirdWeatherData = new WeatherData(4F, 1F, 1F);
        weather.setWeatherData(thirdWeatherData);
    }
}