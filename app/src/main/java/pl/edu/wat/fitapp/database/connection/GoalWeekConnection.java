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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Interface.GoalWeekConnectionCallback;
import pl.edu.wat.fitapp.View.Main.Fragment.ExportFragment;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class GoalWeekConnection {
    private GoalWeekConnectionCallback callback;
    private ArrayList<Integer> goalWeek;

    public GoalWeekConnection(GoalWeekConnectionCallback callback, ArrayList<Integer> goalWeek) {
        this.callback = callback;
        this.goalWeek = goalWeek;
    }

    public void getGoalFromWeek(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            goalWeek.add(row.getInt("Goal"));
                        }
                        callback.onSuccessGoalWeek();
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
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getGoalFromWeek");
                params.put("userId", String.valueOf(userID));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
