package pl.edu.wat.fitapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    private HomeFragment homeFragment;
    private EditText etLogin, etEmail, etPassword1, etPassword2;
    private Button bChangeLogin, bChangeEmail, bChangePassword;

    private User user;
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = getLayoutInflater().inflate(R.layout.fragment_settings, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        etLogin = view.findViewById(R.id.etLogin);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword1 = view.findViewById(R.id.etPassword1);
        etPassword2 = view.findViewById(R.id.etPassword2);

        bChangeLogin = view.findViewById(R.id.bChangeLogin);
        bChangeEmail = view.findViewById(R.id.bChangeEmail);
        bChangePassword= view.findViewById(R.id.bChangePassword);

        homeFragment = new HomeFragment();

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
        if (!etLogin.getText().toString().isEmpty() && etLogin.getText().toString().length() > 5)
        {
            final String userName = etLogin.getText().toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    openHomeActivty();
                                } else {
                                    boolean userError = jsonResponse.getBoolean("userError");
                                    if (userError)
                                        Toast.makeText(getActivity(), "Nazwa użytkownika jest zajęta", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(getActivity(), "Nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                                }
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
            else if(etLogin.getText().toString().length() <= 5)
                Toast.makeText(getActivity(), "Login musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
        }
    }


    public void changeEmail() {
        if (!etEmail.getText().toString().isEmpty())
        {
            final String email = etEmail.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    openHomeActivty();
                                } else {
                                    boolean emailError = jsonResponse.getBoolean("emailError");
                                    if (emailError)
                                        Toast.makeText(getActivity(), "E-mail jest zajęty", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(getActivity(), "Nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                                }
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
                && etPassword1.getText().toString().equals(etPassword2.getText().toString()))
        {
            final String password = etPassword1.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    openHomeActivty();
                                }
                                else {
                                    Toast.makeText(getActivity(), "Nieoczekiwany błąd", Toast.LENGTH_LONG).show();
                                }
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
                    params.put("operation", "setPassword");
                    params.put("password", password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);

        }
        else
            {
            if (etPassword1.getText().toString().isEmpty())
                Toast.makeText(getActivity(), "Wprowadź hasło", Toast.LENGTH_SHORT).show();
            else if(!etPassword1.getText().toString().equals(etPassword2.getText().toString()))
                Toast.makeText(getActivity(), "Hasła się różnią", Toast.LENGTH_SHORT).show();
            else if(etPassword1.getText().toString().length() <= 5)
                Toast.makeText(getActivity(), "Hasło musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
        }
    }

    public void openHomeActivty()
    {
        Intent openHomeScreen = new Intent(getActivity(), MainActivity.class);
        openHomeScreen.putExtra("user", user);
        startActivity(openHomeScreen);
    }


}
