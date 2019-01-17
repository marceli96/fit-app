package pl.edu.wat.fitapp.Main.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.TrainingSystemListAdapter;
import pl.edu.wat.fitapp.Database.Connection.FoodSystemDayConnection;
import pl.edu.wat.fitapp.AndroidComponent.ListAdapter.FoodSystemListAdapter;
import pl.edu.wat.fitapp.Database.Connection.TrainingSystemDayConnection;
import pl.edu.wat.fitapp.Dialog.FoodSystemOnClickDialog;
import pl.edu.wat.fitapp.Dialog.TrainingSystemOnClickDialog;
import pl.edu.wat.fitapp.Main.Fragment.AddToSystem.AddToFoodSystemActivity;
import pl.edu.wat.fitapp.Main.Fragment.AddToSystem.AddToTrainingSystemActivity;
import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.Interface.TrainingSystem;
import pl.edu.wat.fitapp.AndroidComponent.NonScrollListView;
import pl.edu.wat.fitapp.Main.MainActivity;
import pl.edu.wat.fitapp.Mangement.MacrocomponentManagement;
import pl.edu.wat.fitapp.R;

public class HomeFragment extends Fragment {
    private TextView tvEatenCalories, tvReqCalories, tvEatenCarbohydrates, tvReqCarbohydrates, tvEatenProtein, tvReqProtein, tvEatenFat, tvReqFat, tvExerciseAmount;
    private ProgressBar pbCalories, pbCarbohydrates, pbProtein, pbFat, pbLoading;
    private NonScrollListView lvBreakfast, lvSecondBreakfast, lvLunch, lvDinner, lvSnack, lvSupper, lvTraining;
    private ImageView imArrowBreakfast, imArrowSecondBreakfast, imArrowLunch, imArrowDinner, imArrowSnack, imArrowSupper, imArrowTraining, imAddBreakfast, imAddSecondBreakfast, imAddLunch, imAddDinner, imAddSnack, imAddSupper, imAddTraining;
    private LinearLayout llProgressBars, llBreakfast, llSecondBreakfast, llLunch, llDinner, llSnack, llSupper, llTraining;
    private ArrayList<ArrayList<FoodSystem>> foodSystemDay;
    private ArrayList<TrainingSystem> trainingSystemDay;
    private ArrayList<FoodSystemListAdapter> foodSystemMealTimeAdapters;
    private TrainingSystemListAdapter trainingSystemListAdapter;
    private User user;
    private FoodSystemDayConnection foodSystemDayConnection;
    private TrainingSystemDayConnection trainingSystemDayConnection;
    private FoodSystemOnClickDialog foodSystemOnClickDialog;
    private TrainingSystemOnClickDialog trainingSystemOnClickDialog;
    private boolean hiddenBreakfast = false, hiddenSecondBreakfast = false, hiddenLunch = false, hiddenDinner = false, hiddenSnack = false, hiddenSupper = false, hiddenTraining = false;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle("Strona główna");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        user = (User) getActivity().getIntent().getSerializableExtra("user");

        initializeArrays();

        pbLoading = view.findViewById(R.id.pbLoading);
        pbCalories = view.findViewById(R.id.pbCalories);
        pbCarbohydrates = view.findViewById(R.id.pbCarbohydrates);
        pbProtein = view.findViewById(R.id.pbProtein);
        pbFat = view.findViewById(R.id.pbFat);

        tvEatenCalories = view.findViewById(R.id.tvEatenCalories);
        tvReqCalories = view.findViewById(R.id.tvReqCalories);
        tvEatenCarbohydrates = view.findViewById(R.id.tvEatenCarbohydrates);
        tvReqCarbohydrates = view.findViewById(R.id.tvReqCarbohydrates);
        tvEatenProtein = view.findViewById(R.id.tvEatenProtein);
        tvReqProtein = view.findViewById(R.id.tvReqProtein);
        tvEatenFat = view.findViewById(R.id.tvEatenFat);
        tvReqFat = view.findViewById(R.id.tvReqFat);
        tvExerciseAmount = view.findViewById(R.id.tvExerciseAmount);

        lvBreakfast = view.findViewById(R.id.lvBreakfast);
        lvSecondBreakfast = view.findViewById(R.id.lvSecondBreakfast);
        lvLunch = view.findViewById(R.id.lvLunch);
        lvDinner = view.findViewById(R.id.lvDinner);
        lvSnack = view.findViewById(R.id.lvSnack);
        lvSupper = view.findViewById(R.id.lvSupper);
        lvTraining = view.findViewById(R.id.lvTraining);

        imArrowBreakfast = view.findViewById(R.id.imArrowBreakfast);
        imArrowSecondBreakfast = view.findViewById(R.id.imArrowSecondBreakfast);
        imArrowLunch = view.findViewById(R.id.imArrowLunch);
        imArrowDinner = view.findViewById(R.id.imArrowDinner);
        imArrowSnack = view.findViewById(R.id.imArrowSnack);
        imArrowSupper = view.findViewById(R.id.imArrowSupper);
        imArrowTraining = view.findViewById(R.id.imArrowTraining);

        llProgressBars = view.findViewById(R.id.llProgressBars);
        llBreakfast = view.findViewById(R.id.llBreakfast);
        llSecondBreakfast = view.findViewById(R.id.llSecondBreakfast);
        llLunch = view.findViewById(R.id.llLunch);
        llDinner = view.findViewById(R.id.llDinner);
        llSnack = view.findViewById(R.id.llSnack);
        llSupper = view.findViewById(R.id.llSupper);
        llTraining = view.findViewById(R.id.llTraining);

        imAddBreakfast = view.findViewById(R.id.imAddBreakfast);
        imAddSecondBreakfast = view.findViewById(R.id.imAddSecondBreakfast);
        imAddLunch = view.findViewById(R.id.imAddLunch);
        imAddDinner = view.findViewById(R.id.imAddDinner);
        imAddSnack = view.findViewById(R.id.imAddSnack);
        imAddSupper = view.findViewById(R.id.imAddSupper);
        imAddTraining = view.findViewById(R.id.imAddTraining);

        trainingSystemListAdapter = new TrainingSystemListAdapter(getActivity(), R.layout.listview_adapter_show_trainingsystem, trainingSystemDay);

        lvBreakfast.setAdapter(foodSystemMealTimeAdapters.get(0));
        lvSecondBreakfast.setAdapter(foodSystemMealTimeAdapters.get(1));
        lvLunch.setAdapter(foodSystemMealTimeAdapters.get(2));
        lvDinner.setAdapter(foodSystemMealTimeAdapters.get(3));
        lvSnack.setAdapter(foodSystemMealTimeAdapters.get(4));
        lvSupper.setAdapter(foodSystemMealTimeAdapters.get(5));
        lvTraining.setAdapter(trainingSystemListAdapter);

        foodSystemDayConnection = new FoodSystemDayConnection(this, foodSystemDay);
        foodSystemDayConnection.getFoodSystemFromDay(user.getUserID(), dateFormat.format(date));

        trainingSystemDayConnection = new TrainingSystemDayConnection(this, trainingSystemDay);
        trainingSystemDayConnection.getTrainingSystem(user.getUserID());

        foodSystemOnClickDialog = new FoodSystemOnClickDialog(this, foodSystemDay, foodSystemMealTimeAdapters);
        trainingSystemOnClickDialog = new TrainingSystemOnClickDialog(this, trainingSystemDay, trainingSystemListAdapter);
        lvBreakfast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClickDialog.build(position, 0, foodSystemDay.get(0), user.getUserID());
            }
        });

        lvSecondBreakfast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClickDialog.build(position, 1, foodSystemDay.get(1), user.getUserID());
            }
        });

        lvLunch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClickDialog.build(position, 2, foodSystemDay.get(2), user.getUserID());
            }
        });

        lvDinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClickDialog.build(position, 3, foodSystemDay.get(3), user.getUserID());
            }
        });

        lvSnack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClickDialog.build(position, 4, foodSystemDay.get(4), user.getUserID());
            }
        });

        lvSupper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodSystemOnClickDialog.build(position, 5, foodSystemDay.get(5), user.getUserID());
            }
        });

        lvTraining.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trainingSystemOnClickDialog.build(position, user.getUserID());
            }
        });

        String tempString = String.valueOf(user.getCaloricDemand()) + " kcal";
        tvReqCalories.setText(tempString);

        tempString = String.valueOf((int) (0.5 * user.getCaloricDemand() / 4)) + "-" + String.valueOf((int) (0.65 * user.getCaloricDemand() / 4));
        tvReqCarbohydrates.setText(tempString);

        tempString = String.valueOf((int) (0.15 * user.getCaloricDemand() / 4)) + "-" + String.valueOf((int) (0.25 * user.getCaloricDemand() / 4));
        tvReqProtein.setText(tempString);

        tempString = String.valueOf((int) (0.2 * user.getCaloricDemand() / 9)) + "-" + String.valueOf((int) (0.3 * user.getCaloricDemand() / 9));
        tvReqFat.setText(tempString);

        pbCalories.setMax(user.getCaloricDemand());
        pbCarbohydrates.setMax((int) (0.5 * user.getCaloricDemand() / 4));
        pbProtein.setMax((int) (0.15 * user.getCaloricDemand() / 4));
        pbFat.setMax((int) (0.2 * user.getCaloricDemand() / 9));

        llBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenBreakfast = layoutOnClick(hiddenBreakfast, lvBreakfast, imArrowBreakfast);
            }
        });
        llSecondBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenSecondBreakfast = layoutOnClick(hiddenSecondBreakfast, lvSecondBreakfast, imArrowSecondBreakfast);
            }
        });
        llLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenLunch = layoutOnClick(hiddenLunch, lvLunch, imArrowLunch);
            }
        });
        llDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenDinner = layoutOnClick(hiddenDinner, lvDinner, imArrowDinner);
            }
        });
        llSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenSnack = layoutOnClick(hiddenSnack, lvSnack, imArrowSnack);
            }
        });
        llSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenSupper = layoutOnClick(hiddenSupper, lvSupper, imArrowSupper);
            }
        });
        llTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenTraining = layoutOnClick(hiddenTraining, lvTraining, imArrowTraining);
            }
        });

        imAddBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddToFoodSystem(0);
            }
        });
        imAddSecondBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddToFoodSystem(1);
            }
        });
        imAddLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddToFoodSystem(2);
            }
        });
        imAddDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddToFoodSystem(3);
            }
        });
        imAddSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddToFoodSystem(4);
            }
        });
        imAddSupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddToFoodSystem(5);
            }
        });
        imAddTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddToTrainingSystem();
            }
        });

        return view;
    }

    private boolean layoutOnClick(boolean hiddenMealTime, NonScrollListView lvMealTime, ImageView imArrowMealTime) {
        if (hiddenMealTime) {
            lvMealTime.setVisibility(View.VISIBLE);
            imArrowMealTime.setImageResource(R.drawable.arrow_down);
            return false;
        } else {
            lvMealTime.setVisibility(View.GONE);
            imArrowMealTime.setImageResource(R.drawable.arrow_up);
            return true;
        }
    }

    private void initializeArrays() {
        foodSystemDay = new ArrayList<>();
        trainingSystemDay = new ArrayList<>();
        foodSystemMealTimeAdapters = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            foodSystemDay.add(new ArrayList<FoodSystem>());
            foodSystemMealTimeAdapters.add(new FoodSystemListAdapter(getActivity(), R.layout.listview_adapter_show_foodsystem, foodSystemDay.get(i)));
        }
    }

    public void showMealLayoutsAndUpdate() {
        pbLoading.setVisibility(View.GONE);
        llProgressBars.setVisibility(View.VISIBLE);
        llBreakfast.setVisibility(View.VISIBLE);
        llSecondBreakfast.setVisibility(View.VISIBLE);
        llLunch.setVisibility(View.VISIBLE);
        llDinner.setVisibility(View.VISIBLE);
        llSnack.setVisibility(View.VISIBLE);
        llSupper.setVisibility(View.VISIBLE);

        for (int i = 0; i < 6; i++)
            foodSystemMealTimeAdapters.get(i).notifyDataSetChanged();
        updateMacrosOnMealTimes();
        updateEatenMacros();
    }

    public void updateMacrosOnMealTimes() {
        DecimalFormat decFormat = new DecimalFormat("0.0");
        MacrocomponentManagement macroMg = new MacrocomponentManagement();
        View view = getView();

        TextView tvCarbohydratesBreakfast = view.findViewById(R.id.tvCarbohydratesBreakfast);
        TextView tvProteinBreakfast = view.findViewById(R.id.tvProteinBreakfast);
        TextView tvFatBreakfast = view.findViewById(R.id.tvFatBreakfast);
        TextView tvCaloriesBreakfast = view.findViewById(R.id.tvCaloriesBreakfast);

        String tempString = String.valueOf(decFormat.format(macroMg.getCarbohydratesForMealTimeFromDay(foodSystemDay.get(0)))) + " g";
        tvCarbohydratesBreakfast.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getProteinForMealTimeFromDay(foodSystemDay.get(0)))) + " g";
        tvProteinBreakfast.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getFatForMealTimeFromDay(foodSystemDay.get(0)))) + " g";
        tvFatBreakfast.setText(tempString);
        tempString = String.valueOf(macroMg.getCaloriesForMealTimeFromDay(foodSystemDay.get(0))) + " kcal";
        tvCaloriesBreakfast.setText(tempString);

        TextView tvCarbohydratesSecondBreakfast = view.findViewById(R.id.tvCarbohydratesSecondBreakfast);
        TextView tvProteinSecondBreakfast = view.findViewById(R.id.tvProteinSecondBreakfast);
        TextView tvFatSecondBreakfast = view.findViewById(R.id.tvFatSecondBreakfast);
        TextView tvCaloriesSecondBreakfast = view.findViewById(R.id.tvCaloriesSecondBreakfast);

        tempString = String.valueOf(decFormat.format(macroMg.getCarbohydratesForMealTimeFromDay(foodSystemDay.get(1)))) + " g";
        tvCarbohydratesSecondBreakfast.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getProteinForMealTimeFromDay(foodSystemDay.get(1)))) + " g";
        tvProteinSecondBreakfast.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getFatForMealTimeFromDay(foodSystemDay.get(1)))) + " g";
        tvFatSecondBreakfast.setText(tempString);
        tempString = String.valueOf(macroMg.getCaloriesForMealTimeFromDay(foodSystemDay.get(1))) + " kcal";
        tvCaloriesSecondBreakfast.setText(tempString);

        TextView tvCarbohydratesLunch = view.findViewById(R.id.tvCarbohydratesLunch);
        TextView tvProteinLunch = view.findViewById(R.id.tvProteinLunch);
        TextView tvFatLunch = view.findViewById(R.id.tvFatLunch);
        TextView tvCaloriesLunch = view.findViewById(R.id.tvCaloriesLunch);

        tempString = String.valueOf(decFormat.format(macroMg.getCarbohydratesForMealTimeFromDay(foodSystemDay.get(2)))) + " g";
        tvCarbohydratesLunch.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getProteinForMealTimeFromDay(foodSystemDay.get(2)))) + " g";
        tvProteinLunch.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getFatForMealTimeFromDay(foodSystemDay.get(2)))) + " g";
        tvFatLunch.setText(tempString);
        tempString = String.valueOf(macroMg.getCaloriesForMealTimeFromDay(foodSystemDay.get(2))) + " kcal";
        tvCaloriesLunch.setText(tempString);

        TextView tvCarbohydratesDinner = view.findViewById(R.id.tvCarbohydratesDinner);
        TextView tvProteinDinner = view.findViewById(R.id.tvProteinDinner);
        TextView tvFatDinner = view.findViewById(R.id.tvFatDinner);
        TextView tvCaloriesDinner = view.findViewById(R.id.tvCaloriesDinner);

        tempString = String.valueOf(decFormat.format(macroMg.getCarbohydratesForMealTimeFromDay(foodSystemDay.get(3)))) + " g";
        tvCarbohydratesDinner.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getProteinForMealTimeFromDay(foodSystemDay.get(3)))) + " g";
        tvProteinDinner.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getFatForMealTimeFromDay(foodSystemDay.get(3)))) + " g";
        tvFatDinner.setText(tempString);
        tempString = String.valueOf(macroMg.getCaloriesForMealTimeFromDay(foodSystemDay.get(3))) + " kcal";
        tvCaloriesDinner.setText(tempString);

        TextView tvCarbohydratesSnack = view.findViewById(R.id.tvCarbohydratesSnack);
        TextView tvProteinSnack = view.findViewById(R.id.tvProteinSnack);
        TextView tvFatSnack = view.findViewById(R.id.tvFatSnack);
        TextView tvCaloriesSnack = view.findViewById(R.id.tvCaloriesSnack);

        tempString = String.valueOf(decFormat.format(macroMg.getCarbohydratesForMealTimeFromDay(foodSystemDay.get(4)))) + " g";
        tvCarbohydratesSnack.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getProteinForMealTimeFromDay(foodSystemDay.get(4)))) + " g";
        tvProteinSnack.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getFatForMealTimeFromDay(foodSystemDay.get(4)))) + " g";
        tvFatSnack.setText(tempString);
        tempString = String.valueOf(macroMg.getCaloriesForMealTimeFromDay(foodSystemDay.get(4))) + " kcal";
        tvCaloriesSnack.setText(tempString);

        TextView tvCarbohydratesSupper = view.findViewById(R.id.tvCarbohydratesSupper);
        TextView tvProteinSupper = view.findViewById(R.id.tvProteinSupper);
        TextView tvFatSupper = view.findViewById(R.id.tvFatSupper);
        TextView tvCaloriesSupper = view.findViewById(R.id.tvCaloriesSupper);

        tempString = String.valueOf(decFormat.format(macroMg.getCarbohydratesForMealTimeFromDay(foodSystemDay.get(5)))) + " g";
        tvCarbohydratesSupper.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getProteinForMealTimeFromDay(foodSystemDay.get(5)))) + " g";
        tvProteinSupper.setText(tempString);
        tempString = String.valueOf(decFormat.format(macroMg.getFatForMealTimeFromDay(foodSystemDay.get(5)))) + " g";
        tvFatSupper.setText(tempString);
        tempString = String.valueOf(macroMg.getCaloriesForMealTimeFromDay(foodSystemDay.get(5))) + " kcal";
        tvCaloriesSupper.setText(tempString);
    }

    public void updateEatenMacros() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        MacrocomponentManagement macroMg = new MacrocomponentManagement();
        tvEatenCalories.setText(String.valueOf(macroMg.getCaloriesFromDay(foodSystemDay)));
        tvEatenCarbohydrates.setText(String.valueOf(decimalFormat.format(macroMg.getCarbohydratesFromDay(foodSystemDay))));
        tvEatenProtein.setText(String.valueOf(decimalFormat.format(macroMg.getProteinFromDay(foodSystemDay))));
        tvEatenFat.setText(String.valueOf(decimalFormat.format(macroMg.getFatFromDay(foodSystemDay))));


        pbCalories.setProgress(macroMg.getCaloriesFromDay(foodSystemDay));
        pbCarbohydrates.setProgress((int) Math.round(macroMg.getCarbohydratesFromDay(foodSystemDay)));
        pbProtein.setProgress((int) Math.round(macroMg.getProteinFromDay(foodSystemDay)));
        pbFat.setProgress((int) Math.round(macroMg.getFatFromDay(foodSystemDay)));
    }

    public void updateExerciseAmount() {
        int exerciseAmount = 0;
        for (int i = 0; i < trainingSystemDay.size(); i++) {
            if (trainingSystemDay.get(i).getClass() == Exercise.class)
                exerciseAmount++;
            else {
                Training tempTraining = (Training) trainingSystemDay.get(i);
                exerciseAmount += tempTraining.getExerciseList().size();
            }
        }
        tvExerciseAmount.setText(String.valueOf(exerciseAmount));
    }

    private void openAddToFoodSystem(int mealTime) {
        Intent openAddToFoodSystemActivity = new Intent(getContext(), AddToFoodSystemActivity.class);
        openAddToFoodSystemActivity.putExtra("user", user);
        openAddToFoodSystemActivity.putExtra("mealTime", mealTime);
        startActivity(openAddToFoodSystemActivity);
    }

    private void openAddToTrainingSystem() {
        Intent openAddToTrainingSystemActivity = new Intent(getContext(), AddToTrainingSystemActivity.class);
        openAddToTrainingSystemActivity.putExtra("user", user);
        startActivity(openAddToTrainingSystemActivity);
    }

    public LinearLayout getLlTraining() {
        return llTraining;
    }

    public TrainingSystemListAdapter getTrainingSystemListAdapter() {
        return trainingSystemListAdapter;
    }
}