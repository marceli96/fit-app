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

import pl.edu.wat.fitapp.Main.Fragment.ExportFragment;


public class CaloricDemandWeekConnection {
    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";
    private Fragment fragment;
    private ArrayList<Integer> caloricDemandWeek;

    public CaloricDemandWeekConnection(Fragment fragment, ArrayList<Integer> caloricDemandWeek) {
        this.fragment = fragment;
        this.caloricDemandWeek = caloricDemandWeek;
    }

    public void getCaloricDemandFromWeek(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            caloricDemandWeek.add(row.getInt("CaloricDemend"));
                        }
                        if (fragment.getClass() == ExportFragment.class) {
                            ((ExportFragment) fragment).getGoalFromWeek();
                        }
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
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getCaloricDemendFromWeek");
                params.put("userId", String.valueOf(userID));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(fragment.getActivity());
        requestQueue.add(stringRequest);
    }
}
