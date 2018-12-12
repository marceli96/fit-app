package pl.edu.wat.fitapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {
    private EditText etLogin, etPassword;
    private Button bLogin, bRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        bLogin = (Button)findViewById(R.id.bLogin);
        bRegister = (Button)findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRegisterActivity = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(openRegisterActivity);
            }
        });

    }
}
