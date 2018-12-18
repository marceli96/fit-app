package pl.edu.wat.fitapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Meal implements Serializable, FoodSystem {
    private int ID;
    private String name;
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Integer> ingredientWeightList;

    public Meal(int mealID, String mealName) {
        this.ID = mealID;
        this.name = mealName;
        ingredientList = new ArrayList<>();
        ingredientWeightList = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public ArrayList<Integer> getIngredientWeightList() {
        return ingredientWeightList;
    }
}
