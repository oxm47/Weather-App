package com.example.weatherapp.Uitls;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.weatherapp.DBHelper.MyHelper;
import com.example.weatherapp.Model.WeatherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.weatherapp.Uitls.Helpers.kelvinToCelsius;
import static com.example.weatherapp.Uitls.Validators.isValidCityName;
import static com.example.weatherapp.Values.API_KEY;

public class NetworkService {
    private static final String TAG = "NetworkService";
    private Context context;
    MyHelper dbHelper;

    private final MutableLiveData<JSONObject> _finalJson = new MutableLiveData<>();
    public LiveData<JSONObject> finalJson = _finalJson;

    public NetworkService(Context context){
        this.context =context;
        AndroidNetworking.initialize(context);
        dbHelper = new MyHelper(context);
    }
    public void callAPIForResult(final String cityName)
    {

        try {
            if(((Activity)context).getWindow()!=null) {
                Progress.ShowProgress(context, "Getting weather information please wait a while...");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
        AndroidNetworking.get("https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+API_KEY)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(MainActivity.this, "Response Received....", Toast.LENGTH_SHORT).show();
                        try {
                            if(((Activity)context).getWindow()!=null) {
                                Progress.CloseProgress();
                            }
                        }catch (Exception e){
                        }

                        Log.d(TAG, "Response ....."+String.valueOf(response));

                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);
                            JSONObject mainArray = response.getJSONObject("main");
                            _finalJson.setValue(response);
                            Log.d(TAG, "Object 2 "+String.valueOf(mainArray.get("temp"))+" : "  +mainArray.get("feels_like")
                                    +" : "  +mainArray.get("temp_min") +" : "  +mainArray.get("temp_max"));
                            Log.d(TAG,String.valueOf(weatherObj.get("description")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            _finalJson.setValue(null);
                            Toast.makeText(context, "An error Occurred!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Object 2"+e);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        try{
                            if(((Activity)context).getWindow()!=null) {
                                Progress.CloseProgress();
                            }
                        }catch (Exception e){
                        }

                        _finalJson.setValue(null);
                        Toast.makeText(context, "somethings goes wrong please try again" , Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onError errorCode : " + error);

                        if (error.getErrorCode() != 0) {
                            // received error from server
                            // error.getErrorCode() - the error code from server
                            // error.getErrorBody() - the error body from server
                            // error.getErrorDetail() - just an error detail
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            // get parsed error object (If ApiError is your class)
                            // ApiError apiError = error.getErrorAsObject(ApiError.class);
                        }
                    }
                });
    }

    public void getApiResult(final EditText cityNameEditText,
                             final TextView tempTV, final TextView minTempTV,
                             final TextView maxTempTV, final TextView feelsLike,
                             final TextView humidity, final TextView cityNameTV,
                             final TextView weatherDescTV,
                             final CardView cardView
                           ){
        String cityName = cityNameEditText.getText().toString();
        if(isValidCityName(cityName)) {
            callAPIForResult(cityName);
            this._finalJson.observe((LifecycleOwner) context, new Observer<JSONObject>() {
                @Override
                public void onChanged(JSONObject response) {
                    try {
                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject weatherObj = weatherArray.getJSONObject(0);
                        JSONObject mainArray = response.getJSONObject("main");


                        cardView.setVisibility(View.VISIBLE);
                        cityNameEditText.setText("");

                        tempTV.setText(kelvinToCelsius((Double) mainArray.get("temp")) +"°");
                        minTempTV.setText(kelvinToCelsius((Double) mainArray.get("temp_min")) +"° / ");
                        maxTempTV.setText(kelvinToCelsius((Double) mainArray.get("temp_max")) +"°");
                        feelsLike.setText("Feels Like : " + kelvinToCelsius((Double) mainArray.get("feels_like")) +"°");
                        humidity.setText("Humidity : " + mainArray.get("humidity"));
                        cityNameTV.setText(String.valueOf(response.get("name")));
                        weatherDescTV.setText(String.valueOf(weatherObj.get("description")));

                        WeatherModel weatherModel = new WeatherModel();
                        weatherModel.setCityName(String.valueOf(response.get("name")));
                        weatherModel.setTemp(kelvinToCelsius((Double) mainArray.get("temp")));
                        weatherModel.setTempMax(kelvinToCelsius((Double) mainArray.get("temp_max")));
                        weatherModel.setTempMin(kelvinToCelsius((Double) mainArray.get("temp_min")));
                        weatherModel.setFeelsLike(kelvinToCelsius((Double) mainArray.get("feels_like")));
                        weatherModel.setHumidity((Integer) mainArray.get("humidity"));
                        weatherModel.setWeatherDesc(String.valueOf(weatherObj.get("description")));
                        weatherModel.setWeatherIcon(String.valueOf(weatherObj.get("icon")));
                        weatherModel.setWeatherId(Integer.parseInt(String.valueOf(weatherObj.get("id"))));
                        dbHelper.addWeather(weatherModel);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Toast.makeText(context, "City Name is not valid!", Toast.LENGTH_SHORT).show();
        }

    }
}

