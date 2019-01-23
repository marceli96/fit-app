package pl.edu.wat.fitapp.Database.Connection;

import android.app.Activity;
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

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.IngredientsListAdapter;
import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Interface.IngredientsConnectionCallback;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class IngredientsConnection {
    private IngredientsConnectionCallback callback;
    private ArrayList<Ingredient> ingredients;

    public IngredientsConnection(IngredientsConnectionCallback callback, ArrayList<Ingredient> ingredients) {
        this.callback = callback;
        this.ingredients = ingredients;
    }

    public void getIngredients() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject ingredient = jsonResponse.getJSONObject(String.valueOf(i));
                            ingredients.add(new Ingredient(ingredient.getInt("ID_Ingredient"), ingredient.getString("IngredientName"),
                                    ingredient.getDouble("Carbohydrates"), ingredient.getDouble("Protein"), ingredient.getDouble("Fat"),
                                    ingredient.getInt("Calories")));
                        }
                        callback.onSuccessIngredients();
                    } else
                        callback.onFailure("Błąd połączenia z bazą");
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Błąd połączenia z bazą " + e.toString());
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
                params.put("operation", "getIngredients");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}