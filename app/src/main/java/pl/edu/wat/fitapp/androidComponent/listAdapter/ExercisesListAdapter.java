package pl.edu.wat.fitapp.androidComponent.listAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.R;

public class ExercisesListAdapter extends ArrayAdapter<Exercise> {
    private ArrayList<Exercise> exerciseList;

    public ExercisesListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exercise> objects) {
        super(context, resource, objects);
        exerciseList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_exercise_with_series_repetitions_simple, parent, false);

        TextView tvExerciseName = convertView.findViewById(R.id.tvExerciseName);
        TextView tvSeries = convertView.findViewById(R.id.tvSeries);
        TextView tvRepetitions = convertView.findViewById(R.id.tvRepetitions);

        tvExerciseName.setText(exerciseList.get(position).getName());
        tvSeries.setText(String.valueOf(exerciseList.get(position).getSeries()));
        tvRepetitions.setText(String.valueOf(exerciseList.get(position).getRepetitions()));

        return convertView;
    }
}
