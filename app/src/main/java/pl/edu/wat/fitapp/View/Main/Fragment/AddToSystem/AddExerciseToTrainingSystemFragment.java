package pl.edu.wat.fitapp.View.Main.Fragment.AddToSystem;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.SimpleExercisesListAdapter;
import pl.edu.wat.fitapp.Database.Connection.ExercisesConnection;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Dialog.AddExerciseToTrainingSystemOnClickDialog;
import pl.edu.wat.fitapp.View.Main.MainActivity;
import pl.edu.wat.fitapp.R;


public class AddExerciseToTrainingSystemFragment extends Fragment {
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

        exercisesConnection = new ExercisesConnection(getActivity(), exerciseList);
        exercisesConnection.getExercises(simpleExercisesListAdapter);

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
}