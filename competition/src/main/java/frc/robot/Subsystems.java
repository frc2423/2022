package frc.robot;

import frc.robot.auto.Trajectories;
import frc.robot.util.TrajectoryFollower;
import frc.robot.auto.Auto;

public class Subsystems {
   
    public static TrajectoryFollower follower;
    public static Auto auto;


    static void init() {
        follower = new TrajectoryFollower(Trajectories.getTrajectories());
        auto = new Auto();
    }
}
