package pl.edu.wat.fitapp.mangement;

import pl.edu.wat.fitapp.database.entity.Ingredient;

public class IngredientManagement {
    private Ingredient ingredient;

    public IngredientManagement(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getCarbohydratesForWeight(){
        return ingredient.getCarbohydrates() * ingredient.getWeight() / 100;
    }

    public double getProteinForWeight(){
        return ingredient.getProtein() * ingredient.getWeight() / 100;
    }

    public double getFatForWeight(){
        return ingredient.getFat() * ingredient.getWeight() / 100;
    }

    public int getCaloriesForWeight(){
        return ingredient.getCalories() * ingredient.getWeight() / 100;
    }

}
