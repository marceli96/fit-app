package pl.edu.wat.fitapp.mangement;

import java.util.ArrayList;

import pl.edu.wat.fitapp.database.entity.Training;

public class MyTrainingManagement {

    public int findTrainingInList(int trainingId, ArrayList<Training> myTrainings) {
        for (int i = 0; i < myTrainings.size(); i++) {
            if (myTrainings.get(i).getID() == trainingId)
                return i;
        }
        return -1;
    }
}
