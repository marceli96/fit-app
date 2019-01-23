package pl.edu.wat.fitapp.mangement;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Meal;

public class MyMealManagement {

    public int findMealInList(int mealId, ArrayList<Meal> myMeals) {
        for (int i = 0; i < myMeals.size(); i++) {
            if (myMeals.get(i).getID() == mealId) {
                return i;
            }
        }
        return -1;
    }
}
