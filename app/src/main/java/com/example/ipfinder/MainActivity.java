package com.example.ipfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    // Identifikátor pro komunikaci napříč aktivit -> pro uživatelem zadanou IP adresu
    public static final String EXTRA_IP_ADDRESS = "com.example.ipfinder.EXTRA_IP_ADDRESS";

    private Button button;
    private Button parse;
    private TextView tw;
    private EditText customIp;


    private ArrayList<String> randomList;
    private int randomCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        tw = findViewById(R.id.textView);
        customIp = findViewById(R.id.customIp);


        randomList = new ArrayList<String>();
        randomList.add("161.185.160.93");
        randomList.add("21.221.136.160");
        randomList.add("157.41.219.211");
        randomList.add("110.93.83.147");
        randomList.add("94.25.171.103");
        randomList.add("218.68.0.42");
    }

    public void randomExample(View v){
        if (randomCounter <= 5){
            customIp.setText(randomList.get(randomCounter));
        }else{
            randomCounter = 0;
            customIp.setText(randomList.get(randomCounter));
        }
        randomCounter++;
    }

    public void historyViewOpen(View v){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void customViewOpen(View v){
        Intent intent = new Intent(this, IpViewActivity.class);
        intent.putExtra(EXTRA_IP_ADDRESS, customIp.getText().toString());
        startActivity(intent);
    }

    public void currentViewOpen(View v){
        // Získáme z další API aktuální ip adresu zařízení
        StringRequest request = new StringRequest("https://api.ipify.org/?format=json", new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseCurrentAddress(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);
    }

    void parseCurrentAddress(String string){
        try {
            JSONObject currentAddress = new JSONObject(string);


            Intent intent = new Intent(this, IpViewActivity.class);
            intent.putExtra(EXTRA_IP_ADDRESS, currentAddress.getString("ip")); // Získá jedinou hodnotu, která se nachází v JSON objektu (ip adresu)
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
