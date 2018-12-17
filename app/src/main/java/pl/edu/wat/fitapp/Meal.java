package pl.edu.wat.fitapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Meal implements Serializable {
    private int mealID;
    private String mealName;
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Integer> ingredientWeightList;

    public Meal(int mealID, String mealName) {
        this.mealID = mealID;
        this.mealName = mealName;
        ingredientList = new ArrayList<>();
        ingredientWeightList = new ArrayList<>();
    }

    public int getMealID() {
        return mealID;
    }

    public String getMealName() {
        return mealName;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public ArrayList<Integer> getIngredientWeightList() {
        return ingredientWeightList;
    }
}
