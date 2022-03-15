package frc.robot.auto;

import java.util.HashMap;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;

public class Trajectories {

    public static HashMap<String, Trajectory> getTrajectories() {
        HashMap<String, Trajectory> trajectoryKeeper = new HashMap<String, Trajectory>();
        Trajectory taxi = PathPlanner.loadPath("Taxi", constants.maxSpeedo, constants.maxAccel, true);
        trajectoryKeeper.put("Taxi", taxi);
        return trajectoryKeeper;  
    }
}