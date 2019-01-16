package pl.edu.wat.fitapp.Main.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pl.edu.wat.fitapp.Database.Connection.UserSettingsConnection;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.R;


public class SettingsFragment extends Fragment {

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
        ((MainActivity) getActivity()).setActionBarTitle("Ustawienia");

        View view = getLayoutInflater().inflate(R.layout.fragment_settings, container, false);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

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
                        Toast.makeText(getActivity(), "Wprowadź login", Toast.LENGTH_SHORT).show();
                    else if (etLogin.getText().toString().length() <= 5)
                        Toast.makeText(getActivity(), "Login musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Wprowadź e-mail", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Wprowadź hasło", Toast.LENGTH_SHORT).show();
                    else if (!etPassword1.getText().toString().equals(etPassword2.getText().toString()))
                        Toast.makeText(getActivity(), "Hasła się różnią", Toast.LENGTH_SHORT).show();
                    else if (etPassword1.getText().toString().length() <= 5)
                        Toast.makeText(getActivity(), "Hasło musi mieć conajmniej 6 znaków", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return view;
    }

    public void openHomeActivity() {
        Intent openHomeScreen = new Intent(getActivity(), MainActivity.class);
        openHomeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openHomeScreen.putExtra("user", user);
        startActivity(openHomeScreen);
    }

    public void setUser(User user) {
        this.user = user;
    }
}