package frc.robot.auto;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.Subsystems;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Devices;

public class Taxi extends StateMachine {
    
    private Timer timer = new Timer();
    private double timerDelay = 5;

    public Taxi(){
        super("Stop");
    }

    @RunState(name = "Stop")
    public void stop(){
        timer.reset();
        Subsystems.shooter.shoot();
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0);
        // setState("Wait");
    }

    @InitState(name = "Wait")
    public void waitInit(){
        timer.start();
        Subsystems.follower.setTrajectory("Taxi");   
    }

    @RunState(name = "Wait")
    public void waitRun(){
        if (timer.get() > timerDelay){
            setState("Taxi");
        }
    }

    @InitState(name = "Taxi")
    public void taxiInit(){
        // Subsystems.follower.startFollowing();
        Devices.leftMotor.setPercent(-.3);
        Devices.rightMotor.setPercent(-.3);
        timer.stop();
    }

    @RunState(name = "Taxi")
    public void taxiRun(){
        Devices.leftMotor.setPercent(-.3);
        Devices.rightMotor.setPercent(-.3);
        Subsystems.follower.follow();
    }
}
