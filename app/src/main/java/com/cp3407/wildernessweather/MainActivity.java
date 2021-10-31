package com.cp3407.wildernessweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cp3407.wildernessweather.settings.SettingsActivity;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences favourites;
    private SharedPreferences settingsData;
    private WeatherDataService weatherDataService;

    private ListView locationList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup custom app bar.
        TextView titleView = findViewById(R.id.tv_title);
        titleView.setText("Wilderness Weather");

        favourites = getSharedPreferences("favourites", Context.MODE_PRIVATE);
        settingsData = getSharedPreferences("settings", Context.MODE_PRIVATE);

        weatherDataService = new WeatherDataService(MainActivity.this);

        TextView currentDate = findViewById(R.id.tv_currentDate);
        SearchView searchBox = findViewById(R.id.sv_searchHomePage);
        ImageButton settingsButton = findViewById(R.id.btn_settingsButton);
        ImageButton historyButton = findViewById(R.id.btn_historyMain);
        locationList = findViewById(R.id.lv_locationList);

        searchBox.setQueryHint("Search");

        currentDate.setText(getDate());
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchText) {

                Intent intent = new Intent(MainActivity.this, APIactivity.class);
                intent.putExtra("cityName", searchText);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        historyButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DbActivity.class);
            intent.putExtra("woeid", "0");
            startActivity(intent);
        });

        initialiseList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseList();
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();

        String dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String daySuffix = getSuffix(day);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);

        return dayName + " " + day + daySuffix + " " + month + ", " + year;
    }

    public String getSuffix(int day) {
        switch (day) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }


    public void initialiseList() {
        String[] favouriteLocations = getFavCityNames();
        boolean isMetric = settingsData.getBoolean("isMetric", true);
        weatherDataService.getFavourites(favouriteLocations, weatherReportModels -> {
            WeatherReportModelListAdapter adapter = new WeatherReportModelListAdapter(MainActivity.this, R.layout.weather_report_list_item, weatherReportModels, isMetric);
            locationList.setAdapter(adapter);
            locationList.setOnItemClickListener((adapterView, view, i, l) -> {
                WeatherReportModel weatherReportModel = weatherReportModels.get(i);
                Intent intent = new Intent(MainActivity.this, SingleWeatherReportActivity.class);
                intent.putExtra("report", Parcels.wrap(weatherReportModel));
                startActivity(intent);
            });
        });
    }

    public String[] getFavCityNames() {
        Set<String> cityNamesAsSet = favourites.getStringSet("locations", new HashSet<>());
        Log.i("favourites", "favourites: " + cityNamesAsSet);
        return convertSetToArray(cityNamesAsSet);
    }

    public String[] convertSetToArray(Set<String> setOfString) {
        return setOfString.toArray(new String[0]);
    }
}
