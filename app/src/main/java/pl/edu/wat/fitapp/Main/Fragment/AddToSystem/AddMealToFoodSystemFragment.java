package pl.edu.wat.fitapp.Main.Fragment.AddToSystem;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Database.Ingredient;
import pl.edu.wat.fitapp.Database.Meal;
import pl.edu.wat.fitapp.Database.User;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;

public class AddMealToFoodSystemFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private ListView lvMeals;
    private ArrayList<Meal> mealList;
    private MealListAdapter mealListAdapter;

    private User user;
    private int mealTime;


    public AddMealToFoodSystemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_meal_to_food_system, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");
        mealTime = (int) getActivity().getIntent().getSerializableExtra("mealTime");

        mealList = new ArrayList<>();
        getMeals();

        lvMeals = view.findViewById(R.id.lvMeals);
        mealListAdapter = new MealListAdapter(getActivity(), R.layout.listview_adapter_add_meal, mealList);
        lvMeals.setAdapter(mealListAdapter);

        lvMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View alertView = getLayoutInflater().inflate(R.layout.dialog_weight_choose_meal, null);

                TextView tvMealName = alertView.findViewById(R.id.tvMealName);
                ListView lvIngredients = alertView.findViewById(R.id.lvIngredients);
                Button bAddMealToFoodSystem = alertView.findViewById(R.id.bAddMealToFoodSystem);

                tvMealName.setText(mealList.get(position).getName());

                IngredientsListAdapter adapter = new IngredientsListAdapter(getActivity(), R.layout.listview_adapter_ingredient_with_weight_simple, mealList.get(position).getIngredientList());
                lvIngredients.setAdapter(adapter);

                builder.setView(alertView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                bAddMealToFoodSystem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText etWeight = alertView.findViewById(R.id.etWeight);
                        String weight = etWeight.getText().toString();
                        if (weight.isEmpty())
                            Toast.makeText(getActivity(), "Wpisz wagę!", Toast.LENGTH_SHORT).show();
                        else {
                            addMealToFoodSystem(mealList.get(position).getID(), user.getUserID(), mealTime, weight);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        return view;
    }

    class MealListAdapter extends ArrayAdapter<Meal> {

        public MealListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Meal> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_add_meal, parent, false);

            TextView tvMealName = convertView.findViewById(R.id.tvMealName);
            TextView tvMealTotalWeight = convertView.findViewById(R.id.tvMealTotalWeight);
            TextView tvMealCarbohydrates = convertView.findViewById(R.id.tvMealCarbohydrates);
            TextView tvMealProtein = convertView.findViewById(R.id.tvMealProtein);
            TextView tvMealFat = convertView.findViewById(R.id.tvMealFat);
            TextView tvMealCalories = convertView.findViewById(R.id.tvMealCalories);

            DecimalFormat decimalFormat = new DecimalFormat("0.0");

            String temp;
            tvMealName.setText(mealList.get(position).getName());

            temp = String.valueOf(mealList.get(position).getTotalWeight()) + " g";
            tvMealTotalWeight.setText(temp);

            temp = String.valueOf(decimalFormat.format(mealList.get(position).getCarbohydrates())) + " g";
            tvMealCarbohydrates.setText(temp);

            temp = String.valueOf(decimalFormat.format(mealList.get(position).getProtein())) + " g";
            tvMealProtein.setText(temp);

            temp = String.valueOf(decimalFormat.format(mealList.get(position).getFat())) + " g";
            tvMealFat.setText(temp);

            temp = String.valueOf(mealList.get(position).getCalories()) + " kcal";
            tvMealCalories.setText(temp);

            return convertView;
        }
    }

    private void addMealToFoodSystem(final int mealId, final int userId, final int mealTime, final String weight) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean message = jsonResponse.getBoolean("message");
                    if (!message) {
                        Toast.makeText(getActivity(), "Dany posiłek został już dodany w tej porze jedzenia", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Toast.makeText(getActivity(), "Dodano pomyślnie", Toast.LENGTH_SHORT).show();
                            openMainActivity();
                        } else
                            Toast.makeText(getActivity(), "Błąd podczas dodawania", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd podczas dodawania " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd podczas dodawania " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "addMealToFoodSystem");
                params.put("myMealId", String.valueOf(mealId));
                params.put("userId", String.valueOf(userId));
                params.put("mealTime", String.valueOf(mealTime));
                params.put("weight", weight);
                params.put("date", dateFormat.format(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void openMainActivity() {
        Intent openMainActivity = new Intent(getContext(), MainActivity.class);
        openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
    }

    private void getMeals() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            int mealPosition = findMealInList(row.getInt("ID_MyMeal"));
                            if (mealPosition == -1) {
                                Meal tempMeal = new Meal(row.getInt("ID_MyMeal"), row.getString("MealName"));
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"),
                                        row.getDouble("Carbohydrates"), row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                tempMeal.addIngredientToList(tempIngredient);
                                mealList.add(tempMeal);
                            } else {
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"),
                                        row.getDouble("Carbohydrates"), row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                mealList.get(mealPosition).addIngredientToList(tempIngredient);
                            }
                        }
                        mealListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getMeals");
                params.put("userId", String.valueOf(user.getUserID()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public int findMealInList(int mealId) {
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getID() == mealId) {
                return i;
            }
        }
        return -1;
    }

    class IngredientsListAdapter extends ArrayAdapter<Ingredient>{
        ArrayList<Ingredient> ingredientList;

        public IngredientsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Ingredient> objects) {
            super(context, resource, objects);
            ingredientList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_ingredient_with_weight_simple, parent, false);

            TextView tvIngredientName = convertView.findViewById(R.id.tvIngredientName);
            TextView tvIngredientWeight = convertView.findViewById(R.id.tvIngredientWeight);

            tvIngredientName.setText(ingredientList.get(position).getName());
            String tempString = ingredientList.get(position).getWeight() + " g";
            tvIngredientWeight.setText(tempString);

            return convertView;
        }
    }

}
