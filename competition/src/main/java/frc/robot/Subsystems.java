package frc.robot;

import frc.robot.auto.Trajectories;
import frc.robot.util.TrajectoryFollower;

public class Subsystems {
   
    public static TrajectoryFollower follower;

    static void init() {
        follower = new TrajectoryFollower(Trajectories.getTrajectories());
    }
}
