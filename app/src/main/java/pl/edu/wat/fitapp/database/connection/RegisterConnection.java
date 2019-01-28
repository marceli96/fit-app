package pl.edu.wat.fitapp.database.connection;

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

import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.R;

public class RegisterConnection {
    private ConnectionCallback callback;
    private String userName, password, email;
    private int sex, age, height, activityLevel, goal, calories;
    private double weight;

    public RegisterConnection(ConnectionCallback callback, String userName, String password, String email, int sex, int age, int height, int activityLevel, int goal, int calories, double weight) {
        this.callback = callback;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.activityLevel = activityLevel;
        this.goal = goal;
        this.calories = calories;
        this.weight = weight;
    }

    public void register() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.REGISTER_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean availableUserName = jsonResponse.getBoolean("availableUserName");
                    boolean availableEmail = jsonResponse.getBoolean("availableEmail");

                    if (availableUserName && availableEmail) {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(callback.activity().getString(R.string.registerError));
                        }
                    } else if (!availableUserName && availableEmail)
                        callback.onFailure(callback.activity().getString(R.string.usernameError));
                    else if (!availableEmail)
                        callback.onFailure(callback.activity().getString(R.string.emailError));
                    else
                        callback.onFailure(callback.activity().getString(R.string.connectionError));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(callback.activity().getString(R.string.connectionError) + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(callback.activity().getString(R.string.connectionError) + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(callback.activity().getString(R.string.formatDate));
                params.put("userName", userName);
                params.put("password", password);
                params.put("email", email);
                params.put("sex", String.valueOf(sex));
                params.put("age", String.valueOf(age));
                params.put("height", String.valueOf(height));
                params.put("activityLevel", String.valueOf(activityLevel));
                params.put("caloricDemand", String.valueOf(calories));
                params.put("weight", String.valueOf(weight));
                params.put("goal", String.valueOf(goal));
                params.put("date", dateFormat.format(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}