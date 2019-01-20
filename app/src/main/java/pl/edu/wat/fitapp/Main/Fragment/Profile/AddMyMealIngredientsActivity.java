package pl.edu.wat.fitapp.Main.Fragment.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.AddedIngredientsToMealListAdapter;
import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.IngredientsListAdapter;
import pl.edu.wat.fitapp.Database.Connection.AddMyMealConnection;
import pl.edu.wat.fitapp.Database.Connection.IngredientsConnection;
import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Dialog.AddMyMealIngredientOnClickDialog;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.Mangement.MacrocomponentManagement;
import pl.edu.wat.fitapp.R;

public class AddMyMealIngredientsActivity extends AppCompatActivity {
    private Button bAddMyMeal;
    private TextView tvMealCalories, tvReqCalories, tvMealCarbohydrates, tvReqCarbohydrates, tvMealProtein,
            tvReqProtein, tvMealFat, tvReqFat, tvIngredientAmount;
    private ProgressBar pbCalories, pbCarbohydrates, pbProtein, pbFat;
    private LinearLayout llShowListView;
    private ImageView imArrow;
    private ListView lvMealIngredients, lvIngredients;

    private IngredientsListAdapter ingredientsListAdapter;
    private AddedIngredientsToMealListAdapter addedIngredientsToMealListAdapter;

    private ArrayList<Ingredient> ingredients, mealIngredients;

    private IngredientsConnection ingredientsConnection;
    private AddMyMealConnection addMyMealConnection;

    private User user;

    private String mealName;
    private boolean hidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_meal_ingredients);

        user = (User) getIntent().getSerializableExtra("user");
        mealName = (String) getIntent().getSerializableExtra("mealName");

        bAddMyMeal = findViewById(R.id.bAddMyMeal);
        tvMealCalories = findViewById(R.id.tvMealCalories);
        tvReqCalories = findViewById(R.id.tvReqCalories);
        tvMealCarbohydrates = findViewById(R.id.tvMealCarbohydrates);
        tvReqCarbohydrates = findViewById(R.id.tvReqCarbohydrates);
        tvMealProtein = findViewById(R.id.tvMealProtein);
        tvReqProtein = findViewById(R.id.tvReqProtein);
        tvMealFat = findViewById(R.id.tvMealFat);
        tvReqFat = findViewById(R.id.tvReqFat);
        tvIngredientAmount = findViewById(R.id.tvIngredientAmount);
        pbCalories = findViewById(R.id.pbCalories);
        pbCarbohydrates = findViewById(R.id.pbCarbohydrates);
        pbProtein = findViewById(R.id.pbProtein);
        pbFat = findViewById(R.id.pbFat);
        llShowListView = findViewById(R.id.llShowListView);
        imArrow = findViewById(R.id.imArrow);
        lvMealIngredients = findViewById(R.id.lvMealIngredients);
        lvIngredients = findViewById(R.id.lvIngredients);

        ingredients = new ArrayList<>();
        mealIngredients = new ArrayList<>();

        ingredientsListAdapter = new IngredientsListAdapter(AddMyMealIngredientsActivity.this, R.layout.listview_adapter_add_ingredient, ingredients);
        addedIngredientsToMealListAdapter = new AddedIngredientsToMealListAdapter(AddMyMealIngredientsActivity.this, R.layout.listview_adapter_ingredient_with_weight, mealIngredients, this);
        lvIngredients.setAdapter(ingredientsListAdapter);
        lvMealIngredients.setAdapter(addedIngredientsToMealListAdapter);

        ingredientsConnection = new IngredientsConnection(AddMyMealIngredientsActivity.this, ingredients);
        ingredientsConnection.getIngredients(ingredientsListAdapter);

        bAddMyMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mealIngredients.size() > 0) {
                    addMyMealConnection = new AddMyMealConnection(AddMyMealIngredientsActivity.this, mealIngredients);
                    addMyMealConnection.addMyMeal(user.getUserID(), mealName);
                } else
                    Toast.makeText(AddMyMealIngredientsActivity.this, "Najpierw dodaj składniki do posiłku", Toast.LENGTH_SHORT).show();
            }
        });

        llShowListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hidden) {
                    lvMealIngredients.setVisibility(View.VISIBLE);
                    hidden = false;
                    imArrow.setImageResource(R.drawable.arrow_down);
                } else {
                    lvMealIngredients.setVisibility(View.GONE);
                    hidden = true;
                    imArrow.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AddMyMealIngredientOnClickDialog addMyMealIngredientOnClickDialog = new AddMyMealIngredientOnClickDialog(AddMyMealIngredientsActivity.this);
                addMyMealIngredientOnClickDialog.build(position, ingredients);
            }
        });

        String tempString = String.valueOf(user.getCaloricDemand()) + " kcal";
        tvReqCalories.setText(tempString);

        tempString = String.valueOf((int) (0.5 * user.getCaloricDemand() / 4)) + "-" + String.valueOf((int) (0.65 * user.getCaloricDemand() / 4));
        tvReqCarbohydrates.setText(tempString);

        tempString = String.valueOf((int) (0.15 * user.getCaloricDemand() / 4)) + "-" + String.valueOf((int) (0.25 * user.getCaloricDemand() / 4));
        tvReqProtein.setText(tempString);

        tempString = String.valueOf((int) (0.2 * user.getCaloricDemand() / 9)) + "-" + String.valueOf((int) (0.3 * user.getCaloricDemand() / 9));
        tvReqFat.setText(tempString);

        pbCalories.setMax(user.getCaloricDemand());
        pbCarbohydrates.setMax((int) (0.5 * user.getCaloricDemand() / 4));
        pbProtein.setMax((int) (0.15 * user.getCaloricDemand() / 4));
        pbFat.setMax((int) (0.2 * user.getCaloricDemand() / 9));

    }

    public void addIngredient(Ingredient ingredient) {
        for (int i = 0; i < mealIngredients.size(); i++) {
            if (mealIngredients.get(i).getID() == ingredient.getID()) {
                mealIngredients.get(i).setWeight(mealIngredients.get(i).getWeight() + ingredient.getWeight());
                addedIngredientsToMealListAdapter.notifyDataSetChanged();
                return;
            }
        }
        mealIngredients.add(ingredient);
        tvIngredientAmount.setText(String.valueOf(mealIngredients.size()));
        updateMealMacros();
    }

    public void deleteIngredient(Ingredient ingredient) {
        mealIngredients.remove(ingredient);
        addedIngredientsToMealListAdapter.notifyDataSetChanged();
        updateMealMacros();
        tvIngredientAmount.setText(String.valueOf(mealIngredients.size()));
    }

    public void openMeFragment() {
        Intent openMeFragment = new Intent(AddMyMealIngredientsActivity.this, MainActivity.class);
        openMeFragment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMeFragment.putExtra("user", user);
        openMeFragment.putExtra("action", "openMeFragment");
        startActivity(openMeFragment);
    }

    private void updateMealMacros() {
        MacrocomponentManagement macroMng = new MacrocomponentManagement();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        int mealCalories = macroMng.getCaloriesForMeal(mealIngredients);
        double mealCarbohydrates = macroMng.getCarbohydratesForMeal(mealIngredients);
        double mealProtein = macroMng.getProteinForMeal(mealIngredients);
        double mealFat = macroMng.getFatForMeal(mealIngredients);

        tvMealCalories.setText(String.valueOf(mealCalories));
        tvMealCarbohydrates.setText(String.valueOf(decimalFormat.format(mealCarbohydrates)));
        tvMealProtein.setText(String.valueOf(decimalFormat.format(mealProtein)));
        tvMealFat.setText(String.valueOf(decimalFormat.format(mealFat)));

        pbCalories.setProgress(mealCalories);
        pbCarbohydrates.setProgress((int) Math.round(mealCarbohydrates));
        pbProtein.setProgress((int) Math.round(mealProtein));
        pbFat.setProgress((int) Math.round(mealFat));
    }
}