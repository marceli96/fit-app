package pl.edu.wat.fitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText etLogin, etPassword, etEmail, etAge, etWeight, etHeight;
    private RadioGroup rgSex, rgWeight;
    private TextView tvCalories;
    private Spinner spinner;
    private Button bRegister, bCalculate;

    private String activityLevel;
    private int calories;
    private final String REGISTER_URL = "http://fitappliaction.cba.pl/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        tvCalories = findViewById(R.id.tvCalories);
        bRegister = findViewById(R.id.bRegister);
        bCalculate = findViewById(R.id.bCalculate);
        rgSex = findViewById(R.id.rgSex);
        rgWeight = findViewById(R.id.rgWeight);

        // DO TESTOWANIA
        etLogin.setText("test");
        etPassword.setText("test");
        etEmail.setText("test@test.pl");
        etWeight.setText("80.0");
        etHeight.setText("190");
        etAge.setText("22");
        //

        spinner = findViewById(R.id.spinnerActivityLevel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activityLevel, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        bCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        activityLevel = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void calculateCalories() {
        if(!etAge.getText().toString().isEmpty() && !etWeight.getText().toString().isEmpty() && !etHeight.getText().toString().isEmpty()){
            String sex = getRadioButtonText(rgSex);
            String goal = getRadioButtonText(rgWeight);
            int age = Integer.parseInt(etAge.getText().toString());
            double weight = Double.parseDouble(etWeight.getText().toString());
            int height = Integer.parseInt(etHeight.getText().toString());

            if (sex.equals("Kobieta")) {
                calories = (int) Math.round(655 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
                if (activityLevel.equals("Brak"))
                    calories *= 1.2;
                else if (activityLevel.equals("Niska"))
                    calories *= 1.3;
                else if (activityLevel.equals("Średnia"))
                    calories *= 1.5;
                else if (activityLevel.equals("Wysoka"))
                    calories *= 1.7;
                else if (activityLevel.equals("Bardzo wysoka"))
                    calories *= 1.9;

                if (goal.equals("Utrata"))
                    calories -= 250;
                else if (goal.equals("Przybranie"))
                    calories += 250;

                tvCalories.setText(String.valueOf(calories));
            } else {
                calories = (int) Math.round(66 + (13.7 * weight) + (5 * height) - (6.76 * age));
                if (activityLevel.equals("Brak"))
                    calories *= 1.2;
                else if (activityLevel.equals("Niska"))
                    calories *= 1.3;
                else if (activityLevel.equals("Średnia"))
                    calories *= 1.5;
                else if (activityLevel.equals("Wysoka"))
                    calories *= 1.7;
                else if (activityLevel.equals("Bardzo wysoka"))
                    calories *= 1.9;

                if (goal.equals("Utrata"))
                    calories -= 250;
                else if (goal.equals("Przybranie"))
                    calories += 250;

                tvCalories.setText(String.valueOf(calories));
            }
        } else {
            if(etAge.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź swój wiek", Toast.LENGTH_SHORT).show();
            else if(etWeight.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź swoją wagę", Toast.LENGTH_SHORT).show();
            else if(etHeight.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź swój wzrost", Toast.LENGTH_SHORT).show();
        }
    }

    public void register() {
        if (!etLogin.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty()
                && !etAge.getText().toString().isEmpty() && !etHeight.getText().toString().isEmpty() && !etWeight.getText().toString().isEmpty()
                && etLogin.getText().toString().length() > 5 && etPassword.getText().toString().length() > 5) {
            final String userName = etLogin.getText().toString();
            final String password = etPassword.getText().toString();
            final String email = etEmail.getText().toString();
            final int sex = getSexInt(getRadioButtonText(rgSex));
            final int age = Integer.parseInt(etAge.getText().toString());
            final int height = Integer.parseInt(etHeight.getText().toString());
            final int activityLevelInt = getActivityLevelInt(activityLevel);
            final double weight = Double.parseDouble(etWeight.getText().toString());
            final int goal = getGoalInt(getRadioButtonText(rgWeight));

            calculateCalories();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean availableUserName = jsonResponse.getBoolean("availableUserName");
                                boolean availableEmail = jsonResponse.getBoolean("availableEmail");

                                if (availableUserName && availableEmail)
                                {
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success)
                                    {
                                        openLoginActivity();
                                        RegisterActivity.this.finish();
                                        Toast.makeText(RegisterActivity.this, "Jesteś nowym użytkownikiem! Zaloguj się do serwisu !", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this, "Nieoczekiwany błąd rejestracji", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else if(!availableUserName && availableEmail)
                                    Toast.makeText(RegisterActivity.this, "Nazwa użytkownika jest zajęta", Toast.LENGTH_LONG).show();
                                else if (!availableEmail)
                                    Toast.makeText(RegisterActivity.this, "E-mail jest zajęty", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(RegisterActivity.this, "Błąd połączenia", Toast.LENGTH_LONG).show();

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(RegisterActivity.this, "Błąd rejestracji " + e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Register error! " + error.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userName", userName);
                    params.put("password", password);
                    params.put("email", email);
                    params.put("sex", String.valueOf(sex));
                    params.put("age", String.valueOf(age));
                    params.put("height", String.valueOf(height));
                    params.put("activityLevel", String.valueOf(activityLevelInt));
                    params.put("caloricDemand", String.valueOf(calories));
                    params.put("weight", String.valueOf(weight));
                    params.put("goal", String.valueOf(goal));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        } else {
            if (etLogin.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź login", Toast.LENGTH_SHORT).show();
            else if (etPassword.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź hasło", Toast.LENGTH_SHORT).show();
            else if (etEmail.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź e-mail", Toast.LENGTH_SHORT).show();
            else if (etAge.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź swój wiek", Toast.LENGTH_SHORT).show();
            else if (etHeight.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź swój wzrost", Toast.LENGTH_SHORT).show();
            else if (etWeight.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź swoją wagę", Toast.LENGTH_SHORT).show();
            else if(etLogin.getText().toString().length() <= 5)
                Toast.makeText(RegisterActivity.this, "Login musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
            else if(etPassword.getText().toString().length() <= 5)
                Toast.makeText(RegisterActivity.this, "Hasło musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
        }
    }

    private void openLoginActivity() {
        Intent openLoginActivity = new Intent(this, WelcomeActivity.class);
        startActivity(openLoginActivity);
    }

    public String getRadioButtonText(RadioGroup rg) {
        int radioId = rg.getCheckedRadioButtonId();
        RadioButton rb = findViewById(radioId);
        return rb.getText().toString();
    }

    public int getActivityLevelInt(String activityLevel) {
        if (activityLevel.equals("Brak"))
            return 0;
        else if (activityLevel.equals("Niska"))
            return 1;
        else if (activityLevel.equals("Średnia"))
            return 2;
        else if (activityLevel.equals("Wysoka"))
            return 3;
        else
            return 4;
    }

    public int getSexInt(String sex) {
        if (sex.equals("Kobieta"))
            return 0;
        else
            return 1;
    }

    public int getGoalInt(String goal) {
        if (goal.equals("Utrata"))
            return 0;
        else if (goal.equals("Przybranie"))
            return 1;
        else
            return 2;
    }

}
