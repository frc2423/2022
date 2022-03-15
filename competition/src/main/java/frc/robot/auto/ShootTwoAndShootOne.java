package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.NtHelper;
import frc.robot.util.TrajectoryFollower;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.Subsystems;
import frc.robot.constants.constants;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.wpilibj.Timer;

public class ShootTwoAndShootOne extends StateMachine{
    private Intake intake = new Intake();
    Trajectory Taxi = PathPlanner.loadPath("Taxi5", constants.maxSpeedo, constants.maxAccel);

    private Timer timer = new Timer();
    
    public ShootTwoAndShootOne(){
        super("CargoAdvance");
        Subsystems.follower.addTrajectory("Taxi", Taxi);
    }

    @InitState(name = "CargoAdvance")
    public void CargoAdvanceInit(){
        Subsystems.follower.setTrajectory("CargoAdvance1");
        // Subsystems.follower.setTrajectory("CargoAdvance");
    }

    @RunState(name = "CargoAdvance")
    public void CargoAdvanceRun(){
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("FirstIntake");
        }
    }

    @InitState(name = "FirstIntake")
    public void FirstIntakeInit(){
        intake.intakeDown();
        timer.reset ();
        timer.start ();
    }

    @RunState(name = "FirstIntake")
    public void FirstIntakeRun(){
        if (timer.get() > 2){
            setState("FirstShooterAdvance");
        }
    }

    @InitState(name = "FirstShooterAdvance")
    public void FirstShooterAdvanceInit(){
        Subsystems.follower.setTrajectory("FirstShooterAdvance");
    }

    @RunState(name = "FirstShooterAdvance")
    public void FirstShooterAdvanceRun(){
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("ShootTwo");
        }
    }

    @InitState(name = "ShootTwo")
    public void ShootTwoInit(){
        timer.reset();
        timer.start();
    }

    @RunState(name = "ShootTwo")
    public void ShootTwoRun(){
        //shooter.shootTwo;
        if (timer.get() > 4){
            setState ("SecondCargoAdvance");
        }
    }

    @InitState(name = "SecondCargoAdvance")
    public void SecondCargoAdvanceInit(){
        Subsystems.follower.setTrajectory("SecondCargoAdvance");
    }

    @RunState(name = "SecondCargoAdvance")
    public void SecondCargoAdvanceRun(){
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("SecondIntake");
        }
    }

    @InitState(name = "SecondIntake")
    public void SecondIntakeInit(){
        intake.intakeDown();
        timer.reset ();
        timer.start ();
    }

    @RunState(name = "SecondIntake")
    public void SecondIntakeRun(){
        if (timer.get() > 2){
            setState("SecondShooterAdvance");
        }
    }

    @InitState(name = "SecondShooterAdvance")
    public void SecondShooterAdvanceInit(){
        Subsystems.follower.setTrajectory("SecondShooterAdvance");
    }

    @RunState(name = "SecondShooterAdvance")
    public void SecondShooterAdvanceRun(){
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("ShootOne");
        }
    }

    @InitState(name = "ShootOne")
    public void ShootOneInit(){
        timer.reset();
        timer.start();
    }

    @RunState(name = "ShootOne")
    public void ShootOneRun(){
        //shooter.shootTwo;
        if (timer.get() > 4){
            setState ("Taxi");
        }
    }

    @InitState(name = "Taxi")
    public void TaxiInit(){
        Subsystems.follower.setTrajectory("Taxi");
        timer.stop();
    }

    @RunState(name = "Taxi")
    public void TaxiRun(){
        Subsystems.follower.follow();
    }


}
