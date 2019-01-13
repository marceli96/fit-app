package pl.edu.wat.fitapp;


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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class AddTrainingToTrainingSystemFragment extends Fragment {

    private final String OPERATIONS_URL = "http://fitappliaction.cba.pl/operations.php";

    private ListView lvTrainings;
    private ArrayList<Training> trainingList;
    private TrainingListAdapter trainingListAdapter;
    private User user;

    public AddTrainingToTrainingSystemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_training_to_training_system, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        trainingList = new ArrayList<>();
        getTrainings();

        lvTrainings = view.findViewById(R.id.lvTrainings);
        trainingListAdapter = new TrainingListAdapter(getActivity(), R.layout.listview_adapter_training, trainingList);
        lvTrainings.setAdapter(trainingListAdapter);

        lvTrainings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(getActivity(), "Wybrales = " + trainingList.get(position).getName(), Toast.LENGTH_SHORT).show();

                View alertView = getLayoutInflater().inflate(R.layout.dialog_add_training_to_training_system, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                TextView tvTrainingName = alertView.findViewById(R.id.tvTrainingName);
                TextView tvExerciseAmount = alertView.findViewById(R.id.tvExerciseAmount);
                ListView lvTrainingExercises = alertView.findViewById(R.id.lvTrainingExercises);
                Button bAddTrainingToTrainingSystem = alertView.findViewById(R.id.bAddTrainingToTrainingSystem);

                tvTrainingName.setText(trainingList.get(position).getName());
                tvExerciseAmount.setText(String.valueOf(trainingList.get(position).getExerciseList().size()));

                ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(getActivity(), R.layout.listview_adapter_exercise_with_series_repetitions_simple, trainingList.get(position).getExerciseList());
                lvTrainingExercises.setAdapter(exercisesListAdapter);

                builder.setView(alertView);
                AlertDialog dialog = builder.create();
                dialog.show();

                bAddTrainingToTrainingSystem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTrainingToTrainingSystem(trainingList.get(position).getID(), user.getUserID());
                    }
                });
            }
        });

        return view;
    }

    private void addTrainingToTrainingSystem(final int trainingId, final int userID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean message = jsonResponse.getBoolean("message");
                    if (!message) {
                        Toast.makeText(getActivity(), "Dany trening został już dodany w tym dniu", Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void openMainActivity() {
        Intent openMainActivity = new Intent(getContext(), MainActivity.class);
        openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
    }

    private void getTrainings() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, OPERATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        for (int i = 0; i < jsonResponse.length() - 1; i++) {
                            JSONObject row = jsonResponse.getJSONObject(String.valueOf(i));
                            int trainingPosition = findTrainingInList(row.getInt("ID_MyTraining"));
                            if (trainingPosition == -1) {
                                Training tempTraining = new Training(row.getInt("ID_MyTraining"), row.getString("TrainingName"));
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("MySeries"));
                                tempExercise.setRepetitions(row.getInt("MyRepetitions"));
                                tempTraining.addExerciseToList(tempExercise);
                                trainingList.add(tempTraining);
                            } else {
                                Exercise tempExercise = new Exercise(row.getInt("ID_Exercise"), row.getString("ExerciseName"));
                                tempExercise.setSeries(row.getInt("MySeries"));
                                tempExercise.setRepetitions(row.getInt("MyRepetitions"));
                                trainingList.get(trainingPosition).addExerciseToList(tempExercise);
                            }
                        }
                        trainingListAdapter.notifyDataSetChanged();
                    } else
                        Toast.makeText(getActivity(), "Blad podczas pobierania treningów", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Blad podczas pobierania treningów " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Blad podczas pobierania treningów " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("operation", "getMyTrainings");
                params.put("userId", String.valueOf(user.getUserID()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private int findTrainingInList(int trainingId) {
        for (int i = 0; i < trainingList.size(); i++) {
            if (trainingList.get(i).getID() == trainingId)
                return i;
        }
        return -1;
    }

    class TrainingListAdapter extends ArrayAdapter<Training> {

        public TrainingListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Training> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_training_2, parent, false);

            TextView tvTrainingName = convertView.findViewById(R.id.tvTrainingName);
            TextView tvExercisesAmount = convertView.findViewById(R.id.tvExercisesAmount);

            tvTrainingName.setText(trainingList.get(position).getName());
            tvExercisesAmount.setText(String.valueOf(trainingList.get(position).getExerciseList().size()));

            return convertView;
        }
    }

    class ExercisesListAdapter extends ArrayAdapter<Exercise> {
        private ArrayList<Exercise> exerciseList;

        public ExercisesListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exercise> objects) {
            super(context, resource, objects);
            exerciseList = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_adapter_exercise_with_series_repetitions_simple, parent, false);

            TextView tvExerciseName = convertView.findViewById(R.id.tvExerciseName);
            TextView tvSeries = convertView.findViewById(R.id.tvSeries);
            TextView tvRepetitions = convertView.findViewById(R.id.tvRepetitions);

            tvExerciseName.setText(exerciseList.get(position).getName());
            tvSeries.setText(String.valueOf(exerciseList.get(position).getSeries()));
            tvRepetitions.setText(String.valueOf(exerciseList.get(position).getRepetitions()));

            return convertView;
        }
    }
}
