package pl.edu.wat.fitapp.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.androidComponent.listAdapter.ExercisesListAdapter;
import pl.edu.wat.fitapp.androidComponent.listAdapter.TrainingSystemListAdapter;
import pl.edu.wat.fitapp.database.connection.DeleteTrainingSystemConnection;
import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.database.entity.Training;
import pl.edu.wat.fitapp.interfaces.TrainingSystem;
import pl.edu.wat.fitapp.interfaces.callback.TrainingSystemCallback;
import pl.edu.wat.fitapp.R;

public class TrainingSystemOnClickDialog {
    private TrainingSystemCallback callback;

    public TrainingSystemOnClickDialog(TrainingSystemCallback callback) {
        this.callback = callback;
    }

    public void build(final int position, final int userID, final ArrayList<TrainingSystem> trainingSystemDay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(callback.activity());
        LayoutInflater inflater = LayoutInflater.from(callback.activity());
        View alertView = inflater.inflate(R.layout.dialog_training_system_details, null);

        TextView tvName = alertView.findViewById(R.id.tvName);
        TextView tvSeries = alertView.findViewById(R.id.tvSeries);
        TextView tvRepetitions = alertView.findViewById(R.id.tvRepetitions);
        TextView tvExerciseAmount = alertView.findViewById(R.id.tvExerciseAmount);
        ListView lvExercises = alertView.findViewById(R.id.lvExercises);
        Button bDelete = alertView.findViewById(R.id.bDelete);
        LinearLayout llExercise = alertView.findViewById(R.id.llExercise);
        LinearLayout llTraining = alertView.findViewById(R.id.llTraining);

        tvName.setText(trainingSystemDay.get(position).getName());

        if (trainingSystemDay.get(position).getClass() == Exercise.class) {
            Exercise tempExercise = (Exercise) trainingSystemDay.get(position);
            llTraining.setVisibility(View.GONE);
            tvSeries.setText(String.valueOf(tempExercise.getSeries()));
            tvRepetitions.setText(String.valueOf(tempExercise.getRepetitions()));
            bDelete.setText(callback.activity().getString(R.string.comunicat7));
        } else {
            Training tempTraining = (Training) trainingSystemDay.get(position);
            llExercise.setVisibility(View.GONE);
            tvExerciseAmount.setText(String.valueOf(tempTraining.getExerciseList().size()));
            bDelete.setText(callback.activity().getString(R.string.comunicat8));

            ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(callback.activity(), R.layout.listview_adapter_exercise_with_series_repetitions_simple, tempTraining.getExerciseList());
            lvExercises.setAdapter(exercisesListAdapter);
        }

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTrainingSystemConnection deleteConnection = new DeleteTrainingSystemConnection(callback);
                deleteConnection.deleteFromTrainingSystem(trainingSystemDay.get(position), userID);
                dialog.dismiss();
            }
        });
    }
}
