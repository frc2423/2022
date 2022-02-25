package frc.robot.auto;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;
import frc.robot.Devices;
import frc.robot.constants.constants;
import frc.robot.subsystem.Drivetrain;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.util.Targeting;
import frc.robot.util.TrajectoryFollower;

import frc.robot.util.TrajectoryFollower;

import java.util.List;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import frc.robot.subsystem.Intake;

public class taxi extends StateMachine {
    //Values subject to change upon completed trajectory integration
    Trajectory cabTrajectory = TrajectoryGenerator.generateTrajectory(
        //the line is going along the x axis
        new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
        List.of(),
        new Pose2d(Units.feetToMeters(10), 0, Rotation2d.fromDegrees(0)),
        new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );
    TrajectoryFollower follower = new TrajectoryFollower();

    public taxi(){
        super("Taxicab");
        follower.addTrajectory("CabRoute", cabTrajectory);
        follower.setTrajectory("CabRoute");
    }
    @InitState(name = "Taxicab")
    public void taxicabinit(){
        follower.startFollowing();
    }

    @RunState(name = "Taxicab")
    public void taxicabrun(){
        follower.follow();
    }
}
