package pl.edu.wat.fitapp.androidComponent.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.R;

public class SimpleExercisesListAdapter extends ArrayAdapter<Exercise> {
    private ArrayList<Exercise> exercises;

    public SimpleExercisesListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exercise> objects) {
        super(context, resource, objects);
        exercises = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_exercise, parent, false);

        TextView tvExerciseName = convertView.findViewById(R.id.tvExerciseName);

        tvExerciseName.setText(exercises.get(position).getName());

        return convertView;
    }
}
