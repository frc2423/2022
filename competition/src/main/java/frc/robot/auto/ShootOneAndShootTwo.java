package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.NtHelper;
import frc.robot.util.TrajectoryFollower;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;
import frc.robot.subsystem.Intake;

import edu.wpi.first.wpilibj.Timer;

import java.util.List;

import com.pathplanner.lib.PathPlanner;
public class ShootOneAndShootTwo extends StateMachine{
    //private Shooter shooter = new Shooter();

    private Intake intake = new Intake();
    Trajectory IntakeAimTrajectory = PathPlanner.loadPath("IntakeAim4", constants.maxSpeedo, constants.maxAccel);
    Trajectory TaxiTrajectory = PathPlanner.loadPath("Taxi4", constants.maxSpeedo, constants.maxAccel);

    private TrajectoryFollower follower = new TrajectoryFollower();
    private Timer timer = new Timer();

    public ShootOneAndShootTwo() {
        super("FirstShot");
        NtHelper.setString("/robot/auto/name", "threeMuskets4");
        follower.addTrajectory("IntakeAim4", IntakeAimTrajectory);
        follower.addTrajectory("Taxi4", TaxiTrajectory);
    }

    @InitState(name = "FirstShot")
    public void firstShotInit(){
        follower.setTrajectory("IntakeAim4");
        timer.reset();
        timer.start();
        //TODO: Implement shooter code following general shooter implementation
    }

    @RunState(name = "FirstShot")
    public void firstShotRun(){
        //shooter.shootOne();
        if (timer.get() > 4){
            timer.stop();
            setState ("CargoAdvance");
        }
        //Seconds subject to change upon testing
    }

    @InitState(name = "CargoAdvance")
    public void cargoAdvanceInit(){
        follower.resetPosition();
        intake.intakeDown();
    }

    @RunState(name = "CargoAdvance")
    public void cargoAdvanceRun(){
        follower.follow();
        if (follower.isDone()) {
            setState("ShootTwo");
        }
    }

    @InitState(name = "ShootTwo")
    public void shootTwoInit(){
        timer.reset();
        timer.start();
        intake.intakeUp();
    }

    @RunState(name = "ShootTwo")
    public void shootTwoRun(){
        //shooter.shootTwo();
        if (timer.get() > 4){
            timer.stop();
            setState ("TaxiBack");
        }
        //Seconds subject to change upon testing
    }

    @InitState (name = "TaxiBack")
    public void taxiBackInit(){
        follower.setTrajectory("Taxi4");
        follower.resetPosition();
        timer.stop();
    }

    @RunState (name = "TaxiBack")
    public void taxiBackRun (){
        follower.follow ();
    }
}
