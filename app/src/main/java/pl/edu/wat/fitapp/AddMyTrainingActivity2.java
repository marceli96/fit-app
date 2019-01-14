package pl.edu.wat.fitapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddMyTrainingActivity2 extends AppCompatActivity {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private Button bAddMyTraining;
    private LinearLayout llShowListView;
    private ImageView imArrow;
    private TextView tvExerciseAmount;
    private ListView lvTrainingExercises, lvExercises;

    private ExercisesAdapter exercisesAdapter;
    private TrainingExercisesAdapter trainingExercisesAdapter;

    private String trainingName;
    private ArrayList<Exercise> exercises, trainingExercises;
    private boolean hidden = true;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_training2);

        user = (User) getIntent().getSerializableExtra("user");
        trainingName = (String) getIntent().getSerializableExtra("trainingName");

        exercises = new ArrayList<>();
        trainingExercises = new ArrayList<>();
        getExercises();

        bAddMyTraining = findViewById(R.id.bAddMyTraining);
        llShowListView = findViewById(R.id.llShowListView);
        imArrow = findViewById(R.id.imArrow);
        tvExerciseAmount = findViewById(R.id.tvExerciseAmount);
        lvTrainingExercises = findViewById(R.id.lvTrainingExercies);
        lvExercises = findViewById(R.id.lvExercises);

        bAddMyTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMyTraining();
            }
        });

        exercisesAdapter = new ExercisesAdapter(AddMyTrainingActivity2.this, R.layout.listview_adapter_exercise, exercises);
        lvExercises.setAdapter(exercisesAdapter);
        trainingExercisesAdapter = new TrainingExercisesAdapter(AddMyTrainingActivity2.this, R.layout.listview_adapter_exercise_with_series_repetitions, trainingExercises);
        lvTrainingExercises.setAdapter(trainingExercisesAdapter);

        lvExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(AddMyTrainingActivity2.this, "Wybrales skladnik o nazwie = " + exercises.get(position).getName(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(AddMyTrainingActivity2.this);
                View alertView = getLayoutInflater().inflate(R.layout.dialog_choose_series_repetitions_exercise, null);

                TextView tvExerciseName = alertView.findViewById(R.id.tvExerciseName);
                final EditText etSeries = alertView.findViewById(R.id.etSeries);
                final EditText etRepetitions = alertView.findViewById(R.id.etRepetitions);
                Button bAddExerciseToTraining = alertView.findViewById(R.id.bAddExerciseToTraining);

                tvExerciseName.setText(exercises.get(position).getName());

                builder.setView(alertView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                bAddExerciseToTraining.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String series = etSeries.getText().toString();
                        String repetitions = etRepetitions.getText().toString();
                        if (!series.isEmpty() && !repetitions.isEmpty()) {
                            Exercise tempExercise = null;
                            try {
                                tempExercise = (Exercise) exercises.get(position).clone();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            tempExercise.setSeries(Integer.parseInt(series));
                            tempExercise.setRepetitions(Integer.parseInt(repetitions));
                            addExercise(tempExercise);
                            tvExerciseAmount.setText(String.valueOf(trainingExercises.size()));
                            dialog.dismiss();
                        } else {
                            if (series.isEmpty())
                                Toast.makeText(AddMyTrainingActivity2.this, "Wpisz liczbę serii!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AddMyTrainingActivity2.this, "Wpisz liczbę powtórzeń!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        llShowListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hidden) {
                    lvTrainingExercises.setVisibility(View.VISIBLE);
                    hidden = false;
                    imArrow.setImageResource(R.drawable.arrow_down);
                } else {
                    lvTrainingExercises.setVisibility(View.GONE);
                    hidden = true;
                    imArrow.setImageResource(R.drawable.arrow_up);
                }
            }
        });

    }

    private void addMyTraining() {
        if (trainingExercises.size() > 0) {
            String exerciseIds = "";
            String exerciseSeries = "";
            String exerciseRepetitions = "";
            for (int i = 0; i < trainingExercises.size(); i++) {
                if (i == trainingExercises.size() - 1) {
                    exerciseIds += String.valueOf(trainingExercises.get(i).getID());
                    exerciseSeries += String.valueOf(trainingExercises.get(i).getSeries());
                    exerciseRepetitions += String.valueOf(trainingExercises.get(i).getRepetitions());
                } else {
                    exerciseIds += String.valueOf(trainingExercises.get(i).getID()) + "/";
                    exerciseSeries += String.valueOf(trainingExercises.get(i).getSeries()) + "/";
                    exerciseRepetitions += String.valueOf(trainingExercises.get(i).getRepetitions()) + "/";
                }
            }

            final String finalExerciseIds = exerciseIds;
            final String finalExerciseSeries = exerciseSeries;
            final String finalExerciseRepetitions = exerciseRepetitions;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            Toast.makeText(AddMyTrainingActivity2.this, "Dodano trening", Toast.LENGTH_SHORT).show();
                            openMeFragment();
                        } else
                            Toast.makeText(AddMyTrainingActivity2.this, "Błąd podczas dodawania treningu", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(AddMyTrainingActivity2.this, "Błąd podczas dodawania treningu " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddMyTrainingActivity2.this, "Błąd podczas dodawania treningu " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("operation", "addMyTraining");
                    params.put("userId", String.valueOf(user.getUserID()));
                    params.put("trainingName", trainingName);
                    params.put("exerciseIds", finalExerciseIds);
                    params.put("exerciseSeries", finalExerciseSeries);
                    params.put("exerciseRepetitions", finalExerciseRepetitions);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(AddMyTrainingActivity2.this);
            requestQueue.add(stringRequest);
        } else
            Toast.makeText(AddMyTrainingActivity2.this, "Najpierw dodaj ćwiczenia do treningu", Toast.LENGTH_SHORT).show();
    }

    private void openMeFragment() {
        Intent openMeFragment = new Intent(AddMyTrainingActivity2.this, MainActivity.class);
        openMeFragment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMeFragment.putExtra("user", user);
        openMeFragment.putExtra("action", "openMeFragment");
        startActivity(openMeFragment);
    }

    private void addExercise(Exercise exercise) {
        for (int i = 0; i < trainingExercises.size(); i++) {
            if (trainingExercises.get(i).getID() == exercise.getID()) {
                trainingExercises.get(i).setSeries(trainingExercises.get(i).getSeries() + exercise.getSeries());
                trainingExercises.get(i).setRepetitions(trainingExercises.get(i).getRepetitions() + exercise.getRepetitions());
                trainingExercisesAdapter.notifyDataSetChanged();
                return;
            }
        }
        trainingExercises.add(exercise);
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
                            exercises.add(new Exercise(exercise.getInt("ID_Exercise"), exercise.getString("ExerciseName")));
                        }
                        exercisesAdapter.notifyDataSetChanged();
                    } else
                        Toast.makeText(AddMyTrainingActivity2.this, "Wystąpił błąd podczas pobierania ćwiczeń", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddMyTrainingActivity2.this, "Wystąpił błąd podczas pobierania ćwiczeń " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddMyTrainingActivity2.this, "Wystąpił błąd podczas pobierania ćwiczeń " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getExercises");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddMyTrainingActivity2.this);
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

            tvExerciseName.setText(exercises.get(position).getName());

            return convertView;
        }
    }

    class TrainingExercisesAdapter extends ArrayAdapter<Exercise> {

        public TrainingExercisesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exercise> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_exercise_with_series_repetitions, parent, false);

            TextView tvExerciseName = convertView.findViewById(R.id.tvExerciseName);
            TextView tvSeries = convertView.findViewById(R.id.tvSeries);
            TextView tvRepetitions = convertView.findViewById(R.id.tvRepetitions);
            ImageView imDeleteExercise = convertView.findViewById(R.id.imDeleteExercise);

            final Exercise exercise = trainingExercises.get(position);

            tvExerciseName.setText(exercise.getName());
            tvSeries.setText(String.valueOf(exercise.getSeries()));
            tvRepetitions.setText(String.valueOf(exercise.getRepetitions()));

            imDeleteExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trainingExercises.remove(exercise);
                    trainingExercisesAdapter.notifyDataSetChanged();
                    tvExerciseAmount.setText(String.valueOf(trainingExercises.size()));
                }
            });

            return convertView;
        }
    }
}
