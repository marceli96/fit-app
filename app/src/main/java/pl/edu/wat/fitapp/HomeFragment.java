package pl.edu.wat.fitapp;


import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private NonScrollListView lvBreakfast;
    private NonScrollListView lvSecondBreakfast;
    private NonScrollListView lvLunch;
    private NonScrollListView lvDinner;
    private NonScrollListView lvSnack;
    private NonScrollListView lvSupper;

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
    public void onResume() {
        super.onResume();
        Log.d("TESTOWANIE", "Zadziałało onResume");
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

        lvBreakfast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClick(position, 0, foodSystemListBreakfast);
            }
        });

        lvSecondBreakfast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClick(position, 1, foodSystemListSecondBreakfast);
            }
        });

        lvLunch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClick(position, 2, foodSystemListLunch);
            }
        });

        lvDinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClick(position, 3, foodSystemListDinner);
            }
        });

        lvSnack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClick(position, 4, foodSystemListSnack);
            }
        });

        lvSupper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClick(position, 5, foodSystemListSupper);
            }
        });


        return view;
    }

    private void foodSystemOnClick(final int position, final int mealTime, final ArrayList<FoodSystem> tempList) {

        Toast.makeText(getActivity(), "Wybrales = " + tempList.get(position).getName(), Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View alertView = getLayoutInflater().inflate(R.layout.dialog_food_system_details, null);

        TextView tvName = alertView.findViewById(R.id.tvName);
        TextView tvCarbohydrates = alertView.findViewById(R.id.tvCarbohydrates);
        TextView tvProtein = alertView.findViewById(R.id.tvProtein);
        TextView tvFat = alertView.findViewById(R.id.tvFat);
        TextView tvCalories = alertView.findViewById(R.id.tvCalories);
        TextView tvIngredients = alertView.findViewById(R.id.tvIngredients);
        ListView lvIngredients = alertView.findViewById(R.id.lvIngredients);
        Button bDelete = alertView.findViewById(R.id.bDelete);

        tvName.setText(tempList.get(position).getName());
        DecimalFormat format = new DecimalFormat("#.0");

        String tempString = String.valueOf(format.format(tempList.get(position).getCarbohydrates())) + " g";
        tvCarbohydrates.setText(tempString);

        tempString = String.valueOf(format.format(tempList.get(position).getProtein())) + " g";
        tvProtein.setText(tempString);

        tempString = String.valueOf(format.format(tempList.get(position).getFat())) + " g";
        tvFat.setText(tempString);

        tempString = String.valueOf(tempList.get(position).getCalories()) + " g";
        tvCalories.setText(tempString);

        if (tempList.get(position).getClass() == Meal.class) {
            bDelete.setText("Usuń posiłek z sekcji 'Śniadanie'");
            final Meal tempMeal = (Meal) tempList.get(position);
            ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, tempMeal.getIngredientList()) {
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
            lvIngredients.setAdapter(adapter);
        } else {
            tvIngredients.setVisibility(View.GONE);
            bDelete.setText("Usuń produkt z sekcji 'Śniadanie'");
            lvIngredients.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 0));
        }

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromFoodSystem(tempList.get(position), user.getUserID(), mealTime, tempList.get(position).getWeight());
                dialog.dismiss();
            }
        });
    }

    private void deleteFromFoodSystem(final FoodSystem food, final int userId, final int mealTime, final int weight) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getActivity(), "Pomyślnie usunięto", Toast.LENGTH_SHORT).show();
                        switch (mealTime) {
                            case 0:
                                foodSystemListBreakfast.remove(food);
                                foodSystemListBreakfastAdapter.notifyDataSetChanged();
                                break;
                            case 1:
                                foodSystemListSecondBreakfast.remove(food);
                                foodSystemListSecondBreakfastAdapter.notifyDataSetChanged();
                                break;
                            case 2:
                                foodSystemListLunch.remove(food);
                                foodSystemListLunchAdapter.notifyDataSetChanged();
                                break;
                            case 3:
                                foodSystemListDinner.remove(food);
                                foodSystemListDinnerAdapter.notifyDataSetChanged();
                                break;
                            case 4:
                                foodSystemListSnack.remove(food);
                                foodSystemListSnackAdapter.notifyDataSetChanged();
                                break;
                            case 5:
                                foodSystemListSupper.remove(food);
                                foodSystemListSupperAdapter.notifyDataSetChanged();
                                break;
                        }
                    } else
                        Toast.makeText(getActivity(), "Błąd podczas usuwania", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd podczas usuwania " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd podczas usuwania " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (food.getClass() == Ingredient.class) {
                    params.put("operation", "deleteIngredientFromFoodSystem");
                    params.put("ingredientId", String.valueOf(food.getID()));
                } else {
                    params.put("operation", "deleteMealFromFoodSystem");
                    params.put("myMealId", String.valueOf(food.getID()));
                }

                params.put("userId", String.valueOf(userId));
                params.put("mealTime", String.valueOf(mealTime));
                params.put("weight", String.valueOf(weight));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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
            TextView tvWeight = convertView.findViewById(R.id.tvWeight);

            tvName.setText(tempList.get(position).getName());
            String tempString = String.valueOf(tempList.get(position).getWeight()) + " g";
            tvWeight.setText(tempString);

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
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    tempMeal.addIngredientToList(tempIngredient);
                                    tempMeal.setWeight(row.getInt("Weight"));
                                    addMealToFoodSystemList(tempMeal, row.getInt("MealTime"));
                                } else {
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    updateMealInFoodSystemList(mealPosition, tempIngredient, row.getInt("MealTime"));
                                }
                            } else {
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                        row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempIngredient.setWeight(row.getInt("Weight"));
                                addIngredientToFoodSystemList(tempIngredient, row.getInt("MealTime"));
                            }
                        }
                        foodSystemListBreakfastAdapter.notifyDataSetChanged();
                        foodSystemListSecondBreakfastAdapter.notifyDataSetChanged();
                        foodSystemListLunchAdapter.notifyDataSetChanged();
                        foodSystemListDinnerAdapter.notifyDataSetChanged();
                        foodSystemListSnackAdapter.notifyDataSetChanged();
                        foodSystemListSupperAdapter.notifyDataSetChanged();
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

    public void addIngredientToFoodSystemList(Ingredient ingredient, int mealTime) {
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

    private void updateMealInFoodSystemList(int position, Ingredient ingredient, int mealTime) {
        Meal tempMeal;
        switch (mealTime) {
            case 0:
                tempMeal = (Meal) foodSystemListBreakfast.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 1:
                tempMeal = (Meal) foodSystemListSecondBreakfast.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 2:
                tempMeal = (Meal) foodSystemListLunch.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 3:
                tempMeal = (Meal) foodSystemListDinner.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 4:
                tempMeal = (Meal) foodSystemListSnack.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 5:
                tempMeal = (Meal) foodSystemListSupper.get(position);
                tempMeal.addIngredientToList(ingredient);
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
