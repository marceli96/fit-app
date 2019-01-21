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

import pl.edu.wat.fitapp.Charts.JournalChartsWeightWeek;
import pl.edu.wat.fitapp.Main.Fragment.JournalFragment;
import pl.edu.wat.fitapp.R;

public class WeightConnection {
    private JournalFragment journalFragment;
    private final String OPERATIONS_URL = journalFragment.getString(R.string.OPERATIONS_URL);
    private ArrayList<Double> weightWeek;

    public WeightConnection(JournalFragment journalFragment, ArrayList<Double> weightWeek) {
        this.journalFragment = journalFragment;
        this.weightWeek = weightWeek;
    }

    public void getWeightFromDay(final int userID, final String date) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        if (jsonResponse.length() == 2) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(0));
                            double weightDay = Double.parseDouble(row.getString("UserWeight"));
                            journalFragment.setWeightDay(weightDay);
                            journalFragment.getTvWeightDay().setText(String.valueOf(weightDay));
                            journalFragment.getLlWeightDay().setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(journalFragment.getActivity(), "Błąd podczas pobierania wagi z dnia", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(journalFragment.getActivity(), "Błąd połączenia z bazą! " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(journalFragment.getActivity(), "Błąd połączenia z bazą! " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getWeight");
                params.put("userId", String.valueOf(userID));
                params.put("date", date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(journalFragment.getActivity());
        requestQueue.add(stringRequest);
    }

    public void getWeightFromWeek(final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            weightWeek.add(row.getDouble("UserWeight"));
                        }
                        journalFragment.setWeightWeek(weightWeek);
                        JournalChartsWeightWeek journalChartsWeightWeek = new JournalChartsWeightWeek(journalFragment, weightWeek);
                        journalChartsWeightWeek.drawChartsWeightWeek();
                    } else
                        Toast.makeText(journalFragment.getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(journalFragment.getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(journalFragment.getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                params.put("operation", "getWeightFromWeek");
                params.put("userId", String.valueOf(userID));
                params.put("dateNow", dateFormat.format(date));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(journalFragment.getActivity());
        requestQueue.add(stringRequest);
    }
}
