package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.NtHelper;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.Subsystems;
import frc.robot.constants.constants;
import edu.wpi.first.wpilibj.Timer;

import com.pathplanner.lib.PathPlanner;
public class ShootOneAndShootTwo extends StateMachine{
    Trajectory IntakeAimTrajectory = PathPlanner.loadPath("IntakeAim4", constants.maxSpeedo, constants.maxAccel);
    Trajectory TaxiTrajectory = PathPlanner.loadPath("Taxi4", constants.maxSpeedo, constants.maxAccel);

    private Timer timer = new Timer();

    public ShootOneAndShootTwo() {
        super("FirstShot");
        NtHelper.setString("/robot/auto/name", "threeMuskets4");
        Subsystems.follower.addTrajectory("IntakeAim4", IntakeAimTrajectory);
        Subsystems.follower.addTrajectory("Taxi4", TaxiTrajectory);
    }

    @InitState(name = "FirstShot")
    public void firstShotInit(){
        Subsystems.follower.setTrajectory("IntakeAim4");
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
        Subsystems.follower.resetPosition();
        Subsystems.intake.intakeDown();
    }

    @RunState(name = "CargoAdvance")
    public void cargoAdvanceRun(){
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("ShootTwo");
        }
    }

    @InitState(name = "ShootTwo")
    public void shootTwoInit(){
        timer.reset();
        timer.start();
        Subsystems.intake.intakeUp();
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
        Subsystems.follower.setTrajectory("Taxi4");
        Subsystems.follower.resetPosition();
        timer.stop();
    }

    @RunState (name = "TaxiBack")
    public void taxiBackRun (){
        Subsystems.follower.follow ();
    }
}
