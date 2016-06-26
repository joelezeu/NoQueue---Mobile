package com.noqueue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.noqueue.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    AppCompatSpinner locationSpinner;
    private JSONArray result;
    private ArrayList<String> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        students = new ArrayList<String>();

        locationSpinner = (AppCompatSpinner)findViewById(R.id.LocationSpinner);
        getData();
    }
    private void getData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(AppConfig.LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("locations");
                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Spinner Error", Toast.LENGTH_LONG).show();
                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray result) {
        for(int i=0;i<result.length();i++){
            try {
                //Getting json object
                JSONObject json = result.getJSONObject(i);
                //Adding the name of the student to array list
                students.add(json.getString("location"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        locationSpinner.setAdapter(new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_dropdown_item, students));
    }

}