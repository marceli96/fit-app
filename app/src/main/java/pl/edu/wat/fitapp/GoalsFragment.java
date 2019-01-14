package pl.edu.wat.fitapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class GoalsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText etWeight;
    TextView tvCaloricDemand;
    private Spinner spinnerLevelActivity;
    private RadioButton rbLose, rbKeep, rbGain;
    private RadioGroup rgGoal;
    private Button bCount, bSave;

    private User user;
    private View v;

    private int calories;
    private String activityLevel;

    private ArrayAdapter<CharSequence> adapter;

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    public GoalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_goals, container, false);
        v = view;

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        etWeight = view.findViewById(R.id.etWeight);
        tvCaloricDemand = view.findViewById(R.id.tvCaloricDemand);
        spinnerLevelActivity = view.findViewById(R.id.spinnerLevelActivity);
        rgGoal = view.findViewById(R.id.rgGoal);
        rbLose = view.findViewById(R.id.rbLose);
        rbKeep = view.findViewById(R.id.rbKeep);
        rbGain = view.findViewById(R.id.rbGain);
        bCount = view.findViewById(R.id.bCount);
        bSave = view.findViewById(R.id.bSave);

        etWeight.setText(String.valueOf(user.getWeight()));

        switch (user.getGoal()) {
            case 0:
                rbLose.setChecked(true);
                break;
            case 1:
                rbKeep.setChecked(true);
                break;
            case 2:
                rbGain.setChecked(true);
                break;
        }

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.activityLevel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevelActivity.setAdapter(adapter);
        spinnerLevelActivity.setOnItemSelectedListener(this);


        bCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        activityLevel = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void calculateCalories() {
        String goal = getRadioButtonText(rgGoal);
        int sex = user.getSex();
        int age = user.getAge();
        double weight = Double.parseDouble(etWeight.getText().toString());
        int height = user.getHeight();

        if (weight > 0)
            switch (sex) {
                case 0:
                    calories = (int) Math.round(655 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
                    if (activityLevel.equals("Brak"))
                        calories *= 1.2;
                    else if (activityLevel.equals("Niska"))
                        calories *= 1.3;
                    else if (activityLevel.equals("Średnia"))
                        calories *= 1.5;
                    else if (activityLevel.equals("Wysoka"))
                        calories *= 1.7;
                    else if (activityLevel.equals("Bardzo wysoka"))
                        calories *= 1.9;

                    if (goal.equals("Utrata"))
                        calories -= 250;
                    else if (goal.equals("Przybranie"))
                        calories += 250;

                    tvCaloricDemand.setText(String.valueOf(calories));
                    break;
                case 1:
                    calories = (int) Math.round(66 + (13.7 * weight) + (5 * height) - (6.76 * age));
                    if (activityLevel.equals("Brak"))
                        calories *= 1.2;
                    else if (activityLevel.equals("Niska"))
                        calories *= 1.3;
                    else if (activityLevel.equals("Średnia"))
                        calories *= 1.5;
                    else if (activityLevel.equals("Wysoka"))
                        calories *= 1.7;
                    else if (activityLevel.equals("Bardzo wysoka"))
                        calories *= 1.9;

                    if (goal.equals("Utrata"))
                        calories -= 250;
                    else if (goal.equals("Przybranie"))
                        calories += 250;

                    tvCaloricDemand.setText(String.valueOf(calories));
                    break;
            }

        else
            Toast.makeText(getActivity(), "Masa ciała musi być dodatnia", Toast.LENGTH_SHORT).show();
    }

    public void save() {
        if (!etWeight.getText().toString().isEmpty()) {
            final int userID = user.getUserID();
            final double weight = Double.parseDouble(etWeight.getText().toString());
            final int goal = getGoalInt(getRadioButtonText(rgGoal));

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            final String date = df.format(Calendar.getInstance().getTime());

            calculateCalories();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean successUser = jsonResponse.getBoolean("successUser");
                                boolean successWeight = jsonResponse.getBoolean("successWeight");
                                if (successUser && successWeight) {
                                    Toast.makeText(getActivity(), "Dane zaaktualizowane ", Toast.LENGTH_SHORT).show();
                                    openHomeActivty();
                                } else
                                    Toast.makeText(getActivity(), "Nieoczkiwany błąd, powtórz wcześniej wprowadzone zmiany", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Błąd połączenia z bazą" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("operation", "setWeightGoalActivityLevel");
                    params.put("userId", String.valueOf(userID));
                    params.put("userWeight", String.valueOf(weight));
                    params.put("weightDate", String.valueOf(date));
                    params.put("caloricDemend", String.valueOf(calories));
                    params.put("goal", String.valueOf(goal));
                    params.put("activityLevel", String.valueOf(getActivityLevelInt()));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        } else
            Toast.makeText(getActivity(), "Wprowadź wagę", Toast.LENGTH_SHORT).show();
    }

    public String getRadioButtonText(RadioGroup rg) {
        int radioId = rg.getCheckedRadioButtonId();
        RadioButton rb = v.findViewById(radioId);
        return rb.getText().toString();
    }

    public int getGoalInt(String goal) {
        if (goal.equals("Utrata"))
            return 0;
        else if (goal.equals("Przybranie"))
            return 1;
        else
            return 2;
    }

    public int getActivityLevelInt() {
        if (activityLevel.equals("Brak"))
            return 0;
        else if (activityLevel.equals("Niska"))
            return 1;
        else if (activityLevel.equals("Średnia"))
            return 2;
        else if (activityLevel.equals("Wysoka"))
            return 3;
        else
            return 4;
    }

    public void openHomeActivty() {
        Intent openHomeScreen = new Intent(getActivity(), MainActivity.class);
        openHomeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openHomeScreen.putExtra("user", user);
        startActivity(openHomeScreen);
    }

}
