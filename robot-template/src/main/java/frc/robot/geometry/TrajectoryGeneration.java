package frc.robot.geometry;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.geometry.Translation2d;

//Main point of this file is to do trajectory generation, inputing feet and outputing a trajectory in meters

public class TrajectoryGeneration {
    private static TrajectoryConfig config;

    public static void setConfig(double maxSpeedFeet, double maxAccelFeet, TrajectoryHelper trajectoryHelper){
        config = new TrajectoryConfig( // kmaxSpeed, kmaxAccel
            Units.feetToMeters(maxSpeedFeet),  Units.feetToMeters(maxAccelFeet)
        );
        config.setKinematics(trajectoryHelper.getKinematics());
    }

    public static TrajectoryConfig getConfig(){
        return config;
    }

    public static void switchReverseConfig() {
        config.setReversed(!config.isReversed());
    }

    public static void reverseConfig(boolean val) {
        config.setReversed(val);
    }

    public static Trajectory Generate(Pose start, Pose end, List<Translate> waypointsInit){
        config.setReversed(false);
        List<Translation2d> waypoints = translateWaypoints(waypointsInit);
        Trajectory traj = TrajectoryGenerator.generateTrajectory(
            // path
            start.getPose(), waypoints, end.getPose(),
            // Pass config
            config
        );
        return traj;
    }

    public static Trajectory GenerateReversed(Pose start, Pose end, List<Translate> waypointsInit){
        List<Translation2d> waypoints = translateWaypoints(waypointsInit);
        config.setReversed(true);
        Trajectory traj = TrajectoryGenerator.generateTrajectory(
            // path
            start.getPose(), waypoints, end.getPose(),
            // Pass config
            config
        );
        return traj;
    }

    public static Trajectory Generate(List<Pose> waypointsInitial){
        List<Pose2d> waypoints = PoseToPose2d(waypointsInitial);
        Trajectory traj = TrajectoryGenerator.generateTrajectory(
            waypoints, config
        );
        return traj;
    }

    private static List<Translation2d> translateWaypoints(List<Translate> initial){
        List<Translation2d> waypoints = new ArrayList<Translation2d>();
        initial.forEach((point) -> {
            waypoints.add(point.getTranslation());
        });
        return waypoints;
    }

    private static List<Pose2d> PoseToPose2d(List<Pose> initial){
        List<Pose2d> waypoints = new ArrayList<Pose2d>();
        initial.forEach((point) -> {
            waypoints.add(point.getPose());
        });
        return waypoints;
    }

}
