package pl.edu.wat.fitapp.Database.Entity;

import java.io.Serializable;
import java.util.ArrayList;

import pl.edu.wat.fitapp.Interface.FoodSystem;
import pl.edu.wat.fitapp.Mangement.MealManagement;

public class Meal implements Serializable, FoodSystem {
    private int ID;
    private String name;
    private int weight;
    private ArrayList<Ingredient> ingredientList;

    public Meal(int mealID, String mealName) {
        this.ID = mealID;
        this.name = mealName;
        ingredientList = new ArrayList<>();
    }

    public int getTotalWeight(){
        MealManagement mealManagement = new MealManagement(this);
        return mealManagement.getTotalWeight();
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public double getCarbohydrates() {
        MealManagement mealManagement = new MealManagement(this);
        return mealManagement.getCarbohydrates();
    }

    public double getProtein() {
        MealManagement mealManagement = new MealManagement(this);
        return mealManagement.getProtein();
    }

    public double getFat() {
        MealManagement mealManagement = new MealManagement(this);
        return mealManagement.getFat();
    }

    public int getCalories() {
        MealManagement mealManagement = new MealManagement(this);
        return mealManagement.getCalories();
    }

    public void addIngredientToList(Ingredient ingredient) {
        ingredientList.add(ingredient);
    }
}
