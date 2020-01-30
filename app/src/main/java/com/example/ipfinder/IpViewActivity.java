package com.example.ipfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IpViewActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";

    Button menu;
    TextView tw;
    String customIp;

    TextView address;
    TextView country;
    TextView city;
    TextView zip;
    TextView time;
    TextView isp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_view);

        /* Získání instancí na objekty ve View */
        address = findViewById(R.id.addressView);
        country = findViewById(R.id.countryView);
        city = findViewById(R.id.cityView);
        zip = findViewById(R.id.zipView);
        time = findViewById(R.id.zoneView);
        isp = findViewById(R.id.ispView);
        menu = findViewById(R.id.menuButton);


        Intent intent = getIntent(); // Získání kontextu na aktuální intent
        customIp = intent.getStringExtra(MainActivity.EXTRA_IP_ADDRESS); // Získání předaného EXTRA stringu s adresou k načtení
        getJson(customIp); // Získání informací o adrese a nastavení výsledků do view
    }

    public void openMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void getJson(String ipAddress){
        String url = "http://ip-api.com/json/" + ipAddress;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string); // Pošle získané data do parseru
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(IpViewActivity.this);
        rQueue.add(request);
    }

    void parseJsonData(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);

            /* Vložení informací o adrese do jednotlivých polí */
            address.setText(object.getString("query"));
            country.setText(object.getString("country")+" (" + object.getString("countryCode") + ")");
            city.setText(object.getString("city") + " (" + object.getString("regionName") + ")");
            zip.setText(object.getString("zip"));
            time.setText(object.getString("timezone"));
            isp.setText(object.getString("isp"));
            saveAddress(object.getString("query"), jsonString); // Uloží adresu a všechny její data do JSON souboru
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void saveAddress(String address, String data){

        SharedPreferences sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString(address, data);
        editor.apply();

        Toast.makeText(this, "Address " + address + " saved to history.", Toast.LENGTH_SHORT).show();
    }
}
