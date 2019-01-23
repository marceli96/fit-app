package pl.edu.wat.fitapp.Database.Connection;

import android.widget.Toast;

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

import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Interface.ConnectionCallback;
import pl.edu.wat.fitapp.Interface.UserConnectionCallback;
import pl.edu.wat.fitapp.View.Main.Fragment.SettingsFragment;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class UserSettingsConnection {
    private UserConnectionCallback callback;

    public UserSettingsConnection(UserConnectionCallback callback) {
        this.callback = callback;
    }

    public void changeLogin(final User user, final String newUserName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean available = jsonResponse.getBoolean("available");
                    if (available) {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            user.setUserName(newUserName);
                            callback.onSuccess(user);
                        } else
                            callback.onFailure("Nieoczekiwany błąd");
                    } else
                        callback.onFailure("Nazwa użytkownika jest zajęta");
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Błąd połączenia z bazą  " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("Błąd połączenia z bazą " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "setUsername");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("userName", newUserName);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);

    }

    public void changeEmail(final User user, final String newEmail) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean available = jsonResponse.getBoolean("available");
                    if (available) {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            user.setEmail(newEmail);
                            callback.onSuccess(user);
                        } else
                            callback.onFailure("Nieoczekiwany błąd");
                    } else
                        callback.onFailure("E-mail jest zajęty");
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Błąd połączenia z bazą " + e.toString());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure("Błąd połączenia z bazą " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "setEmail");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("email", newEmail);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }

    public void changePassword(final User user, final String newPassword) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        callback.onSuccess(user);
                    } else
                        callback.onFailure("Nieoczekiwany błąd");
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Błąd połączenia z bazą " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("Błąd połączenia z bazą " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "setPassword");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("password", newPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
