package pl.edu.wat.fitapp.view.main.fragment.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.utils.ToastUtils;

public class AddMyMealNameActivity extends AppCompatActivity {

    private EditText etMealName;

    private Button bSecondStep;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_meal_name);

        user = (User) getIntent().getSerializableExtra(getString(R.string.userExtra));

        etMealName = findViewById(R.id.etMealName);
        bSecondStep = findViewById(R.id.bSecondStep);

        bSecondStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etMealName.getText().toString().isEmpty()) {
                    Intent openAddMyMealActivity2 = new Intent(AddMyMealNameActivity.this, AddMyMealIngredientsActivity.class);
                    openAddMyMealActivity2.putExtra(getString(R.string.userExtra), user);
                    openAddMyMealActivity2.putExtra(getString(R.string.mealNameExtra), etMealName.getText().toString());
                    startActivity(openAddMyMealActivity2);
                } else
                    ToastUtils.shortToast(AddMyMealNameActivity.this, getString(R.string.enterMealName));
            }
        });


    }

}
