package pl.edu.wat.fitapp;

import java.io.Serializable;
import java.util.ArrayList;

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