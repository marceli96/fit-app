package pl.edu.wat.fitapp.AndroidComponent.ListAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.R;

public class MyTrainingsListAdapter extends ArrayAdapter<Training> {
    private ArrayList<Training> myTrainings;

    public MyTrainingsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Training> objects) {
        super(context, resource, objects);
        myTrainings = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.listview_adapter_training, parent, false);

        TextView tvTrainingName = convertView.findViewById(R.id.tvTrainingName);
        TextView tvExercisesAmount = convertView.findViewById(R.id.tvExercisesAmount);

        tvTrainingName.setText(myTrainings.get(position).getName());
        tvExercisesAmount.setText(String.valueOf(myTrainings.get(position).getExerciseList().size()));

        return convertView;
    }
}
