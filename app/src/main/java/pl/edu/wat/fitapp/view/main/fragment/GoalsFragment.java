package pl.edu.wat.fitapp.view.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import pl.edu.wat.fitapp.database.connection.UserConnection;
import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.interfaces.callback.UserConnectionCallback;
import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.mangement.UserSettingsManagement;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.utils.ToastUtils;

public class GoalsFragment extends Fragment implements AdapterView.OnItemSelectedListener, UserConnectionCallback {

    private EditText etWeight;
    private TextView tvCaloricDemand;
    private Spinner spinnerLevelActivity;
    private RadioButton rbLose, rbKeep, rbGain;
    private RadioGroup rgGoal;
    private Button bCount, bSave;

    private User user;

    private int calories;
    private String activityLevel;

    private ArrayAdapter<CharSequence> adapter;


    public GoalsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.nav_goals));

        View view = getLayoutInflater().inflate(R.layout.fragment_goals, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra(getString(R.string.userExtra));

        etWeight = view.findViewById(R.id.etWeight);
        tvCaloricDemand = view.findViewById(R.id.tvCaloricDemand);
        spinnerLevelActivity = view.findViewById(R.id.spinnerLevelActivity);
        rgGoal = view.findViewById(R.id.rgGoal);
        rbLose = view.findViewById(R.id.rbLose);
        rbKeep = view.findViewById(R.id.rbKeep);
        rbGain = view.findViewById(R.id.rbGain);
        bCount = view.findViewById(R.id.bCount);
        bSave = view.findViewById(R.id.bSave);

        etWeight.setText(String.valueOf(user.getWeight()));

        switch (user.getGoal()) {
            case 0:
                rbLose.setChecked(true);
                break;
            case 1:
                rbGain.setChecked(true);
                break;
            case 2:
                rbKeep.setChecked(true);
                break;
        }

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.activityLevel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevelActivity.setAdapter(adapter);
        spinnerLevelActivity.setOnItemSelectedListener(this);
        spinnerLevelActivity.setSelection(user.getActivityLevel());


        bCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etWeight.getText().toString().isEmpty()) {
                    UserSettingsManagement userMgn = new UserSettingsManagement();
                    calories = userMgn.calculateCaloriesForExistingUser(rgGoal, getView(), user, activityLevel, Double.parseDouble(etWeight.getText().toString()));
                    tvCaloricDemand.setText(String.valueOf(calories));
                } else
                    ToastUtils.shortToast(getActivity(), getString(R.string.fillWeight));
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etWeight.getText().toString().isEmpty()) {
                    UserSettingsManagement userMgn = new UserSettingsManagement();
                    calories = userMgn.calculateCaloriesForExistingUser(rgGoal, getView(), user, activityLevel, Double.parseDouble(etWeight.getText().toString()));
                    tvCaloricDemand.setText(String.valueOf(calories));
                    UserConnection userConnection = new UserConnection(GoalsFragment.this);
                    userConnection.saveWeight(user, Double.parseDouble(etWeight.getText().toString()), userMgn.getGoalInt(rgGoal, getView()),
                            calories, userMgn.getActivityLevelInt(activityLevel));
                } else
                    ToastUtils.shortToast(getActivity(), getString(R.string.fillWeight));
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        activityLevel = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openHomeActivity() {
        Intent openHomeScreen = new Intent(getActivity(), MainActivity.class);
        openHomeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openHomeScreen.putExtra(getString(R.string.userExtra), user);
        startActivity(openHomeScreen);
    }

    @Override
    public void onSuccess(User user) {
        ToastUtils.shortToast(getActivity(), getString(R.string.updateData));
        this.user = user;
        openHomeActivity();
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