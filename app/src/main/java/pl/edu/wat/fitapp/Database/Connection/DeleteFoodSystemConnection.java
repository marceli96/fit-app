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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.FoodSystemListAdapter;
import pl.edu.wat.fitapp.Main.Fragment.HomeFragment;
import pl.edu.wat.fitapp.R;

public class DeleteFoodSystemConnection {
    private HomeFragment homeFragment;
    private final String OPERATIONS_URL = homeFragment.getString(R.string.OPERATIONS_URL);
    private ArrayList<ArrayList<FoodSystem>> foodSystemDay;
    private ArrayList<FoodSystemListAdapter> foodSystemListAdapters;

    public DeleteFoodSystemConnection(HomeFragment homeFragment, ArrayList<ArrayList<FoodSystem>> foodSystemDay, ArrayList<FoodSystemListAdapter> foodSystemMealTimeAdapters) {
        this.homeFragment = homeFragment;
        this.foodSystemDay = foodSystemDay;
        this.foodSystemListAdapters = foodSystemMealTimeAdapters;
    }

    public void deleteFromFoodSystem(final FoodSystem food, final int userId, final int mealTime, final int weight) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        switch (mealTime) {
                            case 0:
                                foodSystemDay.get(0).remove(food);
                                foodSystemListAdapters.get(0).notifyDataSetChanged();
                                break;
                            case 1:
                                foodSystemDay.get(1).remove(food);
                                foodSystemListAdapters.get(1).notifyDataSetChanged();
                                break;
                            case 2:
                                foodSystemDay.get(2).remove(food);
                                foodSystemListAdapters.get(2).notifyDataSetChanged();
                                break;
                            case 3:
                                foodSystemDay.get(3).remove(food);
                                foodSystemListAdapters.get(3).notifyDataSetChanged();
                                break;
                            case 4:
                                foodSystemDay.get(4).remove(food);
                                foodSystemListAdapters.get(4).notifyDataSetChanged();
                                break;
                            case 5:
                                foodSystemDay.get(5).remove(food);
                                foodSystemListAdapters.get(5).notifyDataSetChanged();
                                break;
                        }
                        Toast.makeText(homeFragment.getActivity(), "Pomyślnie usunięto", Toast.LENGTH_SHORT).show();
                        homeFragment.updateMacrosOnMealTimes();
                        homeFragment.updateEatenMacros();
                    } else
                        Toast.makeText(homeFragment.getActivity(), "Błąd podczas usuwania", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(homeFragment.getActivity(), "Błąd podczas usuwania " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(homeFragment.getActivity(), "Błąd podczas usuwania " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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

        RequestQueue requestQueue = Volley.newRequestQueue(homeFragment.getActivity());
        requestQueue.add(stringRequest);
    }

}
