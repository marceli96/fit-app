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
import org.w3c.dom.Text;

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

        foodSystemListBreakfastAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListBreakfast, 0);
        foodSystemListSecondBreakfastAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListSecondBreakfast, 1);
        foodSystemListLunchAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListLunch, 2);
        foodSystemListDinnerAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListDinner, 3);
        foodSystemListSnackAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListSnack, 4);
        foodSystemListSupperAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListSupper, 5);

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

        private int mealTime;
        private ArrayList<FoodSystem> temp;

        public FoodSystemListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<FoodSystem> objects, int mealTime) {
            super(context, resource, objects);
            this.mealTime = mealTime;
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

            switch (mealTime){
                case 0:
                    temp = foodSystemListBreakfast;
                    break;
                case 1:
                    temp = foodSystemListSecondBreakfast;
                    break;
                case 2:
                    temp = foodSystemListLunch;
                    break;
                case 3:
                    temp = foodSystemListDinner;
                    break;
                case 4:
                    temp = foodSystemListSnack;
                    break;
                case 5:
                    temp = foodSystemListSupper;
                    break;
            }

            String tempString;
            tvName.setText(temp.get(position).getName());


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
                        Log.d("TESTOWANIE", "Dlugosc response = " + jsonResponse.length());
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject temp = jsonResponse.getJSONObject(String.valueOf(i));
                            if (temp.getString("type").equals("meal")) {
                                int mealID = temp.getInt("ID_MyMeal");
                                int mealPosition;
                                Meal tempMeal;
                                Ingredient tempIngredient;
                                switch (temp.getInt("MealTime")) {
                                    case 0:
                                        mealPosition = checkMealPositionInList(mealID, foodSystemListBreakfast);
                                        if (mealPosition == -1) {
                                            tempMeal = new Meal(temp.getInt("ID_MyMeal"), temp.getString("MealName"));
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                            foodSystemListBreakfast.add(tempMeal);
                                        } else {
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal = (Meal) foodSystemListBreakfast.get(mealPosition);
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                        }
                                        break;
                                    case 1:
                                        mealPosition = checkMealPositionInList(mealID, foodSystemListSecondBreakfast);
                                        if (mealPosition == -1) {
                                            tempMeal = new Meal(temp.getInt("ID_MyMeal"), temp.getString("MealName"));
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                            foodSystemListSecondBreakfast.add(tempMeal);
                                        } else {
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal = (Meal) foodSystemListSecondBreakfast.get(mealPosition);
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                        }
                                        break;
                                    case 2:
                                        mealPosition = checkMealPositionInList(mealID, foodSystemListLunch);
                                        if (mealPosition == -1) {
                                            tempMeal = new Meal(temp.getInt("ID_MyMeal"), temp.getString("MealName"));
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                            foodSystemListLunch.add(tempMeal);
                                        } else {
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal = (Meal) foodSystemListLunch.get(mealPosition);
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                        }
                                        break;
                                    case 3:
                                        mealPosition = checkMealPositionInList(mealID, foodSystemListDinner);
                                        if (mealPosition == -1) {
                                            tempMeal = new Meal(temp.getInt("ID_MyMeal"), temp.getString("MealName"));
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                            foodSystemListDinner.add(tempMeal);
                                        } else {
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal = (Meal) foodSystemListDinner.get(mealPosition);
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                        }
                                        break;
                                    case 4:
                                        mealPosition = checkMealPositionInList(mealID, foodSystemListSnack);
                                        if (mealPosition == -1) {
                                            tempMeal = new Meal(temp.getInt("ID_MyMeal"), temp.getString("MealName"));
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                            foodSystemListSnack.add(tempMeal);
                                        } else {
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal = (Meal) foodSystemListSnack.get(mealPosition);
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                        }
                                        break;
                                    case 5:
                                        mealPosition = checkMealPositionInList(mealID, foodSystemListSupper);
                                        if (mealPosition == -1) {
                                            tempMeal = new Meal(temp.getInt("ID_MyMeal"), temp.getString("MealName"));
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                            foodSystemListSupper.add(tempMeal);
                                        } else {
                                            tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                    temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                            tempMeal = (Meal) foodSystemListSupper.get(mealPosition);
                                            tempMeal.getIngredientList().add(tempIngredient);
                                            tempMeal.getIngredientWeightList().add(temp.getInt("IngredientWeight"));
                                        }
                                        break;
                                }
                            } else {
                                Ingredient tempIngredient;
                                switch (temp.getInt("MealTime")) {
                                    case 0:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListBreakfast.add(tempIngredient);
                                        break;
                                    case 1:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListSecondBreakfast.add(tempIngredient);
                                        break;
                                    case 2:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListLunch.add(tempIngredient);
                                        break;
                                    case 3:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListDinner.add(tempIngredient);
                                        break;
                                    case 4:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListSnack.add(tempIngredient);
                                        break;
                                    case 5:
                                        tempIngredient = new Ingredient(temp.getInt("ID_Ingredient"), temp.getString("IngredientName"),
                                                temp.getDouble("Carbohydrates"), temp.getDouble("Protein"), temp.getDouble("Fat"), temp.getInt("Calories"));
                                        foodSystemListSupper.add(tempIngredient);
                                        break;
                                }
                            }
                        }
                        foodSystemListBreakfastAdapter.notifyDataSetChanged();
                        foodSystemListSecondBreakfastAdapter.notifyDataSetChanged();
                        foodSystemListLunchAdapter.notifyDataSetChanged();
                        foodSystemListDinnerAdapter.notifyDataSetChanged();
                        foodSystemListSnackAdapter.notifyDataSetChanged();
                        foodSystemListSupperAdapter.notifyDataSetChanged();

                        Log.d("TESTOWANIE", "Liczba elementow w tablicy sniadanie = " + foodSystemListBreakfast.size());
                        for (int i = 0; i < foodSystemListBreakfast.size(); i++)
                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListBreakfast = " + foodSystemListBreakfast.get(i).getID());

                        Log.d("TESTOWANIE", "Liczba elementow w tablicy drugie sniadnaie = " + foodSystemListSecondBreakfast.size());
                        for (int i = 0; i < foodSystemListSecondBreakfast.size(); i++)
                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListSecondBreakfast = " + foodSystemListSecondBreakfast.get(i).getID());

                        Log.d("TESTOWANIE", "Liczba elementow w tablicy lunch = " + foodSystemListLunch.size());
                        for (int i = 0; i < foodSystemListLunch.size(); i++)
                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListLunch = " + foodSystemListLunch.get(i).getID());

                        Log.d("TESTOWANIE", "Liczba elementow w tablicy obiad = " + foodSystemListDinner.size());
                        for (int i = 0; i < foodSystemListDinner.size(); i++)
                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListDinner = " + foodSystemListDinner.get(i).getID());


                        Log.d("TESTOWANIE", "Liczba elementow w tablicy przekaska = " + foodSystemListSnack.size());
                        for (int i = 0; i < foodSystemListSnack.size(); i++)
                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListSnack = " + foodSystemListSnack.get(i).getID());

                        Log.d("TESTOWANIE", "Liczba elementow w tablicy kolacja = " + foodSystemListSupper.size());
                        for (int i = 0; i < foodSystemListSupper.size(); i++)
                            Log.d("TESTOWANIE", "Element " + i + " w tablicy foodSystemListBreakfast = " + foodSystemListSupper.get(i).getID());



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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getFoodSystemFromDay");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("date", format.format(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public int checkMealPositionInList(int mealId, ArrayList<FoodSystem> foodSystemList) {
        Meal tempMeal;
        for (int i = 0; i < foodSystemList.size(); i++) {
            if (foodSystemList.get(i).getClass() == Meal.class) {
                tempMeal = (Meal) foodSystemList.get(i);
                if (tempMeal.getID() == mealId) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void addToFoodSystem(int mealTime) {
        Intent openAddToFoodSystemActivity = new Intent(this.getContext(), AddToFoodSystemActivity.class);
        openAddToFoodSystemActivity.putExtra("user", user);
        openAddToFoodSystemActivity.putExtra("mealTime", mealTime);
        startActivity(openAddToFoodSystemActivity);
    }
}
