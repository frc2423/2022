package frc.robot.auto;

import java.util.HashMap;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;

public class Trajectories {
    private static HashMap<String, Trajectory> trajectoryKeeper= new HashMap<String, Trajectory>();
    public static HashMap<String, Trajectory> getTrajectories() {
        trajectoryUploader("Taxi");
        trajectoryUploader("BottomTarmacToBottomCargo");
        trajectoryUploader("MiddleTarmacToMiddleCargo");
        trajectoryUploader("TopTarmacToTopCargo");
        trajectoryUploader("BottomCargoToHub");
        trajectoryUploader("MiddleCargoToHub");
        trajectoryUploader("TopCargoToHub");
        trajectoryUploader("BottomCargoToMiddleCargo");
      //  trajectoryUploader("AngledMiddleCargoToHub");
        return trajectoryKeeper;  
    }

private static void trajectoryUploader(String name){
     Trajectory addedTrajectory = PathPlanner.loadPath(name, constants.maxSpeedo, constants.maxAccel, true);
        trajectoryKeeper.put(name, addedTrajectory);
    }
}