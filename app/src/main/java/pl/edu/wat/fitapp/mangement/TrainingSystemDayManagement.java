package pl.edu.wat.fitapp.mangement;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Exercise;
import pl.edu.wat.fitapp.database.entity.Training;
import pl.edu.wat.fitapp.interfaces.TrainingSystem;

public class TrainingSystemDayManagement {

    public int checkTrainingPositionInList(int trainingId, ArrayList<TrainingSystem> trainingSystem) {
        Training tempTraining;
        for (int i = 0; i < trainingSystem.size(); i++) {
            if (trainingSystem.get(i).getClass() == Training.class) {
                tempTraining = (Training) trainingSystem.get(i);
                if (tempTraining.getID() == trainingId)
                    return i;
            }
        }
        return -1;
    }

    public void updateTrainingInTrainingSystemList(int trainingPosition, Exercise tempExercise, ArrayList<TrainingSystem> trainingSystem) {
        Training tempTraining = (Training) trainingSystem.get(trainingPosition);
        tempTraining.addExerciseToList(tempExercise);
    }
}
