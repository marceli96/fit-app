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
import pl.edu.wat.fitapp.interfaces.callback.AddMyTrainingConnectionCallback;
import pl.edu.wat.fitapp.R;

public class AddMyTrainingConnection {
    private AddMyTrainingConnectionCallback callback;
    private ArrayList<Exercise> trainingExercises;

    public AddMyTrainingConnection(AddMyTrainingConnectionCallback callback, ArrayList<Exercise> trainingExercises) {
        this.callback = callback;
        this.trainingExercises = trainingExercises;
    }

    public void addMyTraining(final int userID, final String trainingName) {
        String exerciseIds = callback.activity().getString(R.string.empty);
        String exerciseSeries = callback.activity().getString(R.string.empty);
        String exerciseRepetitions = callback.activity().getString(R.string.empty);
        for (int i = 0; i < trainingExercises.size(); i++) {
            if (i == trainingExercises.size() - 1) {
                exerciseIds += String.valueOf(trainingExercises.get(i).getID());
                exerciseSeries += String.valueOf(trainingExercises.get(i).getSeries());
                exerciseRepetitions += String.valueOf(trainingExercises.get(i).getRepetitions());
            } else {
                exerciseIds += String.valueOf(trainingExercises.get(i).getID()) + callback.activity().getString(R.string.slash);
                exerciseSeries += String.valueOf(trainingExercises.get(i).getSeries()) + callback.activity().getString(R.string.slash);
                exerciseRepetitions += String.valueOf(trainingExercises.get(i).getRepetitions()) + callback.activity().getString(R.string.slash);
            }
        }

        final String finalExerciseIds = exerciseIds;
        final String finalExerciseSeries = exerciseSeries;
        final String finalExerciseRepetitions = exerciseRepetitions;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        callback.onSuccessAddMyTraining();
                    } else
                        callback.onFailure(callback.activity().getString(R.string.addError));
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
                params.put("operation", "addMyTraining");
                params.put("userId", String.valueOf(userID));
                params.put("trainingName", trainingName);
                params.put("exerciseIds", finalExerciseIds);
                params.put("exerciseSeries", finalExerciseSeries);
                params.put("exerciseRepetitions", finalExerciseRepetitions);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
