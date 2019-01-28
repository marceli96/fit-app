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

import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.interfaces.callback.UserConnectionCallback;
import pl.edu.wat.fitapp.R;

public class LoginConnection {
    private UserConnectionCallback callback;

    private String userName, password;
    private User user;

    public LoginConnection(UserConnectionCallback callback, String userName, String password, User user) {
        this.callback = callback;
        this.userName = userName;
        this.password = password;
        this.user = user;
    }

    public void moveWeight() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                login();
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
                params.put("operation", "moveWeight");
                params.put("userName", userName);
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }

    private void login() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.LOGIN_URL), new Response.Listener<String>() {
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
                        callback.onSuccess(user);
                    } else {
                        callback.onFailure(callback.activity().getString(R.string.wrongDataComunicat));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(callback.activity().getString(R.string.connectionError) + e.toString());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure(callback.activity().getString(R.string.connectionError) + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
