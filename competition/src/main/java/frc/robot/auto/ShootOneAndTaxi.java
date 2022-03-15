package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.NtHelper;
import frc.robot.util.TrajectoryFollower;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;
import frc.robot.subsystem.Shooter;
import edu.wpi.first.wpilibj.Timer;

import com.pathplanner.lib.PathPlanner;
import frc.robot.Robot;
import frc.robot.Subsystems;


public class ShootOneAndTaxi extends StateMachine {

    private Trajectory line;
    //private Shooter shooter = new Shooter();
    private Timer timer = new Timer();

    public ShootOneAndTaxi() {
        super("Stop");
        // TODO: Implement shooter follow-through and trajectory values
         NtHelper.setString("/robot/auto/name", "simpleAuto2");
    }

    @RunState(name = "Stop")
    public void stopState(){
        setState("shooter");
    }

    @InitState(name = "shooter")
    public void runShooterInit(){
        Robot.shooter.setAuto(false);
        timer.start();
    }

    @RunState(name = "shooter")
    public void runShooter(){
        Robot.shooter.shoot();
        Robot.shooter.run();
        if (timer.get() > 4){
            setState("taxi");
        }
    }

    @InitState(name = "taxi")
    public void initTaxi(){
        Robot.shooter.stop();
        Robot.shooter.run();
        Subsystems.follower.setTrajectory("line");
        Subsystems.follower.startFollowing();
        timer.stop();
    }

    @RunState(name = "taxi")
    public void runTaxi(){
        Subsystems.follower.follow();
    }
}
