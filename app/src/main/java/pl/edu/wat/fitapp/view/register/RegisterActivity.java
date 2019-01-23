package pl.edu.wat.fitapp.view.register;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import pl.edu.wat.fitapp.database.connection.RegisterConnection;
import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.mangement.UserSettingsManagement;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.utils.ToastUtils;
import pl.edu.wat.fitapp.view.welcome.WelcomeActivity;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ConnectionCallback {
    private EditText etLogin, etPassword, etEmail, etAge, etWeight, etHeight;
    private RadioGroup rgSex, rgGoal;
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
        rgGoal = findViewById(R.id.rgGoal);

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
                    UserSettingsManagement userMgn = new UserSettingsManagement();
                    String userName = etLogin.getText().toString();
                    String password = etPassword.getText().toString();
                    String email = etEmail.getText().toString();
                    int sex = userMgn.getSexInt(rgSex, getWindow().getDecorView().getRootView());
                    int age = Integer.parseInt(etAge.getText().toString());
                    int height = Integer.parseInt(etHeight.getText().toString());
                    int activityLevelInt = userMgn.getActivityLevelInt(activityLevel);
                    double weight = Double.parseDouble(etWeight.getText().toString());
                    int goal = userMgn.getGoalInt(rgGoal, getWindow().getDecorView().getRootView());

                    calories = userMgn.calculateCaloriesForNewUser(rgSex, rgGoal, getWindow().getDecorView().getRootView(), Integer.parseInt(etAge.getText().toString()),
                            Double.parseDouble(etWeight.getText().toString()), Integer.parseInt(etHeight.getText().toString()), activityLevel);
                    tvCalories.setText(String.valueOf(calories));
                    registerConnection = new RegisterConnection(RegisterActivity.this, userName, password, email, sex, age, height, activityLevelInt, goal, calories, weight);
                    registerConnection.register();
                } else {
                    if (etLogin.getText().toString().isEmpty())
                        ToastUtils.shortToast(RegisterActivity.this, "Wprowadź login");
                    else if (etPassword.getText().toString().isEmpty())
                        ToastUtils.shortToast(RegisterActivity.this, "Wprowadź hasło");
                    else if (etEmail.getText().toString().isEmpty())
                        ToastUtils.shortToast(RegisterActivity.this, "Wprowadź e-mail");
                    else if (etAge.getText().toString().isEmpty())
                        ToastUtils.shortToast(RegisterActivity.this, "Wprowadź swój wiek");
                    else if (etHeight.getText().toString().isEmpty())
                        ToastUtils.shortToast(RegisterActivity.this, "Wprowadź swój wzrost");
                    else if (etWeight.getText().toString().isEmpty())
                        ToastUtils.shortToast(RegisterActivity.this, "Wprowadź swoją wagę");
                    else if (etLogin.getText().toString().length() <= 5)
                        ToastUtils.shortToast(RegisterActivity.this, "Login musi mieć conajmniej 6 znaków");
                    else if (etPassword.getText().toString().length() <= 5)
                        ToastUtils.shortToast(RegisterActivity.this, "Hasło musi mieć conajmniej 6 znaków");
                }
            }
        });

        bCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etAge.getText().toString().isEmpty() && !etWeight.getText().toString().isEmpty() && !etHeight.getText().toString().isEmpty()) {
                    UserSettingsManagement userMgn = new UserSettingsManagement();
                    calories = userMgn.calculateCaloriesForNewUser(rgSex, rgGoal, getWindow().getDecorView().getRootView(), Integer.parseInt(etAge.getText().toString()),
                            Double.parseDouble(etWeight.getText().toString()), Integer.parseInt(etHeight.getText().toString()), activityLevel);
                    tvCalories.setText(String.valueOf(calories));
                } else {
                    if (etAge.getText().toString().isEmpty())
                        ToastUtils.shortToast(RegisterActivity.this, "Wprowadź swój wiek");
                    else if (etWeight.getText().toString().isEmpty())
                        ToastUtils.shortToast(RegisterActivity.this, "Wprowadź swoją wagę");
                    else if (etHeight.getText().toString().isEmpty())
                        ToastUtils.shortToast(RegisterActivity.this, "Wprowadź swój wzrost");
                }
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

    public void openLoginActivity() {
        Intent openLoginActivity = new Intent(this, WelcomeActivity.class);
        startActivity(openLoginActivity);
    }

    @Override
    public void onSuccess() {
        ToastUtils.shortToast(RegisterActivity.this, "Jesteś nowym użytkownikiem! Zaloguj się do serwisu!");
        openLoginActivity();
        this.finish();
    }

    @Override
    public void onFailure(String message) {
        ToastUtils.shortToast(RegisterActivity.this, message);
    }

    @Override
    public Activity activity() {
        return RegisterActivity.this;
    }
}