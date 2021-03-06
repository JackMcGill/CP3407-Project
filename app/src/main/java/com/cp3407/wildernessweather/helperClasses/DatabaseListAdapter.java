package com.cp3407.wildernessweather.helperClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cp3407.wildernessweather.R;
import com.cp3407.wildernessweather.SingleWeatherReportActivity;
import com.cp3407.wildernessweather.WeatherReportModel;

import org.parceler.Parcels;

import java.util.List;

public class DatabaseListAdapter extends RecyclerView.Adapter<DatabaseListAdapter.WeatherReportViewHolder> {

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(WeatherReportModel myModel);
    }

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<WeatherReportModel> weatherReports;
    private final OnDeleteClickListener onDeleteClickListener;

    public DatabaseListAdapter(Context context, OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public WeatherReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recycler_list_item, parent, false);
        return new WeatherReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherReportViewHolder holder, int position) {
        // Check if the list of WeatherReportModels is empty
        if (weatherReports != null) {
            WeatherReportModel weatherReport = weatherReports.get(position);
            holder.setData(weatherReport.getId(), weatherReport.getCityName(), weatherReport.getApplicableDate(), position);
            holder.setListeners();
        } else {
            // This is only run if the database is empty
            holder.listItemID.setText(R.string.nothing_in_database);
        }
    }

    @Override
    public int getItemCount() {
        if (weatherReports != null) {
            return weatherReports.size();
        } else return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setWeatherReports(List<WeatherReportModel> weatherReports) {
        this.weatherReports = weatherReports;
        notifyDataSetChanged();

    }

    public class WeatherReportViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout listItem;
        private final TextView listItemID;
        private final TextView listItemCityName;
        private final TextView listItemDate;
        private int position;
        private final ImageView deleteButton;

        public WeatherReportViewHolder(@NonNull View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.ll_itemRow);
            listItemID = itemView.findViewById(R.id.tv_itemID);
            listItemCityName = itemView.findViewById(R.id.tv_itemCityName);
            listItemDate = itemView.findViewById(R.id.tv_itemDate);
            deleteButton = itemView.findViewById(R.id.iv_RowDelete);
        }

        // This method is setting the weather report item into the recyclerView
        public void setData(long id, String cityName, String date, int position) {
            listItemID.setText(String.valueOf(id)); // This is what will be displayed on the recyclerview
            listItemCityName.setText(cityName);
            listItemDate.setText(date);
            this.position = position;
        }

        // Sets onClickListeners
        public void setListeners() {
            // Code here runs whenever an item in the recyclerView is pressed
            listItem.setOnClickListener(view -> {
                Log.i("recyclerView", "Item " + position + " pressed");
                // Opens a new SingleWeatherReportActivity - does not populate fields yet.
                Intent intent = new Intent(context, SingleWeatherReportActivity.class);
                intent.putExtra("report", Parcels.wrap(weatherReports.get(position)));
                context.startActivity(intent);
            });

            // Code for the delete button
            deleteButton.setOnClickListener(view -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.OnDeleteClickListener(weatherReports.get(position));
                }
            });
        }
    }
}
