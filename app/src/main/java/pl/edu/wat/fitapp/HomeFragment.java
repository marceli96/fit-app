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
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private TextView tvEatenCalories, tvReqCalories, tvEatenCarbohydrates, tvReqCarbohydrates, tvEatenProtein, tvReqProtein, tvEatenFat, tvReqFat, tvExerciseAmount;
    private ProgressBar pbCalories, pbCarbohydrates, pbProtein, pbFat, pbLoading;
    private NonScrollListView lvBreakfast, lvSecondBreakfast, lvLunch, lvDinner, lvSnack, lvSupper, lvTraining;
    private ImageView imArrowBreakfast, imArrowSecondBreakfast, imArrowLunch, imArrowDinner, imArrowSnack, imArrowSupper, imArrowTraining,
            imAddBreakfast, imAddSecondBreakfast, imAddLunch, imAddDinner, imAddSnack, imAddSupper, imAddTraining;
    private LinearLayout llProgressBars, llBreakfast, llSecondBreakfast, llLunch, llDinner, llSnack, llSupper, llTraining;
    private ArrayList<FoodSystem> foodSystemListBreakfast, foodSystemListSecondBreakfast, foodSystemListLunch, foodSystemListDinner,
            foodSystemListSnack, foodSystemListSupper;
    private ArrayList<TrainingSystem> trainingSystem;
    private FoodSystemListAdapter foodSystemListBreakfastAdapter, foodSystemListSecondBreakfastAdapter, foodSystemListLunchAdapter,
            foodSystemListDinnerAdapter, foodSystemListSnackAdapter, foodSystemListSupperAdapter;
    private TrainingSystemListAdapter trainingSystemListAdapter;

    private User user;

    private int minReqCarbohydrates = 0, maxReqCarbohydrates = 0, minReqProtein = 0, maxReqProtein = 0, minReqFat = 0, maxReqFat;
    private double eatenCarbohydrates, eatenProtein, eatenFat;
    private int eatenCalories;
    private boolean hiddenBreakfast = false, hiddenSecondBreakfast = false, hiddenLunch = false, hiddenDinner = false, hiddenSnack = false,
            hiddenSupper = false, hiddenTraining = false;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Strona główna");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        pbLoading = view.findViewById(R.id.pbLoading);
        pbCalories = view.findViewById(R.id.pbCalories);
        pbCarbohydrates = view.findViewById(R.id.pbCarbohydrates);
        pbProtein = view.findViewById(R.id.pbProtein);
        pbFat = view.findViewById(R.id.pbFat);

        tvEatenCalories = view.findViewById(R.id.tvEatenCalories);
        tvReqCalories = view.findViewById(R.id.tvReqCalories);
        tvEatenCarbohydrates = view.findViewById(R.id.tvEatenCarbohydrates);
        tvReqCarbohydrates = view.findViewById(R.id.tvReqCarbohydrates);
        tvEatenProtein = view.findViewById(R.id.tvEatenProtein);
        tvReqProtein = view.findViewById(R.id.tvReqProtein);
        tvEatenFat = view.findViewById(R.id.tvEatenFat);
        tvReqFat = view.findViewById(R.id.tvReqFat);
        tvExerciseAmount = view.findViewById(R.id.tvExerciseAmount);

        lvBreakfast = view.findViewById(R.id.lvBreakfast);
        lvSecondBreakfast = view.findViewById(R.id.lvSecondBreakfast);
        lvLunch = view.findViewById(R.id.lvLunch);
        lvDinner = view.findViewById(R.id.lvDinner);
        lvSnack = view.findViewById(R.id.lvSnack);
        lvSupper = view.findViewById(R.id.lvSupper);
        lvTraining = view.findViewById(R.id.lvTraining);

        imArrowBreakfast = view.findViewById(R.id.imArrowBreakfast);
        imArrowSecondBreakfast = view.findViewById(R.id.imArrowSecondBreakfast);
        imArrowLunch = view.findViewById(R.id.imArrowLunch);
        imArrowDinner = view.findViewById(R.id.imArrowDinner);
        imArrowSnack = view.findViewById(R.id.imArrowSnack);
        imArrowSupper = view.findViewById(R.id.imArrowSupper);
        imArrowTraining = view.findViewById(R.id.imArrowTraining);

        llProgressBars = view.findViewById(R.id.llProgressBars);
        llBreakfast = view.findViewById(R.id.llBreakfast);
        llSecondBreakfast = view.findViewById(R.id.llSecondBreakfast);
        llLunch = view.findViewById(R.id.llLunch);
        llDinner = view.findViewById(R.id.llDinner);
        llSnack = view.findViewById(R.id.llSnack);
        llSupper = view.findViewById(R.id.llSupper);
        llTraining = view.findViewById(R.id.llTraining);

        foodSystemListBreakfast = new ArrayList<>();
        foodSystemListSecondBreakfast = new ArrayList<>();
        foodSystemListLunch = new ArrayList<>();
        foodSystemListDinner = new ArrayList<>();
        foodSystemListSnack = new ArrayList<>();
        foodSystemListSupper = new ArrayList<>();
        trainingSystem = new ArrayList<>();
        getFoodSystem();
        getTrainingSystem();

        foodSystemListBreakfastAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListBreakfast);
        foodSystemListSecondBreakfastAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListSecondBreakfast);
        foodSystemListLunchAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListLunch);
        foodSystemListDinnerAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListDinner);
        foodSystemListSnackAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListSnack);
        foodSystemListSupperAdapter = new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemListSupper);
        trainingSystemListAdapter = new TrainingSystemListAdapter(getActivity(), R.layout.listview_adapter_show_trainingsystem, trainingSystem);


        lvBreakfast.setAdapter(foodSystemListBreakfastAdapter);
        lvSecondBreakfast.setAdapter(foodSystemListSecondBreakfastAdapter);
        lvLunch.setAdapter(foodSystemListLunchAdapter);
        lvDinner.setAdapter(foodSystemListDinnerAdapter);
        lvSnack.setAdapter(foodSystemListSnackAdapter);
        lvSupper.setAdapter(foodSystemListSupperAdapter);
        lvTraining.setAdapter(trainingSystemListAdapter);

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

        lvTraining.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trainingSystemOnClick(position);
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

        llBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenBreakfast) {
                    lvBreakfast.setVisibility(View.VISIBLE);
                    hiddenBreakfast = false;
                    imArrowBreakfast.setImageResource(R.drawable.arrow_down);
                } else {
                    lvBreakfast.setVisibility(View.GONE);
                    hiddenBreakfast = true;
                    imArrowBreakfast.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        llSecondBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenSecondBreakfast) {
                    lvSecondBreakfast.setVisibility(View.VISIBLE);
                    hiddenSecondBreakfast = false;
                    imArrowSecondBreakfast.setImageResource(R.drawable.arrow_down);
                } else {
                    lvSecondBreakfast.setVisibility(View.GONE);
                    hiddenSecondBreakfast = true;
                    imArrowSecondBreakfast.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        llLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenLunch) {
                    lvLunch.setVisibility(View.VISIBLE);
                    hiddenLunch = false;
                    imArrowLunch.setImageResource(R.drawable.arrow_down);
                } else {
                    lvSecondBreakfast.setVisibility(View.GONE);
                    hiddenLunch = true;
                    imArrowLunch.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        llDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenDinner) {
                    lvDinner.setVisibility(View.VISIBLE);
                    hiddenDinner = false;
                    imArrowDinner.setImageResource(R.drawable.arrow_down);
                } else {
                    lvDinner.setVisibility(View.GONE);
                    hiddenDinner = true;
                    imArrowDinner.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        llSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenSnack) {
                    lvSnack.setVisibility(View.VISIBLE);
                    hiddenSnack = false;
                    imArrowSnack.setImageResource(R.drawable.arrow_down);
                } else {
                    lvSnack.setVisibility(View.GONE);
                    hiddenSnack = true;
                    imArrowSnack.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        llSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenSupper) {
                    lvSupper.setVisibility(View.VISIBLE);
                    hiddenSupper = false;
                    imArrowSupper.setImageResource(R.drawable.arrow_down);
                } else {
                    lvSupper.setVisibility(View.GONE);
                    hiddenSupper = true;
                    imArrowSupper.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        llTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenTraining) {
                    lvTraining.setVisibility(View.VISIBLE);
                    hiddenTraining = false;
                    imArrowTraining.setImageResource(R.drawable.arrow_down);
                } else {
                    lvTraining.setVisibility(View.GONE);
                    hiddenTraining = true;
                    imArrowTraining.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        imAddBreakfast = view.findViewById(R.id.imAddBreakfast);
        imAddSecondBreakfast = view.findViewById(R.id.imAddSecondBreakfast);
        imAddLunch = view.findViewById(R.id.imAddLunch);
        imAddDinner = view.findViewById(R.id.imAddDinner);
        imAddSnack = view.findViewById(R.id.imAddSnack);
        imAddSupper = view.findViewById(R.id.imAddSupper);
        imAddTraining = view.findViewById(R.id.imAddTraining);

        imAddBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(0);
            }
        });

        imAddSecondBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(1);
            }
        });

        imAddLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(2);
            }
        });

        imAddDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(3);
            }
        });

        imAddSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(4);
            }
        });

        imAddSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(5);
            }
        });

        imAddTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToTrainingSystem();
            }
        });

        return view;
    }


    class FoodSystemListAdapter extends ArrayAdapter<FoodSystem> {

        ArrayList<FoodSystem> tempList;
        DecimalFormat format = new DecimalFormat("0.0");

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
            TextView tvCarbohydrates = convertView.findViewById(R.id.tvCarbohydrates);
            TextView tvProtein = convertView.findViewById(R.id.tvProtein);
            TextView tvFat = convertView.findViewById(R.id.tvFat);
            TextView tvCalories = convertView.findViewById(R.id.tvCalories);

            tvName.setText(tempList.get(position).getName());
            String tempString = String.valueOf(tempList.get(position).getWeight()) + " g";
            tvWeight.setText(tempString);
            if (tempList.get(position).getClass() == Ingredient.class) {
                tempString = String.valueOf(format.format(tempList.get(position).getCarbohydrates() * tempList.get(position).getWeight() / 100)) + " g";
                tvCarbohydrates.setText(tempString);
                tempString = String.valueOf(format.format(tempList.get(position).getProtein() * tempList.get(position).getWeight() / 100)) + " g";
                tvProtein.setText(tempString);
                tempString = String.valueOf(format.format(tempList.get(position).getFat() * tempList.get(position).getWeight() / 100)) + " g";
                tvFat.setText(tempString);
                tempString = String.valueOf(tempList.get(position).getCalories() * tempList.get(position).getWeight() / 100) + " kcal";
                tvCalories.setText(tempString);
            } else {
                Meal tempMeal = (Meal) tempList.get(position);
                tempString = String.valueOf(format.format(tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight())) + " g";
                tvCarbohydrates.setText(tempString);
                tempString = String.valueOf(format.format(tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight())) + " g";
                tvProtein.setText(tempString);
                tempString = String.valueOf(format.format(tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight())) + " g";
                tvFat.setText(tempString);
                tempString = String.valueOf(tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight()) + " kcal";
                tvCalories.setText(tempString);
            }

            return convertView;
        }
    }

    class IngredientsListAdapter extends ArrayAdapter<Ingredient> {
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

    class TrainingSystemListAdapter extends ArrayAdapter<TrainingSystem> {

        public TrainingSystemListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TrainingSystem> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_show_trainingsystem, parent, false);

            TextView tvName = convertView.findViewById(R.id.tvName);
            TextView tvExerciseAmount = convertView.findViewById(R.id.tvExerciseAmount);
            TextView tvSeries = convertView.findViewById(R.id.tvSeries);
            TextView tvRepetitions = convertView.findViewById(R.id.tvRepetitions);
            LinearLayout llTraining = convertView.findViewById(R.id.llTraining);
            LinearLayout llExercise = convertView.findViewById(R.id.llExercise);

            tvName.setText(trainingSystem.get(position).getName());

            if (trainingSystem.get(position).getClass() == Exercise.class) {
                Exercise tempExercise = (Exercise) trainingSystem.get(position);
                llTraining.setVisibility(View.GONE);
                tvSeries.setText(String.valueOf(tempExercise.getSeries()));
                tvRepetitions.setText(String.valueOf(tempExercise.getRepetitions()));
            } else {
                Training tempTraining = (Training) trainingSystem.get(position);
                llExercise.setVisibility(View.GONE);
                tvExerciseAmount.setText(String.valueOf(tempTraining.getExerciseList().size()));
            }

            return convertView;
        }
    }

    class ExercisesListAdapter extends ArrayAdapter<Exercise> {
        private ArrayList<Exercise> exerciseList;

        public ExercisesListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exercise> objects) {
            super(context, resource, objects);
            exerciseList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_exercise_with_series_repetitions_simple, parent, false);

            TextView tvExerciseName = convertView.findViewById(R.id.tvExerciseName);
            TextView tvSeries = convertView.findViewById(R.id.tvSeries);
            TextView tvRepetitions = convertView.findViewById(R.id.tvRepetitions);

            tvExerciseName.setText(exerciseList.get(position).getName());
            tvSeries.setText(String.valueOf(exerciseList.get(position).getSeries()));
            tvRepetitions.setText(String.valueOf(exerciseList.get(position).getRepetitions()));

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
                        pbLoading.setVisibility(View.GONE);
                        llProgressBars.setVisibility(View.VISIBLE);
                        llBreakfast.setVisibility(View.VISIBLE);
                        llSecondBreakfast.setVisibility(View.VISIBLE);
                        llLunch.setVisibility(View.VISIBLE);
                        llDinner.setVisibility(View.VISIBLE);
                        llSnack.setVisibility(View.VISIBLE);
                        llSupper.setVisibility(View.VISIBLE);

                        foodSystemListBreakfastAdapter.notifyDataSetChanged();
                        foodSystemListSecondBreakfastAdapter.notifyDataSetChanged();
                        foodSystemListLunchAdapter.notifyDataSetChanged();
                        foodSystemListDinnerAdapter.notifyDataSetChanged();
                        foodSystemListSnackAdapter.notifyDataSetChanged();
                        foodSystemListSupperAdapter.notifyDataSetChanged();
                        updateMacrosOnMealTimes();
                        updateEatenMacros();
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

    private int checkMealPositionInList(int mealId, int mealTime) {
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

    private void addIngredientToFoodSystemList(Ingredient ingredient, int mealTime) {
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

    private void addMealToFoodSystemList(Meal meal, int mealTime) {
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
        Intent openAddToFoodSystemActivity = new Intent(getContext(), AddToFoodSystemActivity.class);
        openAddToFoodSystemActivity.putExtra("user", user);
        openAddToFoodSystemActivity.putExtra("mealTime", mealTime);
        startActivity(openAddToFoodSystemActivity);
    }

    private void foodSystemOnClick(final int position, final int mealTime, final ArrayList<FoodSystem> tempList) {

        Toast.makeText(getActivity(), "Wybrales = " + tempList.get(position).getName(), Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View alertView = getLayoutInflater().inflate(R.layout.dialog_food_system_details, null);

        TextView tvName = alertView.findViewById(R.id.tvName);
        TextView tvIn100 = alertView.findViewById(R.id.tvIn100);
        LinearLayout llMeal = alertView.findViewById(R.id.llMeal);
        TextView tvTotalMealWeight = alertView.findViewById(R.id.tvTotalMealWeight);
        TextView tvCarbohydrates = alertView.findViewById(R.id.tvCarbohydrates);
        TextView tvProtein = alertView.findViewById(R.id.tvProtein);
        TextView tvFat = alertView.findViewById(R.id.tvFat);
        TextView tvCalories = alertView.findViewById(R.id.tvCalories);
        TextView tvIngredients = alertView.findViewById(R.id.tvIngredients);
        ListView lvIngredients = alertView.findViewById(R.id.lvIngredients);
        Button bDelete = alertView.findViewById(R.id.bDelete);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        tvName.setText(tempList.get(position).getName());

        String tempString = String.valueOf(decimalFormat.format(tempList.get(position).getCarbohydrates())) + " g";
        tvCarbohydrates.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(tempList.get(position).getProtein())) + " g";
        tvProtein.setText(tempString);

        tempString = String.valueOf(decimalFormat.format(tempList.get(position).getFat())) + " g";
        tvFat.setText(tempString);

        tempString = String.valueOf(tempList.get(position).getCalories()) + " g";
        tvCalories.setText(tempString);

        if (tempList.get(position).getClass() == Meal.class) {
            tvIn100.setVisibility(View.GONE);
            bDelete.setText("Usuń posiłek z sekcji 'Śniadanie'");
            final Meal tempMeal = (Meal) tempList.get(position);
            tempString = String.valueOf(tempMeal.getTotalWeight()) + " g";
            tvTotalMealWeight.setText(tempString);

            IngredientsListAdapter adapter = new IngredientsListAdapter(getActivity(), R.layout.listview_adapter_ingredient_with_weight_simple, tempMeal.getIngredientList());
            lvIngredients.setAdapter(adapter);
        } else {
            tvIngredients.setVisibility(View.GONE);
            llMeal.setVisibility(View.GONE);
            bDelete.setText("Usuń produkt z sekcji 'Śniadanie'");
            lvIngredients.setVisibility(View.GONE);
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
                        updateMacrosOnMealTimes();
                        updateEatenMacros();
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

    private void updateMacrosOnMealTimes() {
        // TODO Rozważyć update na jednym mealTime zamiast na wszystkich na raz (niepotrzebny udpate, kiedy usuwamy z jednej sekcji)
        DecimalFormat format = new DecimalFormat("0.0");

        View view = getView();

        eatenCalories = 0;
        eatenCarbohydrates = 0;
        eatenProtein = 0;
        eatenFat = 0;

        //Śniadanie
        TextView tvCarbohydratesBreakfast = view.findViewById(R.id.tvCarbohydratesBreakfast);
        TextView tvProteinBreakfast = view.findViewById(R.id.tvProteinBreakfast);
        TextView tvFatBreakfast = view.findViewById(R.id.tvFatBreakfast);
        TextView tvCaloriesBreakfast = view.findViewById(R.id.tvCaloriesBreakfast);

        double carbohydrates = 0, protein = 0, fat = 0;
        int calories = 0;

        for (int i = 0; i < foodSystemListBreakfast.size(); i++) {
            if (foodSystemListBreakfast.get(i).getClass() == Ingredient.class) {
                carbohydrates += foodSystemListBreakfast.get(i).getCarbohydrates() * foodSystemListBreakfast.get(i).getWeight() / 100;
                protein += foodSystemListBreakfast.get(i).getProtein() * foodSystemListBreakfast.get(i).getWeight() / 100;
                fat += foodSystemListBreakfast.get(i).getFat() * foodSystemListBreakfast.get(i).getWeight() / 100;
                calories += foodSystemListBreakfast.get(i).getCalories() * foodSystemListBreakfast.get(i).getWeight() / 100;
            } else {
                Meal tempMeal = (Meal) foodSystemListBreakfast.get(i);
                carbohydrates += tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                protein += tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                fat += tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                calories += tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }

        eatenCalories += calories;
        eatenCarbohydrates += carbohydrates;
        eatenProtein += protein;
        eatenFat += fat;

        String tempString = String.valueOf(format.format(carbohydrates)) + " g";
        tvCarbohydratesBreakfast.setText(tempString);
        tempString = String.valueOf(format.format(protein)) + " g";
        tvProteinBreakfast.setText(tempString);
        tempString = String.valueOf(format.format(fat)) + " g";
        tvFatBreakfast.setText(tempString);
        tempString = String.valueOf(calories) + " kcal";
        tvCaloriesBreakfast.setText(tempString);

        //Drugie śniadanie
        TextView tvCarbohydratesSecondBreakfast = view.findViewById(R.id.tvCarbohydratesSecondBreakfast);
        TextView tvProteinSecondBreakfast = view.findViewById(R.id.tvProteinSecondBreakfast);
        TextView tvFatSecondBreakfast = view.findViewById(R.id.tvFatSecondBreakfast);
        TextView tvCaloriesSecondBreakfast = view.findViewById(R.id.tvCaloriesSecondBreakfast);

        carbohydrates = 0;
        protein = 0;
        fat = 0;
        calories = 0;

        for (int i = 0; i < foodSystemListSecondBreakfast.size(); i++) {
            if (foodSystemListSecondBreakfast.get(i).getClass() == Ingredient.class) {
                carbohydrates += foodSystemListSecondBreakfast.get(i).getCarbohydrates() * foodSystemListSecondBreakfast.get(i).getWeight() / 100;
                protein += foodSystemListSecondBreakfast.get(i).getProtein() * foodSystemListSecondBreakfast.get(i).getWeight() / 100;
                fat += foodSystemListSecondBreakfast.get(i).getFat() * foodSystemListSecondBreakfast.get(i).getWeight() / 100;
                calories += foodSystemListSecondBreakfast.get(i).getCalories() * foodSystemListSecondBreakfast.get(i).getWeight() / 100;
            } else {
                Meal tempMeal = (Meal) foodSystemListSecondBreakfast.get(i);
                carbohydrates += tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                protein += tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                fat += tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                calories += tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }

        eatenCalories += calories;
        eatenCarbohydrates += carbohydrates;
        eatenProtein += protein;
        eatenFat += fat;

        tempString = String.valueOf(format.format(carbohydrates)) + " g";
        tvCarbohydratesSecondBreakfast.setText(tempString);
        tempString = String.valueOf(format.format(protein)) + " g";
        tvProteinSecondBreakfast.setText(tempString);
        tempString = String.valueOf(format.format(fat)) + " g";
        tvFatSecondBreakfast.setText(tempString);
        tempString = String.valueOf(calories) + " kcal";
        tvCaloriesSecondBreakfast.setText(tempString);

        // Lunch
        TextView tvCarbohydratesLunch = view.findViewById(R.id.tvCarbohydratesLunch);
        TextView tvProteinLunch = view.findViewById(R.id.tvProteinLunch);
        TextView tvFatLunch = view.findViewById(R.id.tvFatLunch);
        TextView tvCaloriesLunch = view.findViewById(R.id.tvCaloriesLunch);

        carbohydrates = 0;
        protein = 0;
        fat = 0;
        calories = 0;

        for (int i = 0; i < foodSystemListLunch.size(); i++) {
            if (foodSystemListLunch.get(i).getClass() == Ingredient.class) {
                carbohydrates += foodSystemListLunch.get(i).getCarbohydrates() * foodSystemListLunch.get(i).getWeight() / 100;
                protein += foodSystemListLunch.get(i).getProtein() * foodSystemListLunch.get(i).getWeight() / 100;
                fat += foodSystemListLunch.get(i).getFat() * foodSystemListLunch.get(i).getWeight() / 100;
                calories += foodSystemListLunch.get(i).getCalories() * foodSystemListLunch.get(i).getWeight() / 100;
            } else {
                Meal tempMeal = (Meal) foodSystemListLunch.get(i);
                carbohydrates += tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                protein += tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                fat += tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                calories += tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }

        eatenCalories += calories;
        eatenCarbohydrates += carbohydrates;
        eatenProtein += protein;
        eatenFat += fat;

        tempString = String.valueOf(format.format(carbohydrates)) + " g";
        tvCarbohydratesLunch.setText(tempString);
        tempString = String.valueOf(format.format(protein)) + " g";
        tvProteinLunch.setText(tempString);
        tempString = String.valueOf(format.format(fat)) + " g";
        tvFatLunch.setText(tempString);
        tempString = String.valueOf(calories) + " kcal";
        tvCaloriesLunch.setText(tempString);

        //Obiad
        TextView tvCarbohydratesDinner = view.findViewById(R.id.tvCarbohydratesDinner);
        TextView tvProteinDinner = view.findViewById(R.id.tvProteinDinner);
        TextView tvFatDinner = view.findViewById(R.id.tvFatDinner);
        TextView tvCaloriesDinner = view.findViewById(R.id.tvCaloriesDinner);

        carbohydrates = 0;
        protein = 0;
        fat = 0;
        calories = 0;

        for (int i = 0; i < foodSystemListDinner.size(); i++) {
            if (foodSystemListDinner.get(i).getClass() == Ingredient.class) {
                carbohydrates += foodSystemListDinner.get(i).getCarbohydrates() * foodSystemListDinner.get(i).getWeight() / 100;
                protein += foodSystemListDinner.get(i).getProtein() * foodSystemListDinner.get(i).getWeight() / 100;
                fat += foodSystemListDinner.get(i).getFat() * foodSystemListDinner.get(i).getWeight() / 100;
                calories += foodSystemListDinner.get(i).getCalories() * foodSystemListDinner.get(i).getWeight() / 100;
            } else {
                Meal tempMeal = (Meal) foodSystemListDinner.get(i);
                carbohydrates += tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                protein += tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                fat += tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                calories += tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }

        eatenCalories += calories;
        eatenCarbohydrates += carbohydrates;
        eatenProtein += protein;
        eatenFat += fat;

        tempString = String.valueOf(format.format(carbohydrates)) + " g";
        tvCarbohydratesDinner.setText(tempString);
        tempString = String.valueOf(format.format(protein)) + " g";
        tvProteinDinner.setText(tempString);
        tempString = String.valueOf(format.format(fat)) + " g";
        tvFatDinner.setText(tempString);
        tempString = String.valueOf(calories) + " kcal";
        tvCaloriesDinner.setText(tempString);

        //Przekąska
        TextView tvCarbohydratesSnack = view.findViewById(R.id.tvCarbohydratesSnack);
        TextView tvProteinSnack = view.findViewById(R.id.tvProteinSnack);
        TextView tvFatSnack = view.findViewById(R.id.tvFatSnack);
        TextView tvCaloriesSnack = view.findViewById(R.id.tvCaloriesSnack);

        carbohydrates = 0;
        protein = 0;
        fat = 0;
        calories = 0;

        for (int i = 0; i < foodSystemListSnack.size(); i++) {
            if (foodSystemListSnack.get(i).getClass() == Ingredient.class) {
                carbohydrates += foodSystemListSnack.get(i).getCarbohydrates() * foodSystemListSnack.get(i).getWeight() / 100;
                protein += foodSystemListSnack.get(i).getProtein() * foodSystemListSnack.get(i).getWeight() / 100;
                fat += foodSystemListSnack.get(i).getFat() * foodSystemListSnack.get(i).getWeight() / 100;
                calories += foodSystemListSnack.get(i).getCalories() * foodSystemListSnack.get(i).getWeight() / 100;
            } else {
                Meal tempMeal = (Meal) foodSystemListSnack.get(i);
                carbohydrates += tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                protein += tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                fat += tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                calories += tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }

        eatenCalories += calories;
        eatenCarbohydrates += carbohydrates;
        eatenProtein += protein;
        eatenFat += fat;

        tempString = String.valueOf(format.format(carbohydrates)) + " g";
        tvCarbohydratesSnack.setText(tempString);
        tempString = String.valueOf(format.format(protein)) + " g";
        tvProteinSnack.setText(tempString);
        tempString = String.valueOf(format.format(fat)) + " g";
        tvFatSnack.setText(tempString);
        tempString = String.valueOf(calories) + " kcal";
        tvCaloriesSnack.setText(tempString);

        //Kolacja
        TextView tvCarbohydratesSupper = view.findViewById(R.id.tvCarbohydratesSupper);
        TextView tvProteinSupper = view.findViewById(R.id.tvProteinSupper);
        TextView tvFatSupper = view.findViewById(R.id.tvFatSupper);
        TextView tvCaloriesSupper = view.findViewById(R.id.tvCaloriesSupper);

        carbohydrates = 0;
        protein = 0;
        fat = 0;
        calories = 0;

        for (int i = 0; i < foodSystemListSupper.size(); i++) {
            if (foodSystemListSupper.get(i).getClass() == Ingredient.class) {
                carbohydrates += foodSystemListSupper.get(i).getCarbohydrates() * foodSystemListSupper.get(i).getWeight() / 100;
                protein += foodSystemListSupper.get(i).getProtein() * foodSystemListSupper.get(i).getWeight() / 100;
                fat += foodSystemListSupper.get(i).getFat() * foodSystemListSupper.get(i).getWeight() / 100;
                calories += foodSystemListSupper.get(i).getCalories() * foodSystemListSupper.get(i).getWeight() / 100;
            } else {
                Meal tempMeal = (Meal) foodSystemListSupper.get(i);
                carbohydrates += tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                protein += tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                fat += tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                calories += tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight();
            }
        }

        eatenCalories += calories;
        eatenCarbohydrates += carbohydrates;
        eatenProtein += protein;
        eatenFat += fat;

        tempString = String.valueOf(format.format(carbohydrates)) + " g";
        tvCarbohydratesSupper.setText(tempString);
        tempString = String.valueOf(format.format(protein)) + " g";
        tvProteinSupper.setText(tempString);
        tempString = String.valueOf(format.format(fat)) + " g";
        tvFatSupper.setText(tempString);
        tempString = String.valueOf(calories) + " kcal";
        tvCaloriesSupper.setText(tempString);
    }

    private void updateEatenMacros() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        tvEatenCalories.setText(String.valueOf(eatenCalories));
        tvEatenCarbohydrates.setText(String.valueOf(decimalFormat.format(eatenCarbohydrates)));
        tvEatenProtein.setText(String.valueOf(decimalFormat.format(eatenProtein)));
        tvEatenFat.setText(String.valueOf(decimalFormat.format(eatenFat)));


        pbCalories.setProgress(eatenCalories);
        pbCarbohydrates.setProgress((int) Math.round(eatenCarbohydrates));
        pbProtein.setProgress((int) Math.round(eatenProtein));
        pbFat.setProgress((int) Math.round(eatenFat));

        // TODO zmiana na czerwony kolor (napisy + ewentualnie progres bary) kiedy przekroczymy zadane wartości

    }


    private void getTrainingSystem() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            if (row.getString("type").equals("training")) {
                                int trainingPosition = checkTrainingPositionInList(row.getInt("ID_MyTraining"));
                                if (trainingPosition == -1) {
                                    Training tempTraining = new Training(row.getInt("ID_MyTraining"), row.getString("TrainingName"));
                                    Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                    tempExercise.setSeries(row.getInt("Series"));
                                    tempExercise.setRepetitions(row.getInt("Repetitions"));
                                    tempTraining.addExerciseToList(tempExercise);
                                    trainingSystem.add(tempTraining);
                                } else {
                                    Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                    tempExercise.setSeries(row.getInt("Series"));
                                    tempExercise.setRepetitions(row.getInt("Repetitions"));
                                    updateTrainingInTrainingSystemList(trainingPosition, tempExercise);
                                }
                            } else {
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("Series"));
                                tempExercise.setRepetitions(row.getInt("Repetitions"));
                                trainingSystem.add(tempExercise);
                            }
                        }
                        llTraining.setVisibility(View.VISIBLE);
                        trainingSystemListAdapter.notifyDataSetChanged();
                        updateExerciseAmount();
                    } else
                        Toast.makeText(getActivity(), "Błąd podczas pobieranie do TrainingSystem", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd podczas pobieranie do TrainingSystem " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd podczas pobieranie do TrainingSystem " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getTrainingSystemFromDay");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("date", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void updateTrainingInTrainingSystemList(int trainingPosition, Exercise tempExercise) {
        Training tempTraining = (Training) trainingSystem.get(trainingPosition);
        tempTraining.addExerciseToList(tempExercise);
    }

    private int checkTrainingPositionInList(int trainingId) {
        Training tempTraining;
        for (int i = 0; i < trainingSystem.size(); i++) {
            if (trainingSystem.get(i).getClass() == Training.class) {
                tempTraining = (Training) trainingSystem.get(i);
                if (tempTraining.getID() == trainingId)
                    return i;
            }
        }
        return -1;
    }

    private void addToTrainingSystem() {
        Intent openAddToTrainingSystemActivity = new Intent(getContext(), AddToTrainingSystemActivity.class);
        openAddToTrainingSystemActivity.putExtra("user", user);
        startActivity(openAddToTrainingSystemActivity);
    }

    private void trainingSystemOnClick(final int position) {
        Toast.makeText(getActivity(), "Wybrales = " + trainingSystem.get(position).getName(), Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View alertView = getLayoutInflater().inflate(R.layout.dialog_training_system_details, null);

        TextView tvName = alertView.findViewById(R.id.tvName);
        TextView tvSeries = alertView.findViewById(R.id.tvSeries);
        TextView tvRepetitions = alertView.findViewById(R.id.tvRepetitions);
        TextView tvExerciseAmount = alertView.findViewById(R.id.tvExerciseAmount);
        ListView lvExercises = alertView.findViewById(R.id.lvExercises);
        Button bDelete = alertView.findViewById(R.id.bDelete);
        LinearLayout llExercise = alertView.findViewById(R.id.llExercise);
        LinearLayout llTraining = alertView.findViewById(R.id.llTraining);

        tvName.setText(trainingSystem.get(position).getName());

        if (trainingSystem.get(position).getClass() == Exercise.class) {
            Exercise tempExercise = (Exercise) trainingSystem.get(position);
            llTraining.setVisibility(View.GONE);
            tvSeries.setText(String.valueOf(tempExercise.getSeries()));
            tvRepetitions.setText(String.valueOf(tempExercise.getRepetitions()));
            bDelete.setText("Usuń ćwiczenie");
        } else {
            Training tempTraining = (Training) trainingSystem.get(position);
            llExercise.setVisibility(View.GONE);
            tvExerciseAmount.setText(String.valueOf(tempTraining.getExerciseList().size()));
            bDelete.setText("Usuń trening");

            ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(getActivity(), R.layout.listview_adapter_exercise_with_series_repetitions_simple, tempTraining.getExerciseList());
            lvExercises.setAdapter(exercisesListAdapter);
        }

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromTrainingSystem(trainingSystem.get(position), user.getUserID());
                dialog.dismiss();
            }
        });
    }

    private void deleteFromTrainingSystem(final TrainingSystem training, final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(getActivity(), "Pomyślnie usunięto", Toast.LENGTH_SHORT).show();
                        trainingSystem.remove(training);
                        trainingSystemListAdapter.notifyDataSetChanged();
                        updateExerciseAmount();
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
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("userId", String.valueOf(userID));
                params.put("date", dateFormat.format(date));
                if (training.getClass() == Exercise.class) {
                    params.put("operation", "deleteExerciseFromTrainingSystem");
                    params.put("exerciseId", String.valueOf(training.getID()));

                } else {
                    params.put("operation", "deleteTrainingFromTrainingSystem");
                    params.put("myTrainingId", String.valueOf(training.getID()));
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void updateExerciseAmount() {
        int exerciseAmount = 0;
        for (int i = 0; i < trainingSystem.size(); i++) {
            if (trainingSystem.get(i).getClass() == Exercise.class)
                exerciseAmount++;
            else {
                Training tempTraining = (Training) trainingSystem.get(i);
                exerciseAmount += tempTraining.getExerciseList().size();
            }
        }
        tvExerciseAmount.setText(String.valueOf(exerciseAmount));
    }
}
