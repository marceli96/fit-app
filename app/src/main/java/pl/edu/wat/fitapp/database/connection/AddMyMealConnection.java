package pl.edu.wat.fitapp.database.connection;

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

import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.interfaces.callback.AddMyMealConnectionCallback;
import pl.edu.wat.fitapp.R;

public class AddMyMealConnection {
    private AddMyMealConnectionCallback callback;
    private ArrayList<Ingredient> mealIngredients;

    public AddMyMealConnection(AddMyMealConnectionCallback callback, ArrayList<Ingredient> mealIngredients) {
        this.callback = callback;
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
                ingredientIds += String.valueOf(mealIngredients.get(i).getID()) + callback.activity().getString(R.string.slash);
                ingredientWeights += String.valueOf(mealIngredients.get(i).getWeight()) + callback.activity().getString(R.string.slash);
            }
        }

        final String finalIngredientWeights = ingredientWeights;
        final String finalIngredientIds = ingredientIds;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        callback.onSuccessAddMyMeal();
                    } else {
                        callback.onFailure(callback.activity().getString(R.string.addError));
                    }
                } catch (JSONException e) {
                    callback.onFailure(callback.activity().getString(R.string.addError) + e.toString());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(callback.activity().getString(R.string.addError) + error.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
