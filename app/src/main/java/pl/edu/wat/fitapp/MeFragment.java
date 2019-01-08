package pl.edu.wat.fitapp;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private Button bAddMyMeal, bAddMyTraining;
    private TextView tvMyMealsEmpty, tvMyTrainingsEmpty;
    private NonScrollListView lvMyMeals, lvMyTrainings;
    private ImageView imArrowMeals, imArrowTrainings;
    private LinearLayout llMyMeals, llMyTrainings;
    private ProgressBar pbLoadingMeals;

    private ArrayList<Meal> myMeals, myTrainings;
    private MyMealsListAdapter myMealsListAdapter;

    private User user;

    private boolean hiddenMyMeals = false, hiddenMyTrainings = false;

    public MeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        user = (User) getActivity().getIntent().getSerializableExtra("user");

        bAddMyMeal = view.findViewById(R.id.bAddMyMeal);
        tvMyMealsEmpty = view.findViewById(R.id.tvMyMealsEmpty);
        imArrowMeals = view.findViewById(R.id.imArrowMeals);
        llMyMeals = view.findViewById(R.id.llMyMeals);
        pbLoadingMeals = view.findViewById(R.id.pbLoadingMeals);

        myMeals = new ArrayList<>();
        getMyMeals();

        lvMyMeals = view.findViewById(R.id.lvMyMeals);
        myMealsListAdapter = new MyMealsListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, myMeals);
        lvMyMeals.setAdapter(myMealsListAdapter);

        lvMyMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Wybrales = " + myMeals.get(position).getName(), Toast.LENGTH_SHORT).show();

                View alertView = getLayoutInflater().inflate(R.layout.dialog_my_meal_details, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                TextView tvName = alertView.findViewById(R.id.tvName);
                TextView tvTotalMealWeight = alertView.findViewById(R.id.tvTotalMealWeight);
                TextView tvCarbohydrates = alertView.findViewById(R.id.tvCarbohydrates);
                TextView tvProtein = alertView.findViewById(R.id.tvProtein);
                TextView tvFat = alertView.findViewById(R.id.tvFat);
                TextView tvCalories = alertView.findViewById(R.id.tvCalories);
                TextView tvIngredientAmount = alertView.findViewById(R.id.tvIngredientAmount);
                ListView lvIngredients = alertView.findViewById(R.id.lvIngredients);

                DecimalFormat decimalFormat = new DecimalFormat("0.0");

                tvName.setText(myMeals.get(position).getName());

                String tempString = String.valueOf(myMeals.get(position).getTotalWeight()) + " g";
                tvTotalMealWeight.setText(tempString);

                tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getCarbohydrates())) + " g";
                tvCarbohydrates.setText(tempString);

                tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getProtein())) + " g";
                tvProtein.setText(tempString);

                tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getFat())) + " g";
                tvFat.setText(tempString);

                tempString = String.valueOf(myMeals.get(position).getCalories()) + " g";
                tvCalories.setText(tempString);

                tempString = String.valueOf(myMeals.get(position).getIngredientList().size());
                tvIngredientAmount.setText(tempString);

                final Meal tempMeal = myMeals.get(position);

                ArrayAdapter<Ingredient> mealIngredientsAdapter = new ArrayAdapter<Ingredient>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, tempMeal.getIngredientList()) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text1 = view.findViewById(android.R.id.text1);
                        TextView text2 = view.findViewById(android.R.id.text2);

                        text1.setText(tempMeal.getIngredientList().get(position).getName());
                        String tempString = String.valueOf(tempMeal.getIngredientList().get(position).getWeight()) + " g";
                        text2.setText(tempString);

                        return view;
                    }
                };
                lvIngredients.setAdapter(mealIngredientsAdapter);

                builder.setView(alertView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        bAddMyMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMyMealActivity1();
            }
        });

        llMyMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hiddenMyMeals){
                    lvMyMeals.setVisibility(View.VISIBLE);
                    hiddenMyMeals = false;
                    imArrowMeals.setImageResource(R.drawable.arrow_down);
                } else {
                    lvMyMeals.setVisibility(View.GONE);
                    hiddenMyMeals = true;
                    imArrowMeals.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        return view;
    }

    private void openAddMyMealActivity1() {
        Intent openAddMyMealActivity1 = new Intent(getContext(), AddMyMealActivity1.class);
        openAddMyMealActivity1.putExtra("user", user);
        startActivity(openAddMyMealActivity1);
    }

    private void getMyMeals() {
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
                                myMeals.add(tempMeal);
                            } else {
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"),
                                        row.getDouble("Carbohydrates"), row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                myMeals.get(mealPosition).addIngredientToList(tempIngredient);
                            }
                        }
                        if (myMeals.size() == 0)
                            tvMyMealsEmpty.setVisibility(View.VISIBLE);
                        pbLoadingMeals.setVisibility(View.GONE);
                        myMealsListAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Pobrano posiłki", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), "Blad podczas pobierania posiłków", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Blad podczas pobierania posiłków " + e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Blad podczas pobierania posiłków " + error.toString(), Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i < myMeals.size(); i++) {
            if (myMeals.get(i).getID() == mealId) {
                return i;
            }
        }
        return -1;
    }

    class MyMealsListAdapter extends ArrayAdapter<Meal> {

        public MyMealsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Meal> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_show_foodsystem, parent, false);

            TextView tvName = convertView.findViewById(R.id.tvName);
            TextView tvWeight = convertView.findViewById(R.id.tvWeight);
            TextView tvCarbohydrates = convertView.findViewById(R.id.tvCarbohydrates);
            TextView tvProtein = convertView.findViewById(R.id.tvProtein);
            TextView tvFat = convertView.findViewById(R.id.tvFat);
            TextView tvCalories = convertView.findViewById(R.id.tvCalories);

            DecimalFormat decimalFormat = new DecimalFormat("0.0");

            tvName.setText(myMeals.get(position).getName());

            String tempString = String.valueOf(myMeals.get(position).getTotalWeight()) + " g";
            tvWeight.setText(tempString);

            tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getCarbohydrates())) + " g";
            tvCarbohydrates.setText(tempString);

            tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getProtein())) + " g";
            tvProtein.setText(tempString);

            tempString = String.valueOf(decimalFormat.format(myMeals.get(position).getFat())) + " g";
            tvFat.setText(tempString);

            tempString = String.valueOf(myMeals.get(position).getCalories()) + " g";
            tvCalories.setText(tempString);

            return convertView;
        }
    }

}
