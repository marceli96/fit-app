package pl.edu.wat.fitapp.Database.Connection;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.Main.Fragment.HomeFragment;
import pl.edu.wat.fitapp.Main.Fragment.JournalFragment;
import pl.edu.wat.fitapp.Mangement.FoodSystemDayManagement;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class FoodSystemDayConnection {
    private Fragment fragment;
    private ArrayList<ArrayList<FoodSystem>> foodSystemDay;

    public FoodSystemDayConnection(Fragment fragment, ArrayList<ArrayList<FoodSystem>> foodSystemDay) {
        this.fragment = fragment;
        this.foodSystemDay = foodSystemDay;
    }

    public void getFoodSystemFromDay(final int userID, final String date) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fragment.getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    FoodSystemDayManagement foodSystemManagement = new FoodSystemDayManagement();
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            if (row.getString("type").equals("meal")) {
                                int mealPosition = foodSystemManagement.checkMealPositionInList(row.getInt("ID_MyMeal"), row.getInt("MealTime"), foodSystemDay);
                                Meal tempMeal;
                                if (mealPosition == -1) {
                                    tempMeal = new Meal(row.getInt("ID_MyMeal"), row.getString("MealName"));
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    tempMeal.addIngredientToList(tempIngredient);
                                    tempMeal.setWeight(row.getInt("Weight"));
                                    foodSystemManagement.addMealToFoodSystemList(tempMeal, row.getInt("MealTime"), foodSystemDay);
                                } else {
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    foodSystemManagement.updateMealInFoodSystemList(mealPosition, tempIngredient, row.getInt("MealTime"), foodSystemDay);
                                }
                            } else {
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                        row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempIngredient.setWeight(row.getInt("Weight"));
                                foodSystemManagement.addIngredientToFoodSystemList(tempIngredient, row.getInt("MealTime"), foodSystemDay);
                            }
                        }
                        if (fragment.getClass() == JournalFragment.class) {
                            ((JournalFragment) fragment).drawChartsMacroDaily();
                        } else if(fragment.getClass() == HomeFragment.class){
                            ((HomeFragment) fragment).showMealLayoutsAndUpdate();
                        }
                    } else {
                        ToastUtils.shortToast(fragment.getActivity(), "Błąd połączenia z bazą");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.shortToast(fragment.getActivity(), "Błąd połączenia z bazą " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.shortToast(fragment.getActivity(), "Błąd połączenia z bazą " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getFoodSystemFromDay");
                params.put("userId", String.valueOf(userID));
                params.put("date", date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity());
        requestQueue.add(stringRequest);
    }

}
