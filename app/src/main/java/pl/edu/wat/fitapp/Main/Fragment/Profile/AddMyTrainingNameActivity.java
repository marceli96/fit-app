package pl.edu.wat.fitapp.Main.Fragment.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.R;

public class AddMyTrainingNameActivity extends AppCompatActivity {

    private EditText etTrainingName;
    private Button bSecondStep;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_training_name);

        user = (User) getIntent().getSerializableExtra("user");

        etTrainingName = findViewById(R.id.etTrainingName);
        bSecondStep = findViewById(R.id.bSecondStep);

        bSecondStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTrainingName.getText().toString().isEmpty()) {
                    Intent openAddMyTrainingActivity2 = new Intent(AddMyTrainingNameActivity.this, AddMyTrainingExercisesActivity.class);
                    openAddMyTrainingActivity2.putExtra("user", user);
                    openAddMyTrainingActivity2.putExtra("trainingName", etTrainingName.getText().toString());
                    startActivity(openAddMyTrainingActivity2);
                } else
                    Toast.makeText(AddMyTrainingNameActivity.this, "Wpisz nazwę treningu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
