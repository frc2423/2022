package frc.robot.auto;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.Subsystems;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Devices;

public class TestAuto extends StateMachine {
    
    private Timer timer = new Timer();

    public TestAuto(){
        super("Start");
    }

    @InitState(name = "Start")
    public void initStart(){
        timer.reset();
        Subsystems.follower.setTrajectory("TestCurvyPath");   
        Subsystems.follower.startFollowing();
    }

    @RunState(name = "Start")
    public void runStart(){
        if (!Subsystems.follower.isDone()) {
            Subsystems.follower.follow();
        } else {
            setState("Stop");
        }
    }

    @RunState(name = "Stop")
    public void runStop(){
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0); 
    }
}
