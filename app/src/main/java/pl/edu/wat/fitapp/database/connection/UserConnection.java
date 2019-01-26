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

import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.interfaces.callback.UserConnectionCallback;
import pl.edu.wat.fitapp.R;

public class UserConnection {
    private UserConnectionCallback callback;

    public UserConnection(UserConnectionCallback callback) {
        this.callback = callback;
    }

    public void saveWeight(final User user, final double weight, final int goal, final int calories, final int activityLevel) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean successUser = jsonResponse.getBoolean("successUser");
                            boolean successWeight = jsonResponse.getBoolean("successWeight");
                            if (successUser && successWeight) {
                                user.setWeight(weight);
                                user.setGoal(goal);
                                user.setCaloricDemand(calories);
                                user.setActivityLevel(activityLevel);
                                callback.onSuccess(user);
                            } else
                                callback.onFailure(callback.activity().getString(R.string.saveChangeError));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onFailure(callback.activity().getString(R.string.connectionError) + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFailure(callback.activity().getString(R.string.connectionError) + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(callback.activity().getString(R.string.formatDate));
                params.put("operation", "setWeightGoalActivityLevel");
                params.put("userId", String.valueOf(user.getUserID()));
                params.put("userWeight", String.valueOf(weight));
                params.put("weightDate", String.valueOf(dateFormat.format(date)));
                params.put("caloricDemend", String.valueOf(calories));
                params.put("goal", String.valueOf(goal));
                params.put("activityLevel", String.valueOf(activityLevel));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
