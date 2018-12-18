package pl.edu.wat.fitapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button bBreakfast, bSecondBreakfast, bLunch, bDinner, bSnack, bSupper;
    private ListView lvBreakfast;
    private ListView lvSecondBreakfast;
    private ListView lvLunch;
    private ListView lvDinner;
    private ListView lvSnack;
    private ListView lvSupper;

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private ArrayList<FoodSystem> foodSystemListBreakfast;
    private ArrayList<FoodSystem> foodSystemListSecondBreakfast;
    private ArrayList<FoodSystem> foodSystemListLunch;
    private ArrayList<FoodSystem> foodSystemListDinner;
    private ArrayList<FoodSystem> foodSystemListSnack;
    private ArrayList<FoodSystem> foodSystemListSupper;

    private FoodSystemListAdapter foodSystemListBreakfastAdapter;
    private FoodSystemListAdapter foodSystemListSecondBreakfastAdapter;
    private FoodSystemListAdapter foodSystemListLunchAdapter;
    private FoodSystemListAdapter foodSystemListDinnerAdapter;
    private FoodSystemListAdapter foodSystemListSnackAdapter;
    private FoodSystemListAdapter foodSystemListSupperAdapter;

    private User user;

    public HomeFragment() {

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        user = (User) args.getSerializable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        getArguments nie chce zadziałać
//        user = (User) getArguments().getSerializable("user");
        user = (User) getActivity().getIntent().getSerializableExtra("user");

        foodSystemListBreakfast = new ArrayList<>();
        foodSystemListSecondBreakfast = new ArrayList<>();
        foodSystemListLunch = new ArrayList<>();
        foodSystemListDinner = new ArrayList<>();
        foodSystemListSnack = new ArrayList<>();
        foodSystemListSupper = new ArrayList<>();
        getFoodSystem();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        lvBreakfast = view.findViewById(R.id.lvBreakfast);
        lvSecondBreakfast = view.findViewById(R.id.lvSecondBreakfast);
        lvLunch = view.findViewById(R.id.lvLunch);
        lvDinner = view.findViewById(R.id.lvDinner);
        lvSnack = view.findViewById(R.id.lvSnack);
        lvSupper = view.findViewById(R.id.lvSupper);

        foodSystemListBreakfastAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListBreakfast);
        foodSystemListSecondBreakfastAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListSecondBreakfast);
        foodSystemListLunchAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListLunch);
        foodSystemListDinnerAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListDinner);
        foodSystemListSnackAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListSnack);
        foodSystemListSupperAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListSupper);

        lvBreakfast.setAdapter(foodSystemListBreakfastAdapter);
        lvSecondBreakfast.setAdapter(foodSystemListSecondBreakfastAdapter);
        lvLunch.setAdapter(foodSystemListLunchAdapter);
        lvDinner.setAdapter(foodSystemListDinnerAdapter);
        lvSnack.setAdapter(foodSystemListSnackAdapter);
        lvSupper.setAdapter(foodSystemListSupperAdapter);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        bBreakfast = getView().findViewById(R.id.bBreakfast);
        bSecondBreakfast = getView().findViewById(R.id.bSecondBreakfast);
        bLunch = getView().findViewById(R.id.bLunch);
        bDinner = getView().findViewById(R.id.bDinner);
        bSnack = getView().findViewById(R.id.bSnack);
        bSupper = getView().findViewById(R.id.bSupper);

        bBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(0);
            }
        });

        bSecondBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(1);
            }
        });

        bLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(2);
            }
        });

        bDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(3);
            }
        });

        bSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(4);
            }
        });

        bSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(5);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    class FoodSystemListAdapter extends ArrayAdapter<FoodSystem> {

        ArrayList<FoodSystem> tempList;

        public FoodSystemListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FoodSystem> objects) {
            super(context, resource, objects);
            tempList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_show_foodsystem, parent, false);

            TextView tvName = convertView.findViewById(R.id.tvName);
            TextView tvCarbohydrates = convertView.findViewById(R.id.tvCarbohydrates);
            TextView tvProtein = convertView.findViewById(R.id.tvProtein);
            TextView tvFat = convertView.findViewById(R.id.tvFat);
            TextView tvCalories = convertView.findViewById(R.id.tvCalories);

            DecimalFormat decimalFormat = new DecimalFormat("#0.0");


            String tempString;
            tvName.setText(tempList.get(position).getName());

            Ingredient tempIngredient;
            Meal tempMeal;
            if (tempList.get(position).getClass() == Ingredient.class) {
                tempIngredient = (Ingredient) tempList.get(position);

                tempString = String.valueOf(decimalFormat.format(tempIngredient.getCarbohydrates())) + " g";
                tvCarbohydrates.setText(tempString);

                tempString = String.valueOf(decimalFormat.format(tempIngredient.getProtein())) + " g";
                tvProtein.setText(tempString);

                tempString = String.valueOf(decimalFormat.format(tempIngredient.getFat())) + " g";
                tvFat.setText(tempString);

                tempString = String.valueOf(decimalFormat.format(tempIngredient.getCalories())) + " kcal";
                tvCalories.setText(tempString);
            }

            if (tempList.get(position).getClass() == Meal.class) {
                tempMeal = (Meal) tempList.get(position);

                tempString = String.valueOf(decimalFormat.format(tempMeal.getCarbohydrates())) + " g";
                tvCarbohydrates.setText(tempString);

                tempString = String.valueOf(decimalFormat.format(tempMeal.getProtein())) + " g";
                tvProtein.setText(tempString);

                tempString = String.valueOf(decimalFormat.format(tempMeal.getFat())) + " g";
                tvFat.setText(tempString);

                tempString = String.valueOf(decimalFormat.format(tempMeal.getCalories())) + " kcal";
                tvCalories.setText(tempString);
            }
            return convertView;
        }
    }

    private void getFoodSystem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            if (row.getString("type").equals("meal")) {
                                int mealPosition = checkMealPositionInList(row.getInt("ID_MyMeal"), row.getInt("MealTime"));
                                Meal tempMeal;
                                if (mealPosition == -1) {
                                    tempMeal = new Meal(row.getInt("ID_MyMeal"), row.getString("MealName"));
                                    tempMeal.addIngriedientToList(new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories")), row.getInt("IngredientWeight"));
                                    addMealToFoodSystemList(tempMeal, row.getInt("MealTime"));
                                } else {
                                    updateMealInFoodSystemList(mealPosition, new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories")), row.getInt("IngredientWeight"), row.getInt("MealTime"));
                                }
                            } else {
                                addIngredientToFoodSystemList(new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                        row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories")), row.getInt("MealTime"));
                            }
                        }
                        foodSystemListBreakfastAdapter.notifyDataSetChanged();
                        foodSystemListSecondBreakfastAdapter.notifyDataSetChanged();
                        foodSystemListLunchAdapter.notifyDataSetChanged();
                        foodSystemListDinnerAdapter.notifyDataSetChanged();
                        foodSystemListSnackAdapter.notifyDataSetChanged();
                        foodSystemListSupperAdapter.notifyDataSetChanged();

//                        Log.d("TESTOWANIE", "Liczba elementow w tablicy sniadanie = " + foodSystemListBreakfast.size());
//                        for (int i = 0; i < foodSystemListBreakfast.size(); i++)
//                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListBreakfast = " + foodSystemListBreakfast.get(i).getID());
//
//                        Log.d("TESTOWANIE", "Liczba elementow w tablicy drugie sniadnaie = " + foodSystemListSecondBreakfast.size());
//                        for (int i = 0; i < foodSystemListSecondBreakfast.size(); i++)
//                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListSecondBreakfast = " + foodSystemListSecondBreakfast.get(i).getID());
//
//                        Log.d("TESTOWANIE", "Liczba elementow w tablicy lunch = " + foodSystemListLunch.size());
//                        for (int i = 0; i < foodSystemListLunch.size(); i++)
//                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListLunch = " + foodSystemListLunch.get(i).getID());
//
//                        Log.d("TESTOWANIE", "Liczba elementow w tablicy obiad = " + foodSystemListDinner.size());
//                        for (int i = 0; i < foodSystemListDinner.size(); i++)
//                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListDinner = " + foodSystemListDinner.get(i).getID());
//
//
//                        Log.d("TESTOWANIE", "Liczba elementow w tablicy przekaska = " + foodSystemListSnack.size());
//                        for (int i = 0; i < foodSystemListSnack.size(); i++)
//                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListSnack = " + foodSystemListSnack.get(i).getID());
//
//                        Log.d("TESTOWANIE", "Liczba elementow w tablicy kolacja = " + foodSystemListSupper.size());
//                        for (int i = 0; i < foodSystemListSupper.size(); i++)
//                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListBreakfast = " + foodSystemListSupper.get(i).getID());


                        Toast.makeText(getActivity(), "Pobrano do FoodSystem", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Błąd podczas pobierania do FoodSystem", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd podczas pobierania do FoodSystem " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd podczas pobierania do FoodSystem " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getFoodSystemFromDay");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("date", dateFormat.format(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public int checkMealPositionInList(int mealId, int mealTime) {
        Meal tempMeal;
        switch (mealTime) {
            case 0:
                for (int i = 0; i < foodSystemListBreakfast.size(); i++) {
                    if (foodSystemListBreakfast.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) foodSystemListBreakfast.get(i);
                        if (tempMeal.getID() == mealId) {
                            return i;
                        }
                    }
                }
                break;
            case 1:
                for (int i = 0; i < foodSystemListSecondBreakfast.size(); i++) {
                    if (foodSystemListSecondBreakfast.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) foodSystemListSecondBreakfast.get(i);
                        if (tempMeal.getID() == mealId) {
                            return i;
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < foodSystemListLunch.size(); i++) {
                    if (foodSystemListLunch.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) foodSystemListLunch.get(i);
                        if (tempMeal.getID() == mealId) {
                            return i;
                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < foodSystemListDinner.size(); i++) {
                    if (foodSystemListDinner.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) foodSystemListDinner.get(i);
                        if (tempMeal.getID() == mealId) {
                            return i;
                        }
                    }
                }
                break;
            case 4:
                for (int i = 0; i < foodSystemListSnack.size(); i++) {
                    if (foodSystemListSnack.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) foodSystemListSnack.get(i);
                        if (tempMeal.getID() == mealId) {
                            return i;
                        }
                    }
                }
                break;
            case 5:
                for (int i = 0; i < foodSystemListSupper.size(); i++) {
                    if (foodSystemListSupper.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) foodSystemListSupper.get(i);
                        if (tempMeal.getID() == mealId) {
                            return i;
                        }
                    }
                }
                break;
        }
        return -1;
    }

    public void addIngredientToFoodSystemList(Ingredient ingredient, int mealTime){
        switch (mealTime) {
            case 0:
                foodSystemListBreakfast.add(ingredient);
                break;
            case 1:
                foodSystemListSecondBreakfast.add(ingredient);
                break;
            case 2:
                foodSystemListLunch.add(ingredient);
                break;
            case 3:
                foodSystemListDinner.add(ingredient);
                break;
            case 4:
                foodSystemListSnack.add(ingredient);
                break;
            case 5:
                foodSystemListSupper.add(ingredient);
                break;
        }
    }

    public void addMealToFoodSystemList(Meal meal, int mealTime) {
        switch (mealTime) {
            case 0:
                foodSystemListBreakfast.add(meal);
                break;
            case 1:
                foodSystemListSecondBreakfast.add(meal);
                break;
            case 2:
                foodSystemListLunch.add(meal);
                break;
            case 3:
                foodSystemListDinner.add(meal);
                break;
            case 4:
                foodSystemListSnack.add(meal);
                break;
            case 5:
                foodSystemListSupper.add(meal);
                break;
        }
    }

    private void updateMealInFoodSystemList(int position, Ingredient ingredient, int weight, int mealTime) {
        Meal tempMeal;
        switch (mealTime) {
            case 0:
                tempMeal = (Meal) foodSystemListBreakfast.get(position);
                tempMeal.addIngriedientToList(ingredient, weight);
                break;
            case 1:
                tempMeal = (Meal) foodSystemListSecondBreakfast.get(position);
                tempMeal.addIngriedientToList(ingredient, weight);
                break;
            case 2:
                tempMeal = (Meal) foodSystemListLunch.get(position);
                tempMeal.addIngriedientToList(ingredient, weight);
                break;
            case 3:
                tempMeal = (Meal) foodSystemListDinner.get(position);
                tempMeal.addIngriedientToList(ingredient, weight);
                break;
            case 4:
                tempMeal = (Meal) foodSystemListSnack.get(position);
                tempMeal.addIngriedientToList(ingredient, weight);
                break;
            case 5:
                tempMeal = (Meal) foodSystemListSupper.get(position);
                tempMeal.addIngriedientToList(ingredient, weight);
                break;
        }
    }

    private void addToFoodSystem(int mealTime) {
        Intent openAddToFoodSystemActivity = new Intent(this.getContext(), AddToFoodSystemActivity.class);
        openAddToFoodSystemActivity.putExtra("user", user);
        openAddToFoodSystemActivity.putExtra("mealTime", mealTime);
        startActivity(openAddToFoodSystemActivity);
    }
}
