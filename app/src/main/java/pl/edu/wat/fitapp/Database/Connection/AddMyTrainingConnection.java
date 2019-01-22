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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Main.Fragment.Profile.AddMyTrainingExercisesActivity;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class AddMyTrainingConnection {
    private AddMyTrainingExercisesActivity addMyTrainingExercisesActivity;
    private ArrayList<Exercise> trainingExercises;

    public AddMyTrainingConnection(AddMyTrainingExercisesActivity addMyTrainingExercisesActivity, ArrayList<Exercise> trainingExercises) {
        this.addMyTrainingExercisesActivity = addMyTrainingExercisesActivity;
        this.trainingExercises = trainingExercises;
    }

    public void addMyTraining(final int userID, final String trainingName) {
        String exerciseIds = "";
        String exerciseSeries = "";
        String exerciseRepetitions = "";
        for (int i = 0; i < trainingExercises.size(); i++) {
            if (i == trainingExercises.size() - 1) {
                exerciseIds += String.valueOf(trainingExercises.get(i).getID());
                exerciseSeries += String.valueOf(trainingExercises.get(i).getSeries());
                exerciseRepetitions += String.valueOf(trainingExercises.get(i).getRepetitions());
            } else {
                exerciseIds += String.valueOf(trainingExercises.get(i).getID()) + "/";
                exerciseSeries += String.valueOf(trainingExercises.get(i).getSeries()) + "/";
                exerciseRepetitions += String.valueOf(trainingExercises.get(i).getRepetitions()) + "/";
            }
        }

        final String finalExerciseIds = exerciseIds;
        final String finalExerciseSeries = exerciseSeries;
        final String finalExerciseRepetitions = exerciseRepetitions;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, addMyTrainingExercisesActivity.getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        ToastUtils.shortToast(addMyTrainingExercisesActivity, "Dodano trening");
                        addMyTrainingExercisesActivity.openMeFragment();
                    } else
                        ToastUtils.shortToast(addMyTrainingExercisesActivity, "Błąd podczas dodawania treningu");
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.shortToast(addMyTrainingExercisesActivity, "Błąd podczas dodawania treningu " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.shortToast(addMyTrainingExercisesActivity, "Błąd podczas dodawania treningu " + error.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(addMyTrainingExercisesActivity);
        requestQueue.add(stringRequest);
    }
}
