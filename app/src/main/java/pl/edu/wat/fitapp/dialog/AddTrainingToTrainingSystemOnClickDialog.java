package pl.edu.wat.fitapp.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.androidComponent.listAdapter.ExercisesListAdapter;
import pl.edu.wat.fitapp.database.connection.AddTrainingToTrainingSystemConnection;
import pl.edu.wat.fitapp.database.entity.Training;
import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.R;

public class AddTrainingToTrainingSystemOnClickDialog {
    private ConnectionCallback callback;

    public AddTrainingToTrainingSystemOnClickDialog(ConnectionCallback callback) {
        this.callback = callback;
    }

    public void build(final int position, final ArrayList<Training> trainingList, final int userID){
        LayoutInflater inflater = LayoutInflater.from(callback.activity());
        View alertView = inflater.inflate(R.layout.dialog_add_training_to_training_system, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(callback.activity());

        TextView tvTrainingName = alertView.findViewById(R.id.tvTrainingName);
        TextView tvExerciseAmount = alertView.findViewById(R.id.tvExerciseAmount);
        ListView lvTrainingExercises = alertView.findViewById(R.id.lvTrainingExercises);
        Button bAddTrainingToTrainingSystem = alertView.findViewById(R.id.bAddTrainingToTrainingSystem);

        tvTrainingName.setText(trainingList.get(position).getName());
        tvExerciseAmount.setText(String.valueOf(trainingList.get(position).getExerciseList().size()));

        ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(callback.activity(), R.layout.listview_adapter_exercise_with_series_repetitions_simple, trainingList.get(position).getExerciseList());
        lvTrainingExercises.setAdapter(exercisesListAdapter);

        builder.setView(alertView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        bAddTrainingToTrainingSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTrainingToTrainingSystemConnection addConnection = new AddTrainingToTrainingSystemConnection(callback);
                addConnection.addTrainingToTrainingSystem(trainingList.get(position).getID(), userID);
                dialog.dismiss();
            }
        });
    }
}
