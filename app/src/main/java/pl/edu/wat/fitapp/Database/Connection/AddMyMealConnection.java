package pl.edu.wat.fitapp.Database.Connection;

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
import pl.edu.wat.fitapp.Main.Fragment.Profile.AddMyMealIngredientsActivity;
import pl.edu.wat.fitapp.R;

public class AddMyMealConnection {
    private AddMyMealIngredientsActivity addMyMealIngredientsActivity;
    private final String OPERATIONS_URL = addMyMealIngredientsActivity.getString(R.string.OPERATIONS_URL);
    private ArrayList<Ingredient> mealIngredients;

    public AddMyMealConnection(AddMyMealIngredientsActivity addMyMealIngredientsActivity, ArrayList<Ingredient> mealIngredients) {
        this.addMyMealIngredientsActivity = addMyMealIngredientsActivity;
        this.mealIngredients = mealIngredients;
    }

    public void addMyMeal(final int userID, final String mealName) {
        String ingredientIds = "";
        String ingredientWeights = "";
        for (int i = 0; i < mealIngredients.size(); i++) {
            if (i == mealIngredients.size() - 1) {
                ingredientIds += String.valueOf(mealIngredients.get(i).getID());
                ingredientWeights += String.valueOf(mealIngredients.get(i).getWeight());
            } else {
                ingredientIds += String.valueOf(mealIngredients.get(i).getID()) + "/";
                ingredientWeights += String.valueOf(mealIngredients.get(i).getWeight()) + "/";
            }
        }

        final String finalIngredientWeights = ingredientWeights;
        final String finalIngredientIds = ingredientIds;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(addMyMealIngredientsActivity, "Dodano posiłek", Toast.LENGTH_SHORT).show();
                        addMyMealIngredientsActivity.openMeFragment();
                    } else {
                        Toast.makeText(addMyMealIngredientsActivity, "Blad podczas dodawania posiłku", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(addMyMealIngredientsActivity, "Blad podczas dodawania posiłku " + e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(addMyMealIngredientsActivity, "Blad podczas dodawania posiłku " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "addMyMeal");
                params.put("ingredientIds", finalIngredientIds);
                params.put("userId", String.valueOf(userID));
                params.put("mealName", mealName);
                params.put("ingredientWeights", finalIngredientWeights);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(addMyMealIngredientsActivity);
        requestQueue.add(stringRequest);
    }
}
