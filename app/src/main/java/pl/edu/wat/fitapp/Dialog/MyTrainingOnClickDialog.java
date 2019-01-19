package pl.edu.wat.fitapp.Dialog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.TrainingExercisesListAdapter;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Main.Fragment.Profile.ProfileFragment;
import pl.edu.wat.fitapp.R;

public class MyTrainingOnClickDialog {
    private ProfileFragment profileFragment;

    public MyTrainingOnClickDialog(ProfileFragment profileFragment) {
        this.profileFragment = profileFragment;
    }

    public void build(int position, ArrayList<Training> myTrainings){
        AlertDialog.Builder builder = new AlertDialog.Builder(profileFragment.getActivity());
        LayoutInflater inflater = LayoutInflater.from(profileFragment.getContext());
        View alertView = inflater.inflate(R.layout.dialog_my_training_details, null);

        TextView tvTrainingName = alertView.findViewById(R.id.tvTrainingName);
        TextView tvExerciseAmount = alertView.findViewById(R.id.tvExerciseAmount);
        ListView lvExercises = alertView.findViewById(R.id.lvExercises);

        tvTrainingName.setText(myTrainings.get(position).getName());
        tvExerciseAmount.setText(String.valueOf(myTrainings.get(position).getExerciseList().size()));

        TrainingExercisesListAdapter exercisesListAdapter = new TrainingExercisesListAdapter(profileFragment.getActivity(), R.layout.listview_adapter_exercise_with_series_repetitions_simple, myTrainings.get(position).getExerciseList());
        lvExercises.setAdapter(exercisesListAdapter);

        builder.setView(alertView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
