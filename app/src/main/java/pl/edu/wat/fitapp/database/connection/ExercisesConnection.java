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

import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.interfaces.callback.ExercisesConnectionCallback;
import pl.edu.wat.fitapp.R;

public class ExercisesConnection {
    private ExercisesConnectionCallback callback;
    private ArrayList<Exercise> exercises;

    public ExercisesConnection(ExercisesConnectionCallback callback, ArrayList<Exercise> exercises) {
        this.callback = callback;
        this.exercises = exercises;
    }

    public void getExercises() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
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
                        callback.onSuccessExercises();
                    } else
                        callback.onFailure(callback.activity().getString(R.string.exerciseError));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(callback.activity().getString(R.string.exerciseError) + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(callback.activity().getString(R.string.exerciseError) + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getExercises");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }

}
