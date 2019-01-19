package pl.edu.wat.fitapp.Mangement;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Entity.Meal;

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
