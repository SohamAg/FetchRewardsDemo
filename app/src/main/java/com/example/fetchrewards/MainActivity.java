package com.example.fetchrewards;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    //Variables necessary to be used
    private static final String JSON_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
    private RecyclerView recyclerView;
    private ListDisplayAdapter adapter;
    private List<Item> itemList;

    //Declared Recycler View to display data


    //The onCreate method for the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        
        //Recycler View Established
        recyclerView = findViewById(R.id.datalist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //The Itemlist and the adapter
        itemList = new ArrayList<>();
        adapter = new ListDisplayAdapter(itemList);
        recyclerView.setAdapter(adapter);

        //Button clicking function
        Button fetchBtn = findViewById(R.id.fetchBtn);
        fetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });
    }


    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        //JSON request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONArray response) {
                        itemList.clear();

                        Gson gson = new Gson();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Item item = gson.fromJson(response.getJSONObject(i).toString(), Item.class);
                                if (item.getName() != null && !item.getName().isEmpty()) {
                                    itemList.add(item);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        SortHelper();
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void SortHelper() {
        // First sorting via the Listid
        Map<Integer, List<Item>> groups = new HashMap<>();
        for (Item item : itemList) {
            if (!groups.containsKey(item.getListId())) {
                groups.put(item.getListId(), new ArrayList<Item>());
            }
            Objects.requireNonNull(groups.get(item.getListId())).add(item);
        }

        // Sorting all the by the names
        for (List<Item> group : groups.values()) {
            group.sort(new Comparator<Item>() {
                @Override
                public int compare(Item item1, Item item2) {
                    return item1.getName().compareToIgnoreCase(item2.getName());
                }
            });
        }

        // Clearing it and adding
        itemList.clear();
        for (List<Item> group : groups.values()) {
            itemList.addAll(group);
        }
    }
}