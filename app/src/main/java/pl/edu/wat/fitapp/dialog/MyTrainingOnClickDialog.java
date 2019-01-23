package pl.edu.wat.fitapp.dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.androidComponent.listAdapter.ExercisesListAdapter;
import pl.edu.wat.fitapp.database.entity.Training;
import pl.edu.wat.fitapp.interfaces.callback.Callback;
import pl.edu.wat.fitapp.R;

public class MyTrainingOnClickDialog {
    private Callback callback;

    public MyTrainingOnClickDialog(Callback callback) {
        this.callback = callback;
    }

    public void build(int position, ArrayList<Training> myTrainings){
        AlertDialog.Builder builder = new AlertDialog.Builder(callback.activity());
        LayoutInflater inflater = LayoutInflater.from(callback.activity());
        View alertView = inflater.inflate(R.layout.dialog_my_training_details, null);

        TextView tvTrainingName = alertView.findViewById(R.id.tvTrainingName);
        TextView tvExerciseAmount = alertView.findViewById(R.id.tvExerciseAmount);
        ListView lvExercises = alertView.findViewById(R.id.lvExercises);

        tvTrainingName.setText(myTrainings.get(position).getName());
        tvExerciseAmount.setText(String.valueOf(myTrainings.get(position).getExerciseList().size()));

        ExercisesListAdapter exercisesListAdapter = new ExercisesListAdapter(callback.activity(), R.layout.listview_adapter_exercise_with_series_repetitions_simple, myTrainings.get(position).getExerciseList());
        lvExercises.setAdapter(exercisesListAdapter);

        builder.setView(alertView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
