package com.example.teamsplash.donationtracker.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.example.teamsplash.donationtracker.R;
import com.example.teamsplash.donationtracker.model.Item;
import com.example.teamsplash.donationtracker.model.ItemType;
import com.example.teamsplash.donationtracker.model.Items;
import com.example.teamsplash.donationtracker.model.Location;
import com.example.teamsplash.donationtracker.model.Locations;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * this is the way to add items into inventory
 */
//@SuppressWarnings("ALL")
public class InventoryActivity extends AppCompatActivity implements Observer {
    private List<Location> locations;
    private Spinner locationSpinner;
    private ItemListAdapter adapter;

    private void update(Locations locationsInstance) {
        locations = locationsInstance.getLocations();
        final List<String> locationsList = new ArrayList<>();
        locationsList.add("Search by name");
        locationsList.addAll(locations.stream().map(new Function<Location, String>() {
            @Override
            public String apply(Location location) {
                return location.getName();
            }
        }).collect(new Supplier<List<String>>() {
            @Override
            public List<String> get() {
                return new ArrayList<>();
            }
        }, new BiConsumer<List<String>, String>() {
            @Override
            public void accept(List<String> strings, String s) {
                strings.add(s);
            }
        }, new BiConsumer<List<String>, List<String>>() {
            @Override
            public void accept(List<String> strings, List<String> strings2) {
                strings.addAll(strings2);
            }
        }));
        ArrayAdapter<? extends String> spinneradapter = new ArrayAdapter<>(this,
                                                                           android.R.layout.simple_spinner_item, locationsList);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(spinneradapter);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof Locations))
            return;

        update((Locations) o);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_activity);
        final ListView list = findViewById(R.id.itemsList);
        locationSpinner = findViewById(R.id.locationSpinner);
        final Spinner categorySpinner = findViewById(R.id.categorySpinner);
        EditText searchBar = findViewById(R.id.searchFilter);
        final List<Item> itemList = Items.getInstance().get();
        final Locations locationsInstance = Locations.getInstance();
        locationsInstance.addObserver(this);
        update(locationsInstance);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * @param parentView an adapterview item
             * @param selectedItemView what was selected
             * @param position where it's locted
             * @param id the id of the inventory item
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                if (position == 0) {
                    adapter = new ItemListAdapter(InventoryActivity.this, itemList);
                    list.setAdapter(adapter);
                } else {
                    final List<Item> locItemsList = Items.getInstance().getByLocation(
                            locations.get(position - 1));
                    adapter = new ItemListAdapter(InventoryActivity.this, locItemsList);
                    list.setAdapter(adapter);
                }
            }

            /**
             * @param parentView the adapterview item
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                adapter = new ItemListAdapter(InventoryActivity.this, itemList);
                list.setAdapter(adapter);
            }
        });

        ArrayAdapter<Enum<ItemType>> spinneradapter2 = new ArrayAdapter<Enum<ItemType>>(this,
                android.R.layout.simple_spinner_item, ItemType.values());
        spinneradapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinneradapter2);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            /*
              @param parentView an adapterview item
             * @param selectedItemView what was selected
             * @param position where it's locted
             * @param id the id of the inventory item
             */
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                if (position == 0) {
                    adapter = new ItemListAdapter(InventoryActivity.this, itemList);
                    list.setAdapter(adapter);
                } else {
                    final List<Item> catItemsList = Items.getInstance().getByCategory(
                            (ItemType) categorySpinner.getSelectedItem());
                    adapter = new ItemListAdapter(InventoryActivity.this, catItemsList);
                    list.setAdapter(adapter);
                }
            }

            /**
             * @param parentView parent view item
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                adapter = new ItemListAdapter(InventoryActivity.this, itemList);
                list.setAdapter(adapter);
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            /**
             * @param charSequence the sequence for search
             * @param i  first one
             * @param i1 second one
             * @param i2 third one
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * @param cs the character sequence
             * @param i  first one
             * @param i1 second one
             * @param i2 third one
             */
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                int textlength = cs.length();
                List<Item> tempArrayList = new ArrayList<>();
                for(Item c: itemList){
                    if (textlength <= c.getDesc().length()) {
                        if (c.getDesc().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                    if (textlength <= c.getLocation().toString().length()) {
                        String csToLowerCase = cs.toString().toLowerCase();
                        if (c.getLocation().toString().toLowerCase().contains(csToLowerCase)) {
                            tempArrayList.add(c);
                        }
                    }
                }
                if (tempArrayList.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "No items match your search",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

                adapter = new ItemListAdapter(InventoryActivity.this, tempArrayList);
                list.setAdapter(adapter);
            }

            /**
             * @param editable what it is after search
             */
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Item itemClicked = itemList.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("ITEM", itemClicked);
                Intent listDetails = new Intent(InventoryActivity.this,
                        ItemDetail.class);
                listDetails.putExtras(bundle);
                startActivity(listDetails);
            }
        });
    }
}