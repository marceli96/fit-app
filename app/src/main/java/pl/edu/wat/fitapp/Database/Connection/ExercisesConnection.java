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

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.SimpleExercisesListAdapter;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class ExercisesConnection {
    private Activity activity;
    private ArrayList<Exercise> exercises;

    public ExercisesConnection(Activity activity, ArrayList<Exercise> exercises) {
        this.activity = activity;
        this.exercises = exercises;
    }

    public void getExercises(final SimpleExercisesListAdapter simpleExercisesListAdapter) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, activity.getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject exercise = jsonResponse.getJSONObject(String.valueOf(i));
                            exercises.add(new Exercise(exercise.getInt("ID_Exercise"), exercise.getString("ExerciseName")));
                        }
                        simpleExercisesListAdapter.notifyDataSetChanged();
                    } else
                        ToastUtils.shortToast(activity, "Wystąpił błąd podczas pobierania ćwiczeń");
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.shortToast(activity, "Wystąpił błąd podczas pobierania ćwiczeń " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.shortToast(activity, "Wystąpił błąd podczas pobierania ćwiczeń " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getExercises");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

}
