package pl.edu.wat.fitapp.view.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pl.edu.wat.fitapp.database.connection.UserSettingsConnection;
import pl.edu.wat.fitapp.database.entity.User;
import pl.edu.wat.fitapp.interfaces.callback.UserConnectionCallback;
import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.utils.ToastUtils;

public class SettingsFragment extends Fragment implements UserConnectionCallback {

    private EditText etLogin, etEmail, etPassword1, etPassword2;
    private Button bChangeLogin, bChangeEmail, bChangePassword;
    private TextView tvLogin, tvEmail;

    private User user;

    private UserSettingsConnection userSettingsConnection;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.nav_settings));

        View view = getLayoutInflater().inflate(R.layout.fragment_settings, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra(getString(R.string.userExtra));

        tvLogin = view.findViewById(R.id.tvLogin);
        tvEmail = view.findViewById(R.id.tvEmail);

        tvLogin.setText(user.getUserName());
        tvEmail.setText(user.getEmail());

        etLogin = view.findViewById(R.id.etLogin);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword1 = view.findViewById(R.id.etPassword1);
        etPassword2 = view.findViewById(R.id.etPassword2);

        bChangeLogin = view.findViewById(R.id.bChangeLogin);
        bChangeEmail = view.findViewById(R.id.bChangeEmail);
        bChangePassword = view.findViewById(R.id.bChangePassword);

        bChangeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etLogin.getText().toString().isEmpty() && etLogin.getText().toString().length() > 5) {
                    userSettingsConnection = new UserSettingsConnection(SettingsFragment.this);
                    userSettingsConnection.changeLogin(user, etLogin.getText().toString());
                } else {
                    if (etLogin.getText().toString().isEmpty())
                        ToastUtils.shortToast(getActivity(), getString(R.string.fillLogin));
                    else if (etLogin.getText().toString().length() <= 5)
                        ToastUtils.shortToast(getActivity(), getString(R.string.reqLogin));
                }
            }
        });

        bChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmail.getText().toString().isEmpty()) {
                    userSettingsConnection = new UserSettingsConnection(SettingsFragment.this);
                    userSettingsConnection.changeEmail(user, etEmail.getText().toString());
                } else {
                    ToastUtils.shortToast(getActivity(), getString(R.string.fillEmail));
                }
            }
        });

        bChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPassword1.getText().toString().isEmpty() && etPassword1.getText().toString().length() > 5
                        && etPassword1.getText().toString().equals(etPassword2.getText().toString())) {
                    userSettingsConnection = new UserSettingsConnection(SettingsFragment.this);
                    userSettingsConnection.changePassword(user, etPassword1.getText().toString());
                } else {
                    if (etPassword1.getText().toString().isEmpty())
                        ToastUtils.shortToast(getActivity(), getString(R.string.fillPassword));
                    else if (!etPassword1.getText().toString().equals(etPassword2.getText().toString()))
                        ToastUtils.shortToast(getActivity(), getString(R.string.notSamePassword));
                    else if (etPassword1.getText().toString().length() <= 5)
                        ToastUtils.shortToast(getActivity(), getString(R.string.reqPassword));
                }
            }

        });

        return view;
    }

    public void openHomeActivity() {
        Intent openHomeScreen = new Intent(getActivity(), MainActivity.class);
        openHomeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openHomeScreen.putExtra(getString(R.string.userExtra), user);
        startActivity(openHomeScreen);
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public void onSuccess(User user) {
        this.user = user;
        ToastUtils.shortToast(getActivity(), getString(R.string.successChange));
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