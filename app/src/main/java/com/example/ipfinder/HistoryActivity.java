package com.example.ipfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String EXTRA_IP_ADDRESS = "com.example.ipfinder.EXTRA_IP_ADDRESS";

    private RecyclerView _recyclerView;
    private IpAdapter _adapter;
    private RecyclerView.LayoutManager _layoutManager;

    private ArrayList<IpItem> ipList;

    private Button button;
    private TextView tw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        button = findViewById(R.id.button);
        tw = findViewById(R.id.textView);

        ipList = new ArrayList<>();
        load();

        buildRecyclerView();
    }

    public void customViewOpen(String address){
        Intent intent = new Intent(this, IpViewActivity.class);
        intent.putExtra(EXTRA_IP_ADDRESS, address);
        startActivity(intent);
    }

    public void openMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clearHistory(View v){
        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        sharedPrefs.edit().clear().commit(); // Vymaže uložené data z perzistentní paměti

        ipList.clear(); // Vyčistí list objektů
        buildRecyclerView(); // Načte čistou historii
    }

    public void buildRecyclerView(){
        _recyclerView = findViewById(R.id.recyclerView);
        _recyclerView.setHasFixedSize(true);
        _layoutManager = new LinearLayoutManager(this);
        _adapter = new IpAdapter(ipList);

        _recyclerView.setLayoutManager(_layoutManager);
        _recyclerView.setAdapter(_adapter);

        _adapter.setOnItemClickListener(new IpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                customViewOpen(ipList.get(position).getText1());
            }

            @Override
            public void onDeleteClick(int position) {

                SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                sharedPrefs.edit().remove(ipList.get(position).getText1()).commit(); // Vymaže z sharedprefs podle nadpisu okna v historii

                ipList.remove(position);
                buildRecyclerView(); // Aktualizuje recyclerview
            }
        });
    }

    public void load(){
        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        try {
            Map<String, ?> allEntries = sharedPrefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                JSONObject object = new JSONObject(entry.getValue().toString()); // Vytvoří JSON objekt z uložené hodnoty ve vstupu

                ipList.add(new IpItem(R.drawable.ic_finder,object.getString("query"), object.getString("country")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
