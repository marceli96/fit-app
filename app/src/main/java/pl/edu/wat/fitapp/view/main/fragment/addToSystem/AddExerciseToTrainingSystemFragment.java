package pl.edu.wat.fitapp.view.main.fragment.addToSystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import pl.edu.wat.fitapp.androidComponent.listAdapter.SimpleExercisesListAdapter;
import pl.edu.wat.fitapp.database.connection.ExercisesConnection;
import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.dialog.AddExerciseToTrainingSystemOnClickDialog;
import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.interfaces.callback.ExercisesConnectionCallback;
import pl.edu.wat.fitapp.utils.ToastUtils;
import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.R;

public class AddExerciseToTrainingSystemFragment extends Fragment implements ExercisesConnectionCallback, ConnectionCallback {
    private ListView lvExercises;
    private ArrayList<Exercise> exerciseList;
    private SimpleExercisesListAdapter simpleExercisesListAdapter;

    private ExercisesConnection exercisesConnection;

    private User user;

    public AddExerciseToTrainingSystemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = (User) getActivity().getIntent().getSerializableExtra("user");

        View view = inflater.inflate(R.layout.fragment_add_exercise_to_training_system, container, false);

        exerciseList = new ArrayList<>();

        lvExercises = view.findViewById(R.id.lvExercises);
        simpleExercisesListAdapter = new SimpleExercisesListAdapter(getActivity(), R.layout.listview_adapter_exercise, exerciseList);
        lvExercises.setAdapter(simpleExercisesListAdapter);

        exercisesConnection = new ExercisesConnection(this, exerciseList);
        exercisesConnection.getExercises();

        lvExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AddExerciseToTrainingSystemOnClickDialog addExerciseToTrainingSystemOnClickDialog = new AddExerciseToTrainingSystemOnClickDialog(AddExerciseToTrainingSystemFragment.this);
                addExerciseToTrainingSystemOnClickDialog.build(position, exerciseList, user.getUserID());
            }
        });

        return view;
    }

    public void openMainActivity() {
        Intent openMainActivity = new Intent(getContext(), MainActivity.class);
        openMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openMainActivity.putExtra("user", user);
        startActivity(openMainActivity);
    }

    @Override
    public void onSuccessExercises() {
        simpleExercisesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess() {
        ToastUtils.shortToast(getActivity(), "Dodano pomyślnie");
        openMainActivity();
    }

    @Override
    public void onFailure(String message) {
        ToastUtils.shortToast(getActivity(), message);
    }

    @Override
    public Activity activity() {
        return getActivity();
    }
}