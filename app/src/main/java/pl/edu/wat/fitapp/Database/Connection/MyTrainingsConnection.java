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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Main.Fragment.AddToSystem.AddTrainingToTrainingSystemFragment;
import pl.edu.wat.fitapp.Main.Fragment.Profile.ProfileFragment;
import pl.edu.wat.fitapp.Mangement.MyTrainingManagement;
import pl.edu.wat.fitapp.R;

public class MyTrainingsConnection {
    private Fragment fragment;
    private ArrayList<Training> myTrainings;

    public MyTrainingsConnection(Fragment fragment, ArrayList<Training> myTrainings) {
        this.fragment = fragment;
        this.myTrainings = myTrainings;
    }

    public void getMyTrainings(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fragment.getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
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
                        if(fragment.getClass() == ProfileFragment.class)
                            ((ProfileFragment) fragment).showMyTrainings();
                        else if(fragment.getClass() == AddTrainingToTrainingSystemFragment.class)
                            ((AddTrainingToTrainingSystemFragment) fragment).showMyTrainings();
                    } else
                        Toast.makeText(fragment.getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(fragment.getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("operation", "getMyTrainings");
                params.put("userId", String.valueOf(userID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity());
        requestQueue.add(stringRequest);
    }
}
