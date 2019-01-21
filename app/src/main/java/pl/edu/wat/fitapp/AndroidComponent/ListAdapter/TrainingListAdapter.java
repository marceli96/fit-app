package pl.edu.wat.fitapp.AndroidComponent.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.R;

public class TrainingListAdapter extends ArrayAdapter<Training> {
    private ArrayList<Training> trainingList;

    public TrainingListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Training> objects) {
        super(context, resource, objects);
        trainingList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_training_2, parent, false);

        TextView tvTrainingName = convertView.findViewById(R.id.tvTrainingName);
        TextView tvExercisesAmount = convertView.findViewById(R.id.tvExercisesAmount);

        tvTrainingName.setText(trainingList.get(position).getName());
        tvExercisesAmount.setText(String.valueOf(trainingList.get(position).getExerciseList().size()));

        return convertView;
    }
}
