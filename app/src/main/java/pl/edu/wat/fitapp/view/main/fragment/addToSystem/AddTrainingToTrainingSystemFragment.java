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

import pl.edu.wat.fitapp.androidComponent.listAdapter.TrainingListAdapter;
import pl.edu.wat.fitapp.database.connection.MyTrainingsConnection;
import pl.edu.wat.fitapp.database.entity.Training;
import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.dialog.AddTrainingToTrainingSystemOnClickDialog;
import pl.edu.wat.fitapp.interfaces.callback.ConnectionCallback;
import pl.edu.wat.fitapp.interfaces.callback.MyTrainingsConnectionCallback;
import pl.edu.wat.fitapp.utils.ToastUtils;
import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.R;


public class AddTrainingToTrainingSystemFragment extends Fragment implements MyTrainingsConnectionCallback, ConnectionCallback {
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

    @Override
    public void onSuccessMyTrainings() {
        trainingListAdapter.notifyDataSetChanged();
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
