package com.example.weatherapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.weatherapp.R;
import com.example.weatherapp.Uitls.NetworkService;

public class MainActivity extends AppCompatActivity {

    EditText cityNameEditText;
    CardView cardView;
    ImageButton btnSearch;
    TextView tempTV,minTempTV,maxTempTV,weatherDescTV,cityNameTV;
    TextView humidity,feelsLike;
    private String TAG = MainActivity.class.getSimpleName();
    private String cityNameTxt;

    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
        }

        cityNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cityNameTxt = String.valueOf(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ((findViewById(R.id.next_btn))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));

            }
        });
        ((findViewById(R.id.records_btn))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(getApplicationContext(),RecordsActivity.class));}
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.GONE);

                networkService.getApiResult(cityNameEditText,
                        tempTV,
                        minTempTV,maxTempTV,feelsLike,humidity,cityNameTV,weatherDescTV,cardView);
//                callAPIForResult(cityNameTxt);
            }
        });

    }

    private void init(){
        networkService = new NetworkService(this);

        cityNameEditText = (EditText)findViewById(R.id.city_name_edit);
        btnSearch = findViewById(R.id.btn_search);
        cardView = findViewById(R.id.card_view);
        tempTV = (TextView) findViewById(R.id.temp_tv);
        minTempTV = (TextView) findViewById(R.id.minTempTV);
        maxTempTV = (TextView) findViewById(R.id.maxTempTv);
        weatherDescTV = (TextView) findViewById(R.id.weather_desc_tv);
        humidity = (TextView) findViewById(R.id.humidity);
        feelsLike = (TextView) findViewById(R.id.feels_like);
        cityNameTV = (TextView) findViewById(R.id.cityNameTv);

//        cityNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    //
//                    if(!(cityNameEditText.getText().toString()).isEmpty() && !cityNameTxt.isEmpty()){
//                        //Toast.makeText(MainActivity.this, "Seacrh Is not Empty"+cityNameEditText.getText().toString(), Toast.LENGTH_SHORT).show();
//                        cardView.setVisibility(View.GONE);
//                        networkService.getApiResult(cityNameEditText,
//                                tempTV,
//                                minTempTV,maxTempTV,feelsLike,humidity,cityNameTV,weatherDescTV,cardView);
////                        callAPIForResult(cityNameTxt);
//                    }
//                    else {
//                        Toast.makeText(MainActivity.this, "Please enter valid city name", Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//
//                }
//                return false;
//            }
//        });
    }

}

//{"coord":{"lon":73.1338,"lat":33.7104},"weather":[{"id":801,"main":"Clouds","description":"few clouds","icon":"02d"}],"base":"stations","main":{"temp":309.76,"feels_like":309.04,"temp_min":309.76,"temp_max":309.76,"pressure":1003,"humidity":25,"sea_level":1003,"grnd_level":946},"visibility":10000,"wind":{"speed":3.33,"deg":195,"gust":2.35},"clouds":{"all":17},"dt":1624695442,"sys":{"country":"PK","sunrise":1624665519,"sunset":1624717305},"timezone":18000,"id":1162015,"name":"Islamabad","cod":200}