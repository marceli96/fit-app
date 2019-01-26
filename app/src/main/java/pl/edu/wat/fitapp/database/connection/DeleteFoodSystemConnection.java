package pl.edu.wat.fitapp.database.connection;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.interfaces.FoodSystem;
import pl.edu.wat.fitapp.androidComponent.listAdapter.FoodSystemListAdapter;
import pl.edu.wat.fitapp.interfaces.callback.FoodSystemCallback;
import pl.edu.wat.fitapp.R;

public class DeleteFoodSystemConnection {
    private FoodSystemCallback callback;
    private ArrayList<ArrayList<FoodSystem>> foodSystemDay;
    private ArrayList<FoodSystemListAdapter> foodSystemListAdapters;

    public DeleteFoodSystemConnection(FoodSystemCallback callback, ArrayList<ArrayList<FoodSystem>> foodSystemDay, ArrayList<FoodSystemListAdapter> foodSystemListAdapters) {
        this.callback = callback;
        this.foodSystemDay = foodSystemDay;
        this.foodSystemListAdapters = foodSystemListAdapters;
    }

    public void deleteFromFoodSystem(final FoodSystem food, final int userId, final int mealTime, final int weight) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        callback.onSuccessFoodSystem(mealTime, food);
                    } else
                        callback.onFailure(callback.activity().getString(R.string.eraseError));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(callback.activity().getString(R.string.eraseError)+ e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(callback.activity().getString(R.string.eraseError) + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(callback.activity().getString(R.string.formatDate));
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
                params.put("date", dateFormat.format(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }

}
