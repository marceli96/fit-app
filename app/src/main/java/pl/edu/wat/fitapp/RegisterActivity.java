package pl.edu.wat.fitapp;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText etLogin, etPassword, etEmail, etAge, etWeight, etHeight;
    RadioGroup rgSex, rgWeight;
    TextView tvCalories;
    Spinner spinner;
    Button bRegister, bCalculate;

    String activityLevel;
    int calories;
    private static final String REGISTER_REQUEST_URL = "http://fitappliaction.cba.pl/register.php";

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
        etWeight.setText("80");
        etHeight.setText("190");
        etAge.setText("22");
        //

        spinner = findViewById(R.id.spinnerActivityLevel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activityLevel, android.R.layout.simple_spinner_item);
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
        // TODO zabezpieczenie przed kliknięciem kiedy puste dane
        String sex = getRadioButtonText(rgSex);
        String goal = getRadioButtonText(rgWeight);
        int age = Integer.parseInt(etAge.getText().toString());
        int weight = Integer.parseInt(etWeight.getText().toString());
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
    }

    public void register() {
        String userName = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();
        int sex = getSexInt(getRadioButtonText(rgSex));
//        int weight = Integer.parseInt(etWeight.getText().toString());
        int height = Integer.parseInt(etHeight.getText().toString());
        int age = Integer.parseInt(etAge.getText().toString());
        int activityLevelInt = getActivityLevelInt(activityLevel);

        String goal = getRadioButtonText(rgWeight);

        calculateCalories();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(getApplicationContext(), "onResponse", Toast.LENGTH_SHORT);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Toast.makeText(getApplicationContext(), "Rejestracja przebiegła pomyślnie",
                                Toast.LENGTH_LONG).show();
                    } else {
                        boolean userError = jsonResponse.getBoolean("userError");
                        boolean emailError = jsonResponse.getBoolean("emailError");
                        if (userError && emailError)
                            Toast.makeText(getApplicationContext(), "Nazwa użytkownika oraz e-mail są zajęte",
                                    Toast.LENGTH_LONG).show();
                        else if (userError)
                            Toast.makeText(getApplicationContext(), "Nazwa użytkownika jest zajęta",
                                    Toast.LENGTH_LONG).show();
                        else if (emailError)
                            Toast.makeText(getApplicationContext(), "E-mail jest zajęty",
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Nieoczekiwany błąd",
                                    Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        RegisterRequest registerRequest = new RegisterRequest(userName, password, email, sex, age,
                height, activityLevelInt, calories, responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(registerRequest);
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

}
