package com.example.teamsplash.donationtracker.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teamsplash.donationtracker.R;
import com.example.teamsplash.donationtracker.model.Item;
import com.example.teamsplash.donationtracker.model.Items;
import com.example.teamsplash.donationtracker.model.Location;
import com.example.teamsplash.donationtracker.model.Locations;

import java.util.List;

public class ItemFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.item_list, container, false);

        Location location = (Location) getArguments().getSerializable("LOCATION");

        final List<Item> ItemsList = Items.getInstance().getLocItems(location);

        ItemsList listAdapter = new ItemsList(inflater, ItemsList);
        final ListView list = fragment.findViewById(R.id.item_list);
        list.setAdapter(listAdapter);
        setListViewHeightBasedOnItems(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Item itemClicked = ItemsList.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("ITEM", itemClicked);
                Intent listDetails = new Intent(ItemFragment.this.getActivity(), ItemDetail.class);
                listDetails.putExtras(bundle);

                startActivity(listDetails);
            }
        });
        return fragment;
    }

    private class ItemsList extends ArrayAdapter<Item> {
        private final LayoutInflater inflater;
        private final List<Item> items;

        public ItemsList(LayoutInflater inflater, List<Item> donationItems) {
            super(inflater.getContext(), R.layout.item_fragment, donationItems);
            this.inflater = inflater;
            this.items = donationItems;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Item item = items.get(position);
            View rowView= inflater.inflate(R.layout.item_fragment, null, true);
            TextView name = rowView.findViewById(R.id.name);
            TextView price = rowView.findViewById(R.id.value);
            name.setText(item.getDesc());
            price.setText(Double.toString(item.getValue()));
            return rowView;
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }
            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        } else {
            return false;
        }
    }
}
