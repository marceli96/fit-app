package pl.edu.wat.fitapp.mangement;

import android.content.res.Resources;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.database.entity.User;

public class UserSettingsManagement {


    public int calculateCaloriesForExistingUser(RadioGroup rgGoal, View view, User user, String activityLevel, double weight) {
        int calories = 0;
        String goal = getRadioButtonText(rgGoal, view);
        int sex = user.getSex();
        int age = user.getAge();
        int height = user.getHeight();
        String[] activityLevelR = Resources.getSystem().getStringArray(R.array.activityLevel);

        switch (sex) {
            case 0:
                calories = (int) Math.round(655 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
                if (activityLevel.equals(activityLevelR[0]))
                    calories *= 1.2;
                else if (activityLevel.equals(activityLevelR[1]))
                    calories *= 1.3;
                else if (activityLevel.equals(activityLevelR[2]))
                    calories *= 1.5;
                else if (activityLevel.equals(activityLevelR[3]))
                    calories *= 1.7;
                else if (activityLevel.equals(activityLevelR[4]))
                    calories *= 1.9;

                if (goal.equals(Resources.getSystem().getString(R.string.lose)))
                    calories -= 250;
                else if (goal.equals(Resources.getSystem().getString(R.string.gain)))
                    calories += 250;
                break;
            case 1:
                calories = (int) Math.round(66 + (13.7 * weight) + (5 * height) - (6.76 * age));
                if (activityLevel.equals(activityLevelR[0]))
                    calories *= 1.2;
                else if (activityLevel.equals(activityLevelR[1]))
                    calories *= 1.3;
                else if (activityLevel.equals(activityLevelR[2]))
                    calories *= 1.5;
                else if (activityLevel.equals(activityLevelR[3]))
                    calories *= 1.7;
                else if (activityLevel.equals(activityLevelR[4]))
                    calories *= 1.9;

                if (goal.equals(Resources.getSystem().getString(R.string.lose)))
                    calories -= 250;
                else if (goal.equals(Resources.getSystem().getString(R.string.gain)))
                    calories += 250;
                break;
        }
        return calories;
    }

    public int calculateCaloriesForNewUser(RadioGroup rgSex, RadioGroup rgGoal, View view, int age, double weight, int height, String activityLevel) {
        int calories;
        String sex = getRadioButtonText(rgSex, view);
        String goal = getRadioButtonText(rgGoal, view);
        String[] activityLevelR = Resources.getSystem().getStringArray(R.array.activityLevel);

        if (sex.equals(Resources.getSystem().getString(R.string.woman))) {
            calories = (int) Math.round(655 + (9.6 * weight) + (1.8 * height) - (4.7 * age));
            if (activityLevel.equals(activityLevelR[0]))
                calories *= 1.2;
            else if (activityLevel.equals(activityLevelR[1]))
                calories *= 1.3;
            else if (activityLevel.equals(activityLevelR[2]))
                calories *= 1.5;
            else if (activityLevel.equals(activityLevelR[3]))
                calories *= 1.7;
            else if (activityLevel.equals(activityLevelR[4]))
                calories *= 1.9;

            if (goal.equals(Resources.getSystem().getString(R.string.lose)))
                calories -= 250;
            else if (goal.equals(Resources.getSystem().getString(R.string.gain)))
                calories += 250;
        } else {
            calories = (int) Math.round(66 + (13.7 * weight) + (5 * height) - (6.76 * age));
            if (activityLevel.equals(activityLevelR[0]))
                calories *= 1.2;
            else if (activityLevel.equals(activityLevelR[1]))
                calories *= 1.3;
            else if (activityLevel.equals(activityLevelR[2]))
                calories *= 1.5;
            else if (activityLevel.equals(activityLevelR[3]))
                calories *= 1.7;
            else if (activityLevel.equals(activityLevelR[4]))
                calories *= 1.9;

            if (goal.equals(Resources.getSystem().getString(R.string.lose)))
                calories -= 250;
            else if (goal.equals(Resources.getSystem().getString(R.string.gain)))
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
        if (getRadioButtonText(radioGroup, view).equals(Resources.getSystem().getString(R.string.lose)))
            return 0;
        else if (getRadioButtonText(radioGroup, view).equals(Resources.getSystem().getString(R.string.gain)))
            return 1;
        else
            return 2;
    }

    public int getSexInt(RadioGroup radioGroup, View view) {
        if (getRadioButtonText(radioGroup, view).equals(Resources.getSystem().getString(R.string.woman)))
            return 0;
        else
            return 1;
    }

    public int getActivityLevelInt(String activityLevel) {
        String[] activityLevelR = Resources.getSystem().getStringArray(R.array.activityLevel);
        if (activityLevel.equals(activityLevelR[0]))
            return 0;
        else if (activityLevel.equals(activityLevelR[1]))
            return 1;
        else if (activityLevel.equals(activityLevelR[2]))
            return 2;
        else if (activityLevel.equals(activityLevelR[3]))
            return 3;
        else
            return 4;
    }
}
