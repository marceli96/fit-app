package pl.edu.wat.fitapp.Main.Fragment.AddToSystem;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.ExercisesListAdapter;
import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.TrainingListAdapter;
import pl.edu.wat.fitapp.Database.Connection.MyTrainingsConnection;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Dialog.AddTrainingToTrainingSystemOnClickDialog;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;


public class AddTrainingToTrainingSystemFragment extends Fragment {
    private ListView lvTrainings;
    private ArrayList<Training> trainingList;
    private TrainingListAdapter trainingListAdapter;

    private MyTrainingsConnection myTrainingsConnection;

    private User user;

    public AddTrainingToTrainingSystemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_training_to_training_system, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        trainingList = new ArrayList<>();

        lvTrainings = view.findViewById(R.id.lvTrainings);
        trainingListAdapter = new TrainingListAdapter(getActivity(), R.layout.listview_adapter_training, trainingList);
        lvTrainings.setAdapter(trainingListAdapter);

        myTrainingsConnection = new MyTrainingsConnection(AddTrainingToTrainingSystemFragment.this, trainingList);
        myTrainingsConnection.getMyTrainings(user.getUserID());

        lvTrainings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AddTrainingToTrainingSystemOnClickDialog addTrainingToTrainingSystemOnClickDialog = new AddTrainingToTrainingSystemOnClickDialog(AddTrainingToTrainingSystemFragment.this);
                addTrainingToTrainingSystemOnClickDialog.build(position, trainingList, user.getUserID());
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

    public void showMyTrainings(){
        trainingListAdapter.notifyDataSetChanged();
    }
}
