package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.Subsystems;

import edu.wpi.first.wpilibj.Timer;

public class ShootOneAndShootTwo extends StateMachine{
    private Timer timer = new Timer();

    public ShootOneAndShootTwo() {
        super("FirstShot");
      }

    @InitState(name = "FirstShot")
    public void firstShotInit(){
        Subsystems.follower.setTrajectory("BottomTarmacToCargosToHub");
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
        Subsystems.follower.startFollowing();
        Subsystems.intake.goDown();
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
        Subsystems.intake.goUp();
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
        Subsystems.follower.startFollowing();
        timer.stop();
    }

    @RunState (name = "TaxiBack")
    public void taxiBackRun (){
        Subsystems.follower.follow ();
    }
}
