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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Main.Fragment.GoalsFragment;
import pl.edu.wat.fitapp.R;

public class UserConnection {
    private GoalsFragment goalsFragment;
    private final String OPERATIONS_URL = goalsFragment.getString(R.string.OPERATIONS_URL);

    public UserConnection(GoalsFragment goalsFragment) {
        this.goalsFragment = goalsFragment;
    }

    public void saveWeight(final User user, final double weight, final int goal, final int calories, final int activityLevel) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean successUser = jsonResponse.getBoolean("successUser");
                                boolean successWeight = jsonResponse.getBoolean("successWeight");
                                if (successUser && successWeight) {
                                    Toast.makeText(goalsFragment.getActivity(), "Dane zaaktualizowane ", Toast.LENGTH_SHORT).show();
                                    user.setWeight(weight);
                                    user.setGoal(goal);
                                    user.setCaloricDemand(calories);
                                    user.setActivityLevel(activityLevel);
                                    goalsFragment.openHomeActivity();
                                } else
                                    Toast.makeText(goalsFragment.getActivity(), "Nieoczkiwany błąd, powtórz wcześniej wprowadzone zmiany", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(goalsFragment.getActivity(), "Błąd połączenia z bazą" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(goalsFragment.getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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

            RequestQueue requestQueue = Volley.newRequestQueue(goalsFragment.getActivity());
            requestQueue.add(stringRequest);
    }
}
