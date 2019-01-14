package pl.edu.wat.fitapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddMyMealActivity2 extends AppCompatActivity {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private Button bAddMyMeal;
    private TextView tvMealCalories, tvReqCalories, tvMealCarbohydrates, tvReqCarbohydrates, tvMealProtein,
            tvReqProtein, tvMealFat, tvReqFat, tvIngredientAmount;
    private ProgressBar pbCalories, pbCarbohydrates, pbProtein, pbFat;
    private LinearLayout llShowListView;
    private ImageView imArrow;
    private ListView lvMealIngredients, lvIngredients;

    private IngredientsAdapter ingredientsAdapter;
    private MealIngredientsAdapter mealIngredientsAdapter;

    private ArrayList<Ingredient> ingredients, mealIngredients;

    private User user;

    private String mealName;
    private int minReqCarbohydrates = 0, maxReqCarbohydrates = 0, minReqProtein = 0, maxReqProtein = 0, minReqFat = 0, maxReqFat;
    private double mealCarbohydrates, mealProtein, mealFat;
    private int mealCalories;
    private boolean hidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_meal2);

        user = (User) getIntent().getSerializableExtra("user");
        mealName = (String) getIntent().getSerializableExtra("mealName");

        ingredients = new ArrayList<>();
        mealIngredients = new ArrayList<>();
        getIngredients();

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

        bAddMyMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMyMeal();
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


        ingredientsAdapter = new IngredientsAdapter(AddMyMealActivity2.this, R.layout.listview_adapter_add_ingredient, ingredients);
        mealIngredientsAdapter = new MealIngredientsAdapter(AddMyMealActivity2.this, R.layout.listview_adapter_ingredient_with_weight, mealIngredients);
        lvIngredients.setAdapter(ingredientsAdapter);
        lvMealIngredients.setAdapter(mealIngredientsAdapter);

        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMyMealActivity2.this);
                final View alertView = getLayoutInflater().inflate(R.layout.dialog_weight_choose_inredient, null);

                TextView tvIngredientName = alertView.findViewById(R.id.tvIngredientName);
                Button bAddIngredientToList = alertView.findViewById(R.id.bAddIngredientToFoodSystem);

                tvIngredientName.setText(ingredients.get(position).getName());

                builder.setView(alertView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                bAddIngredientToList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText etWeight = alertView.findViewById(R.id.etWeight);
                        String weight = etWeight.getText().toString();
                        if (weight.isEmpty())
                            Toast.makeText(AddMyMealActivity2.this, "Wpisz wagę!", Toast.LENGTH_SHORT).show();
                        else {
                            Ingredient tempIngredient = null;
                            try {
                                tempIngredient = (Ingredient) ingredients.get(position).clone();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            tempIngredient.setWeight(Integer.parseInt(weight));
                            addIngredient(tempIngredient);
                            updateMealMacros();
                            tvIngredientAmount.setText(String.valueOf(mealIngredients.size()));
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        String tempString = String.valueOf(user.getCaloricDemand()) + " kcal";
        tvReqCalories.setText(tempString);

        minReqCarbohydrates = (int) (0.5 * user.getCaloricDemand() / 4);
        maxReqCarbohydrates = (int) (0.65 * user.getCaloricDemand() / 4);
        tempString = String.valueOf(minReqCarbohydrates) + "-" + String.valueOf(maxReqCarbohydrates);
        tvReqCarbohydrates.setText(tempString);

        minReqProtein = (int) (0.15 * user.getCaloricDemand() / 4);
        maxReqProtein = (int) (0.25 * user.getCaloricDemand() / 4);
        tempString = String.valueOf(minReqProtein) + "-" + String.valueOf(maxReqProtein);
        tvReqProtein.setText(tempString);

        minReqFat = (int) (0.2 * user.getCaloricDemand() / 9);
        maxReqFat = (int) (0.3 * user.getCaloricDemand() / 9);
        tempString = String.valueOf(minReqFat) + "-" + String.valueOf(maxReqFat);
        tvReqFat.setText(tempString);

        pbCalories.setMax(user.getCaloricDemand());
        pbCarbohydrates.setMax(minReqCarbohydrates);
        pbProtein.setMax(minReqProtein);
        pbFat.setMax(minReqFat);

    }

    private void addIngredient(Ingredient ingredient) {
        for (int i = 0; i < mealIngredients.size(); i++) {
            if (mealIngredients.get(i).getID() == ingredient.getID()) {
                mealIngredients.get(i).setWeight(mealIngredients.get(i).getWeight() + ingredient.getWeight());
                mealIngredientsAdapter.notifyDataSetChanged();
                return;
            }
        }
        mealIngredients.add(ingredient);
    }

    private void getIngredients() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject ingredient = jsonResponse.getJSONObject(String.valueOf(i));
                            ingredients.add(new Ingredient(ingredient.getInt("ID_Ingredient"), ingredient.getString("IngredientName"),
                                    ingredient.getDouble("Carbohydrates"), ingredient.getDouble("Protein"), ingredient.getDouble("Fat"),
                                    ingredient.getInt("Calories")));
                        }
                        ingredientsAdapter.notifyDataSetChanged();
                    } else
                        Toast.makeText(AddMyMealActivity2.this, "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddMyMealActivity2.this, "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddMyMealActivity2.this, "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getIngredients");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddMyMealActivity2.this);
        requestQueue.add(stringRequest);
    }

    private void addMyMeal() {
        if (mealIngredients.size() > 0) {
            String ingredientIds = "";
            String ingredientWeights = "";
            for (int i = 0; i < mealIngredients.size(); i++) {
                if (i == mealIngredients.size() - 1) {
                    ingredientIds += String.valueOf(mealIngredients.get(i).getID());
                    ingredientWeights += String.valueOf(mealIngredients.get(i).getWeight());
                } else {
                    ingredientIds += String.valueOf(mealIngredients.get(i).getID()) + "/";
                    ingredientWeights += String.valueOf(mealIngredients.get(i).getWeight()) + "/";
                }
            }

            final String finalIngredientWeights = ingredientWeights;
            final String finalIngredientIds = ingredientIds;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Toast.makeText(AddMyMealActivity2.this, "Dodano posiłek", Toast.LENGTH_SHORT).show();
                            openMeFragment();
                        } else {
                            Toast.makeText(AddMyMealActivity2.this, "Blad podczas dodawania posiłku", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(AddMyMealActivity2.this, "Blad podczas dodawania posiłku " + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddMyMealActivity2.this, "Blad podczas dodawania posiłku " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("operation", "addMyMeal");
                    params.put("ingredientIds", finalIngredientIds);
                    params.put("userId", String.valueOf(user.getUserID()));
                    params.put("mealName", mealName);
                    params.put("ingredientWeights", finalIngredientWeights);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(AddMyMealActivity2.this);
            requestQueue.add(stringRequest);
        } else
            Toast.makeText(AddMyMealActivity2.this, "Najpierw dodaj składniki do posiłku", Toast.LENGTH_SHORT).show();
    }

    private void openMeFragment() {
        Intent openMeFragment = new Intent(AddMyMealActivity2.this, MainActivity.class);
        openMeFragment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMeFragment.putExtra("user", user);
        openMeFragment.putExtra("action", "openMeFragment");
        startActivity(openMeFragment);
    }

    class IngredientsAdapter extends ArrayAdapter<Ingredient> {

        public IngredientsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_add_ingredient, parent, false);

            TextView tvIngredientName = convertView.findViewById(R.id.tvIngredientName);
            TextView tvIngredientCarbohydrates = convertView.findViewById(R.id.tvIngredientCarbohydrates);
            TextView tvIngredientProtein = convertView.findViewById(R.id.tvIngredientProtein);
            TextView tvIngredientFat = convertView.findViewById(R.id.tvIngredientFat);
            TextView tvIngredientCalories = convertView.findViewById(R.id.tvIngredientCalories);

            DecimalFormat decimalFormat = new DecimalFormat("0.0");

            String tempString;
            tvIngredientName.setText(ingredients.get(position).getName());

            tempString = String.valueOf(decimalFormat.format(ingredients.get(position).getCarbohydrates())) + " g";
            tvIngredientCarbohydrates.setText(tempString);

            tempString = String.valueOf(decimalFormat.format(ingredients.get(position).getProtein())) + " g";
            tvIngredientProtein.setText(tempString);

            tempString = String.valueOf(decimalFormat.format(ingredients.get(position).getFat())) + " g";
            tvIngredientFat.setText(tempString);

            tempString = String.valueOf(decimalFormat.format(ingredients.get(position).getCalories())) + " kcal";
            tvIngredientCalories.setText(tempString);

            return convertView;
        }
    }

    class MealIngredientsAdapter extends ArrayAdapter<Ingredient> {

        public MealIngredientsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_ingredient_with_weight, parent, false);

            TextView tvIngredientName = convertView.findViewById(R.id.tvIngredientName);
            TextView tvIngredientWeight = convertView.findViewById(R.id.tvIngredientWeight);
            TextView tvIngredientCarbohydrates = convertView.findViewById(R.id.tvIngredientCarbohydrates);
            TextView tvIngredientProtein = convertView.findViewById(R.id.tvIngredientProtein);
            TextView tvIngredientFat = convertView.findViewById(R.id.tvIngredientFat);
            TextView tvIngredientCalories = convertView.findViewById(R.id.tvIngredientCalories);
            ImageView imDeleteIngredient = convertView.findViewById(R.id.imDeleteIngredient);

            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            String tempString;

            final Ingredient ingredient = mealIngredients.get(position);

            tvIngredientName.setText(ingredient.getName());

            tempString = String.valueOf(ingredient.getWeight()) + " g";
            tvIngredientWeight.setText(tempString);

            tempString = String.valueOf(decimalFormat.format(ingredient.getCarbohydrates() * ingredient.getWeight() / 100)) + " g";
            tvIngredientCarbohydrates.setText(tempString);

            tempString = String.valueOf(decimalFormat.format(ingredient.getProtein() * ingredient.getWeight() / 100)) + " g";
            tvIngredientProtein.setText(tempString);

            tempString = String.valueOf(decimalFormat.format(ingredient.getFat() * ingredient.getWeight() / 100)) + " g";
            tvIngredientFat.setText(tempString);

            tempString = String.valueOf(ingredient.getCalories() * ingredient.getWeight() / 100) + " kcal";
            tvIngredientCalories.setText(tempString);

            imDeleteIngredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO rozważyć dodania dialogu, który zapyta czy na pewno chcemy tu usnąć
                    mealIngredients.remove(ingredient);
                    mealIngredientsAdapter.notifyDataSetChanged();
                    updateMealMacros();
                    tvIngredientAmount.setText(String.valueOf(mealIngredients.size()));
                }
            });

            return convertView;
        }
    }


    private void updateMealMacros() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        mealCalories = 0;
        mealCarbohydrates = 0;
        mealProtein = 0;
        mealFat = 0;

        for (Ingredient ingredient : mealIngredients) {
            mealCalories += ingredient.getCalories() * ingredient.getWeight() / 100;
            mealCarbohydrates += ingredient.getCarbohydrates() * ingredient.getWeight() / 100;
            mealProtein += ingredient.getProtein() * ingredient.getWeight() / 100;
            mealFat += ingredient.getFat() * ingredient.getWeight() / 100;
        }

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
