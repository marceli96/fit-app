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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.database.entity.Training;
import pl.edu.wat.fitapp.interfaces.TrainingSystem;
import pl.edu.wat.fitapp.interfaces.callback.TrainingSystemDayConnectionCallback;
import pl.edu.wat.fitapp.mangement.TrainingSystemDayManagement;
import pl.edu.wat.fitapp.R;

public class TrainingSystemDayConnection {
    private TrainingSystemDayConnectionCallback callback;
    private ArrayList<TrainingSystem> trainingSystemDay;

    public TrainingSystemDayConnection(TrainingSystemDayConnectionCallback callback, ArrayList<TrainingSystem> trainingSystemDay) {
        this.callback = callback;
        this.trainingSystemDay = trainingSystemDay;
    }

    public void getTrainingSystem(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    TrainingSystemDayManagement trainingSystemDayManagement = new TrainingSystemDayManagement();
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            if (row.getString("type").equals("training")) {
                                int trainingPosition = trainingSystemDayManagement.checkTrainingPositionInList(row.getInt("ID_MyTraining"), trainingSystemDay);
                                if (trainingPosition == -1) {
                                    Training tempTraining = new Training(row.getInt("ID_MyTraining"), row.getString("TrainingName"));
                                    Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                    tempExercise.setSeries(row.getInt("Series"));
                                    tempExercise.setRepetitions(row.getInt("Repetitions"));
                                    tempTraining.addExerciseToList(tempExercise);
                                    trainingSystemDay.add(tempTraining);
                                } else {
                                    Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                    tempExercise.setSeries(row.getInt("Series"));
                                    tempExercise.setRepetitions(row.getInt("Repetitions"));
                                    trainingSystemDayManagement.updateTrainingInTrainingSystemList(trainingPosition, tempExercise, trainingSystemDay);
                                }
                            } else {
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("Series"));
                                tempExercise.setRepetitions(row.getInt("Repetitions"));
                                trainingSystemDay.add(tempExercise);
                            }
                        }
                        callback.onSuccessTrainingSystemDay();
                    } else
                        callback.onFailure(callback.activity().getString(R.string.connectionError));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(callback.activity().getString(R.string.connectionError) + e.toString());
                }
            }
        }, new Response.ErrorListener() {
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
                params.put("operation", "getTrainingSystemFromDay");
                params.put("userId", String.valueOf(userID));
                params.put("date", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
