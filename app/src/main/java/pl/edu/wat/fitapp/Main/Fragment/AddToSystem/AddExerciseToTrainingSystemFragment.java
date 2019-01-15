package pl.edu.wat.fitapp.Main.Fragment.AddToSystem;


import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;


public class AddExerciseToTrainingSystemFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private ListView lvExercises;
    private ArrayList<Exercise> exerciseList;
    private ExercisesAdapter exercisesAdapter;

    private User user;

    public AddExerciseToTrainingSystemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = (User) getActivity().getIntent().getSerializableExtra("user");

        View view = inflater.inflate(R.layout.fragment_add_exercise_to_training_system, container, false);

        exerciseList = new ArrayList<>();
        getExercises();

        lvExercises = view.findViewById(R.id.lvExercises);
        exercisesAdapter = new ExercisesAdapter(getActivity(), R.layout.listview_adapter_exercise, exerciseList);
        lvExercises.setAdapter(exercisesAdapter);

        lvExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View alertView = getLayoutInflater().inflate(R.layout.dialog_choose_series_repetitions_exercise, null);

                TextView tvExerciseName = alertView.findViewById(R.id.tvExerciseName);
                final EditText etSeries = alertView.findViewById(R.id.etSeries);
                final EditText etRepetitions = alertView.findViewById(R.id.etRepetitions);
                Button bAddExerciseToTraining = alertView.findViewById(R.id.bAddExerciseToTraining);

                tvExerciseName.setText(exerciseList.get(position).getName());

                builder.setView(alertView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                bAddExerciseToTraining.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String series = etSeries.getText().toString();
                        String repetitions = etRepetitions.getText().toString();
                        if (!series.isEmpty() && !repetitions.isEmpty()) {
                            addExerciseToTrainingSystem(exerciseList.get(position).getID(), user.getUserID(), series, repetitions);
                            dialog.dismiss();
                        } else {
                            if (series.isEmpty())
                                Toast.makeText(getActivity(), "Wpisz liczbę serii!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), "Wpisz liczbę powtórzeń!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }

    private void addExerciseToTrainingSystem(final int exerciseId, final int userID, final String series, final String repetitions) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean message = jsonResponse.getBoolean("message");
                    if (!message) {
                        Toast.makeText(getActivity(), "Dane ćwiczenie zostało już dodane w tym dniu", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Toast.makeText(getActivity(), "Dodano pomyślnie", Toast.LENGTH_SHORT).show();
                            openMainActivity();
                        } else
                            Toast.makeText(getActivity(), "Błąd podczas dodawania", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd podczas dodawania " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd podczas dodawania " + error.toString(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void openMainActivity() {
        Intent openMainActivity = new Intent(getContext(), MainActivity.class);
        openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
    }

    private void getExercises() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject exercise = jsonResponse.getJSONObject(String.valueOf(i));
                            exerciseList.add(new Exercise(exercise.getInt("ID_Exercise"), exercise.getString("ExerciseName")));
                        }
                        exercisesAdapter.notifyDataSetChanged();
                    } else
                        Toast.makeText(getActivity(), "Błąd połączenia z bazą", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Błąd połączenia z bazą " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Błąd połączenia z bazą " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getExercises");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    class ExercisesAdapter extends ArrayAdapter<Exercise> {

        public ExercisesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exercise> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_exercise, parent, false);

            TextView tvExerciseName = convertView.findViewById(R.id.tvExerciseName);

            tvExerciseName.setText(exerciseList.get(position).getName());

            return convertView;
        }
    }

}
