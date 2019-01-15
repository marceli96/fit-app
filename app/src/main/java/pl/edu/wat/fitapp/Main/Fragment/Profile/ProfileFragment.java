package pl.edu.wat.fitapp.Main.Fragment.Profile;


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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.JavaComponent.NonScrollListView;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private ImageView imAddMyMeal, imAddMyTraining, imArrowMeals, imArrowTrainings;
    private TextView tvMyMealsEmpty, tvMyTrainingsEmpty;
    private NonScrollListView lvMyMeals, lvMyTrainings;
    private LinearLayout llMyMeals, llMyTrainings;
    private ProgressBar pbLoadingMeals, pbLoadingTrainings;

    private ArrayList<Meal> myMeals;
    private ArrayList<Training> myTrainings;
    private MyMealsListAdapter myMealsListAdapter;
    private MyTrainingsListAdapter myTrainingsListAdapter;

    private User user;

    private boolean hiddenMyMeals = false, hiddenMyTrainings = false;

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Profil");

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        user = (User) getActivity().getIntent().getSerializableExtra("user");

        imAddMyMeal = view.findViewById(R.id.imAddMyMeal);
        imAddMyTraining = view.findViewById(R.id.imAddMyTraining);
        imArrowMeals = view.findViewById(R.id.imArrowMeals);
        imArrowTrainings = view.findViewById(R.id.imArrowTrainings);
        tvMyMealsEmpty = view.findViewById(R.id.tvMyMealsEmpty);
        tvMyTrainingsEmpty = view.findViewById(R.id.tvMyTrainingsEmpty);
        llMyMeals = view.findViewById(R.id.llMyMeals);
        llMyTrainings = view.findViewById(R.id.llMyTrainings);
        pbLoadingMeals = view.findViewById(R.id.pbLoadingMeals);
        pbLoadingTrainings = view.findViewById(R.id.pbLoadingTrainings);
        lvMyMeals = view.findViewById(R.id.lvMyMeals);
        lvMyTrainings = view.findViewById(R.id.lvMyTrainings);

        myMeals = new ArrayList<>();
        myTrainings = new ArrayList<>();
        getMyMeals();
        getMyTrainings();

        myMealsListAdapter = new MyMealsListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, myMeals);
        lvMyMeals.setAdapter(myMealsListAdapter);
        myTrainingsListAdapter = new MyTrainingsListAdapter(getActivity(), R.layout.listview_adapter_training, myTrainings);
        lvMyTrainings.setAdapter(myTrainingsListAdapter);

        lvMyMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                IngredientsListAdapter ingredientsListAdapter = new IngredientsListAdapter(getActivity(), R.layout.listview_adapter_ingredient_with_weight_simple, myMeals.get(position).getIngredientList());
                lvIngredients.setAdapter(ingredientsListAdapter);

                builder.setView(alertView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        lvMyTrainings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View alertView = getLayoutInflater().inflate(R.layout.dialog_my_training_details, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                TextView tvTrainingName = alertView.findViewById(R.id.tvTrainingName);
                TextView tvExerciseAmount = alertView.findViewById(R.id.tvExerciseAmount);
                ListView lvExercises = alertView.findViewById(R.id.lvExercises);

                tvTrainingName.setText(myTrainings.get(position).getName());
                tvExerciseAmount.setText(String.valueOf(myTrainings.get(position).getExerciseList().size()));

                ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(getActivity(), R.layout.listview_adapter_exercise_with_series_repetitions_simple, myTrainings.get(position).getExerciseList());
                lvExercises.setAdapter(exercisesListAdapter);

                builder.setView(alertView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        imAddMyMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMyMealActivity1();
            }
        });

        imAddMyTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMyTrainingActivity1();
            }
        });

        llMyMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenMyMeals) {
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

        llMyTrainings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hiddenMyTrainings) {
                    lvMyTrainings.setVisibility(View.VISIBLE);
                    hiddenMyTrainings = false;
                    imArrowTrainings.setImageResource(R.drawable.arrow_down);
                } else {
                    lvMyTrainings.setVisibility(View.GONE);
                    hiddenMyTrainings = true;
                    imArrowTrainings.setImageResource(R.drawable.arrow_up);
                }
            }
        });

        return view;
    }

    private void getMyTrainings() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            int trainingPosition = findTrainingInList(row.getInt("ID_MyTraining"));
                            if (trainingPosition == -1) {
                                Training tempTraining = new Training(row.getInt("ID_MyTraining"), row.getString("TrainingName"));
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("MySeries"));
                                tempExercise.setRepetitions(row.getInt("MyRepetitions"));
                                tempTraining.addExerciseToList(tempExercise);
                                myTrainings.add(tempTraining);
                            } else {
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("MySeries"));
                                tempExercise.setRepetitions(row.getInt("MyRepetitions"));
                                myTrainings.get(trainingPosition).addExerciseToList(tempExercise);
                            }
                        }
                        if (myTrainings.size() == 0)
                            tvMyTrainingsEmpty.setVisibility(View.VISIBLE);
                        pbLoadingTrainings.setVisibility(View.GONE);
                        myTrainingsListAdapter.notifyDataSetChanged();
                    } else
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getMyTrainings");
                params.put("userId", String.valueOf(user.getUserID()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private int findTrainingInList(int trainingId) {
        for (int i = 0; i < myTrainings.size(); i++) {
            if (myTrainings.get(i).getID() == trainingId)
                return i;
        }
        return -1;
    }

    private void openAddMyMealActivity1() {
        Intent openAddMyMealActivity1 = new Intent(getContext(), AddMyMealNameActivity.class);
        openAddMyMealActivity1.putExtra("user", user);
        startActivity(openAddMyMealActivity1);
    }

    private void openAddMyTrainingActivity1() {
        Intent openAddMyTrainingActivity1 = new Intent(getContext(), AddMyTrainingNameActivity.class);
        openAddMyTrainingActivity1.putExtra("user", user);
        startActivity(openAddMyTrainingActivity1);
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
                        final ScrollView scrollView = getView().findViewById(R.id.scrollView);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_UP);
                            }
                        });
                    } else
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() {
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

    class MyTrainingsListAdapter extends ArrayAdapter<Training> {

        public MyTrainingsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Training> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_training, parent, false);

            TextView tvTrainingName = convertView.findViewById(R.id.tvTrainingName);
            TextView tvExercisesAmount = convertView.findViewById(R.id.tvExercisesAmount);

            tvTrainingName.setText(myTrainings.get(position).getName());
            tvExercisesAmount.setText(String.valueOf(myTrainings.get(position).getExerciseList().size()));

            return convertView;
        }
    }

    class IngredientsListAdapter extends ArrayAdapter<Ingredient> {
        private ArrayList<Ingredient> ingredientList;

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
}
