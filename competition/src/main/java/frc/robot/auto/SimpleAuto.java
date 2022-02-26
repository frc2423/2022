package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.NtHelper;
import frc.robot.util.TrajectoryFollower;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;
import frc.robot.subsystem.Shooter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;

import java.util.List;

import com.pathplanner.lib.PathPlanner;


public class SimpleAuto extends StateMachine {

    private Trajectory line;
    private TrajectoryFollower follower = new TrajectoryFollower();
    //private Shooter shooter = new Shooter();
    private Timer timer = new Timer();

    public SimpleAuto() {
        super("shooter");
        //TODO: Implement shooter follow-through and trajectory values
        line = PathPlanner.loadPath("TaxiTaxi", constants.maxSpeedo, constants.maxAccel);
        follower.addTrajectory("line", line);
        follower.setTrajectory("line");
        NtHelper.setString("/robot/auto/name", "simpleAuto2");
    }

    @InitState(name = "shooter")
    public void runShooterInit(){
        //Shooter initialization
        timer.start();
    }

    @RunState(name = "shooter")
    public void runShooter(){
        //shooter.shootOne();
        if (timer.get() > 4){
            setState("taxi");
        }
    }

    @InitState(name = "taxi")
    public void initTaxi(){
        follower.setTrajectory("line");
        follower.startFollowing();
        timer.stop();
    }

    @RunState(name = "taxi")
    public void runTaxi(){
        follower.follow();
    }
}
