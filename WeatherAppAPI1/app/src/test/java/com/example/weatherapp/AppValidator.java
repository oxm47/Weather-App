package com.example.weatherapp;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;

import com.example.weatherapp.Uitls.NetworkService;
import com.example.weatherapp.Uitls.Validators;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import static com.example.weatherapp.Uitls.Helpers.kelvinToCelsius;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class AppValidator {
    @Test
    public void kalvinConverterTest() {
        assertEquals(kelvinToCelsius((double) 600), (int) Math.ceil(326.85));
        assertThrows(NullPointerException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                kelvinToCelsius(null);
            }
        });
        assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
            @Override
            public void run() {
                kelvinToCelsius((double) -1);
            }
        });
    }

    @Test
    public void cityNameValidator() {
        String cityName = "ValidName";
        assertTrue(Validators.isValidCityName(cityName));
        cityName = "In-Valid-Name";
        assertFalse(Validators.isValidCityName(cityName));
    }
}
