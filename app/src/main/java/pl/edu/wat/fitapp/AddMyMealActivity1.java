package pl.edu.wat.fitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMyMealActivity1 extends AppCompatActivity {

    private EditText etMealName;

    private Button bSecondStep;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_meal1);

        user = (User) getIntent().getSerializableExtra("user");

        etMealName = findViewById(R.id.etMealName);
        bSecondStep = findViewById(R.id.bSecondStep);

        bSecondStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etMealName.getText().toString().isEmpty()){
                    Intent openAddMyMealActivity2 = new Intent(AddMyMealActivity1.this, AddMyMealActivity2.class);
                    openAddMyMealActivity2.putExtra("user", user);
                    openAddMyMealActivity2.putExtra("mealName", etMealName.getText().toString());
                    startActivity(openAddMyMealActivity2);
                } else
                    Toast.makeText(AddMyMealActivity1.this, "Wpisz nazwę posiłku", Toast.LENGTH_SHORT).show();

            }
        });


    }

}
