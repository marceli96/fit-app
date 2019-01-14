package pl.edu.wat.fitapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {
    private EditText etLogin, etPassword;
    private Button bLogin, bRegister;
    private ProgressBar pbLogin;
    private User user;

    private final String LOGIN_URL = "http://fitappliaction.cba.pl/login.php";
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        bLogin = findViewById(R.id.bLogin);
        bRegister = findViewById(R.id.bRegister);
        pbLogin = findViewById(R.id.pbLogin);

        //DO TESTOWANIA
        etLogin.setText("admin");
        etPassword.setText("admin");
        //

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbLogin.setVisibility(View.VISIBLE);
                login();
            }
        });
    }

    private void moveWeight(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                openMainActivity();
                Toast.makeText(WelcomeActivity.this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();
                WelcomeActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "moveWeight");
                params.put("userId", String.valueOf(userID));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(WelcomeActivity.this);
        requestQueue.add(stringRequest);
    }

    private void login() {
        if(!etLogin.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()){
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
                                    // TODO Zmienić w bazie na CaloricDemAnd
                                    JSONObject jsonObject = jsonResponse.getJSONObject("0");
                                    JSONObject jsonObject1 = jsonResponse.getJSONObject("1");
                                    user = new User(jsonObject.getInt("ID_User"), jsonObject.getString("UserName"),
                                            jsonObject.getString("Email"), jsonObject.getInt("Sex"), jsonObject.getInt("Age"),
                                            jsonObject.getInt("Height"), jsonObject.getInt("ActivityLevel"), jsonObject1.getDouble("UserWeight"),
                                            jsonObject1.getInt("CaloricDemend"), jsonObject1.getInt("Goal"));
                                    moveWeight(user.getUserID());
                                } else {
                                    pbLogin.setVisibility(View.INVISIBLE);
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
        } else {
         if(etLogin.getText().toString().isEmpty())
             Toast.makeText(WelcomeActivity.this, "Wpisz login", Toast.LENGTH_SHORT).show();
         else if (etPassword.getText().toString().isEmpty())
             Toast.makeText(WelcomeActivity.this, "Wpisz hasło", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMainActivity() {
        Intent openMainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
    }

    private void openRegisterActivity() {
        Intent openRegisterActivity = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(openRegisterActivity);
    }
}
