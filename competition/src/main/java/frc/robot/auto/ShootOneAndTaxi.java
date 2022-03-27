package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.NtHelper;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Devices;
import frc.robot.Subsystems;

public class ShootOneAndTaxi extends StateMachine {

    private Timer timer = new Timer();

    public ShootOneAndTaxi() {
        super("Stop");
        // TODO: Implement shooter follow-through and trajectory values
    }

    @RunState(name = "Stop")
    public void stopState(){
        setState("shooter");
    }

    @InitState(name = "shooter")
    public void runShooterInit(){
        Subsystems.shooter.setAuto(false);
        timer.start();
    }

    @RunState(name = "shooter")
    public void runShooter(){
        Subsystems.shooter.shoot();
        if (timer.get() > 4){
            setState("taxi");
        }
    }

    @InitState(name = "taxi")
    public void initTaxi(){
        Subsystems.shooter.stop();
        Subsystems.follower.setTrajectory("Taxi");
        Subsystems.follower.startFollowing();
        timer.stop();
    }

    @RunState(name = "taxi")
    public void runTaxi(){
        if (!Subsystems.follower.isDone()) {
            Subsystems.follower.follow();
        } else {
            setState("done");
        }
    }

    @InitState(name = "done")
    public void done(){
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0);
    }
}
