package pl.edu.wat.fitapp.Database.Connection;

import android.view.View;

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
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;
import pl.edu.wat.fitapp.View.Welcome.WelcomeActivity;

public class LoginConnection {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, welcomeActivity.getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, welcomeActivity.getString(R.string.LOGIN_URL),
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
                                ToastUtils.shortToast(welcomeActivity, "Błędne dane");
                                welcomeActivity.getPbLogin().setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.shortToast(welcomeActivity, "Błąd połączenia z bazą! " + e.toString());
                            welcomeActivity.getPbLogin().setVisibility(View.INVISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.shortToast(welcomeActivity, "Błąd połączenia z bazą! " + error.toString());
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
