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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Interface.ConnectionCallback;
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.Interface.FoodSystemWeekConnectionCallback;
import pl.edu.wat.fitapp.View.Main.Fragment.ExportFragment;
import pl.edu.wat.fitapp.View.Main.Fragment.JournalFragment;
import pl.edu.wat.fitapp.Mangement.FoodSystemWeekManagement;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class FoodSystemWeekConnection {
    private FoodSystemWeekConnectionCallback callback;
    private ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek;

    public FoodSystemWeekConnection(FoodSystemWeekConnectionCallback callback, ArrayList<ArrayList<ArrayList<FoodSystem>>> foodSystemWeek) {
        this.callback = callback;
        this.foodSystemWeek = foodSystemWeek;
    }

    public void getFoodSystemFromWeek(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    FoodSystemWeekManagement foodSystemWeekManagement = new FoodSystemWeekManagement();
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = row.getString("FoodDate");
                            Date date = sdf.parse(dateString);
                            if (row.getString("type").equals("meal")) {
                                int mealPosition = foodSystemWeekManagement.checkMealPositionInListForDate(row.getInt("ID_MyMeal"), date, row.getInt("MealTime"), foodSystemWeek);
                                Meal tempMeal;
                                if (mealPosition == -1) {
                                    tempMeal = new Meal(row.getInt("ID_MyMeal"), row.getString("MealName"));
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    tempMeal.addIngredientToList(tempIngredient);
                                    tempMeal.setWeight(row.getInt("Weight"));
                                    foodSystemWeekManagement.addMealToFoodSystemListForDate(tempMeal, date, row.getInt("MealTime"), foodSystemWeek);
                                } else {
                                    Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                            row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                    tempIngredient.setWeight(row.getInt("IngredientWeight"));
                                    foodSystemWeekManagement.updateMealInFoodSystemListForDate(mealPosition, tempIngredient, date, row.getInt("MealTime"), foodSystemWeek);
                                }
                            } else {
                                Ingredient tempIngredient = new Ingredient(row.getInt("ID_Ingredient"), row.getString("IngredientName"), row.getDouble("Carbohydrates"),
                                        row.getDouble("Protein"), row.getDouble("Fat"), row.getInt("Calories"));
                                tempIngredient.setWeight(row.getInt("Weight"));
                                foodSystemWeekManagement.addIngredientToFoodSystemListForDate(tempIngredient, date, row.getInt("MealTime"), foodSystemWeek);
                            }
                        }
                        callback.onSuccessFoodSystemWeek();
                    } else
                        callback.onFailure("Błąd połączenia z bazą");
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Błąd połączenia z bazą " + e.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("Błąd połączenia z bazą " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getFoodSystemFromWeek");
                params.put("userId", String.valueOf(userID));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
