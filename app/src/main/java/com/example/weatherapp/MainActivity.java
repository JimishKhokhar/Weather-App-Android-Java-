package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.Formatter;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout MainLayout;
    TextView tv,City;
    EditText et;
    TextView MinTemp,AvgTemp,Maxtemp,Pressure,Humidity,Visibility;
    LinearLayout Parameters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parameters=findViewById(R.id.WeatherParameters);
        City=findViewById(R.id.TVCity);
        MainLayout=findViewById(R.id.Main_Layout);
        et=findViewById(R.id.et);
        tv=findViewById(R.id.tv);
        MinTemp=findViewById(R.id.TVMinTemp);
        Maxtemp=findViewById(R.id.TVMaxTemp);
        AvgTemp=findViewById(R.id.TVAvgTemp);
        Pressure=findViewById(R.id.TVPressure);
        Humidity=findViewById(R.id.TVHumidity);
        Visibility=findViewById(R.id.TVVisibility);

        City.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.INVISIBLE);
        Parameters.setVisibility(View.INVISIBLE);


    }
    public void get(View v)
    {
        String apiKey="9c2e8ef3d8ca2de16067a51006412142";
        String city=et.getText().toString();
        String url="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey;
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    hideKeybaord(v);

                    City.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    Parameters.setVisibility(View.VISIBLE);

                    JSONObject object=response.getJSONObject("main");
                    String Temp= object.getString("temp");
                    double temperatureInCelcius=Double.parseDouble(Temp)-273.15;
                    Formatter f1=new Formatter();
                    tv.setText(f1.format("%.2f",temperatureInCelcius)+" °C");

                    City.setText(response.getString("name"));


                    if(temperatureInCelcius>=40)
                    {
                        MainLayout.setBackgroundResource(R.drawable.above_40);
                    }
                    else if(temperatureInCelcius>=30)
                    {
                        MainLayout.setBackgroundResource(R.drawable.temp_30_40);
                    }
                    else if(temperatureInCelcius>=20)
                    {
                        MainLayout.setBackgroundResource(R.drawable.temp_20_30);
                    }
                    else if(temperatureInCelcius>=10)
                    {
                        MainLayout.setBackgroundResource(R.drawable.temp_10_20);
                    }
                    else if(temperatureInCelcius>=0)
                    {
                        MainLayout.setBackgroundResource(R.drawable.temp_0_10);
                    }
                    else if(temperatureInCelcius>=-10)
                    {
                        MainLayout.setBackgroundResource(R.drawable.temp_n10_0);
                    }
                    else if(temperatureInCelcius>=-20)
                    {
                        MainLayout.setBackgroundResource(R.drawable.temp_n20_n10);
                    }
                    else
                    {
                        MainLayout.setBackgroundResource(R.drawable.below_n20);
                    }


                    f1=new Formatter();
                    double MinTempValue=Double.parseDouble(object.getString("temp_min"))-273.15;
                    MinTemp.setText(f1.format("%.2f",MinTempValue)+" °C");

                    f1=new Formatter();
                    double MaxTempValue=Double.parseDouble(object.getString("temp_max"))-273.15;
                    Maxtemp.setText(f1.format("%.2f",MaxTempValue)+" °C");

                    f1=new Formatter();
                    double AvgTempValue=Double.parseDouble(object.getString("temp"))-273.15;
                    AvgTemp.setText(f1.format("%.2f",AvgTempValue)+" °C");

                    f1=new Formatter();
                    double PressureValue=Double.parseDouble(object.getString("pressure"));
                    Pressure.setText(f1.format("%.2f",PressureValue)+" Pa");

                    f1=new Formatter();
                    double HumidityValue=Double.parseDouble(object.getString("humidity"));
                    Humidity.setText(f1.format("%.2f",HumidityValue)+" g.m-3");

                    f1=new Formatter();
//                    JSONObject ob=response.getJSONObject("visibility");
//                    response.getString("visibility");
                    int VisibilityValue=Integer.parseInt(response.getString("visibility"));
                    Visibility.setText(VisibilityValue+" metres");


                }catch (JSONException e) {
                    //Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No Place Found! or Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}