package frc.robot.auto;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.util.NtHelper;
import frc.robot.constants.constants;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.TrajectoryFollower;

import com.pathplanner.lib.PathPlanner;

import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

/* Move towards cargo in straight line
 * Intake cargo
 * Aim and shoot both cargos at upper hub (testing necessary)
 * Move backwards to taxi
 */ 

public class ShootTwoTaxi extends StateMachine{

    private Intake intake = new Intake();
    //TODO: Values subject to change upon completed trajcetory integration
    Trajectory CargoAdvanceTrajectory = PathPlanner.loadPath("CargoAdvance3", constants.maxSpeedo, constants.maxAccel);
    Trajectory ShooterAdvanceTrajectory = PathPlanner.loadPath("ShooterAdvance3", constants.maxSpeedo, constants.maxAccel);
    Trajectory TaxiBackTrajectory = PathPlanner.loadPath("Taxi3", constants.maxSpeedo, constants.maxAccel);
    private TrajectoryFollower follower = new TrajectoryFollower();
    private Timer timer = new Timer();

    // private Shooter shooter = new Shooter();

    public ShootTwoTaxi() {
        super("Stop");
        follower.addTrajectory("CargoAdvance", CargoAdvanceTrajectory);
        follower.addTrajectory("ShooterAdvance", ShooterAdvanceTrajectory);
        follower.addTrajectory("TaxiBack", TaxiBackTrajectory);
        NtHelper.setString("/robot/auto/name", "shootTwoTaxi3");
    }

    @RunState(name = "Stop")
    public void stopState(){
        setState("CargoAdvance");
    }

    @InitState(name = "CargoAdvance")
    public void CargoAdvanceInit (){
        follower.setTrajectory("CargoAdvance");
        follower.resetPosition();
    }

    @RunState(name = "CargoAdvance")
    public void CargoAdvanceRun(){
        follower.follow();
        if (follower.isDone()) {
            setState("Intake");
        }
    
    }

    @InitState(name = "Intake")
    public void IntakeInit (){
        intake.intakeDown();
        timer.reset ();
        timer.start ();
    }

    @RunState(name = "Intake")
    public void Intake (){
        if (timer.get() > 2){
            setState("ShooterAdvance");
        }
        //Seconds; subject to change

    }

    @InitState(name = "ShooterAdvance")
    public void ShooterAdvanceInit (){
        follower.setTrajectory("ShooterAdvance");
        follower.resetPosition();
    }

    @RunState(name = "ShooterAdvance")
    public void ShooterAdvance (){
        follower.follow ();
        if (follower.isDone()){
            setState ("ShootTwo");
        }

    }

    @InitState(name = "ShootTwo")
    public void ShootTwoInit (){
        timer.reset();
        timer.start();
    //Shooter initialization
    }

    @RunState(name = "ShootTwo")
    public void ShootTwo (){
        //shooter.shootTwo;
        if (timer.get() > 4){
            setState ("TaxiBack");
        }
        //Seconds subject to change upon testing
    
    }

    @InitState(name = "TaxiBack")
    public void TaxiBackInit (){
        follower.setTrajectory ("CargoAdvance");
        follower.resetPosition();
        timer.stop();
    }

    @RunState(name = "TaxiBack")
    public void TaxiBack (){
        follower.follow ();
    }


}
