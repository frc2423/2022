package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;

import frc.robot.util.TrajectoryFollower;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import java.util.List;


public class SimpleAuto extends StateMachine {

    private Trajectory line;
    private TrajectoryFollower follower = new TrajectoryFollower();
    
    public SimpleAuto() {
        super("shooter");

        line = TrajectoryGenerator.generateTrajectory(
            //the line is going along the x axis
            new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
            List.of(),
            new Pose2d(Units.feetToMeters(10), 0, Rotation2d.fromDegrees(0)),
            new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
        );
        follower.addTrajectory("line", line);
    }

    @RunState(name = "shooter")
    public void runShooter(){
        setState("taxi");
        System.out.println("runShooter");
    }

    @InitState(name = "taxi")
    public void initTaxi(){
        follower.setTrajectory("line");
        follower.startFollowing();
    }

    @RunState(name = "taxi")
    public void runTaxi(){
        follower.follow();
        System.out.println("runTaxi");
    }
}
