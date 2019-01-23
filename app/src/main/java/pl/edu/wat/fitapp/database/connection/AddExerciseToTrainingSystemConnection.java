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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.View.Main.Fragment.AddToSystem.AddExerciseToTrainingSystemFragment;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class AddExerciseToTrainingSystemConnection {
    private Fragment fragment;

    public AddExerciseToTrainingSystemConnection(Fragment fragment) {
        this.fragment = fragment;
    }

    public void addExerciseToTrainingSystem(final int exerciseId, final int userID, final String series, final String repetitions) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fragment.getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean message = jsonResponse.getBoolean("message");
                    if (!message) {
                        ToastUtils.shortToast(fragment.getActivity(), "Dane ćwiczenie zostało już dodane w tym dniu");
                    } else {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            ToastUtils.shortToast(fragment.getActivity(), "Dodano pomyślnie");
                            if(fragment.getClass() == AddExerciseToTrainingSystemFragment.class)
                                ((AddExerciseToTrainingSystemFragment) fragment).openMainActivity();
                        } else
                            ToastUtils.shortToast(fragment.getActivity(),"Błąd podczas dodawania");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.shortToast(fragment.getActivity(), "Błąd podczas dodawania " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.shortToast(fragment.getActivity(), "Błąd podczas dodawania " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "addExerciseToTrainingSystem");
                params.put("userId", String.valueOf(userID));
                params.put("exerciseId", String.valueOf(exerciseId));
                params.put("repetitions", repetitions);
                params.put("series", series);
                params.put("date", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity());
        requestQueue.add(stringRequest);
    }

}
