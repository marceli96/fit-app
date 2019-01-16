package pl.edu.wat.fitapp.Register;

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

import pl.edu.wat.fitapp.Database.Connection.RegisterConnection;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Welcome.WelcomeActivity;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String REGISTER_URL = "http://fitappliaction.cba.pl/register.php";

    private EditText etLogin, etPassword, etEmail, etAge, etWeight, etHeight;
    private RadioGroup rgSex, rgWeight;
    private TextView tvCalories;
    private Spinner spinner;
    private Button bRegister, bCalculate;

    private String activityLevel;
    private int calories;

    private RegisterConnection registerConnection;

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

        spinner = findViewById(R.id.spinnerActivityLevel);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activityLevel, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etLogin.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty() && !etEmail.getText().toString().isEmpty()
                        && !etAge.getText().toString().isEmpty() && !etHeight.getText().toString().isEmpty() && !etWeight.getText().toString().isEmpty()
                        && etLogin.getText().toString().length() > 5 && etPassword.getText().toString().length() > 5) {
                    String userName = etLogin.getText().toString();
                    String password = etPassword.getText().toString();
                    String email = etEmail.getText().toString();
                    int sex = getSexInt(getRadioButtonText(rgSex));
                    int age = Integer.parseInt(etAge.getText().toString());
                    int height = Integer.parseInt(etHeight.getText().toString());
                    int activityLevelInt = getActivityLevelInt(activityLevel);
                    double weight = Double.parseDouble(etWeight.getText().toString());
                    int goal = getGoalInt(getRadioButtonText(rgWeight));

                    calculateCalories();
                    registerConnection = new RegisterConnection(RegisterActivity.this, userName, password, email, sex, age, height, activityLevelInt, goal, calories, weight);
                    registerConnection.register();
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
                    else if (etLogin.getText().toString().length() <= 5)
                        Toast.makeText(RegisterActivity.this, "Login musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
                    else if (etPassword.getText().toString().length() <= 5)
                        Toast.makeText(RegisterActivity.this, "Hasło musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
                }
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
        if (!etAge.getText().toString().isEmpty() && !etWeight.getText().toString().isEmpty() && !etHeight.getText().toString().isEmpty()) {
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
            if (etAge.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź swój wiek", Toast.LENGTH_SHORT).show();
            else if (etWeight.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź swoją wagę", Toast.LENGTH_SHORT).show();
            else if (etHeight.getText().toString().isEmpty())
                Toast.makeText(RegisterActivity.this, "Wprowadź swój wzrost", Toast.LENGTH_SHORT).show();
        }
    }

    public void openLoginActivity() {
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
