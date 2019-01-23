package pl.edu.wat.fitapp.view.main.fragment.profile;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.androidComponent.listAdapter.AddedExercisesToTrainingListAdapter;
import pl.edu.wat.fitapp.androidComponent.listAdapter.SimpleExercisesListAdapter;
import pl.edu.wat.fitapp.database.connection.AddMyTrainingConnection;
import pl.edu.wat.fitapp.database.connection.ExercisesConnection;
import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.dialog.AddMyTrainingExerciseOnClickDialog;
import pl.edu.wat.fitapp.interfaces.callback.AddMyTrainingConnectionCallback;
import pl.edu.wat.fitapp.interfaces.callback.AddMyTrainingExerciseCallback;
import pl.edu.wat.fitapp.interfaces.callback.ExercisesConnectionCallback;
import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.utils.ToastUtils;

public class AddMyTrainingExercisesActivity extends AppCompatActivity implements ExercisesConnectionCallback, AddMyTrainingConnectionCallback, AddMyTrainingExerciseCallback {
    private Button bAddMyTraining;
    private LinearLayout llShowListView;
    private ImageView imArrow;
    private TextView tvExerciseAmount;
    private ListView lvTrainingExercises, lvExercises;

    private SimpleExercisesListAdapter simpleExercisesListAdapter;
    private AddedExercisesToTrainingListAdapter addedExercisesToTrainingListAdapter;

    private ExercisesConnection exercisesConnection;
    private AddMyTrainingConnection addMyTrainingConnection;

    private String trainingName;
    private ArrayList<Exercise> exercises, trainingExercises;
    private boolean hidden = true;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_training_exercises);

        user = (User) getIntent().getSerializableExtra("user");
        trainingName = (String) getIntent().getSerializableExtra("trainingName");

        bAddMyTraining = findViewById(R.id.bAddMyTraining);
        llShowListView = findViewById(R.id.llShowListView);
        imArrow = findViewById(R.id.imArrow);
        tvExerciseAmount = findViewById(R.id.tvExerciseAmount);
        lvTrainingExercises = findViewById(R.id.lvTrainingExercies);
        lvExercises = findViewById(R.id.lvExercises);

        exercises = new ArrayList<>();
        trainingExercises = new ArrayList<>();

        simpleExercisesListAdapter = new SimpleExercisesListAdapter(AddMyTrainingExercisesActivity.this, R.layout.listview_adapter_exercise, exercises);
        lvExercises.setAdapter(simpleExercisesListAdapter);
        addedExercisesToTrainingListAdapter = new AddedExercisesToTrainingListAdapter(AddMyTrainingExercisesActivity.this, R.layout.listview_adapter_exercise_with_series_repetitions, trainingExercises, AddMyTrainingExercisesActivity.this);
        lvTrainingExercises.setAdapter(addedExercisesToTrainingListAdapter);

        exercisesConnection = new ExercisesConnection(AddMyTrainingExercisesActivity.this, exercises);
        exercisesConnection.getExercises();

        bAddMyTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trainingExercises.size() > 0){
                    addMyTrainingConnection = new AddMyTrainingConnection(AddMyTrainingExercisesActivity.this, trainingExercises);
                    addMyTrainingConnection.addMyTraining(user.getUserID(), trainingName);
                }
                else
                    ToastUtils.shortToast(AddMyTrainingExercisesActivity.this, "Najpierw dodaj ćwiczenia do treningu");
            }
        });

        lvExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AddMyTrainingExerciseOnClickDialog addMyTrainingExerciseOnClickDialog = new AddMyTrainingExerciseOnClickDialog(AddMyTrainingExercisesActivity.this);
                addMyTrainingExerciseOnClickDialog.build(position, exercises);
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

    public void openMeFragment() {
        Intent openMeFragment = new Intent(AddMyTrainingExercisesActivity.this, MainActivity.class);
        openMeFragment.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMeFragment.putExtra("user", user);
        openMeFragment.putExtra("action", "openMeFragment");
        startActivity(openMeFragment);
    }

    public void addExercise(Exercise exercise) {
        for (int i = 0; i < trainingExercises.size(); i++) {
            if (trainingExercises.get(i).getID() == exercise.getID()) {
                trainingExercises.get(i).setSeries(trainingExercises.get(i).getSeries() + exercise.getSeries());
                trainingExercises.get(i).setRepetitions(trainingExercises.get(i).getRepetitions() + exercise.getRepetitions());
                addedExercisesToTrainingListAdapter.notifyDataSetChanged();
                return;
            }
        }
        trainingExercises.add(exercise);
        tvExerciseAmount.setText(String.valueOf(trainingExercises.size()));
    }

    public void deleteExercise(Exercise exercise) {
        trainingExercises.remove(exercise);
        addedExercisesToTrainingListAdapter.notifyDataSetChanged();
        tvExerciseAmount.setText(String.valueOf(trainingExercises.size()));
    }

    @Override
    public void onSuccessExercises() {
        simpleExercisesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessAddMyTraining() {
        ToastUtils.shortToast(AddMyTrainingExercisesActivity.this, "Dodano trening");
        openMeFragment();
    }

    @Override
    public void onSuccess(Exercise exercise) {
        addExercise(exercise);
    }

    @Override
    public void onFailure(String message) {
        ToastUtils.shortToast(AddMyTrainingExercisesActivity.this, message);
    }

    @Override
    public Activity activity() {
        return AddMyTrainingExercisesActivity.this;
    }
}