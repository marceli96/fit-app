package pl.edu.wat.fitapp.Welcome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import pl.edu.wat.fitapp.Database.Connection.LoginConnection;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Register.RegisterActivity;

public class WelcomeActivity extends AppCompatActivity {
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

        //TODO do testowania
        etLogin.setText("admin");
        etPassword.setText("admin");

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
                        Toast.makeText(WelcomeActivity.this, "Wpisz login", Toast.LENGTH_SHORT).show();
                    else if (etPassword.getText().toString().isEmpty())
                        Toast.makeText(WelcomeActivity.this, "Wpisz hasło", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openMainActivity() {
        Toast.makeText(WelcomeActivity.this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();
        Intent openMainActivity = new Intent(WelcomeActivity.this, MainActivity.class);
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
        WelcomeActivity.this.finish();
    }

    private void openRegisterActivity() {
        Intent openRegisterActivity = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(openRegisterActivity);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProgressBar getPbLogin() {
        return pbLogin;
    }
}
