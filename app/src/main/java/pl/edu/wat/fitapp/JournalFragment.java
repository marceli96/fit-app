package pl.edu.wat.fitapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JournalFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private BarChart chartCarloriesWeek, chartCarbohydratesWeek, chartProteinWeek, chartFatWeek;

    private ArrayList<FoodSystem> foodSystem0DayBefore, foodSystem1DayBefore, foodSystem2DayBefore, foodSystem3DayBefore, foodSystem4DayBefore, foodSystem5DayBefore, foodSystem6DayBefore;

    private User user;

    public JournalFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_journal, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        chartCarloriesWeek = view.findViewById(R.id.chartCarloriesWeek);
        chartCarbohydratesWeek = view.findViewById(R.id.chartCarbohydratesWeek);
        chartProteinWeek = view.findViewById(R.id.chartProteinWeek);
        chartFatWeek = view.findViewById(R.id.chartFatWeek);

        foodSystem0DayBefore = new ArrayList<>();
        foodSystem1DayBefore = new ArrayList<>();
        foodSystem2DayBefore = new ArrayList<>();
        foodSystem3DayBefore = new ArrayList<>();
        foodSystem4DayBefore = new ArrayList<>();
        foodSystem5DayBefore = new ArrayList<>();
        foodSystem6DayBefore = new ArrayList<>();

        getFoodSystemFromWeek();

//        chartCarloriesWeek.setDrawBarShadow(false);
//        chartCarloriesWeek.setDrawValueAboveBar(false);
//
//        ArrayList<BarEntry> barEntries = new ArrayList<>();
//
//        barEntries.add(new BarEntry(1, 40));
//        barEntries.add(new BarEntry(2, 50));
//        barEntries.add(new BarEntry(3, 30));
//        barEntries.add(new BarEntry(4, 70));
//
//        BarDataSet barDataSet = new BarDataSet(barEntries, "Test");
//
//        BarData data = new BarData(barDataSet);
//        chartCarloriesWeek.setData(data);

        return view;
    }

    private void getFoodSystemFromWeek() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = row.getString("FoodDate");
                            Date date = sdf.parse(dateString);
                            if (row.getString("type").equals("meal")) {
                                int mealPosition = checkMealPositionInList(row.getInt("ID_MyMeal"), date);
                                Meal tempMeal;
                                if (mealPosition == -1) {
                                    tempMeal = new Meal(row.getInt("ID_MyMeal"), row.getString("MealName"));
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    tempMeal.addIngredientToList(tempIngredient);
                                    tempMeal.setWeight(row.getInt("Weight"));
                                    addMealToFoodSystemList(tempMeal, date);
                                } else {
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    updateMealInFoodSystemList(mealPosition, tempIngredient, date);
                                }
                            } else {
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                        row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempIngredient.setWeight(row.getInt("Weight"));
                                addIngredientToFoodSystemList(tempIngredient, date);
                            }
                        }
                        Toast.makeText(getActivity(), "Pobrano do FoodSystem", Toast.LENGTH_SHORT).show();
                        Log.d("TESTOWANIE", "Today size = " + foodSystem0DayBefore.size());
                        Log.d("TESTOWANIE", "1day before size = " + foodSystem1DayBefore.size());
                        Log.d("TESTOWANIE", "2day before size = " + foodSystem2DayBefore.size());
                        Log.d("TESTOWANIE", "3day before size = " + foodSystem3DayBefore.size());
                        Log.d("TESTOWANIE", "4day before size = " + foodSystem4DayBefore.size());
                        Log.d("TESTOWANIE", "5day before size = " + foodSystem5DayBefore.size());
                        Log.d("TESTOWANIE", "6day before size = " + foodSystem6DayBefore.size());
                    } else
                        Toast.makeText(getActivity(), "Błąd podczas pobierania do FoodSystem", Toast.LENGTH_SHORT).show();
                    ;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd podczas pobierania do FoodSystem " + e.toString(), Toast.LENGTH_SHORT).show();
                    ;
                } catch (ParseException e) {
                    e.printStackTrace();
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
                params.put("operation", "getFoodSystemFromWeek");
                params.put("userId", String.valueOf(user.getUserID()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private int checkMealPositionInList(int mealId, Date date) {
        Meal tempMeal;

        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date1Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date2Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date3Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date4Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date5Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date6Before = calendar.getTime();

        if (date.toString().equals(today.toString())) {
            for (int i = 0; i < foodSystem0DayBefore.size(); i++) {
                if (foodSystem0DayBefore.get(i).getClass() == Meal.class) {
                    tempMeal = (Meal) foodSystem0DayBefore.get(i);
                    if (tempMeal.getID() == mealId) {
                        return i;
                    }
                }
            }
        } else if (date.toString().equals(date1Before.toString())) {
            for (int i = 0; i < foodSystem1DayBefore.size(); i++) {
                if (foodSystem1DayBefore.get(i).getClass() == Meal.class) {
                    tempMeal = (Meal) foodSystem1DayBefore.get(i);
                    if (tempMeal.getID() == mealId) {
                        return i;
                    }
                }
            }
        } else if (date.toString().equals(date2Before.toString())) {
            for (int i = 0; i < foodSystem2DayBefore.size(); i++) {
                if (foodSystem2DayBefore.get(i).getClass() == Meal.class) {
                    tempMeal = (Meal) foodSystem2DayBefore.get(i);
                    if (tempMeal.getID() == mealId) {
                        return i;
                    }
                }
            }
        } else if (date.toString().equals(date3Before.toString())) {
            for (int i = 0; i < foodSystem3DayBefore.size(); i++) {
                if (foodSystem3DayBefore.get(i).getClass() == Meal.class) {
                    tempMeal = (Meal) foodSystem3DayBefore.get(i);
                    if (tempMeal.getID() == mealId) {
                        return i;
                    }
                }
            }
        } else if (date.toString().equals(date4Before.toString())) {
            for (int i = 0; i < foodSystem4DayBefore.size(); i++) {
                if (foodSystem4DayBefore.get(i).getClass() == Meal.class) {
                    tempMeal = (Meal) foodSystem4DayBefore.get(i);
                    if (tempMeal.getID() == mealId) {
                        return i;
                    }
                }
            }
        } else if (date.toString().equals(date5Before.toString())) {
            for (int i = 0; i < foodSystem5DayBefore.size(); i++) {
                if (foodSystem5DayBefore.get(i).getClass() == Meal.class) {
                    tempMeal = (Meal) foodSystem5DayBefore.get(i);
                    if (tempMeal.getID() == mealId) {
                        return i;
                    }
                }
            }
        } else if (date.toString().equals(date6Before.toString())) {
            for (int i = 0; i < foodSystem6DayBefore.size(); i++) {
                if (foodSystem6DayBefore.get(i).getClass() == Meal.class) {
                    tempMeal = (Meal) foodSystem6DayBefore.get(i);
                    if (tempMeal.getID() == mealId) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private void addMealToFoodSystemList(Meal meal, Date date){
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date1Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date2Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date3Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date4Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date5Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date6Before = calendar.getTime();

        if (date.toString().equals(today.toString())) {
            foodSystem0DayBefore.add(meal);
        } else if (date.toString().equals(date1Before.toString())) {
            foodSystem1DayBefore.add(meal);
        } else if (date.toString().equals(date2Before.toString())) {
            foodSystem2DayBefore.add(meal);
        } else if (date.toString().equals(date3Before.toString())) {
            foodSystem3DayBefore.add(meal);
        } else if (date.toString().equals(date4Before.toString())) {
            foodSystem4DayBefore.add(meal);
        } else if (date.toString().equals(date5Before.toString())) {
            foodSystem5DayBefore.add(meal);
        } else if (date.toString().equals(date6Before.toString())) {
            foodSystem6DayBefore.add(meal);
        }
    }

    private void addIngredientToFoodSystemList(Ingredient ingredient, Date date) {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        today = calendar.getTime();

        // TODO poprawic skrypt, bo nie pobiera danych z dnia dzisiejszego
        Log.d("TESTOWANIE", "Today = " + today.toString());
        Log.d("TESTOWANIE", "Data paramater = " + date.toString());

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date1Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date2Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date3Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date4Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date5Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date6Before = calendar.getTime();

        if (date.toString().equals(today.toString())) {
            foodSystem0DayBefore.add(ingredient);
        } else if (date.toString().equals(date1Before.toString())) {
            foodSystem1DayBefore.add(ingredient);
        } else if (date.toString().equals(date2Before.toString())) {
            foodSystem2DayBefore.add(ingredient);
        } else if (date.toString().equals(date3Before.toString())) {
            foodSystem3DayBefore.add(ingredient);
        } else if (date.toString().equals(date4Before.toString())) {
            foodSystem4DayBefore.add(ingredient);
        } else if (date.toString().equals(date5Before.toString())) {
            foodSystem5DayBefore.add(ingredient);
        } else if (date.toString().equals(date6Before.toString())) {
            foodSystem6DayBefore.add(ingredient);
        }
    }

    private void updateMealInFoodSystemList(int position, Ingredient ingredient, Date date) {
        Meal tempMeal;

        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date1Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date2Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date3Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date4Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date5Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date6Before = calendar.getTime();

        if (date.toString().equals(today.toString())) {
            tempMeal = (Meal) foodSystem0DayBefore.get(position);
            tempMeal.addIngredientToList(ingredient);
        } else if (date.toString().equals(date1Before.toString())) {
            tempMeal = (Meal) foodSystem1DayBefore.get(position);
            tempMeal.addIngredientToList(ingredient);
        } else if (date.toString().equals(date2Before.toString())) {
            tempMeal = (Meal) foodSystem2DayBefore.get(position);
            tempMeal.addIngredientToList(ingredient);
        } else if (date.toString().equals(date3Before.toString())) {
            tempMeal = (Meal) foodSystem3DayBefore.get(position);
            tempMeal.addIngredientToList(ingredient);
        } else if (date.toString().equals(date4Before.toString())) {
            tempMeal = (Meal) foodSystem4DayBefore.get(position);
            tempMeal.addIngredientToList(ingredient);
        } else if (date.toString().equals(date5Before.toString())) {
            tempMeal = (Meal) foodSystem5DayBefore.get(position);
            tempMeal.addIngredientToList(ingredient);
        } else if (date.toString().equals(date6Before.toString())) {
            tempMeal = (Meal) foodSystem6DayBefore.get(position);
            tempMeal.addIngredientToList(ingredient);
        }
    }
}
