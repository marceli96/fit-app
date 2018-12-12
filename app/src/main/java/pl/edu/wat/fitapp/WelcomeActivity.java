package pl.edu.wat.fitapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {
    private EditText etLogin, etPassword;
    private Button bLogin, bRegister;

    private final String LOGIN_URL = "http://fitappliaction.cba.pl/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        bLogin = findViewById(R.id.bLogin);
        bRegister = findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login() {
        final String userName = etLogin.getText().toString();
        final String password = etPassword.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                openMainActivity();
                                WelcomeActivity.this.finish();
                            } else {
                                Toast.makeText(WelcomeActivity.this, "Błędne dane", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(WelcomeActivity.this, "Login error! " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WelcomeActivity.this, "Login error! " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void openMainActivity() {
        Intent openMainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(openMainActivity);
    }

    private void openRegisterActivity() {
        Intent openRegisterActivity = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(openRegisterActivity);
    }
}
