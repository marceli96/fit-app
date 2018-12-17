package pl.edu.wat.fitapp;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddMealToFoodSystemFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";
    private ListView lvMeals;
    private ArrayList<Meal> mealList;
    private MealListAdapter mealListAdapter;

    private User user;
    private int mealTime;


    public AddMealToFoodSystemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        mealTime = (int) getActivity().getIntent().getSerializableExtra("mealTime");

        mealList = new ArrayList<>();
        getMeals();

        View view = inflater.inflate(R.layout.fragment_add_meal_to_food_system, container, false);

        lvMeals = view.findViewById(R.id.lvMeals);
        mealListAdapter = new MealListAdapter(getActivity(), R.layout.add_meal_listview_adapter, mealList);
        lvMeals.setAdapter(mealListAdapter);

        lvMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(getActivity(), "Wybrałeś posiłek o nazwie = " + mealList.get(position).getMealName(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View alertView = getLayoutInflater().inflate(R.layout.dialog_weight_choose_meal, null);

                TextView tvIngredientName = alertView.findViewById(R.id.tvMealName);
                Button bAddMealToFoodSystem = alertView.findViewById(R.id.bAddMealToFoodSystem);

                tvIngredientName.setText(mealList.get(position).getMealName());

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
                            addMealToFoodSystem(mealList.get(position).getMealID(), user.getUserID(), mealTime, weight);
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
            convertView = getLayoutInflater().inflate(R.layout.add_meal_listview_adapter, parent, false);

            TextView tvMealName = convertView.findViewById(R.id.tvMealName);
            TextView tvMealCarbohydrates = convertView.findViewById(R.id.tvMealCarbohydrates);
            TextView tvMealProtein = convertView.findViewById(R.id.tvMealProtein);
            TextView tvMealFat = convertView.findViewById(R.id.tvMealFat);
            TextView tvMealCalories = convertView.findViewById(R.id.tvMealCalories);

            DecimalFormat decimalFormat = new DecimalFormat("#0.0");

            String temp;
            tvMealName.setText(mealList.get(position).getMealName());

            temp = String.valueOf(decimalFormat.format(getMealMacro("Carbohydrates", position))) + " g";
            tvMealCarbohydrates.setText(temp);

            temp = String.valueOf(decimalFormat.format(getMealMacro("Protein", position))) + " g";
            tvMealProtein.setText(temp);

            temp = String.valueOf(decimalFormat.format(getMealMacro("Fat", position))) + " g";
            tvMealFat.setText(temp);

            temp = String.valueOf(getMealCalories(position)) + " kcal";
            tvMealCalories.setText(temp);

            return convertView;
        }
    }

    private void addMealToFoodSystem(final int mealId, final int userId, final int mealTime, final String weight) {
        Log.d("TESTOWNAIE", "Przesyłane parametry. mealid = " + mealId + " userId = " + userId + " mealTime = " + mealTime);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getActivity(), "Dodano pomyślnie", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Błąd podczas dodawania", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd podczas dodawania " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd podczas dodawania " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "addMealToFoodSystem");
                params.put("mealId", String.valueOf(mealId));
                params.put("userId", String.valueOf(userId));
                params.put("mealTime", String.valueOf(mealTime));
                params.put("weight", weight);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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
                            int mealId = row.getInt("ID_MyMeal");
                            int mealPosition = checkMealPositionInList(mealId);
                            if (mealPosition == -1) {
                                Meal tempMeal = new Meal(row.getInt("ID_MyMeal"), row.getString("MealName"));
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"),
                                        row.getDouble("Carbohydrates"), row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempMeal.getIngredientList().add(tempIngredient);
                                tempMeal.getIngredientWeightList().add(row.getInt("IngredientWeight"));
                                mealList.add(tempMeal);
                            } else {
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"),
                                        row.getDouble("Carbohydrates"), row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                mealList.get(mealPosition).getIngredientList().add(tempIngredient);
                                mealList.get(mealPosition).getIngredientWeightList().add(row.getInt("IngredientWeight"));
                            }
                        }
                        mealListAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Pobrano posiłki", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Blad podczas pobierania posiłków", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Login error! " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Login error! " + error.toString(), Toast.LENGTH_SHORT).show();
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

    public int checkMealPositionInList(int mealId) {
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getMealID() == mealId) {
                return i;
            }
        }
        return -1;
    }

    public double getMealMacro(String macro, int position) {
        double sum = 0;
        switch (macro) {
            case "Carbohydrates":
                for (int i = 0; i < mealList.get(position).getIngredientList().size(); i++) {
                    double ingredientWeight = mealList.get(position).getIngredientWeightList().get(i);
                    sum += (ingredientWeight / 100) * mealList.get(position).getIngredientList().get(i).getIngredientCarbohydrates();
                }
                break;
            case "Protein":
                for (int i = 0; i < mealList.get(position).getIngredientList().size(); i++) {
                    double ingredientWeight = mealList.get(position).getIngredientWeightList().get(i);
                    sum += (ingredientWeight / 100) * mealList.get(position).getIngredientList().get(i).getIngredientProtein();
                }
                break;
            case "Fat":
                for (int i = 0; i < mealList.get(position).getIngredientList().size(); i++) {
                    double ingredientWeight = mealList.get(position).getIngredientWeightList().get(i);
                    sum += (ingredientWeight / 100) * mealList.get(position).getIngredientList().get(i).getIngredientFat();
                }
                break;
        }
        return sum;
    }

    public int getMealCalories(int position) {
        int sum = 0;
        for (int i = 0; i < mealList.get(position).getIngredientList().size(); i++) {
            double ingredientWeight = mealList.get(position).getIngredientWeightList().get(i);
            sum += (ingredientWeight / 100) * mealList.get(position).getIngredientList().get(i).getIngredientCalories();
        }
        return sum;
    }
}
