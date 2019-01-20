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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.TrainingSystemListAdapter;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Interface.TrainingSystem;
import pl.edu.wat.fitapp.Main.Fragment.HomeFragment;

public class DeleteTrainingSystemConnection {
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";
    private HomeFragment homeFragment;
    private ArrayList<TrainingSystem> trainingSystemDay;
    private TrainingSystemListAdapter trainingSystemListAdapter;

    public DeleteTrainingSystemConnection(HomeFragment homeFragment, ArrayList<TrainingSystem> trainingSystemDay, TrainingSystemListAdapter trainingSystemListAdapter) {
        this.homeFragment = homeFragment;
        this.trainingSystemDay = trainingSystemDay;
        this.trainingSystemListAdapter = trainingSystemListAdapter;
    }

    public void deleteFromTrainingSystem(final TrainingSystem training, final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(homeFragment.getActivity(), "Pomyślnie usunięto", Toast.LENGTH_SHORT).show();
                        trainingSystemDay.remove(training);
                        trainingSystemListAdapter.notifyDataSetChanged();
                        homeFragment.updateExerciseAmount();
                    } else
                        Toast.makeText(homeFragment.getActivity(), "Błąd podczas usuwania", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(homeFragment.getActivity(), "Błąd podczas usuwania " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(homeFragment.getActivity(), "Błąd podczas usuwania " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("userId", String.valueOf(userID));
                params.put("date", dateFormat.format(date));
                if (training.getClass() == Exercise.class) {
                    params.put("operation", "deleteExerciseFromTrainingSystem");
                    params.put("exerciseId", String.valueOf(training.getID()));

                } else {
                    params.put("operation", "deleteTrainingFromTrainingSystem");
                    params.put("myTrainingId", String.valueOf(training.getID()));
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(homeFragment.getActivity());
        requestQueue.add(stringRequest);
    }
}