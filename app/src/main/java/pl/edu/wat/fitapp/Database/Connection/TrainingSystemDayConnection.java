package pl.edu.wat.fitapp.Database.Connection;

import android.view.View;
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

import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Interface.TrainingSystem;
import pl.edu.wat.fitapp.Main.Fragment.HomeFragment;
import pl.edu.wat.fitapp.Mangement.TrainingSystemDayManagement;

public class TrainingSystemDayConnection {
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";
    private HomeFragment homeFragment;
    private ArrayList<TrainingSystem> trainingSystemDay;

    public TrainingSystemDayConnection(HomeFragment homeFragment, ArrayList<TrainingSystem> trainingSystemDay) {
        this.homeFragment = homeFragment;
        this.trainingSystemDay = trainingSystemDay;
    }

    public void getTrainingSystem(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
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
                        homeFragment.getLlTraining().setVisibility(View.VISIBLE);
                        homeFragment.getTrainingSystemListAdapter().notifyDataSetChanged();
                        homeFragment.updateExerciseAmount();
                    } else
                        Toast.makeText(homeFragment.getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(homeFragment.getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(homeFragment.getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getTrainingSystemFromDay");
                params.put("userId", String.valueOf(userID));
                params.put("date", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(homeFragment.getActivity());
        requestQueue.add(stringRequest);
    }
}
