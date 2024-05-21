package bsu.bsit3d.weathermap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class fragWeather extends Fragment {

    EditText etCity, etCountry;
    TextView intro, humid, descrip, cloud, celsius;
    ImageView iconimg, tempIcon, humidIcon, cloudIcon;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragweather, container, false);

        etCountry = view.findViewById(R.id.etCountry);
        etCity = view.findViewById(R.id.etCity);
        celsius = view.findViewById(R.id.temp);
        intro = view.findViewById(R.id.intro);
        humid = view.findViewById(R.id.humidity);
        descrip = view.findViewById(R.id.description);
        cloud = view.findViewById(R.id.cloudiness);
        iconimg = view.findViewById(R.id.weathericon);
        tempIcon = view.findViewById(R.id.tempIcon);
        humidIcon = view.findViewById(R.id.humidIcon);
        cloudIcon = view.findViewById(R.id.cloudIcon);

        Button btnGet = view.findViewById(R.id.btnGet);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherDetails();
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void getWeatherDetails() {
        String tempUrl;
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if(city.isEmpty()){
            descrip.setText("City field cannot be empty!");
        } else {
            String url = "https://api.openweathermap.org/data/2.5/weather";
            String appid = "e53301e27efa0b66d05045d91b2742d3";
            if(!country.isEmpty()){
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
            } else {
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, response -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");
                    int icon = jsonObjectWeather.getInt("id");
                    if (icon >= 200 && icon <= 232) {
                        iconimg.setImageResource(R.drawable.d11); // Thunderstorm
                    } else if ((icon >= 300 && icon <= 321) || (icon >= 520 && icon <= 531)) {
                        iconimg.setImageResource(R.drawable.d09); // Drizzle / Rain
                    } else if (icon >= 500 && icon <= 504) {
                        iconimg.setImageResource(R.drawable.d10); // Rain / Freezing Rain
                    } else if ((icon >= 600 && icon <= 622) || icon == 511) {
                        iconimg.setImageResource(R.drawable.d13); // Snow
                    } else if (icon >= 701 && icon <= 781) {
                        iconimg.setImageResource(R.drawable.d50); // Mist / Fog / Haze
                    } else if (icon == 800) {
                        iconimg.setImageResource(R.drawable.d01); // Clear Sky
                    } else if (icon == 801) {
                        iconimg.setImageResource(R.drawable.d02); // Few Clouds
                    } else if (icon == 802) {
                        iconimg.setImageResource(R.drawable.d03); // Scattered Clouds
                    } else if (icon == 803 || icon == 804) {
                        iconimg.setImageResource(R.drawable.d04); // Broken / Overcast Clouds
                    }
                    String output = "Current weather of " + cityName + " (" + countryName + ")";
                    intro.setText(output);
                    celsius.setText(df.format(temp) + " °C");
                    descrip.setText(description);
                    humid.setText(humidity + "%");
                    cloud.setText(clouds + "%");
                    tempIcon.setImageResource(R.drawable.temperature);
                    humidIcon.setImageResource(R.drawable.humid);
                    cloudIcon.setImageResource(R.drawable.cloudiness);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(getContext(), error.toString().trim(), Toast.LENGTH_SHORT).show());
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }
}