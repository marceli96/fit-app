package pl.edu.wat.fitapp.Mangement;

import java.util.ArrayList;

import pl.edu.wat.fitapp.Database.Entity.Training;

public class MyTrainingManagement {

    public int findTrainingInList(int trainingId, ArrayList<Training> myTrainings) {
        for (int i = 0; i < myTrainings.size(); i++) {
            if (myTrainings.get(i).getID() == trainingId)
                return i;
        }
        return -1;
    }
}
