package pl.edu.wat.fitapp.Dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.ExercisesListAdapter;
import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.TrainingSystemListAdapter;
import pl.edu.wat.fitapp.Database.Connection.DeleteTrainingSystemConnection;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Interface.TrainingSystem;
import pl.edu.wat.fitapp.Main.Fragment.HomeFragment;
import pl.edu.wat.fitapp.R;

public class TrainingSystemOnClickDialog {
    private HomeFragment homeFragment;
    private ArrayList<TrainingSystem> trainingSystemDay;
    private TrainingSystemListAdapter trainingSystemListAdapter;

    public TrainingSystemOnClickDialog(HomeFragment homeFragment, ArrayList<TrainingSystem> trainingSystemDay, TrainingSystemListAdapter trainingSystemListAdapter) {
        this.homeFragment = homeFragment;
        this.trainingSystemDay = trainingSystemDay;
        this.trainingSystemListAdapter = trainingSystemListAdapter;
    }

    public void build(final int position, final int userID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(homeFragment.getActivity());
        LayoutInflater inflater = LayoutInflater.from(homeFragment.getContext());
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
            bDelete.setText("Usuń ćwiczenie");
        } else {
            Training tempTraining = (Training) trainingSystemDay.get(position);
            llExercise.setVisibility(View.GONE);
            tvExerciseAmount.setText(String.valueOf(tempTraining.getExerciseList().size()));
            bDelete.setText("Usuń trening");

            ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(homeFragment.getActivity(), R.layout.listview_adapter_exercise_with_series_repetitions_simple, tempTraining.getExerciseList());
            lvExercises.setAdapter(exercisesListAdapter);
        }

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTrainingSystemConnection deleteConnection = new DeleteTrainingSystemConnection(homeFragment, trainingSystemDay, trainingSystemListAdapter);
                deleteConnection.deleteFromTrainingSystem(trainingSystemDay.get(position), userID);
                dialog.dismiss();
            }
        });
    }
}
