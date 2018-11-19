package pl.edu.wat.fitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Button blogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        blogin = (Button)findViewById(R.id.bLogin);
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMainScreen = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(openMainScreen);
                LoginActivity.this.finish();
                Intent intent = new Intent("finish_activity");
                sendBroadcast(intent);
            }
        });
    }
}
