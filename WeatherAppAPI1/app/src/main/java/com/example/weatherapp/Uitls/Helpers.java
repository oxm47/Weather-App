package com.example.weatherapp.Uitls;

public class Helpers {
    public static int kelvinToCelsius(Double kelvin)
    {
        if(kelvin<0) throw new IllegalArgumentException("Value can't be less than 0");
        return (int) Math.round(( kelvin - 273.15F));
    }

}
