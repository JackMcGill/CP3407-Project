package com.cp3407.wildernessweather;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cp3407.wildernessweather.database.WeatherReportViewModel;

import org.parceler.Parcels;

public class SingleWeatherReportActivity extends AppCompatActivity {
    WeatherReportModel singleWeatherReport;
    WeatherReportViewModel viewModel;

    TextView idView, stateView, stateAbbrView, windDirectionCompassView, createdView,
            applicableDateView, minTempView, maxTempView, tempView, windspeedView, windDirectionView,
            airPressureView, humidityView, visibilityView, predictabilityView; // TODO find a better way to do this (this is disgusting)

    Button populateFieldsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_weather_report);

        // Get the report from the intent and unwrap it
        singleWeatherReport = (WeatherReportModel) Parcels.unwrap(getIntent().getParcelableExtra("report"));

        idView = findViewById(R.id.tv_id);
        stateView = findViewById(R.id.tv_weatherStateName);
        stateAbbrView = findViewById(R.id.tv_weatherStateAbbr);
        windDirectionCompassView = findViewById(R.id.tv_windDirectionCompass);
        createdView = findViewById(R.id.tv_created);
        applicableDateView = findViewById(R.id.tv_applicableDate);
        minTempView = findViewById(R.id.tv_minTemp);
        maxTempView = findViewById(R.id.tv_maxTemp);
        tempView = findViewById(R.id.tv_weatherTemp);
        windspeedView = findViewById(R.id.tv_windSpeed);
        windDirectionView = findViewById(R.id.tv_windDirection);
        airPressureView = findViewById(R.id.tv_airPressure);
        humidityView = findViewById(R.id.tv_humidity);
        visibilityView = findViewById(R.id.tv_visibility);
        predictabilityView = findViewById(R.id.tv_predictability);

        populateFieldsButton = findViewById(R.id.btn_unwrap);

        viewModel = new ViewModelProvider(this).get(WeatherReportViewModel.class);

    }

    // Adds the single weather report to the local database
    public void addToDatabase(View view) {
        viewModel.insert(singleWeatherReport);

        Toast.makeText(SingleWeatherReportActivity.this, "Added to database", Toast.LENGTH_SHORT).show();
    }

    // This is just a helper method used to demo the unwrapping of the Parcelable object
    public void populateFields(View view) {
        String stateName = singleWeatherReport.getWeatherStateName();
        Log.i("parcel", "State name: " + stateName);

        idView.setText(String.valueOf("ID: " + singleWeatherReport.getId()));
        stateView.setText(String.valueOf("Weather State: " + singleWeatherReport.getWeatherStateName()));
        stateAbbrView.setText(String.valueOf("Weather State Abbreviation: " + singleWeatherReport.getWeatherStateAbbr()));
        windDirectionCompassView.setText(String.valueOf("Wind Direction Compass: " + singleWeatherReport.getWindDirectionCompass()));
        createdView.setText(String.valueOf("Created: " + singleWeatherReport.getCreated()));
        applicableDateView.setText(String.valueOf("Created: " + singleWeatherReport.getApplicableDate()));
        minTempView.setText(String.valueOf("Min Temp: " + singleWeatherReport.getMinTemp()));
        minTempView.setText(String.valueOf("Max Temp: " + singleWeatherReport.getMaxTemp()));
        tempView.setText(String.valueOf("Weather Temp: " + singleWeatherReport.getTheTemp()));
        windspeedView.setText(String.valueOf("Wind Speed: " + singleWeatherReport.getWindSpeed()));
        windDirectionView.setText(String.valueOf("Wind Direction: " + singleWeatherReport.getWindDirection()));
        airPressureView.setText(String.valueOf("Air Pressure: " + singleWeatherReport.getAirPressure()));
        humidityView.setText(String.valueOf("Humidity: " + singleWeatherReport.getHumidity()));
        visibilityView.setText(String.valueOf("Visibility: " + singleWeatherReport.getVisibility()));
        predictabilityView.setText(String.valueOf("Predictability: " + singleWeatherReport.getPredictability()));
    }
}