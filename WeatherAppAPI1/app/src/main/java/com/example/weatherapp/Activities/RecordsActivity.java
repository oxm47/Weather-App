package com.example.weatherapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Adapter.WeatherRecordAdapter;
import com.example.weatherapp.DBHelper.MyHelper;
import com.example.weatherapp.R;

import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {

    RecyclerView rvRecords;
    ImageButton btnBack;
    MyHelper dbHelper;
    WeatherRecordAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        init();
        listeners();
    }

    private void listeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        dbHelper = new MyHelper(this);
        rvRecords = findViewById(R.id.rv_records);
        btnBack = findViewById(R.id.btn_back);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        rvRecords.addItemDecoration(dividerItemDecoration);
        rvRecords.setHasFixedSize(true);
        rvRecords.setItemViewCacheSize(20);
        rvRecords.setDrawingCacheEnabled(true);
        rvRecords.setNestedScrollingEnabled(false);
        rvRecords.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        setUpAdapter();
    }

    private void setUpAdapter(){
        adapter = new WeatherRecordAdapter(dbHelper.getAllWeatherRecords(),this);
        rvRecords.setAdapter(adapter);
    }

}