package pl.edu.wat.fitapp.mangement;

import pl.edu.wat.fitapp.database.entity.Ingredient;
import pl.edu.wat.fitapp.database.entity.Meal;

public class MealManagement {
    private Meal meal;

    public MealManagement(Meal meal) {
        this.meal = meal;
    }

    public int getTotalWeight(){
        int weight = 0;
        for(int i = 0; i < meal.getIngredientList().size(); i++)
            weight += meal.getIngredientList().get(i).getWeight();
        return weight;
    }

    public int getCalories(){
        int sum = 0;
        for (int i = 0; i < meal.getIngredientList().size(); i++)
            sum += meal.getIngredientList().get(i).getCalories() * meal.getIngredientList().get(i).getWeight() / 100;
        return sum;
    }

    public double getCarbohydrates() {
        double sum = 0;
        for (int i = 0; i < meal.getIngredientList().size(); i++)
            sum += meal.getIngredientList().get(i).getCarbohydrates() * meal.getIngredientList().get(i).getWeight() / 100;
        return sum;
    }

    public double getProtein() {
        double sum = 0;
        for (int i = 0; i < meal.getIngredientList().size(); i++)
            sum += meal.getIngredientList().get(i).getProtein() * meal.getIngredientList().get(i).getWeight() / 100;
        return sum;
    }

    public double getFat() {
        double sum = 0;
        for (int i = 0; i < meal.getIngredientList().size(); i++)
            sum += meal.getIngredientList().get(i).getFat() * meal.getIngredientList().get(i).getWeight() / 100;
        return sum;
    }

    public void addIngredientToList(Ingredient ingredient) {
        meal.getIngredientList().add(ingredient);
    }

}
