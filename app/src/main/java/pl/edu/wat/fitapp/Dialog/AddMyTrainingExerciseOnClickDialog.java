package pl.edu.wat.fitapp.Dialog;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Main.Fragment.Profile.AddMyTrainingExercisesActivity;
import pl.edu.wat.fitapp.R;

public class AddMyTrainingExerciseOnClickDialog {
    private AddMyTrainingExercisesActivity addMyTrainingExercisesActivity;

    public AddMyTrainingExerciseOnClickDialog(AddMyTrainingExercisesActivity addMyTrainingExercisesActivity) {
        this.addMyTrainingExercisesActivity = addMyTrainingExercisesActivity;
    }

    public void build(final int position, final ArrayList<Exercise> exercises){
        AlertDialog.Builder builder = new AlertDialog.Builder(addMyTrainingExercisesActivity);
        LayoutInflater inflater = LayoutInflater.from(addMyTrainingExercisesActivity);
        View alertView = inflater.inflate(R.layout.dialog_choose_series_repetitions_exercise, null);

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
                    addMyTrainingExercisesActivity.addExercise(tempExercise);
                    dialog.dismiss();
                } else {
                    if (series.isEmpty())
                        Toast.makeText(addMyTrainingExercisesActivity, "Wpisz liczbę serii!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(addMyTrainingExercisesActivity, "Wpisz liczbę powtórzeń!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
