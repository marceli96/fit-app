package pl.edu.wat.fitapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


public class SettingsFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private EditText etLogin, etEmail, etPassword1, etPassword2;
    private Button bChangeLogin, bChangeEmail, bChangePassword;
    private TextView tvLogin, tvEmail;

    private User user;


    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Ustawienia");

        View view = getLayoutInflater().inflate(R.layout.fragment_settings, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        tvLogin = view.findViewById(R.id.tvLogin);
        tvEmail = view.findViewById(R.id.tvEmail);

        tvLogin.setText(user.getUserName());
        tvEmail.setText(user.getEmail());

        etLogin = view.findViewById(R.id.etLogin);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword1 = view.findViewById(R.id.etPassword1);
        etPassword2 = view.findViewById(R.id.etPassword2);

        bChangeLogin = view.findViewById(R.id.bChangeLogin);
        bChangeEmail = view.findViewById(R.id.bChangeEmail);
        bChangePassword = view.findViewById(R.id.bChangePassword);

        bChangeLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeLogin();
            }

        });

        bChangeEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeEmail();
            }

        });

        bChangePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changePassword();
            }

        });

        return view;
    }


    public void changeLogin() {
        if (!etLogin.getText().toString().isEmpty() && etLogin.getText().toString().length() > 5) {
            final String userName = etLogin.getText().toString();

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
                                        user.setUserName(userName);
                                        Toast.makeText(getActivity(), "Login zmieniony pomyślnie", Toast.LENGTH_LONG).show();
                                        openHomeActivity();
                                    } else
                                        Toast.makeText(getActivity(), "Nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(getActivity(), "Nazwa użytkownika jest zajęta", Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Settings error! " + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Settings error! " + error.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("operation", "setUsername");
                    params.put("userId", String.valueOf(user.getUserID()));
                    params.put("userName", userName);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        } else {
            if (etLogin.getText().toString().isEmpty())
                Toast.makeText(getActivity(), "Wprowadź login", Toast.LENGTH_SHORT).show();
            else if (etLogin.getText().toString().length() <= 5)
                Toast.makeText(getActivity(), "Login musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeEmail() {
        if (!etEmail.getText().toString().isEmpty()) {
            final String email = etEmail.getText().toString();

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
                                        user.setEmail(email);
                                        Toast.makeText(getActivity(), "E-mail zmieniony pomyślnie", Toast.LENGTH_LONG).show();
                                        openHomeActivity();
                                    } else
                                        Toast.makeText(getActivity(), "Nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(getActivity(), "E-mail jest zajęty", Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Settings error! " + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Settings error! " + error.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("operation", "setEmail");
                    params.put("userId", String.valueOf(user.getUserID()));
                    params.put("email", email);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(getActivity(), "Wprowadź e-mail", Toast.LENGTH_SHORT).show();
        }
    }

    public void changePassword() {
        if (!etPassword1.getText().toString().isEmpty() && etPassword1.getText().toString().length() > 5
                && etPassword1.getText().toString().equals(etPassword2.getText().toString())) {
            final String password = etPassword1.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getActivity(), "Hasło zmienione pomyślnie", Toast.LENGTH_LONG).show();
                                    openHomeActivity();
                                } else
                                    Toast.makeText(getActivity(), "Nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Settings error! " + e.toString(), Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Settings error! " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("operation", "setPassword");
                    params.put("password", password);
                    params.put("userId", String.valueOf(user.getUserID()));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        } else {
            if (etPassword1.getText().toString().isEmpty())
                Toast.makeText(getActivity(), "Wprowadź hasło", Toast.LENGTH_SHORT).show();
            else if (!etPassword1.getText().toString().equals(etPassword2.getText().toString()))
                Toast.makeText(getActivity(), "Hasła się różnią", Toast.LENGTH_SHORT).show();
            else if (etPassword1.getText().toString().length() <= 5)
                Toast.makeText(getActivity(), "Hasło musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
        }
    }

    public void openHomeActivity() {
        Intent openHomeScreen = new Intent(getActivity(), MainActivity.class);
        openHomeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openHomeScreen.putExtra("user", user);
        startActivity(openHomeScreen);
    }
}
