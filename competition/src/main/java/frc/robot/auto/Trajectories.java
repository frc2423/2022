package frc.robot.auto;

import java.util.HashMap;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;

public class Trajectories {
    private static HashMap<String, Trajectory> trajectoryKeeper= new HashMap<String, Trajectory>();
    public static HashMap<String, Trajectory> getTrajectories() {
        trajectoryUploader("Taxi", true);
        trajectoryUploader("BottomTarmacToBottomCargo");
        trajectoryUploader("MiddleTarmacToMiddleCargo", false, 3, 3);
        trajectoryUploader("TopTarmacToTopCargo");
        trajectoryUploader("BottomCargoToHub");
        trajectoryUploader("MiddleCargoToHub");
        trajectoryUploader("TopCargoToHub");
        trajectoryUploader("BottomCargoToMiddleCargo");
        trajectoryUploader("BottomTarmacToCargosToHub");
        trajectoryUploader("BottomHubBackUp");
        trajectoryUploader("BottomTarmacToMiddleCargo");
        trajectoryUploader("MiddleCargoToBottomHub");
      //  trajectoryUploader("AngledMiddleCargoToHub");
        return trajectoryKeeper;  
    }

    private static void trajectoryUploader(String name){
        trajectoryUploader(name, false, constants.maxSpeedo, constants.maxAccel);
    }

    private static void trajectoryUploader(String name, boolean isReversed){
        trajectoryUploader(name, isReversed, constants.maxSpeedo, constants.maxAccel);
    }

    private static void trajectoryUploader(String name, boolean isReversed, double maxVel, double maxAccel){
     Trajectory addedTrajectory = PathPlanner.loadPath(name, maxVel, maxAccel, isReversed);
        trajectoryKeeper.put(name, addedTrajectory);
    }
}