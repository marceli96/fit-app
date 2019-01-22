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
import pl.edu.wat.fitapp.View.Main.Fragment.HomeFragment;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class DeleteTrainingSystemConnection {
    private HomeFragment homeFragment;
    private ArrayList<TrainingSystem> trainingSystemDay;
    private TrainingSystemListAdapter trainingSystemListAdapter;

    public DeleteTrainingSystemConnection(HomeFragment homeFragment, ArrayList<TrainingSystem> trainingSystemDay, TrainingSystemListAdapter trainingSystemListAdapter) {
        this.homeFragment = homeFragment;
        this.trainingSystemDay = trainingSystemDay;
        this.trainingSystemListAdapter = trainingSystemListAdapter;
    }

    public void deleteFromTrainingSystem(final TrainingSystem training, final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, homeFragment.getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        ToastUtils.shortToast(homeFragment.getActivity(), "Pomyślnie usunięto");
                        trainingSystemDay.remove(training);
                        trainingSystemListAdapter.notifyDataSetChanged();
                        homeFragment.updateExerciseAmount();
                    } else
                        ToastUtils.shortToast(homeFragment.getActivity(), "Błąd podczas usuwania");
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.shortToast(homeFragment.getActivity(), "Błąd podczas usuwania " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.shortToast(homeFragment.getActivity(), "Błąd podczas usuwania " + error.toString());
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
