package pl.edu.wat.fitapp.Database;

import java.io.Serializable;
import java.util.ArrayList;

import pl.edu.wat.fitapp.Interface.TrainingSystem;

public class Training implements Serializable, TrainingSystem {
    private int ID;
    private String name;
    private ArrayList<Exercise> exerciseList;

    public Training(int ID, String name) {
        this.ID = ID;
        this.name = name;
        exerciseList = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Exercise> getExerciseList() {
        return exerciseList;
    }


    public void addExerciseToList(Exercise exercise){
        exerciseList.add(exercise);
    }
}
