package com.noqueue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.noqueue.app.AppConfig;
import com.noqueue.app.AppController;
import com.noqueue.helper.SQLiteHandler;
import com.noqueue.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();

    TextView textView;
    Button registerButton;
    private EditText first_name;
    private EditText last_name;
    private Spinner spinner;
    private EditText email;
    private EditText password;
    private EditText dob;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        spinner = (Spinner)findViewById(R.id.input_sex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        textView = (TextView)findViewById(R.id.link_login);
        first_name = (EditText) findViewById(R.id.input_firstname);
        last_name = (EditText)findViewById(R.id.input_lastname);
        email = (EditText)findViewById(R.id.input_email);
        password = (EditText)findViewById(R.id.input_password);
        dob = (EditText)findViewById(R.id.input_dob);
        registerButton = (Button)findViewById(R.id.btn_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(getApplicationContext());

        sqLiteHandler = new SQLiteHandler(getApplicationContext());

        if (sessionManager.isLoggedIn()){
            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = first_name.getText().toString().trim();
                String lastname = last_name.getText().toString().trim();
                String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();
                String birth = dob.getText().toString().trim();
                String sex = spinner.getSelectedItem().toString();

                if(!firstname.isEmpty() && !lastname.isEmpty() && !user_email.isEmpty() && !user_password.isEmpty()){
                    registerCustomer(firstname, lastname, user_email, user_password, birth, sex);
                }else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
    protected void registerCustomer(final String firstname, final String lastname, final String user_email, final String user_password, final String birth, final String sex){
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.SIGNUP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Resgister Responce " + response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        String uid = jsonObject.getString("uid");
                        JSONObject user = jsonObject.getJSONObject("customers");
                        String firstname = user.getString("first_name");
                        String lastname = user.getString("last_name");
                        String sex = user.getString("sex");
                        String email = user.getString("email");
                        String birth = user.getString("dob");
                        String created_at = user.getString("created_at");
                        //String updated_at = user.getString("updated_at");

                        sqLiteHandler.createUser(firstname, lastname, email, uid);
                        Toast.makeText(getApplicationContext(), "Customer Registeration was successful", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        String errorMsg = jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg.toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: "+error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", firstname);
                params.put("last_name", lastname);
                params.put("sex", sex);
                params.put("email", user_email);
                params.put("password", user_password);
                params.put("dob", birth);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
