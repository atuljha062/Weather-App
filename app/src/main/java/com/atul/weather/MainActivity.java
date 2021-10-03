package com.atul.weather;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    Button searchButton;
    TextView tempTextView,maxTempTextView,minTempTextView,weatherTitleTextView,apparentTempTextView,windSpeedTextView,humidityTextView,airPressureTExtView;
    String cityName;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        cityEditText = (EditText) findViewById(R.id.editText);
        searchButton = (Button) findViewById(R.id.searchButton);
        tempTextView = (TextView) findViewById(R.id.tempTextView);
        maxTempTextView = (TextView) findViewById(R.id.maxTempTextView);
        minTempTextView = (TextView) findViewById(R.id.minTempTextView);
        weatherTitleTextView = (TextView) findViewById(R.id.weatherTitleTextView);
        apparentTempTextView = (TextView) findViewById(R.id.apparentTemp);
        windSpeedTextView = (TextView) findViewById(R.id.windSpeed);
        humidityTextView = (TextView) findViewById(R.id.humidity);
        airPressureTExtView = (TextView) findViewById(R.id.airPressure);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cityEditText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "City Name cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    cityName = cityEditText.getText().toString();
                    url = "http://api.openweathermap.org/data/2.5/weather?q="+ cityName +"&appid=b8fe91af6b9fae85a3bed05861dd4d47&units=metric";

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray weatherArray = response.getJSONArray("weather");
                                JSONObject weatherObj = weatherArray.getJSONObject(0);
                                weatherTitleTextView.setText(weatherObj.getString("main"));

                                JSONObject tempObj = response.getJSONObject("main");
                                tempTextView.setText(tempObj.getDouble("temp") + "°C");
                                maxTempTextView.setText(tempObj.getDouble("temp_max") + "°C");
                                minTempTextView.setText("/ " + tempObj.getDouble("temp_min") + "°C");
                                apparentTempTextView.setText(tempObj.getDouble("feels_like") + "°C");
                                airPressureTExtView.setText(tempObj.getInt("pressure") + "hPa");
                                humidityTextView.setText(tempObj.getInt("humidity") + "%");

                                JSONObject windObj = response.getJSONObject("wind");
                                windSpeedTextView.setText(windObj.getDouble("speed") + "m/s");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Invalid City Name", Toast.LENGTH_SHORT).show();
                        }
                    });

                    requestQueue.add(jsonObjectRequest);
                }
            }
        });

    }
}