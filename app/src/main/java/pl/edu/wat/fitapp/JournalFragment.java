package pl.edu.wat.fitapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;


public class JournalFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private TextView tvDate, tvWeightDay;
    private LinearLayout llCaloriesWeekly, llCarbohydratesWeekly, llProteinWeekly, llFatWeekly, llWeightWeekly, llWeightDay;
    private BarChart chartCaloriesWeek, chartCarbohydratesWeek, chartProteinWeek, chartFatWeek, chartDaily, chartWeightWeek;
    private ProgressBar pbLoadingLastWeek, pbLoadingDaily;
    private CalendarView cvDate;

    private User user;
    private ArrayList<ArrayList<FoodSystem>> foodSystem1DayBefore, foodSystem2DayBefore, foodSystem3DayBefore,
            foodSystem4DayBefore, foodSystem5DayBefore, foodSystem6DayBefore, foodSystem7DayBefore, foodSystemDate;
    private ArrayList<String> days;
    private double[] weightWeek;
    private double weightDay;
    private int colorCalories, colorCarbohydrates, colorProtein, colorFat, colorWeight;
    private MyXAxisValueFormatter myXAxisValueFormatter;

    public JournalFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Dziennik");

        View view = getLayoutInflater().inflate(R.layout.fragment_journal, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        llCaloriesWeekly = view.findViewById(R.id.llCaloriesWeekly);
        llCarbohydratesWeekly = view.findViewById(R.id.llCarbohydratesWeekly);
        llProteinWeekly = view.findViewById(R.id.llProteinWeekly);
        llFatWeekly = view.findViewById(R.id.llFatWeekly);
        llWeightWeekly = view.findViewById(R.id.llWeightWeekly);
        llWeightDay = view.findViewById(R.id.llWeightDay);
        tvDate = view.findViewById(R.id.tvDate);
        tvWeightDay = view.findViewById(R.id.tvWeightDay);
        chartCaloriesWeek = view.findViewById(R.id.chartCarloriesWeek);
        chartCarbohydratesWeek = view.findViewById(R.id.chartCarbohydratesWeek);
        chartProteinWeek = view.findViewById(R.id.chartProteinWeek);
        chartFatWeek = view.findViewById(R.id.chartFatWeek);
        chartDaily = view.findViewById(R.id.chartDaily);
        chartWeightWeek = view.findViewById(R.id.chartWeightWeek);
        pbLoadingLastWeek = view.findViewById(R.id.pbLoadingLastWeek);
        pbLoadingDaily = view.findViewById(R.id.pbLoadingDaily);
        cvDate = view.findViewById(R.id.cvDate);

        colorCalories = Color.rgb(13, 202, 232);
        colorCarbohydrates = Color.rgb(67, 153, 70);
        colorProtein = Color.rgb(196, 124, 23);
        colorFat = Color.rgb(198, 188, 7);
        colorWeight = Color.rgb(237, 41, 57);

        initializeArrays();

        getFoodSystemFromWeek();
        getWeightFromWeek();

        cvDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                chartDaily.setVisibility(View.GONE);
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                tvDate.setText(date);
                llWeightDay.setVisibility(View.GONE);
                clearFoodSystemDate();
                getFoodSystemFromDay(date);
                getWeightFromDay(date);
            }
        });

        return view;
    }

    private void clearFoodSystemDate() {
        for (int i = 0; i < 6; i++)
            foodSystemDate.get(i).clear();
    }

    private void initializeArrays() {
        foodSystem1DayBefore = new ArrayList<>();
        foodSystem2DayBefore = new ArrayList<>();
        foodSystem3DayBefore = new ArrayList<>();
        foodSystem4DayBefore = new ArrayList<>();
        foodSystem5DayBefore = new ArrayList<>();
        foodSystem6DayBefore = new ArrayList<>();
        foodSystem7DayBefore = new ArrayList<>();
        foodSystemDate = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            foodSystem1DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem2DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem3DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem4DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem5DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem6DayBefore.add(new ArrayList<FoodSystem>());
            foodSystem7DayBefore.add(new ArrayList<FoodSystem>());
            foodSystemDate.add(new ArrayList<FoodSystem>());
        }
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
                                int mealPosition = checkMealPositionInListForDate(row.getInt("ID_MyMeal"), date, row.getInt("MealTime"));
                                Meal tempMeal;
                                if (mealPosition == -1) {
                                    tempMeal = new Meal(row.getInt("ID_MyMeal"), row.getString("MealName"));
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    tempMeal.addIngredientToList(tempIngredient);
                                    tempMeal.setWeight(row.getInt("Weight"));
                                    addMealToFoodSystemListForDate(tempMeal, date, row.getInt("MealTime"));
                                } else {
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    updateMealInFoodSystemListForDate(mealPosition, tempIngredient, date, row.getInt("MealTime"));
                                }
                            } else {
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                        row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempIngredient.setWeight(row.getInt("Weight"));
                                addIngredientToFoodSystemListForDate(tempIngredient, date, row.getInt("MealTime"));
                            }
                        }
                        drawChartsWeekly();
                    } else
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getFoodSystemFromWeek");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void getWeightFromWeek() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        weightWeek = new double[7];
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            weightWeek[i] = row.getDouble("UserWeight");
                        }
                        drawChartsWeeklyWeight();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getWeightFromWeek");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getFoodSystemFromDay(final String date) {
        pbLoadingDaily.setVisibility(View.VISIBLE);
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
                        pbLoadingDaily.setVisibility(View.GONE);
                        drawChartsDaily();
                    } else {
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getFoodSystemFromDay");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("date", date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void getWeightFromDay(final String date) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            String a = row.getString("UserWeight");
                            weightDay = Double.parseDouble(a);
                            tvWeightDay.setText(String.valueOf(weightDay));
                            llWeightDay.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Błąd podczas pobierania wagi z dnia", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd połączenia z bazą! " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd połączenia z bazą! " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getWeight");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("date", date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void drawChartsWeekly() {
        // TODO ewntualne dodanie 2 data set (wymagane do zjedzenia)
        pbLoadingLastWeek.setVisibility(View.GONE);
        llCaloriesWeekly.setVisibility(View.VISIBLE);
        llCarbohydratesWeekly.setVisibility(View.VISIBLE);
        llProteinWeekly.setVisibility(View.VISIBLE);
        llFatWeekly.setVisibility(View.VISIBLE);

        chartCaloriesWeek.setVisibility(View.VISIBLE);
        chartCarbohydratesWeek.setVisibility(View.VISIBLE);
        chartProteinWeek.setVisibility(View.VISIBLE);
        chartFatWeek.setVisibility(View.VISIBLE);


        Calendar calendar = Calendar.getInstance();
        days = new ArrayList<>();
        Date today = new Date();

        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        days.add(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1));

        // wykres kalorii
        chartCaloriesWeek.getDescription().setEnabled(false);
        chartCaloriesWeek.getLegend().setEnabled(false);
        chartCaloriesWeek.setScaleEnabled(false);

        if (getCaloriesFromList(foodSystem1DayBefore) == 0 && getCaloriesFromList(foodSystem2DayBefore) == 0 && getCaloriesFromList(foodSystem3DayBefore) == 0 &&
                getCaloriesFromList(foodSystem4DayBefore) == 0 && getCaloriesFromList(foodSystem5DayBefore) == 0 && getCaloriesFromList(foodSystem6DayBefore) == 0 &&
                getCaloriesFromList(foodSystem7DayBefore) == 0) {
            YAxis yAxisLeft = chartCaloriesWeek.getAxisLeft();
            YAxis yAxisRight = chartCaloriesWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesCalories = new ArrayList<>();
        barEntriesCalories.add(new BarEntry(0, getCaloriesFromList(foodSystem1DayBefore)));
        barEntriesCalories.add(new BarEntry(1, getCaloriesFromList(foodSystem2DayBefore)));
        barEntriesCalories.add(new BarEntry(2, getCaloriesFromList(foodSystem3DayBefore)));
        barEntriesCalories.add(new BarEntry(3, getCaloriesFromList(foodSystem4DayBefore)));
        barEntriesCalories.add(new BarEntry(4, getCaloriesFromList(foodSystem5DayBefore)));
        barEntriesCalories.add(new BarEntry(5, getCaloriesFromList(foodSystem6DayBefore)));
        barEntriesCalories.add(new BarEntry(6, getCaloriesFromList(foodSystem7DayBefore)));

        BarDataSet barDataSetCalories = new BarDataSet(barEntriesCalories, "Test");
        barDataSetCalories.setColors(colorCalories);
        barDataSetCalories.setValueTextSize(10);

        BarData dataCalories = new BarData(barDataSetCalories);
        chartCaloriesWeek.setData(dataCalories);

        XAxis xAxisCalories = chartCaloriesWeek.getXAxis();
        xAxisCalories.setGranularity(1);
        xAxisCalories.setValueFormatter(new MyXAxisValueFormatter(days));
        xAxisCalories.setPosition(XAxis.XAxisPosition.BOTTOM);

        //wykres węglowodanów
        chartCarbohydratesWeek.getDescription().setEnabled(false);
        chartCarbohydratesWeek.getLegend().setEnabled(false);
        chartCarbohydratesWeek.setScaleEnabled(false);

        if (getCarbohydratesFromList(foodSystem1DayBefore) == 0 && getCarbohydratesFromList(foodSystem2DayBefore) == 0 && getCarbohydratesFromList(foodSystem3DayBefore) == 0 &&
                getCarbohydratesFromList(foodSystem4DayBefore) == 0 && getCarbohydratesFromList(foodSystem5DayBefore) == 0 && getCarbohydratesFromList(foodSystem6DayBefore) == 0 &&
                getCarbohydratesFromList(foodSystem7DayBefore) == 0) {
            YAxis yAxisLeft = chartCarbohydratesWeek.getAxisLeft();
            YAxis yAxisRight = chartCarbohydratesWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesCarbohydrates = new ArrayList<>();
        barEntriesCarbohydrates.add(new BarEntry(0, (float) getCarbohydratesFromList(foodSystem1DayBefore)));
        barEntriesCarbohydrates.add(new BarEntry(1, (float) getCarbohydratesFromList(foodSystem2DayBefore)));
        barEntriesCarbohydrates.add(new BarEntry(2, (float) getCarbohydratesFromList(foodSystem3DayBefore)));
        barEntriesCarbohydrates.add(new BarEntry(3, (float) getCarbohydratesFromList(foodSystem4DayBefore)));
        barEntriesCarbohydrates.add(new BarEntry(4, (float) getCarbohydratesFromList(foodSystem5DayBefore)));
        barEntriesCarbohydrates.add(new BarEntry(5, (float) getCarbohydratesFromList(foodSystem6DayBefore)));
        barEntriesCarbohydrates.add(new BarEntry(6, (float) getCarbohydratesFromList(foodSystem7DayBefore)));

        BarDataSet barDataSetCarbohydrates = new BarDataSet(barEntriesCarbohydrates, "Test");
        barDataSetCarbohydrates.setValueFormatter(new DoubleValueFormatter());
        barDataSetCarbohydrates.setColors(colorCarbohydrates);
        barDataSetCarbohydrates.setValueTextSize(10);

        BarData dataCarbohydrates = new BarData(barDataSetCarbohydrates);
        chartCarbohydratesWeek.setData(dataCarbohydrates);

        XAxis xAxisCarbohydrates = chartCarbohydratesWeek.getXAxis();
        xAxisCarbohydrates.setGranularity(1);
        xAxisCarbohydrates.setValueFormatter(new MyXAxisValueFormatter(days));
        xAxisCarbohydrates.setPosition(XAxis.XAxisPosition.BOTTOM);

        //wykres białka
        chartProteinWeek.getDescription().setEnabled(false);
        chartProteinWeek.getLegend().setEnabled(false);
        chartProteinWeek.setScaleEnabled(false);

        if (getProteinFromList(foodSystem1DayBefore) == 0 && getProteinFromList(foodSystem2DayBefore) == 0 && getProteinFromList(foodSystem3DayBefore) == 0 &&
                getProteinFromList(foodSystem4DayBefore) == 0 && getProteinFromList(foodSystem5DayBefore) == 0 && getProteinFromList(foodSystem6DayBefore) == 0 &&
                getProteinFromList(foodSystem7DayBefore) == 0) {
            YAxis yAxisLeft = chartProteinWeek.getAxisLeft();
            YAxis yAxisRight = chartProteinWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesProtein = new ArrayList<>();
        barEntriesProtein.add(new BarEntry(0, (float) getProteinFromList(foodSystem1DayBefore)));
        barEntriesProtein.add(new BarEntry(1, (float) getProteinFromList(foodSystem2DayBefore)));
        barEntriesProtein.add(new BarEntry(2, (float) getProteinFromList(foodSystem3DayBefore)));
        barEntriesProtein.add(new BarEntry(3, (float) getProteinFromList(foodSystem4DayBefore)));
        barEntriesProtein.add(new BarEntry(4, (float) getProteinFromList(foodSystem5DayBefore)));
        barEntriesProtein.add(new BarEntry(5, (float) getProteinFromList(foodSystem6DayBefore)));
        barEntriesProtein.add(new BarEntry(6, (float) getProteinFromList(foodSystem7DayBefore)));

        BarDataSet barDataSetProtein = new BarDataSet(barEntriesProtein, "Test");
        barDataSetProtein.setValueFormatter(new DoubleValueFormatter());
        barDataSetProtein.setColors(colorProtein);
        barDataSetProtein.setValueTextSize(10);

        BarData dataProtein = new BarData(barDataSetProtein);
        chartProteinWeek.setData(dataProtein);

        myXAxisValueFormatter = new MyXAxisValueFormatter((days));
        XAxis xAxisProtein = chartProteinWeek.getXAxis();
        xAxisProtein.setGranularity(1);
        xAxisProtein.setValueFormatter(myXAxisValueFormatter);
        xAxisProtein.setPosition(XAxis.XAxisPosition.BOTTOM);

        //wykres tłuszczu
        chartFatWeek.getDescription().setEnabled(false);
        chartFatWeek.getLegend().setEnabled(false);
        chartFatWeek.setScaleEnabled(false);

        if (getFatFromList(foodSystem1DayBefore) == 0 && getFatFromList(foodSystem2DayBefore) == 0 && getFatFromList(foodSystem3DayBefore) == 0 &&
                getFatFromList(foodSystem4DayBefore) == 0 && getFatFromList(foodSystem5DayBefore) == 0 && getFatFromList(foodSystem6DayBefore) == 0 &&
                getFatFromList(foodSystem7DayBefore) == 0) {
            YAxis yAxisLeft = chartFatWeek.getAxisLeft();
            YAxis yAxisRight = chartFatWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesFat = new ArrayList<>();
        barEntriesFat.add(new BarEntry(0, (float) getFatFromList(foodSystem1DayBefore)));
        barEntriesFat.add(new BarEntry(1, (float) getFatFromList(foodSystem2DayBefore)));
        barEntriesFat.add(new BarEntry(2, (float) getFatFromList(foodSystem3DayBefore)));
        barEntriesFat.add(new BarEntry(3, (float) getFatFromList(foodSystem4DayBefore)));
        barEntriesFat.add(new BarEntry(4, (float) getFatFromList(foodSystem5DayBefore)));
        barEntriesFat.add(new BarEntry(5, (float) getFatFromList(foodSystem6DayBefore)));
        barEntriesFat.add(new BarEntry(6, (float) getFatFromList(foodSystem7DayBefore)));

        BarDataSet barDataSetFat = new BarDataSet(barEntriesFat, "Test");
        barDataSetFat.setValueFormatter(new DoubleValueFormatter());
        barDataSetFat.setColors(colorFat);
        barDataSetFat.setValueTextSize(10);

        BarData dataFat = new BarData(barDataSetFat);
        chartFatWeek.setData(dataFat);

        XAxis xAxisFat = chartFatWeek.getXAxis();
        xAxisFat.setGranularity(1);
        xAxisFat.setValueFormatter(myXAxisValueFormatter);
        xAxisFat.setPosition(XAxis.XAxisPosition.BOTTOM);

    }

    private void drawChartsWeeklyWeight() {
        // TODO ewntualne dodanie 2 data set (wymagane do zjedzenia)


        llWeightWeekly.setVisibility(View.VISIBLE);
        chartWeightWeek.setVisibility(View.VISIBLE);

        //wykres wagi
        chartWeightWeek.getDescription().setEnabled(false);
        chartWeightWeek.getLegend().setEnabled(false);
        chartWeightWeek.setScaleEnabled(false);

        int weightEqualZero = 0;
        boolean allZero;
        for (int i = 0; i < 7; i++) {
            if (weightWeek[i] == 0.0)
                weightEqualZero++;
        }

        if (weightEqualZero == 7)
            allZero = true;
        else
            allZero = false;

        if (allZero) {
            YAxis yAxisLeft = chartWeightWeek.getAxisLeft();
            YAxis yAxisRight = chartWeightWeek.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesWeight = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            barEntriesWeight.add(new BarEntry(i, (float) weightWeek[i]));
        }

        BarDataSet barDataSetWeight = new BarDataSet(barEntriesWeight, "Test");
        barDataSetWeight.setValueFormatter(new DoubleValueFormatter());
        barDataSetWeight.setColors(colorWeight);
        barDataSetWeight.setValueTextSize(10);

        BarData dataWeight = new BarData(barDataSetWeight);
        chartWeightWeek.setData(dataWeight);

        XAxis xAxisWeight = chartWeightWeek.getXAxis();
        xAxisWeight.setGranularity(1);
        xAxisWeight.setValueFormatter(myXAxisValueFormatter);
        xAxisWeight.setPosition(XAxis.XAxisPosition.BOTTOM);
    }

    private void drawChartsDaily() {
        // TODO ewntualne dodanie 2 data set (wymagane do zjedzenia)
        chartDaily.setVisibility(View.VISIBLE);

        ArrayList<String> xLabels = new ArrayList<>();
        xLabels.add("Kalorie");
        xLabels.add("Węglowodany");
        xLabels.add("Białko");
        xLabels.add("Tłuszcz");

        chartDaily.getDescription().setEnabled(false);
        chartDaily.getLegend().setEnabled(false);
        chartDaily.setScaleEnabled(false);

        if (getCaloriesFromList(foodSystemDate) == 0 && getCarbohydratesFromList(foodSystemDate) == 0 &&
                getProteinFromList(foodSystemDate) == 0 && getFatFromList(foodSystemDate) == 0) {
            YAxis yAxisLeft = chartDaily.getAxisLeft();
            YAxis yAxisRight = chartDaily.getAxisRight();
            yAxisLeft.setAxisMinimum(0);
            yAxisRight.setAxisMinimum(0);
        }

        ArrayList<BarEntry> barEntriesDaily = new ArrayList<>();
        barEntriesDaily.add(new BarEntry(0, getCaloriesFromList(foodSystemDate)));
        barEntriesDaily.add(new BarEntry(1, (float) getCarbohydratesFromList(foodSystemDate)));
        barEntriesDaily.add(new BarEntry(2, (float) getProteinFromList(foodSystemDate)));
        barEntriesDaily.add(new BarEntry(3, (float) getFatFromList(foodSystemDate)));

        BarDataSet barDataSetDaily = new BarDataSet(barEntriesDaily, "Test");
        barDataSetDaily.setValueFormatter(new DoubleValueFormatter());
        barDataSetDaily.setColors(colorCalories, colorCarbohydrates, colorProtein, colorFat);
        barDataSetDaily.setValueTextSize(12);

        BarData dataDaily = new BarData(barDataSetDaily);
        chartDaily.setData(dataDaily);

        XAxis xAxis = chartDaily.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(xLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        final ScrollView scrollView = getView().findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        ArrayList<String> values;

        public MyXAxisValueFormatter(ArrayList<String> values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return values.get((int) value);
        }
    }

    public class DoubleValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            return String.valueOf(decimalFormat.format(value));
        }
    }

    // pobieranie posiłków i składników dla tygodnia
    private int checkMealPositionInListForDate(int mealId, Date date, int mealTime) {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

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
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date7Before = calendar.getTime();

        if (date.toString().equals(date1Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem1DayBefore, mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem2DayBefore, mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem3DayBefore, mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem4DayBefore, mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem5DayBefore, mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem6DayBefore, mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            return checkMealPositionInMealTimeList(mealId, foodSystem7DayBefore, mealTime);
        }
        return -1;
    }

    private int checkMealPositionInMealTimeList(int mealId, ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore, int mealTime) {
        Meal tempMeal;
        ArrayList<FoodSystem> tempList;
        switch (mealTime) {
            case 0:
                tempList = foodSystemXDayBefore.get(0);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 1:
                tempList = foodSystemXDayBefore.get(1);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 2:
                tempList = foodSystemXDayBefore.get(2);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 3:
                tempList = foodSystemXDayBefore.get(3);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 4:
                tempList = foodSystemXDayBefore.get(4);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 5:
                tempList = foodSystemXDayBefore.get(5);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
        }
        return -1;
    }


    private void addMealToFoodSystemListForDate(Meal meal, Date date, int mealTime) {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

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
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date7Before = calendar.getTime();

        if (date.toString().equals(date1Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem1DayBefore, mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem2DayBefore, mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem3DayBefore, mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem4DayBefore, mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem5DayBefore, mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem6DayBefore, mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            addMealToFoodSystemMealTimeList(meal, foodSystem7DayBefore, mealTime);
        }
    }

    private void addMealToFoodSystemMealTimeList(Meal meal, ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore, int mealTime) {
        switch (mealTime) {
            case 0:
                foodSystemXDayBefore.get(0).add(meal);
                break;
            case 1:
                foodSystemXDayBefore.get(1).add(meal);
                break;
            case 2:
                foodSystemXDayBefore.get(2).add(meal);
                break;
            case 3:
                foodSystemXDayBefore.get(3).add(meal);
                break;
            case 4:
                foodSystemXDayBefore.get(4).add(meal);
                break;
            case 5:
                foodSystemXDayBefore.get(5).add(meal);
                break;
        }
    }


    private void updateMealInFoodSystemListForDate(int position, Ingredient ingredient, Date date, int mealTime) {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

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
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date7Before = calendar.getTime();

        if (date.toString().equals(date1Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem1DayBefore, mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem2DayBefore, mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem3DayBefore, mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem4DayBefore, mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem5DayBefore, mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem6DayBefore, mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            updateMealInFoodSystemMealTimeList(position, ingredient, foodSystem7DayBefore, mealTime);
        }
    }

    private void updateMealInFoodSystemMealTimeList(int position, Ingredient ingredient, ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore, int mealTime) {
        Meal tempMeal;
        switch (mealTime) {
            case 0:
                tempMeal = (Meal) foodSystemXDayBefore.get(0).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 1:
                tempMeal = (Meal) foodSystemXDayBefore.get(1).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 2:
                tempMeal = (Meal) foodSystemXDayBefore.get(2).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 3:
                tempMeal = (Meal) foodSystemXDayBefore.get(3).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 4:
                tempMeal = (Meal) foodSystemXDayBefore.get(4).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 5:
                tempMeal = (Meal) foodSystemXDayBefore.get(5).get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
        }
    }


    private void addIngredientToFoodSystemListForDate(Ingredient ingredient, Date date, int mealTime) {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

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
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date7Before = calendar.getTime();

        if (date.toString().equals(date1Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem1DayBefore, mealTime);
        } else if (date.toString().equals(date2Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem2DayBefore, mealTime);
        } else if (date.toString().equals(date3Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem3DayBefore, mealTime);
        } else if (date.toString().equals(date4Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem4DayBefore, mealTime);
        } else if (date.toString().equals(date5Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem5DayBefore, mealTime);
        } else if (date.toString().equals(date6Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem6DayBefore, mealTime);
        } else if (date.toString().equals(date7Before.toString())) {
            addIngredientToFoodSystemMealTimeList(ingredient, foodSystem7DayBefore, mealTime);
        }
    }

    private void addIngredientToFoodSystemMealTimeList(Ingredient ingredient, ArrayList<ArrayList<FoodSystem>> foodSystemXDayBefore, int mealTime) {
        switch (mealTime) {
            case 0:
                foodSystemXDayBefore.get(0).add(ingredient);
                break;
            case 1:
                foodSystemXDayBefore.get(1).add(ingredient);
                break;
            case 2:
                foodSystemXDayBefore.get(2).add(ingredient);
                break;
            case 3:
                foodSystemXDayBefore.get(3).add(ingredient);
                break;
            case 4:
                foodSystemXDayBefore.get(4).add(ingredient);
                break;
            case 5:
                foodSystemXDayBefore.get(5).add(ingredient);
                break;
        }
    }

    // pobieranie posiłków i składników dla konkretnego dnia
    private int checkMealPositionInList(int mealId, int mealTime) {
        Meal tempMeal;
        ArrayList<FoodSystem> tempList;
        switch (mealTime) {
            case 0:
                tempList = foodSystemDate.get(0);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 1:
                tempList = foodSystemDate.get(1);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 2:
                tempList = foodSystemDate.get(2);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 3:
                tempList = foodSystemDate.get(3);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 4:
                tempList = foodSystemDate.get(4);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
            case 5:
                tempList = foodSystemDate.get(5);
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getClass() == Meal.class) {
                        tempMeal = (Meal) tempList.get(i);
                        if (tempMeal.getID() == mealId)
                            return i;
                    }
                }
                break;
        }
        return -1;
    }

    private void updateMealInFoodSystemList(int position, Ingredient ingredient, int mealTime) {
        Meal tempMeal;
        ArrayList<FoodSystem> tempList;
        switch (mealTime) {
            case 0:
                tempList = foodSystemDate.get(0);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 1:
                tempList = foodSystemDate.get(1);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 2:
                tempList = foodSystemDate.get(2);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 3:
                tempList = foodSystemDate.get(3);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 4:
                tempList = foodSystemDate.get(4);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
            case 5:
                tempList = foodSystemDate.get(5);
                tempMeal = (Meal) tempList.get(position);
                tempMeal.addIngredientToList(ingredient);
                break;
        }
    }

    private void addIngredientToFoodSystemList(Ingredient ingredient, int mealTime) {
        switch (mealTime) {
            case 0:
                foodSystemDate.get(0).add(ingredient);
                break;
            case 1:
                foodSystemDate.get(1).add(ingredient);
                break;
            case 2:
                foodSystemDate.get(2).add(ingredient);
                break;
            case 3:
                foodSystemDate.get(3).add(ingredient);
                break;
            case 4:
                foodSystemDate.get(4).add(ingredient);
                break;
            case 5:
                foodSystemDate.get(5).add(ingredient);
                break;
        }
    }

    // pobieranie makroskładników z list z x daty
    private int getCaloriesFromList(ArrayList<ArrayList<FoodSystem>> foodSystemListXDayBefore) {
        int calories = 0;

        for (int i = 0; i < foodSystemListXDayBefore.size(); i++) {
            ArrayList<FoodSystem> tempList = foodSystemListXDayBefore.get(i);
            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).getClass() == Ingredient.class) {
                    calories += tempList.get(j).getCalories() * tempList.get(j).getWeight() / 100;
                } else {
                    Meal tempMeal = (Meal) tempList.get(j);
                    calories += tempMeal.getCalories() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                }
            }
        }

        return calories;
    }

    private double getCarbohydratesFromList(ArrayList<ArrayList<FoodSystem>> foodSystemListXDayBefore) {
        double carbohydrates = 0;

        for (int i = 0; i < foodSystemListXDayBefore.size(); i++) {
            ArrayList<FoodSystem> tempList = foodSystemListXDayBefore.get(i);
            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).getClass() == Ingredient.class) {
                    carbohydrates += tempList.get(j).getCarbohydrates() * tempList.get(j).getWeight() / 100;
                } else {
                    Meal tempMeal = (Meal) tempList.get(j);
                    carbohydrates += tempMeal.getCarbohydrates() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                }
            }
        }

        return carbohydrates;
    }

    private double getProteinFromList(ArrayList<ArrayList<FoodSystem>> foodSystemListXDayBefore) {
        double protein = 0;

        for (int i = 0; i < foodSystemListXDayBefore.size(); i++) {
            ArrayList<FoodSystem> tempList = foodSystemListXDayBefore.get(i);
            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).getClass() == Ingredient.class) {
                    protein += tempList.get(j).getProtein() * tempList.get(j).getWeight() / 100;
                } else {
                    Meal tempMeal = (Meal) tempList.get(j);
                    protein += tempMeal.getProtein() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                }
            }
        }

        return protein;
    }

    private double getFatFromList(ArrayList<ArrayList<FoodSystem>> foodSystemListXDayBefore) {
        double fat = 0;

        for (int i = 0; i < foodSystemListXDayBefore.size(); i++) {
            ArrayList<FoodSystem> tempList = foodSystemListXDayBefore.get(i);
            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).getClass() == Ingredient.class) {
                    fat += tempList.get(j).getFat() * tempList.get(j).getWeight() / 100;
                } else {
                    Meal tempMeal = (Meal) tempList.get(j);
                    fat += tempMeal.getFat() * tempMeal.getWeight() / tempMeal.getTotalWeight();
                }
            }
        }

        return fat;
    }

}