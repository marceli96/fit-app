package pl.edu.wat.fitapp.Dialog;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Connection.AddExerciseToTrainingSystemConnection;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Utils.ToastUtils;

public class AddExerciseToTrainingSystemOnClickDialog {
    private Fragment fragment;

    public AddExerciseToTrainingSystemOnClickDialog(Fragment fragment) {
        this.fragment = fragment;
    }

    public void build(final int position, final ArrayList<Exercise> exerciseList, final int userID){
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
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
                    AddExerciseToTrainingSystemConnection addConnection = new AddExerciseToTrainingSystemConnection(fragment);
                    addConnection.addExerciseToTrainingSystem(exerciseList.get(position).getID(), userID, series, repetitions);
                    dialog.dismiss();
                } else {
                    if (series.isEmpty())
                        ToastUtils.shortToast(fragment.getActivity(), "Wpisz liczbę serii!");
                    else
                        ToastUtils.shortToast(fragment.getActivity(), "Wpisz liczbę powtórzeń!");
                }
            }
        });
    }
}
