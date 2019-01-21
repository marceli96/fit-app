package pl.edu.wat.fitapp.Database.Connection;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ScrollView;
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
import pl.edu.wat.fitapp.Main.Fragment.AddToSystem.AddMealToFoodSystemFragment;
import pl.edu.wat.fitapp.Main.Fragment.Profile.ProfileFragment;
import pl.edu.wat.fitapp.Mangement.MyMealManagement;
import pl.edu.wat.fitapp.R;

public class MyMealsConnection {
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";
    private Fragment fragment;
    private ArrayList<Meal> myMeals;

    public MyMealsConnection(Fragment fragment, ArrayList<Meal> myMeals) {
        this.fragment = fragment;
        this.myMeals = myMeals;
    }

    public void getMyMeals(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    MyMealManagement myMealMng = new MyMealManagement();
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            int mealPosition = myMealMng.findMealInList(row.getInt("ID_MyMeal"), myMeals);
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
                        if (fragment.getClass() == ProfileFragment.class)
                            ((ProfileFragment) fragment).showMyMeals();
                        else if(fragment.getClass() == AddMealToFoodSystemFragment.class)
                            ((AddMealToFoodSystemFragment) fragment).showMyMeals();
                    } else
                        Toast.makeText(fragment.getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(fragment.getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(fragment.getContext(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getMeals");
                params.put("userId", String.valueOf(userID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getContext());
        requestQueue.add(stringRequest);
    }
}
