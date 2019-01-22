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

import pl.edu.wat.fitapp.Main.Fragment.AddToSystem.AddTrainingToTrainingSystemFragment;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class AddTrainingToTrainingSystemConnection {
    private Fragment fragment;

    public AddTrainingToTrainingSystemConnection(Fragment fragment) {
        this.fragment = fragment;
    }

    public void addTrainingToTrainingSystem(final int trainingId, final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fragment.getString(R.string.OPERATIONS_URL), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean message = jsonResponse.getBoolean("message");
                    if (!message) {
                        ToastUtils.shortToast(fragment.getActivity(), "Dany trening został już dodany w tym dniu");
                    } else {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            ToastUtils.shortToast(fragment.getActivity(), "Dodano pomyślnie");
                            if(fragment.getClass() == AddTrainingToTrainingSystemFragment.class)
                                ((AddTrainingToTrainingSystemFragment) fragment).openMainActivity();
                        } else
                            ToastUtils.shortToast(fragment.getActivity(), "Błąd podczas dodawania");
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
                params.put("operation", "addTrainingToTrainingSystem");
                params.put("userId", String.valueOf(userID));
                params.put("myTrainingId", String.valueOf(trainingId));
                params.put("date", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity());
        requestQueue.add(stringRequest);
    }

}
