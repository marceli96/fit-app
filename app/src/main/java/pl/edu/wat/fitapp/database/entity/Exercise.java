package pl.edu.wat.fitapp.database.entity;

import java.io.Serializable;

import pl.edu.wat.fitapp.interfaces.TrainingSystem;

public class Exercise implements Serializable, TrainingSystem, Cloneable {
    private int ID;
    private String name;
    private int series;
    private int repetitions;

    public Exercise(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getSeries() {
        return series;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
