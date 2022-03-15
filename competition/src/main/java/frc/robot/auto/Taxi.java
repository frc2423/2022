package frc.robot.auto;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.Subsystems;
import frc.robot.constants.constants;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.TrajectoryFollower;
import com.pathplanner.lib.PathPlanner;


public class Taxi extends StateMachine {
    // Values subject to change upon completed trajectory integration


    private Timer timer = new Timer();
    private double timerDelay = 5;

    public Taxi(){
        super("Stop");
    }

    @RunState(name = "Stop")
    public void stop(){
        timer.reset();
        setState("Wait");
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
        Subsystems.follower.startFollowing();
        timer.stop();
    }

    @RunState(name = "Taxi")
    public void taxiRun(){
        Subsystems.follower.follow();
    }
}
