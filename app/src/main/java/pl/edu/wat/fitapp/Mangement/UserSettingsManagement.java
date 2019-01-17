package pl.edu.wat.fitapp.Mangement;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import pl.edu.wat.fitapp.Database.Entity.User;

public class UserSettingsManagement {


    public int calculateCaloriesForExistingUser(RadioGroup rgGoal, View view, User user, String activityLevel, double weight) {
        int calories = 0;
        String goal = getRadioButtonText(rgGoal, view);
        int sex = user.getSex();
        int age = user.getAge();
        int height = user.getHeight();

        switch (sex) {
            case 0:
                calories = (int) Math.round(655 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
                if (activityLevel.equals("Brak"))
                    calories *= 1.2;
                else if (activityLevel.equals("Niska"))
                    calories *= 1.3;
                else if (activityLevel.equals("Średnia"))
                    calories *= 1.5;
                else if (activityLevel.equals("Wysoka"))
                    calories *= 1.7;
                else if (activityLevel.equals("Bardzo wysoka"))
                    calories *= 1.9;

                if (goal.equals("Utrata"))
                    calories -= 250;
                else if (goal.equals("Przybranie"))
                    calories += 250;
                break;
            case 1:
                calories = (int) Math.round(66 + (13.7 * weight) + (5 * height) - (6.76 * age));
                if (activityLevel.equals("Brak"))
                    calories *= 1.2;
                else if (activityLevel.equals("Niska"))
                    calories *= 1.3;
                else if (activityLevel.equals("Średnia"))
                    calories *= 1.5;
                else if (activityLevel.equals("Wysoka"))
                    calories *= 1.7;
                else if (activityLevel.equals("Bardzo wysoka"))
                    calories *= 1.9;

                if (goal.equals("Utrata"))
                    calories -= 250;
                else if (goal.equals("Przybranie"))
                    calories += 250;
                break;
        }
        return calories;
    }

    public int calculateCaloriesForNewUser(RadioGroup rgSex, RadioGroup rgGoal, View view, int age, double weight, int height, String activityLevel) {
        int calories;
        String sex = getRadioButtonText(rgSex, view);
        String goal = getRadioButtonText(rgGoal, view);

        if (sex.equals("Kobieta")) {
            calories = (int) Math.round(655 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
            if (activityLevel.equals("Brak"))
                calories *= 1.2;
            else if (activityLevel.equals("Niska"))
                calories *= 1.3;
            else if (activityLevel.equals("Średnia"))
                calories *= 1.5;
            else if (activityLevel.equals("Wysoka"))
                calories *= 1.7;
            else if (activityLevel.equals("Bardzo wysoka"))
                calories *= 1.9;

            if (goal.equals("Utrata"))
                calories -= 250;
            else if (goal.equals("Przybranie"))
                calories += 250;
        } else {
            calories = (int) Math.round(66 + (13.7 * weight) + (5 * height) - (6.76 * age));
            if (activityLevel.equals("Brak"))
                calories *= 1.2;
            else if (activityLevel.equals("Niska"))
                calories *= 1.3;
            else if (activityLevel.equals("Średnia"))
                calories *= 1.5;
            else if (activityLevel.equals("Wysoka"))
                calories *= 1.7;
            else if (activityLevel.equals("Bardzo wysoka"))
                calories *= 1.9;

            if (goal.equals("Utrata"))
                calories -= 250;
            else if (goal.equals("Przybranie"))
                calories += 250;
        }
        return calories;
    }

    private String getRadioButtonText(RadioGroup radioGroup, View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = view.findViewById(radioId);
        return radioButton.getText().toString();
    }

    public int getGoalInt(RadioGroup radioGroup, View view) {
        if (getRadioButtonText(radioGroup, view).equals("Utrata"))
            return 0;
        else if (getRadioButtonText(radioGroup, view).equals("Przybranie"))
            return 1;
        else
            return 2;
    }

    public int getSexInt(RadioGroup radioGroup, View view) {
        if (getRadioButtonText(radioGroup, view).equals("Kobieta"))
            return 0;
        else
            return 1;
    }

    public int getActivityLevelInt(String activityLevel) {
        if (activityLevel.equals("Brak"))
            return 0;
        else if (activityLevel.equals("Niska"))
            return 1;
        else if (activityLevel.equals("Średnia"))
            return 2;
        else if (activityLevel.equals("Wysoka"))
            return 3;
        else
            return 4;
    }
}
