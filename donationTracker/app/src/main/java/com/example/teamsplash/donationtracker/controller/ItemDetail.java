package com.example.teamsplash.donationtracker.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.teamsplash.donationtracker.model.Item;
import com.example.teamsplash.donationtracker.model.Location;
import com.example.teamsplash.donationtracker.R;

import java.util.Locale;

/**
 * descriptions and attributes about item objects
 */
//@SuppressWarnings("ALL")
public class ItemDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_activity);
        Item item = (Item) getIntent().getSerializableExtra("ITEM");

        String name = item.getDesc();
        String description = item.getLongDesc();
        String value = "$" + String.format(Locale.US, "%.2f", item.getValue());
        Location location = item.getLocation();
        String time = item.getTime();

        TextView title = findViewById(R.id.name);
        TextView desc = findViewById(R.id.description);
        TextView price = findViewById(R.id.value);
        TextView loc = findViewById(R.id.location);
        TextView timeStamp = findViewById(R.id.time_stamp);

        title.setText(name);
        desc.setText(description);
        price.setText(value);
        loc.setText(location.getName());
        timeStamp.setText(time);

        Button goBackBtn = findViewById(R.id.backBtn);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
