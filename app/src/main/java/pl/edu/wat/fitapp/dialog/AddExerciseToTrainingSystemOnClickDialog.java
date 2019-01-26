package pl.edu.wat.fitapp.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.connection.AddExerciseToTrainingSystemConnection;
import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.R;

public class AddExerciseToTrainingSystemOnClickDialog {
    private ConnectionCallback callback;

    public AddExerciseToTrainingSystemOnClickDialog(ConnectionCallback callback) {
        this.callback = callback;
    }

    public void build(final int position, final ArrayList<Exercise> exerciseList, final int userID){
        AlertDialog.Builder builder = new AlertDialog.Builder(callback.activity());
        LayoutInflater inflater = LayoutInflater.from(callback.activity());
        View alertView = inflater.inflate(R.layout.dialog_choose_series_repetitions_exercise, null);

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
                    AddExerciseToTrainingSystemConnection addConnection = new AddExerciseToTrainingSystemConnection(callback);
                    addConnection.addExerciseToTrainingSystem(exerciseList.get(position).getID(), userID, series, repetitions);
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
