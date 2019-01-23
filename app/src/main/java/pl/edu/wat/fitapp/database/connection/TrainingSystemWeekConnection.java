package pl.edu.wat.fitapp.database.connection;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.database.entity.Training;
import pl.edu.wat.fitapp.interfaces.TrainingSystem;
import pl.edu.wat.fitapp.interfaces.callback.TrainingSystemWeekConnectionCallback;
import pl.edu.wat.fitapp.mangement.TrainingSystemWeekManagement;
import pl.edu.wat.fitapp.R;

public class TrainingSystemWeekConnection {
    private TrainingSystemWeekConnectionCallback callback;
    private ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek;

    public TrainingSystemWeekConnection(TrainingSystemWeekConnectionCallback callback, ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek) {
        this.callback = callback;
        this.trainingSystemWeek = trainingSystemWeek;
    }

    public void getTrainingSystemFromWeek(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    TrainingSystemWeekManagement trainingSystemWeekManagement = new TrainingSystemWeekManagement();
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 3; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = row.getString("TrainingDate");
                            Date date = sdf.parse(dateString);
                            if (row.getString("type").equals("training")) {
                                int trainingPosition = trainingSystemWeekManagement.checkTrainingPositionInListForDate(row.getInt("ID_MyTraining"), date, trainingSystemWeek);
                                if (trainingPosition == -1) {
                                    Training tempTraining = new Training(row.getInt("ID_MyTraining"), row.getString("TrainingName"));
                                    Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                    tempExercise.setSeries(row.getInt("Series"));
                                    tempExercise.setRepetitions(row.getInt("Repetitions"));
                                    tempTraining.addExerciseToList(tempExercise);
                                    trainingSystemWeekManagement.addTrainingToTrainingSystemListForDate(tempTraining, date, trainingSystemWeek);
                                } else {
                                    Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                    tempExercise.setSeries(row.getInt("Series"));
                                    tempExercise.setRepetitions(row.getInt("Repetitions"));
                                    trainingSystemWeekManagement.updateTrainingInTrainingSystemListForDate(trainingPosition, tempExercise, date, trainingSystemWeek);
                                }
                            } else {
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("Series"));
                                tempExercise.setRepetitions(row.getInt("Repetitions"));
                                trainingSystemWeekManagement.addExerciseToFoodSystemListForDate(tempExercise, date, trainingSystemWeek);
                            }
                        }
                        callback.onSuccessTrainingSystemWeek();
                    } else
                        callback.onFailure("Błąd połączenia z bazą");
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure("Błąd połączenia z bazą " + e.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
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
                params.put("operation", "getTrainingSystemFromWeek");
                params.put("userId", String.valueOf(userID));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
