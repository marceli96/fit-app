package pl.edu.wat.fitapp.Welcome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import pl.edu.wat.fitapp.Database.User;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Register.RegisterActivity;

public class WelcomeActivity extends AppCompatActivity {

    private final String LOGIN_URL = "http://fitappliaction.cba.pl/login.php";
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private EditText etLogin, etPassword;
    private Button bLogin, bRegister;
    private ProgressBar pbLogin;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        bLogin = findViewById(R.id.bLogin);
        bRegister = findViewById(R.id.bRegister);
        pbLogin = findViewById(R.id.pbLogin);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etLogin.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    pbLogin.setVisibility(View.VISIBLE);
                    moveWeight();
                } else {
                    pbLogin.setVisibility(View.INVISIBLE);
                    if (etLogin.getText().toString().isEmpty())
                        Toast.makeText(WelcomeActivity.this, "Wpisz login", Toast.LENGTH_SHORT).show();
                    else if (etPassword.getText().toString().isEmpty())
                        Toast.makeText(WelcomeActivity.this, "Wpisz hasło", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void moveWeight() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                login();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "moveWeight");
                params.put("userName", etLogin.getText().toString());
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(WelcomeActivity.this);
        requestQueue.add(stringRequest);
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
                            if (success) {
                                JSONObject jsonObject = jsonResponse.getJSONObject("0");
                                JSONObject jsonObject1 = jsonResponse.getJSONObject("1");
                                user = new User(jsonObject.getInt("ID_User"), jsonObject.getString("UserName"),
                                        jsonObject.getString("Email"), jsonObject.getInt("Sex"), jsonObject.getInt("Age"),
                                        jsonObject.getInt("Height"), jsonObject.getInt("ActivityLevel"), jsonObject1.getDouble("UserWeight"),
                                        jsonObject1.getInt("CaloricDemend"), jsonObject1.getInt("Goal"));
                                openMainActivity();
                                Toast.makeText(WelcomeActivity.this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();
                                WelcomeActivity.this.finish();
                            } else {
                                pbLogin.setVisibility(View.INVISIBLE);
                                Toast.makeText(WelcomeActivity.this, "Błędne dane", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            pbLogin.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                            Toast.makeText(WelcomeActivity.this, "Błąd połączenia z bazą! " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbLogin.setVisibility(View.INVISIBLE);
                        Toast.makeText(WelcomeActivity.this, "Błąd połączenia z bazą! " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
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
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
    }

    private void openRegisterActivity() {
        Intent openRegisterActivity = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(openRegisterActivity);
    }
}
