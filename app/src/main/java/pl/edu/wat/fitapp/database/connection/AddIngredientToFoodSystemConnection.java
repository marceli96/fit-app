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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.R;


public class AddIngredientToFoodSystemConnection {
    private ConnectionCallback callback;

    public AddIngredientToFoodSystemConnection(ConnectionCallback callback) {
        this.callback = callback;
    }

    public void addIngredientToFoodSystem(final int ingredientId, final int userID, final int mealTime, final String weight) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean message = jsonResponse.getBoolean("message");
                    if (!message) {
                        callback.onFailure(callback.activity().getString(R.string.comunicat2));
                    } else {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(callback.activity().getString(R.string.addError));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(callback.activity().getString(R.string.addError) + e.toString());
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
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(callback.activity().getString(R.string.formatDate));
                params.put("operation", "addIngredientToFoodSystem");
                params.put("ingredientId", String.valueOf(ingredientId));
                params.put("userId", String.valueOf(userID));
                params.put("mealTime", String.valueOf(mealTime));
                params.put("weight", weight);
                params.put("date", dateFormat.format(date));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }

}
