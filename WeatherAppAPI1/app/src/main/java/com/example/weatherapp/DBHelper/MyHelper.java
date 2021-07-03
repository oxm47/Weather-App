package com.example.weatherapp.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.weatherapp.Model.WeatherModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class MyHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DB_PATH_SUFFIX;
    private static final String TAG;
    private static final String TABLE_RES;
    public static final String KEY_TEMPERATURE;
    public static final String KEY_TEMPERATURE_MIN;
    public static final String KEY_TEMPERATURE_MAX;
    public static final String KEY_FEELS_LIKE;
    public static final String KEY_HUMIDITY;
    public static final String KEY_CITY_NAME;
    public static final String KEY_WEATHER_DESC;
    public static final String KEY_WEATHER_ICON;
    public static final String KEY_WEATHER_DATE;
    private static final String KEY_ID;
    private static final int DATABASE_VERSION;
    private static final String DATABASE_NAME;

    static {
        DB_PATH_SUFFIX = "/databases/";
        KEY_HUMIDITY = "humidity";
        KEY_CITY_NAME = "cityName";
        KEY_WEATHER_DESC = "weatherDesc";
        KEY_WEATHER_ICON = "weatherIcon";
        KEY_WEATHER_DATE = "weatherDate";
        KEY_ID = "Id";
        DATABASE_VERSION = 3;
        DATABASE_NAME = "WeatherDB";
        TAG = "MyHelper";
        TABLE_RES = "weather";
        KEY_TEMPERATURE = "temp";
        KEY_TEMPERATURE_MIN = "tempMin";
        KEY_TEMPERATURE_MAX = "tempMax";
        KEY_FEELS_LIKE = "feelsLike";
    }

    private WeatherModel res;

    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDatabaseFromAssets();
                Toast.makeText(context, "Copying success from assets folder", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public MyHelper(Context context) {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
        this.context = context;
    }

    private String getDatabasePath() {
        return String.format("%s%s%s", context.getApplicationInfo().dataDir, DB_PATH_SUFFIX, DATABASE_NAME);
    }

    public void CopyDatabaseFromAssets() throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        //path to the created db
        String outFileName = getDatabasePath();
        //if the path doesnt exists first. create it
        File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();
        //open the db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }
        //close stream
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d(TAG, "Database is opening ");
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        Log.d(TAG, "Database is closing ");
        super.close();
    }

    @Override
    public String getDatabaseName() {
        return "Database : " + super.getDatabaseName();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Check if database exits in assets folder then
        // Import and open database
        try {
            openDataBase();
        }catch (Exception e) {
            Log.e(TAG, "Database failed  ",e );
            e.printStackTrace();
        }

        String CREATE_WEATHER_TABLE = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER  )", TABLE_RES, KEY_ID, KEY_CITY_NAME, KEY_WEATHER_DESC, KEY_TEMPERATURE, KEY_TEMPERATURE_MAX, KEY_TEMPERATURE_MIN, KEY_FEELS_LIKE, KEY_HUMIDITY, KEY_WEATHER_ICON, KEY_WEATHER_DATE);
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    public void deleteWeatherRecord(int date) {
        Log.d("DebugData", "inside deleteBook");
        AtomicReference<SQLiteDatabase> db = new AtomicReference<>(this.getWritableDatabase());
        db.get().delete(TABLE_RES, KEY_WEATHER_DATE + "=?", new String[]{String.valueOf(date)});
        db.get().close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Weather");
        this.onCreate(db);
    }

    public void addWeather(WeatherModel res) {
        this.res = res;
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues values;
        values = new ContentValues();
        values.put(KEY_CITY_NAME,
                res.getCityName());
        values.put(KEY_WEATHER_DESC,
                res.getWeatherDesc());
        values.put(KEY_TEMPERATURE,
                res.getTemp());
        values.put(KEY_TEMPERATURE_MIN, res.getTempMin());
        values.put(KEY_TEMPERATURE_MAX,
                res.getTempMax());
        values.put(KEY_HUMIDITY,
                res.getHumidity());
        values.put(KEY_FEELS_LIKE,
                res.getFeelsLike());
        values.put(KEY_WEATHER_ICON,
                res.getWeatherIcon());
        values.put(KEY_WEATHER_DATE,
                res.getWeatherDate().toString());
        long insert = db.insert(TABLE_RES,
                null,
                values);
        Log.e(TAG, "Number of rows inserted : " + insert );
        db.close();
    }

    public ArrayList<WeatherModel> getAllWeatherRecords() {
        ArrayList<WeatherModel> mainModelList;
        mainModelList = new ArrayList<>();
        String query = String.format("SELECT  * FROM %s", TABLE_RES);
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        Cursor c;
        c = db.rawQuery(query, null);
        if (c.moveToFirst()) do {
            AtomicReference<WeatherModel> mainModel = new AtomicReference<>(new WeatherModel());
            mainModel.get().setTemp(Integer.parseInt(c.getString(c.getColumnIndex(KEY_TEMPERATURE))));
            mainModel.get().setTempMax(Integer.parseInt(c.getString(c.getColumnIndex(KEY_TEMPERATURE_MAX))));
            mainModel.get().setTempMin(Integer.parseInt(c.getString(c.getColumnIndex(KEY_TEMPERATURE_MIN))));
            mainModel.get().setFeelsLike(Integer.parseInt(c.getString(c.getColumnIndex(KEY_FEELS_LIKE))));
            mainModel.get().setHumidity(Integer.parseInt(c.getString(c.getColumnIndex(KEY_HUMIDITY))));
            mainModel.get().setCityName(c.getString(c.getColumnIndex(KEY_CITY_NAME)));
            mainModel.get().setWeatherDesc(c.getString(c.getColumnIndex(KEY_WEATHER_DESC)));
            mainModel.get().setWeatherIcon(c.getString(c.getColumnIndex(KEY_WEATHER_ICON)));
            mainModel.get().setWeatherId(c.getInt(c.getColumnIndex(KEY_WEATHER_ICON)));
            mainModel.get().setWeatherDate(new Date(c.getInt(c.getColumnIndex(KEY_WEATHER_DATE))));
            boolean add = mainModelList.add(mainModel.get());
            if(add)
                Log.i(TAG, "getAllWeatherRecords: Added");
        } while (c.moveToNext());
        c.close();
        Log.e(TAG, "model_list: " +mainModelList.size() );
        return mainModelList;
    }
}
