package com.example.weatherapp.Uitls;

public class Validators {
    public static boolean isValidCityName(String cityName){
        return (cityName.matches("[a-zA-Z_ ]+"));
    }
}
