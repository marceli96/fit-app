package pl.edu.wat.fitapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button bBreakfast, bSecondBreakfast, bLunch, bDinner, bSnack, bSupper;

    private User user;

    public HomeFragment() {

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        user = (User)args.getSerializable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bBreakfast = getView().findViewById(R.id.bBreakfast);
        bSecondBreakfast = getView().findViewById(R.id.bSecondBreakfast);
        bLunch = getView().findViewById(R.id.bLunch);
        bDinner = getView().findViewById(R.id.bDinner);
        bSnack = getView().findViewById(R.id.bSnack);
        bSupper = getView().findViewById(R.id.bSupper);

        bBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(0);
            }
        });

        bSecondBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(1);
            }
        });

        bLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(2);
            }
        });

        bDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(3);
            }
        });

        bSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(4);
            }
        });

        bSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFoodSystem(5);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void addToFoodSystem(int mealTime) {
        Intent openAddToFoodSystemActivity = new Intent(this.getContext(), AddToFoodSystemActivity.class);
        startActivity(openAddToFoodSystemActivity);
    }
}
