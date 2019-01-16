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
import pl.edu.wat.fitapp.Main.Fragment.SettingsFragment;

public class UserSettingsConnection {
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";
    private SettingsFragment settingsFragment;

    public UserSettingsConnection(SettingsFragment settingsFragment) {
        this.settingsFragment = settingsFragment;
    }

    public void changeLogin(final User user, final String newUserName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean available = jsonResponse.getBoolean("available");
                            if (available) {
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    user.setUserName(newUserName);
                                    settingsFragment.setUser(user);
                                    Toast.makeText(settingsFragment.getActivity(), "Login zmieniony pomyślnie", Toast.LENGTH_LONG).show();
                                    settingsFragment.openHomeActivity();
                                } else
                                    Toast.makeText(settingsFragment.getActivity(), "Nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(settingsFragment.getActivity(), "Nazwa użytkownika jest zajęta", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(settingsFragment.getActivity(), "Settings error! " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(settingsFragment.getActivity(), "Settings error! " + error.toString(), Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(settingsFragment.getActivity());
        requestQueue.add(stringRequest);

    }

    public void changeEmail(final User user, final String newEmail) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean available = jsonResponse.getBoolean("available");
                                if (available) {
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        user.setEmail(newEmail);
                                        settingsFragment.setUser(user);
                                        Toast.makeText(settingsFragment.getActivity(), "E-mail zmieniony pomyślnie", Toast.LENGTH_LONG).show();
                                        settingsFragment.openHomeActivity();
                                    } else
                                        Toast.makeText(settingsFragment.getActivity(), "Nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(settingsFragment.getActivity(), "E-mail jest zajęty", Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(settingsFragment.getActivity(), "Settings error! " + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(settingsFragment.getActivity(), "Settings error! " + error.toString(), Toast.LENGTH_LONG).show();
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

            RequestQueue requestQueue = Volley.newRequestQueue(settingsFragment.getActivity());
            requestQueue.add(stringRequest);
    }

    public void changePassword(final User user, final String newPassword) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(settingsFragment.getActivity(), "Hasło zmienione pomyślnie", Toast.LENGTH_LONG).show();
                                    settingsFragment.openHomeActivity();
                                } else
                                    Toast.makeText(settingsFragment.getActivity(), "Nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(settingsFragment.getActivity(), "Settings error! " + e.toString(), Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(settingsFragment.getActivity(), "Settings error! " + error.toString(), Toast.LENGTH_LONG).show();
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

            RequestQueue requestQueue = Volley.newRequestQueue(settingsFragment.getActivity());
            requestQueue.add(stringRequest);
    }
}
