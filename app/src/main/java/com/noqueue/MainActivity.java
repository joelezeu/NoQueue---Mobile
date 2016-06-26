package com.noqueue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.noqueue.app.AppConfig;
import com.noqueue.app.AppController;
import com.noqueue.logic.Business;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        listView = (ListView)findViewById(R.id.LocationlistView);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.BUSINESSES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                ArrayList<Business> jsonObject = new JsonConverter<Business>().toArrayList(response, Business.class);
                BindDictionary<Business> businessBindDictionary = new BindDictionary<>();
                businessBindDictionary.addStringField(R.id.name, new StringExtractor<Business>() {
                    @Override
                    public String getStringValue(Business item, int position) {
                        return item.busnx_name;
                    }
                });
                businessBindDictionary.addDynamicImageField(R.id.feedImage1, new StringExtractor<Business>() {
                    @Override
                    public String getStringValue(Business item, int position) {
                        return item.imageUrl;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String url, ImageView view) {
                        Picasso.with(getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").into(view);
                    }
                });
                FunDapter<Business> adapter = new FunDapter<>(getApplicationContext(), jsonObject, R.layout.business_feeds, businessBindDictionary);
                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No Network Error", Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    switch (id) {
                        case R.id.home:
                            Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.settings:
                            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case R.id.trash:
                            Toast.makeText(getApplicationContext(), "Trash", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                            break;
                        case R.id.logout:
                            finish();
                    }
                    return true;
                }
            });
        }
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
        tv_email.setText("Joel Eze");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}
