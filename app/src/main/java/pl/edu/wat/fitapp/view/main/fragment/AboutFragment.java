package pl.edu.wat.fitapp.view.main.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.edu.wat.fitapp.view.main.MainActivity;
import pl.edu.wat.fitapp.R;

public class AboutFragment extends Fragment {


    public AboutFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.nav_about));

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

}
