package pl.edu.wat.fitapp.view.welcome;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import pl.edu.wat.fitapp.database.connection.LoginConnection;
import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.interfaces.callback.UserConnectionCallback;
import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.utils.ToastUtils;
import pl.edu.wat.fitapp.view.register.RegisterActivity;

public class WelcomeActivity extends AppCompatActivity implements UserConnectionCallback {
    private EditText etLogin, etPassword;
    private Button bLogin, bRegister;
    private ProgressBar pbLogin;

    private User user;

    private LoginConnection loginConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        bLogin = findViewById(R.id.bLogin);
        bRegister = findViewById(R.id.bRegister);
        pbLogin = findViewById(R.id.pbLogin);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etLogin.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    pbLogin.setVisibility(View.VISIBLE);
                    loginConnection = new LoginConnection(WelcomeActivity.this, etLogin.getText().toString(), etPassword.getText().toString(), user);
                    loginConnection.moveWeight();
                } else {
                    pbLogin.setVisibility(View.INVISIBLE);
                    if (etLogin.getText().toString().isEmpty())
                        ToastUtils.shortToast(WelcomeActivity.this, getString(R.string.fillLogin));
                    else if (etPassword.getText().toString().isEmpty())
                        ToastUtils.shortToast(WelcomeActivity.this, getString(R.string.fillPassword));
                }
            }
        });
    }

    private void openMainActivity() {
        ToastUtils.shortToast(WelcomeActivity.this, getString(R.string.successLogIn));
        Intent openMainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
        openMainActivity.putExtra(getString(R.string.userExtra), user);
        startActivity(openMainActivity);
        WelcomeActivity.this.finish();
    }

    private void openRegisterActivity() {
        Intent openRegisterActivity = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(openRegisterActivity);
    }

    @Override
    public void onSuccess(User user) {
        this.user = user;
        openMainActivity();
    }

    @Override
    public void onFailure(String message) {
        ToastUtils.shortToast(WelcomeActivity.this, message);
        pbLogin.setVisibility(View.INVISIBLE);
    }


    @Override
    public Activity activity() {
        return WelcomeActivity.this;
    }

}
