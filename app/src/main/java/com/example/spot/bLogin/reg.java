package com.example.spot.bLogin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.spot.R;
import com.example.spot.publicClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class reg extends AppCompatActivity {

    private Spinner unis, col;
    private ProgressBar prog_uni, prog_col;

    private RequestQueue requestQueue;

    private EditText username, password, conf_password;

    private RadioButton male, female;
    private String university, college;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reg);

        unis = findViewById(R.id.unis);
        col = findViewById(R.id.col);
        prog_col = findViewById(R.id.prog_col);
        prog_uni = findViewById(R.id.prog_uni);
        username = findViewById(R.id.reg_username);
        password = findViewById(R.id.reg_password);
        conf_password = findViewById(R.id.reg_conf_password);
        male = findViewById(R.id.reg_male);
        female = findViewById(R.id.reg_female);

        requestQueue = Volley.newRequestQueue(this);

        get_universities();
    }

    private void get_universities() {

        prog_uni.setVisibility(View.VISIBLE);

        final String url = new publicClass().base_Url+"bLogin.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog_uni.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);

                    final List<String> list = new ArrayList<>();
                    for (int i = 0; i<array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        list.add(object.getString("name"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(reg.this, R.layout.spinner_item, list);

                    unis.setAdapter(adapter);

                    unis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            university = list.get(position);
                            get_college(list.get(position),url);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }catch (Exception e) {
                    Toast.makeText(reg.this, "Failed to load json", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prog_uni.setVisibility(View.GONE);
                Toast.makeText(reg.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("job","get_uni");
                return params;
            }
        };

        requestQueue.add(request);

    }

    private void get_college(final String uni, String url) {
        prog_col.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                prog_col.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    final List<String> list = new ArrayList<>();
                    for (int i = 0; i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        list.add(object.getString("name"));
                    }

                    ArrayAdapter adapter = new ArrayAdapter(reg.this,
                            R.layout.spinner_item,list);

                    col.setAdapter(adapter);

                    col.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            college = list.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prog_col.setVisibility(View.GONE);
                Toast.makeText(reg.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("job","get_col");
                params.put("uni",uni);
                return params;
            }
        };

        requestQueue.add(request);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Register(View view) {
        if (username.getText().toString().isEmpty()) {
            username.setError("Enter your username here!");
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Enter password here");
        } else if (conf_password.getText().toString().isEmpty()) {
            conf_password.setError("Re-enter your password here");
        } else if (!password.getText().toString().equals(conf_password.getText().toString())) {
            conf_password.setError("Password doesn't match");
        } else if (!male.isChecked() && !female.isChecked()) {
            Toast.makeText(this, "Select your gender", Toast.LENGTH_SHORT).show();
        } else {
            final String Username = username.getText().toString();
            String gender = "Female";
            if (male.isChecked()) {
                gender = "Male";
            }

            if (university.isEmpty() || college.isEmpty()) {
                Toast.makeText(this, "Error: Try again", Toast.LENGTH_SHORT).show();
            } else {

                String url = new publicClass().base_Url+"bLogin.php";

                AlertDialog.Builder builder = new AlertDialog.Builder(reg.this)
                        .setCancelable(false)
                        .setView(R.layout.loading);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();


                final String finalGender = gender;
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        alertDialog.dismiss();
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("response").equals("done")) {
                                Toast.makeText(reg.this, "Done", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(reg.this, "Already", Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e) {

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alertDialog.dismiss();
                        Toast.makeText(reg.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("job","register");
                        params.put("username",Username);
                        params.put("gender", finalGender);
                        params.put("uni",university);
                        params.put("col",college);
                        params.put("pass",password.getText().toString());

                        return params;
                    }
                };

                requestQueue.add(request);

            }

        }
    }


}