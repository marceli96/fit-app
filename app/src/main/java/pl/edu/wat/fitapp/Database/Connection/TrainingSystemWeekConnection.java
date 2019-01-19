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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Interface.TrainingSystem;
import pl.edu.wat.fitapp.Main.Fragment.ExportFragment;
import pl.edu.wat.fitapp.Mangement.TrainingSystemWeekManagement;

public class TrainingSystemWeekConnection {
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";
    private Fragment fragment;
    private ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek;

    public TrainingSystemWeekConnection(Fragment fragment, ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek) {
        this.fragment = fragment;
        this.trainingSystemWeek = trainingSystemWeek;
    }

    public void getTrainingSystemFromWeek(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
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
                        if(fragment.getClass() == ExportFragment.class){
                            ((ExportFragment) fragment).getCaloricDemandFromWeek();
                        }
                    } else
                        Toast.makeText(fragment.getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(fragment.getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(fragment.getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity());
        requestQueue.add(stringRequest);
    }
}
