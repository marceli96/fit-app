package pl.edu.wat.fitapp.Mangement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.edu.wat.fitapp.Database.Entity.Exercise;
import pl.edu.wat.fitapp.Database.Entity.Training;
import pl.edu.wat.fitapp.Interface.TrainingSystem;

public class TrainingSystemWeekManagement {
    private Date date1Before, date2Before, date3Before, date4Before, date5Before, date6Before, date7Before;

    public TrainingSystemWeekManagement() {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date1Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date2Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date3Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date4Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date5Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date6Before = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date7Before = calendar.getTime();
    }

    public int checkTrainingPositionInListForDate(int trainingId, Date date, ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek) {
        if (date.toString().equals(date1Before.toString())) {
            return checkTrainingPositionInList(trainingSystemWeek.get(0), trainingId);
        } else if (date.toString().equals(date2Before.toString())) {
            return checkTrainingPositionInList(trainingSystemWeek.get(1), trainingId);
        } else if (date.toString().equals(date3Before.toString())) {
            return checkTrainingPositionInList(trainingSystemWeek.get(2), trainingId);
        } else if (date.toString().equals(date4Before.toString())) {
            return checkTrainingPositionInList(trainingSystemWeek.get(3), trainingId);
        } else if (date.toString().equals(date5Before.toString())) {
            return checkTrainingPositionInList(trainingSystemWeek.get(4), trainingId);
        } else if (date.toString().equals(date6Before.toString())) {
            return checkTrainingPositionInList(trainingSystemWeek.get(5), trainingId);
        } else if (date.toString().equals(date7Before.toString())) {
            return checkTrainingPositionInList(trainingSystemWeek.get(6), trainingId);
        }
        return -1;
    }

    public int checkTrainingPositionInList(ArrayList<TrainingSystem> trainingSystemXDayBefore, int trainingId) {
        Training tempTraining;
        for (int i = 0; i < trainingSystemXDayBefore.size(); i++) {
            if (trainingSystemXDayBefore.get(i).getClass() == Training.class) {
                tempTraining = (Training) trainingSystemXDayBefore.get(i);
                if (tempTraining.getID() == trainingId)
                    return i;
            }
        }
        return -1;
    }

    public void addTrainingToTrainingSystemListForDate(Training tempTraining, Date date, ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek) {
        if (date.toString().equals(date1Before.toString())) {
            trainingSystemWeek.get(0).add(tempTraining);
        } else if (date.toString().equals(date2Before.toString())) {
            trainingSystemWeek.get(1).add(tempTraining);
        } else if (date.toString().equals(date3Before.toString())) {
            trainingSystemWeek.get(2).add(tempTraining);
        } else if (date.toString().equals(date4Before.toString())) {
            trainingSystemWeek.get(3).add(tempTraining);
        } else if (date.toString().equals(date5Before.toString())) {
            trainingSystemWeek.get(4).add(tempTraining);
        } else if (date.toString().equals(date6Before.toString())) {
            trainingSystemWeek.get(5).add(tempTraining);
        } else if (date.toString().equals(date7Before.toString())) {
            trainingSystemWeek.get(6).add(tempTraining);
        }
    }

    public void updateTrainingInTrainingSystemListForDate(int trainingPosition, Exercise tempExercise, Date date, ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek) {
        Training tempTraining;
        if (date.toString().equals(date1Before.toString())) {
            tempTraining = (Training) trainingSystemWeek.get(0).get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date2Before.toString())) {
            tempTraining = (Training) trainingSystemWeek.get(1).get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date3Before.toString())) {
            tempTraining = (Training) trainingSystemWeek.get(2).get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date4Before.toString())) {
            tempTraining = (Training) trainingSystemWeek.get(3).get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date5Before.toString())) {
            tempTraining = (Training) trainingSystemWeek.get(4).get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date6Before.toString())) {
            tempTraining = (Training) trainingSystemWeek.get(5).get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        } else if (date.toString().equals(date7Before.toString())) {
            tempTraining = (Training) trainingSystemWeek.get(6).get(trainingPosition);
            tempTraining.addExerciseToList(tempExercise);
        }
    }

    public void addExerciseToFoodSystemListForDate(Exercise tempExercise, Date date, ArrayList<ArrayList<TrainingSystem>> trainingSystemWeek) {
        if (date.toString().equals(date1Before.toString())) {
            trainingSystemWeek.get(0).add(tempExercise);
        } else if (date.toString().equals(date2Before.toString())) {
            trainingSystemWeek.get(1).add(tempExercise);
        } else if (date.toString().equals(date3Before.toString())) {
            trainingSystemWeek.get(2).add(tempExercise);
        } else if (date.toString().equals(date4Before.toString())) {
            trainingSystemWeek.get(3).add(tempExercise);
        } else if (date.toString().equals(date5Before.toString())) {
            trainingSystemWeek.get(4).add(tempExercise);
        } else if (date.toString().equals(date6Before.toString())) {
            trainingSystemWeek.get(5).add(tempExercise);
        } else if (date.toString().equals(date7Before.toString())) {
            trainingSystemWeek.get(6).add(tempExercise);
        }
    }
}
