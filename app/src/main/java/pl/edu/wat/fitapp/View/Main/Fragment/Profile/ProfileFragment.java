package pl.edu.wat.fitapp.View.Main.Fragment.Profile;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.MyMealsListAdapter;
import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.MyTrainingsListAdapter;
import pl.edu.wat.fitapp.Database.Connection.MyMealsConnection;
import pl.edu.wat.fitapp.Database.Connection.MyTrainingsConnection;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Ingredient;
import pl.edu.wat.fitapp.Database.Entity.Meal;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.AndroidComponent.NonScrollListView;
import pl.edu.wat.fitapp.Dialog.MyMealOnClickDialog;
import pl.edu.wat.fitapp.Dialog.MyTrainingOnClickDialog;
import pl.edu.wat.fitapp.View.Main.MainActivity;
import pl.edu.wat.fitapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ImageView imAddMyMeal, imAddMyTraining, imArrowMeals, imArrowTrainings;
    private TextView tvMyMealsEmpty, tvMyTrainingsEmpty;
    private NonScrollListView lvMyMeals, lvMyTrainings;
    private LinearLayout llMyMeals, llMyTrainings;
    private ProgressBar pbLoadingMeals, pbLoadingTrainings;

    private ArrayList<Meal> myMeals;
    private ArrayList<Training> myTrainings;
    private MyMealsListAdapter myMealsListAdapter;
    private MyTrainingsListAdapter myTrainingsListAdapter;

    private MyMealsConnection myMealsConnection;
    private MyTrainingsConnection myTrainingsConnection;

    private User user;

    private boolean hiddenMyMeals = false, hiddenMyTrainings = false;

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Profil");

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        user = (User) getActivity().getIntent().getSerializableExtra("user");

        imAddMyMeal = view.findViewById(R.id.imAddMyMeal);
        imAddMyTraining = view.findViewById(R.id.imAddMyTraining);
        imArrowMeals = view.findViewById(R.id.imArrowMeals);
        imArrowTrainings = view.findViewById(R.id.imArrowTrainings);
        tvMyMealsEmpty = view.findViewById(R.id.tvMyMealsEmpty);
        tvMyTrainingsEmpty = view.findViewById(R.id.tvMyTrainingsEmpty);
        llMyMeals = view.findViewById(R.id.llMyMeals);
        llMyTrainings = view.findViewById(R.id.llMyTrainings);
        pbLoadingMeals = view.findViewById(R.id.pbLoadingMeals);
        pbLoadingTrainings = view.findViewById(R.id.pbLoadingTrainings);
        lvMyMeals = view.findViewById(R.id.lvMyMeals);
        lvMyTrainings = view.findViewById(R.id.lvMyTrainings);

        myMeals = new ArrayList<>();
        myTrainings = new ArrayList<>();
        myMealsConnection = new MyMealsConnection(this, myMeals);
        myTrainingsConnection = new MyTrainingsConnection(this, myTrainings);
        myMealsConnection.getMyMeals(user.getUserID());
        myTrainingsConnection.getMyTrainings(user.getUserID());

        myMealsListAdapter = new MyMealsListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, myMeals);
        lvMyMeals.setAdapter(myMealsListAdapter);
        myTrainingsListAdapter = new MyTrainingsListAdapter(getActivity(), R.layout.listview_adapter_training, myTrainings);
        lvMyTrainings.setAdapter(myTrainingsListAdapter);

        lvMyMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMealOnClickDialog myMealDialog = new MyMealOnClickDialog(ProfileFragment.this);
                myMealDialog.build(position, myMeals);
            }
        });

        lvMyTrainings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyTrainingOnClickDialog myTrainingDialog = new MyTrainingOnClickDialog(ProfileFragment.this);
                myTrainingDialog.build(position, myTrainings);
            }
        });

        imAddMyMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMyMealNameActivity();
            }
        });

        imAddMyTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMyTrainingNameActivity();
            }
        });

        llMyMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenMyMeals = layoutOnClick(hiddenMyMeals, lvMyMeals, imArrowMeals);
            }
        });

        llMyTrainings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenMyTrainings = layoutOnClick(hiddenMyTrainings, lvMyTrainings, imArrowTrainings);
            }
        });

        return view;
    }

    public void showMyTrainings() {
        if (myTrainings.size() == 0)
            tvMyTrainingsEmpty.setVisibility(View.VISIBLE);
        pbLoadingTrainings.setVisibility(View.GONE);
        myTrainingsListAdapter.notifyDataSetChanged();
    }

    public void showMyMeals() {
        if (myMeals.size() == 0)
            tvMyMealsEmpty.setVisibility(View.VISIBLE);
        pbLoadingMeals.setVisibility(View.GONE);
        myMealsListAdapter.notifyDataSetChanged();
        final ScrollView scrollView = getView().findViewById(R.id.scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void openAddMyMealNameActivity() {
        Intent openAddMyMealNameActivity = new Intent(getContext(), AddMyMealNameActivity.class);
        openAddMyMealNameActivity.putExtra("user", user);
        startActivity(openAddMyMealNameActivity);
    }

    private void openAddMyTrainingNameActivity() {
        Intent openAddMyTrainingNameActivity = new Intent(getContext(), AddMyTrainingNameActivity.class);
        openAddMyTrainingNameActivity.putExtra("user", user);
        startActivity(openAddMyTrainingNameActivity);
    }

    private boolean layoutOnClick(boolean hidden, NonScrollListView listView, ImageView imArrow) {
        if (hidden) {
            listView.setVisibility(View.VISIBLE);
            imArrow.setImageResource(R.drawable.arrow_down);
            return false;
        } else {
            listView.setVisibility(View.GONE);
            imArrow.setImageResource(R.drawable.arrow_up);
            return true;
        }
    }
}