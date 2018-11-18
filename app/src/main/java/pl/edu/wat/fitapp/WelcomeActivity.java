package pl.edu.wat.fitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    private Button bLogin, bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button bLogin = (Button)findViewById(R.id.bLogin);
        Button bRegsiter = (Button)findViewById(R.id.bRegister);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLoginScreen = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(openLoginScreen);
            }
        });

        bRegsiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRegisterScreen = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(openRegisterScreen);
            }
        });

    }
}
