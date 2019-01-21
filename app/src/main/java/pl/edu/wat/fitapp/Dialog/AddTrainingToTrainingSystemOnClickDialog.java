package pl.edu.wat.fitapp.Dialog;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.ExercisesListAdapter;
import pl.edu.wat.fitapp.Database.Connection.AddTrainingToTrainingSystemConnection;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.R;

public class AddTrainingToTrainingSystemOnClickDialog {
    private Fragment fragment;

    public AddTrainingToTrainingSystemOnClickDialog(Fragment fragment) {
        this.fragment = fragment;
    }

    public void build(final int position, final ArrayList<Training> trainingList, final int userID){
        LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
        View alertView = inflater.inflate(R.layout.dialog_add_training_to_training_system, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());

        TextView tvTrainingName = alertView.findViewById(R.id.tvTrainingName);
        TextView tvExerciseAmount = alertView.findViewById(R.id.tvExerciseAmount);
        ListView lvTrainingExercises = alertView.findViewById(R.id.lvTrainingExercises);
        Button bAddTrainingToTrainingSystem = alertView.findViewById(R.id.bAddTrainingToTrainingSystem);

        tvTrainingName.setText(trainingList.get(position).getName());
        tvExerciseAmount.setText(String.valueOf(trainingList.get(position).getExerciseList().size()));

        ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(fragment.getActivity(), R.layout.listview_adapter_exercise_with_series_repetitions_simple, trainingList.get(position).getExerciseList());
        lvTrainingExercises.setAdapter(exercisesListAdapter);

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bAddTrainingToTrainingSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTrainingToTrainingSystemConnection addConnection = new AddTrainingToTrainingSystemConnection(fragment);
                addConnection.addTrainingToTrainingSystem(trainingList.get(position).getID(), userID);
                dialog.dismiss();
            }
        });
    }
}
