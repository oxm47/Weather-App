package com.example.weatherapp.Model;

import java.io.Serializable;
import java.util.Date;

public class WeatherModel implements Serializable {
    int temp,tempMin,tempMax,feelsLike;
    int humidity;
    String cityName,weatherDesc;
    int weatherId;
    String weatherIcon;

    Date weatherDate;
    public WeatherModel(){weatherDate = new Date();}
    public int getTemp() {
        return temp;
    }

    public void setWeatherDate(Date weatherDate) {
        this.weatherDate = weatherDate;
    }
    public Date getWeatherDate() {
        return weatherDate;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(int feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public String  getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String  weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
}

//01d.png 	01n.png 	clear sky
//        02d.png 	02n.png 	few clouds
//        03d.png 	03n.png 	scattered clouds
//        04d.png 	04n.png 	broken clouds
//        09d.png 	09n.png 	shower rain
//        10d.png 	10n.png 	rain
//        11d.png 	11n.png 	thunderstorm
//        13d.png 	13n.png 	snow
//        50d.png 	50n.png 	mist

