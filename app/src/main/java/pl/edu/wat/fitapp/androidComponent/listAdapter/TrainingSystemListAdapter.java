package pl.edu.wat.fitapp.androidComponent.listAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.database.entity.Training;
import pl.edu.wat.fitapp.interfaces.TrainingSystem;
import pl.edu.wat.fitapp.R;

public class TrainingSystemListAdapter extends ArrayAdapter<TrainingSystem> {
    private ArrayList<TrainingSystem> trainingList;

    public TrainingSystemListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TrainingSystem> objects) {
        super(context, resource, objects);
        trainingList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_show_trainingsystem, parent, false);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvExerciseAmount = convertView.findViewById(R.id.tvExerciseAmount);
        TextView tvSeries = convertView.findViewById(R.id.tvSeries);
        TextView tvRepetitions = convertView.findViewById(R.id.tvRepetitions);
        LinearLayout llTraining = convertView.findViewById(R.id.llTraining);
        LinearLayout llExercise = convertView.findViewById(R.id.llExercise);

        tvName.setText(trainingList.get(position).getName());

        if (trainingList.get(position).getClass() == Exercise.class) {
            Exercise tempExercise = (Exercise) trainingList.get(position);
            llTraining.setVisibility(View.GONE);
            tvSeries.setText(String.valueOf(tempExercise.getSeries()));
            tvRepetitions.setText(String.valueOf(tempExercise.getRepetitions()));
        } else {
            Training tempTraining = (Training) trainingList.get(position);
            llExercise.setVisibility(View.GONE);
            tvExerciseAmount.setText(String.valueOf(tempTraining.getExerciseList().size()));
        }

        return convertView;
    }
}
