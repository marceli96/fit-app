package pl.edu.wat.fitapp.androidComponent.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.view.main.fragment.profile.AddMyTrainingExercisesActivity;
import pl.edu.wat.fitapp.R;

public class AddedExercisesToTrainingListAdapter extends ArrayAdapter<Exercise> {
    private ArrayList<Exercise> trainingExercises;
    private AddMyTrainingExercisesActivity addMyTrainingExercisesActivity;

    public AddedExercisesToTrainingListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exercise> objects, AddMyTrainingExercisesActivity addMyTrainingExercisesActivity) {
        super(context, resource, objects);
        trainingExercises = objects;
        this.addMyTrainingExercisesActivity = addMyTrainingExercisesActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_exercise_with_series_repetitions, parent, false);

        TextView tvExerciseName = convertView.findViewById(R.id.tvExerciseName);
        TextView tvSeries = convertView.findViewById(R.id.tvSeries);
        TextView tvRepetitions = convertView.findViewById(R.id.tvRepetitions);
        ImageView imDeleteExercise = convertView.findViewById(R.id.imDeleteExercise);

        final Exercise exercise = trainingExercises.get(position);

        tvExerciseName.setText(exercise.getName());
        tvSeries.setText(String.valueOf(exercise.getSeries()));
        tvRepetitions.setText(String.valueOf(exercise.getRepetitions()));

        imDeleteExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMyTrainingExercisesActivity.deleteExercise(exercise);
            }
        });

        return convertView;
    }
}

