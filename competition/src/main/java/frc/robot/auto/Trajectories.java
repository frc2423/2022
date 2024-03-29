package frc.robot.auto;

import java.util.HashMap;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.trajectory.Trajectory;

public class Trajectories {
    private static HashMap<String, Trajectory> trajectoryKeeper= new HashMap<String, Trajectory>();
    public static HashMap<String, Trajectory> getTrajectories() {
        trajectoryUploader("Taxi", true, 2.5, 1.5);
        trajectoryUploader("BottomTarmacToBottomCargo", false, 2.5, 1.5);
        trajectoryUploader("MiddleTarmacToMiddleCargo", false, 2, 1);
        trajectoryUploader("TopTarmacToTopCargo", false, 2, 1);
        trajectoryUploader("BottomCargoToHub", false, 2.5, 1.5);
        trajectoryUploader("MiddleCargoToHub", false, 2, 1);
        trajectoryUploader("TopCargoToHub", false, 2, 1);
        trajectoryUploader("BottomCargoToMiddleCargo", false, 2, 1);
        trajectoryUploader("BottomTarmacToCargosToHub", false, 3.5, 1.5);
        trajectoryUploader("BottomHubBackUp", false, 2, 1);
        trajectoryUploader("BottomTarmacToMiddleCargo", false, 2, 1);
        trajectoryUploader("MiddleCargoToBottomHub", false, 2, 1);
      //  trajectoryUploader("AngledMiddleCargoToHub");

        trajectoryUploader("TestStraightPath", false, 2, 1);
        trajectoryUploader("TestCurvyPath", false, 2, 1);
        trajectoryUploader("GoHummusPlayer", false, 2, 1);
        trajectoryUploader("HPToHome", false, 2, 1);

        return trajectoryKeeper;  
    }

    private static void trajectoryUploader(String name, boolean isReversed, double maxVel, double maxAccel){
     Trajectory addedTrajectory = PathPlanner.loadPath(name, maxVel, maxAccel, isReversed);
        trajectoryKeeper.put(name, addedTrajectory);
    }
}