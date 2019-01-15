package pl.edu.wat.fitapp.Database.Entity.Connection;

import android.view.View;
import android.widget.Toast;

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

import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Welcome.WelcomeActivity;

public class LoginConnection {
    private final String LOGIN_URL = "http://fitappliaction.cba.pl/login.php";
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";
    private WelcomeActivity welcomeActivity;

    private String userName, password;
    private User user;

    public LoginConnection(WelcomeActivity welcomeActivity, String userName, String password, User user) {
        this.welcomeActivity = welcomeActivity;
        this.userName = userName;
        this.password = password;
        this.user = user;
    }

    public void moveWeight() {
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
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "moveWeight");
                params.put("userName", userName);
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(welcomeActivity);
        requestQueue.add(stringRequest);
    }

    private void login() {
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
                                welcomeActivity.setUser(user);
                                welcomeActivity.openMainActivity();
                            } else {
                                Toast.makeText(welcomeActivity, "Błędne dane", Toast.LENGTH_LONG).show();
                                welcomeActivity.getPbLogin().setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(welcomeActivity, "Błąd połączenia z bazą! " + e.toString(), Toast.LENGTH_LONG).show();
                            welcomeActivity.getPbLogin().setVisibility(View.INVISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(welcomeActivity, "Błąd połączenia z bazą! " + error.toString(), Toast.LENGTH_LONG).show();
                        welcomeActivity.getPbLogin().setVisibility(View.INVISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(welcomeActivity);
        requestQueue.add(stringRequest);
    }
}
