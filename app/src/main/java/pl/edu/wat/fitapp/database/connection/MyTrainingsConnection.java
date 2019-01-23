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
import pl.edu.wat.fitapp.database.entity.Training;
import pl.edu.wat.fitapp.interfaces.callback.MyTrainingsConnectionCallback;
import pl.edu.wat.fitapp.mangement.MyTrainingManagement;
import pl.edu.wat.fitapp.R;

public class MyTrainingsConnection {
    private MyTrainingsConnectionCallback callback;
    private ArrayList<Training> myTrainings;

    public MyTrainingsConnection(MyTrainingsConnectionCallback callback, ArrayList<Training> myTrainings) {
        this.callback = callback;
        this.myTrainings = myTrainings;
    }

    public void getMyTrainings(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    MyTrainingManagement myTrainingMng = new MyTrainingManagement();
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            int trainingPosition = myTrainingMng.findTrainingInList(row.getInt("ID_MyTraining"), myTrainings);
                            if (trainingPosition == -1) {
                                Training tempTraining = new Training(row.getInt("ID_MyTraining"), row.getString("TrainingName"));
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("MySeries"));
                                tempExercise.setRepetitions(row.getInt("MyRepetitions"));
                                tempTraining.addExerciseToList(tempExercise);
                                myTrainings.add(tempTraining);
                            } else {
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("MySeries"));
                                tempExercise.setRepetitions(row.getInt("MyRepetitions"));
                                myTrainings.get(trainingPosition).addExerciseToList(tempExercise);
                            }
                        }
                        callback.onSuccessMyTrainings();
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
                params.put("operation", "getMyTrainings");
                params.put("userId", String.valueOf(userID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
