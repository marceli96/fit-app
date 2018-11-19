package pl.edu.wat.fitapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
    private Button bLogin, bRegister;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals("finish_activity"))
                    finish();
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

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
