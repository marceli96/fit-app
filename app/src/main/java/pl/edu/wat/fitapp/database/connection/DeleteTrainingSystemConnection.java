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

import pl.edu.wat.fitapp.androidComponent.listAdapter.TrainingSystemListAdapter;
import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.interfaces.TrainingSystem;
import pl.edu.wat.fitapp.interfaces.callback.TrainingSystemCallback;
import pl.edu.wat.fitapp.R;

public class DeleteTrainingSystemConnection {
    private TrainingSystemCallback callback;
    private ArrayList<TrainingSystem> trainingSystemDay;
    private TrainingSystemListAdapter trainingSystemListAdapter;

    public DeleteTrainingSystemConnection(TrainingSystemCallback callback, ArrayList<TrainingSystem> trainingSystemDay, TrainingSystemListAdapter trainingSystemListAdapter) {
        this.callback = callback;
        this.trainingSystemDay = trainingSystemDay;
        this.trainingSystemListAdapter = trainingSystemListAdapter;
    }

    public void deleteFromTrainingSystem(final TrainingSystem training, final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, callback.activity().getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        callback.onSuccessTrainingSystem(training);
                    } else
                        callback.onFailure(callback.activity().getString(R.string.eraseError));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(callback.activity().getString(R.string.eraseError) + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(callback.activity().getString(R.string.eraseError) + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(callback.activity().getString(R.string.formatDate));
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
        RequestQueue requestQueue = Volley.newRequestQueue(callback.activity());
        requestQueue.add(stringRequest);
    }
}
