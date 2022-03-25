package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.Subsystems;

import edu.wpi.first.wpilibj.Timer;

public class ShootOneAndShootTwoSeparate extends StateMachine{
    private Timer timer = new Timer();

    public ShootOneAndShootTwoSeparate() {
        super("FirstShot");
      }

    @InitState(name = "FirstShot")
    public void firstShotInit(){
        timer.reset();
        timer.start();
        //TODO: Implement shooter code following general shooter implementation
    }

    @RunState(name = "FirstShot")
    public void firstShotRun(){
        Subsystems.shooter.shoot();
        if (timer.get() > 4){
            timer.stop();
            Subsystems.shooter.stop();
            setState ("CargoAdvance");
        }
        //Seconds subject to change upon testing
    }

    @InitState(name = "CargoAdvance")
    public void cargoAdvanceInit(){
        Subsystems.follower.setTrajectory("BottomTarmacToBottomCargo");
        Subsystems.follower.startFollowing();
    }

    @RunState(name = "CargoAdvance")
    public void cargoAdvanceRun(){
        Subsystems.intake.goDown();
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("MiddleCargoAdvance");
        }
    }

    @InitState(name = "MiddleCargoAdvance")
    public void middleCargoAdvanceInit(){
        Subsystems.follower.setTrajectory("BottomCargoToMiddleCargo");
        Subsystems.follower.startFollowing();
    }

    @RunState(name = "MiddleCargoAdvance")
    public void middleCargoAdvanceRun(){
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("HubAdvance");
        }
    }

    @InitState(name = "HubAdvance")
    public void hubAdvanceInit(){
        Subsystems.follower.setTrajectory("MiddleCargoToHubThreeBall");
        Subsystems.follower.startFollowing();
    }

    @RunState(name = "HubAdvance")
    public void hubAdvanceRun(){
        Subsystems.intake.goUp();
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
        Subsystems.shooter.shoot();
        if (timer.get() > 4){
            timer.stop();
            Subsystems.shooter.stop();
            setState ("TaxiBack");
        }
        //Seconds subject to change upon testing
    }

    @InitState (name = "TaxiBack")
    public void taxiBackInit(){
        Subsystems.follower.setTrajectory("Taxi");
        Subsystems.follower.startFollowing();
        timer.stop();
    }

    @RunState (name = "TaxiBack")
    public void taxiBackRun (){
        Subsystems.follower.follow();
    }
}
