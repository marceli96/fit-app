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
import pl.edu.wat.fitapp.Interface.ExercisesConnectionCallback;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

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
                        callback.onFailure("Wystąpił błąd podczas pobierania ćwiczeń");
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Wystąpił błąd podczas pobierania ćwiczeń " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("Wystąpił błąd podczas pobierania ćwiczeń " + error.toString());
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
