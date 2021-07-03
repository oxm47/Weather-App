package com.example.weatherapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Model.WeatherModel;
import com.example.weatherapp.R;

import java.text.DateFormat;
import java.util.ArrayList;

public class WeatherRecordAdapter extends RecyclerView.Adapter<WeatherRecordAdapter.MyViewHolder> {

    ArrayList<WeatherModel> weatherModels;
    Context context;

    public WeatherRecordAdapter(ArrayList<WeatherModel> weatherModels, Context context) {
        this.weatherModels = weatherModels;
        this.context = context;
    }

    @Override
    public WeatherRecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.snippet_rv_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRecordAdapter.MyViewHolder holder, int position) {
        WeatherModel weatherModel = weatherModels.get(position);
        holder.txCityName.setText(weatherModel.getCityName());
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(holder.imgWeather.getContext());
        holder.txDate.setText(dateFormat.format(weatherModel.getWeatherDate()));
        holder.txTemp.setText(weatherModel.getTemp()+"°c");
        holder.txFeelsLike.setText("Feels Like "+weatherModel.getFeelsLike()+"°c");
        holder.txDesc.setText(weatherModel.getWeatherDesc());
        holder.imgWeather.setImageDrawable(getImgIcon(weatherModel,holder.imgWeather.getContext()));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getImgIcon(WeatherModel weatherModel, Context context) {
        if(weatherModel.getWeatherIcon().contains("01")){
            return context.getDrawable(R.drawable.ic_01);
        }else if(weatherModel.getWeatherIcon().contains("02")){
            return context.getDrawable(R.drawable.ic_02);
        }else if(weatherModel.getWeatherIcon().contains("03")){
            return context.getDrawable(R.drawable.ic_03);
        }else if(weatherModel.getWeatherIcon().contains("04")){
            return context.getDrawable(R.drawable.ic_04);
        }else if(weatherModel.getWeatherIcon().contains("09")){
            return context.getDrawable(R.drawable.ic_09);
        }else if(weatherModel.getWeatherIcon().contains("10")){
            return context.getDrawable(R.drawable.ic__10);
        }else if(weatherModel.getWeatherIcon().contains("11")){
            return context.getDrawable(R.drawable.ic__11);
        }else if(weatherModel.getWeatherIcon().contains("50")){
            return context.getDrawable(R.drawable.ic__50);
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
        return context.getDrawable(R.drawable.ic_01);
    }

    @Override
    public int getItemCount() {
        return weatherModels.size();
    }
     class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txCityName,txDesc,txFeelsLike,txTemp,txDate;
        ImageView imgWeather;
         public MyViewHolder(@NonNull View itemView) {
             super(itemView);
             txCityName = itemView.findViewById(R.id.tx_city_name);
             txDesc = itemView.findViewById(R.id.tx_weather_desc);
             txFeelsLike = itemView.findViewById(R.id.tx_feels_like);
             txTemp = itemView.findViewById(R.id.tx_temp);
             txDate = itemView.findViewById(R.id.tx_date);
             imgWeather = itemView.findViewById(R.id.img_weather);
         }
     }
}



