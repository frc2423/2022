package frc.robot.auto;

import java.util.HashMap;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;

public class Trajectories {
    private static HashMap<String, Trajectory> trajectoryKeeper= new HashMap<String, Trajectory>();
    public static HashMap<String, Trajectory> getTrajectories() {
        trajectoryUploader("Taxi", true, 2, 1);
        trajectoryUploader("BottomTarmacToBottomCargo", false, 2, 1);
        trajectoryUploader("MiddleTarmacToMiddleCargo", false, 2, 1);
        trajectoryUploader("TopTarmacToTopCargo", false, 2, 1);
        trajectoryUploader("BottomCargoToHub", false, 2, 1);
        trajectoryUploader("MiddleCargoToHub", false, 2, 1);
        trajectoryUploader("TopCargoToHub", false, 2, 1);
        trajectoryUploader("BottomCargoToMiddleCargo", false, 2, 1);
        trajectoryUploader("BottomTarmacToCargosToHub", false, 1, 1);
        trajectoryUploader("BottomHubBackUp", false, 2, 1);
        trajectoryUploader("BottomTarmacToMiddleCargo", false, 2, 1);
        trajectoryUploader("MiddleCargoToBottomHub", false, 2, 1);
        trajectoryUploader("MiddleCargoToHubThreeBall", false, 2, 1);

        trajectoryUploader("HubToPlayerStation", false, 2, 1);
        trajectoryUploader("PlayerStationToHub", false, 2, 1);



        trajectoryUploader("TestStraightPath", false, 2, 1);
        trajectoryUploader("TestCurvyPath", false, 2, 1);

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