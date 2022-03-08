package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.NtHelper;
import frc.robot.util.TrajectoryFollower;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.wpilibj.Timer;

public class ThreeMuskets5 extends StateMachine{
    private Shooter shooter = new Shooter();
    private Intake intake = new Intake(shooter);
    Trajectory CargoAdvance1 = PathPlanner.loadPath("CargoAdvance15", constants.maxSpeedo, constants.maxAccel);
    Trajectory CargoAdvance2 = PathPlanner.loadPath("ShooterAdvance15", constants.maxSpeedo, constants.maxAccel);
    Trajectory ShooterAdvance1 = PathPlanner.loadPath("CargoAdvance25", constants.maxSpeedo, constants.maxAccel);
    Trajectory ShooterAdvance2 = PathPlanner.loadPath("ShooterAdvance25", constants.maxSpeedo, constants.maxAccel);
    Trajectory Taxi = PathPlanner.loadPath("Taxi5", constants.maxSpeedo, constants.maxAccel);

    private TrajectoryFollower follower = new TrajectoryFollower();
    private Timer timer = new Timer();
    
    public ThreeMuskets5(){
        super("CargoAdvance");
        follower.addTrajectory("CargoAdvance1", CargoAdvance1);
        follower.addTrajectory("ShooterAdvance1", ShooterAdvance1);
        follower.addTrajectory("CargoAdvance2", CargoAdvance2);
        follower.addTrajectory("ShooterAdvance2", ShooterAdvance2);
        follower.addTrajectory("Taxi", Taxi);
        follower.setTrajectory("CargoAdvance1");
    }

    @InitState(name = "CargoAdvance")
    public void CargoAdvanceInit(){
        follower.setTrajectory("CargoAdvance");
    }

    @RunState(name = "CargoAdvance")
    public void CargoAdvanceRun(){
        follower.follow();
        if (follower.isDone()) {
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
        follower.setTrajectory("FirstShooterAdvance");
    }

    @RunState(name = "FirstShooterAdvance")
    public void FirstShooterAdvanceRun(){
        follower.follow();
        if (follower.isDone()) {
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
        follower.setTrajectory("SecondCargoAdvance");
    }

    @RunState(name = "SecondCargoAdvance")
    public void SecondCargoAdvanceRun(){
        follower.follow();
        if (follower.isDone()) {
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
        follower.setTrajectory("SecondShooterAdvance");
    }

    @RunState(name = "SecondShooterAdvance")
    public void SecondShooterAdvanceRun(){
        follower.follow();
        if (follower.isDone()) {
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
        follower.setTrajectory("Taxi");
        timer.stop();
    }

    @RunState(name = "Taxi")
    public void TaxiRun(){
        follower.follow();
    }


}
