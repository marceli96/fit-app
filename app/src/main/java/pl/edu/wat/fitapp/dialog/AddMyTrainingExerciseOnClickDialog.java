package pl.edu.wat.fitapp.dialog;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.interfaces.callback.AddMyTrainingExerciseCallback;
import pl.edu.wat.fitapp.R;

public class AddMyTrainingExerciseOnClickDialog {
    private AddMyTrainingExerciseCallback callback;

    public AddMyTrainingExerciseOnClickDialog(AddMyTrainingExerciseCallback callback) {
        this.callback = callback;
    }

    public void build(final int position, final ArrayList<Exercise> exercises){
        AlertDialog.Builder builder = new AlertDialog.Builder(callback.activity());
        LayoutInflater inflater = LayoutInflater.from(callback.activity());
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
                    callback.onSuccess(tempExercise);
                    dialog.dismiss();
                } else {
                    if (series.isEmpty())
                        callback.onFailure(callback.activity().getString(R.string.fillSeriesError));
                    else
                        callback.onFailure(callback.activity().getString(R.string.fillRepetiotionsError));
                }
            }
        });
    }
}
