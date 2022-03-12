package frc.robot.auto;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.TrajectoryFollower;
import com.pathplanner.lib.PathPlanner;


public class taxi extends StateMachine {
    // Values subject to change upon completed trajectory integration
    Trajectory trajectory = PathPlanner.loadPath("Taxi", constants.maxSpeedo, constants.maxAccel, true);
    TrajectoryFollower follower = new TrajectoryFollower();


    private Timer timer = new Timer();
    private double timerDelay = 5;

    public taxi(){
        super("Stop");
        follower.addTrajectory("Taxi", trajectory);
    }

    @RunState(name = "Stop")
    public void stop(){
        timer.reset();
        setState("Wait");
    }

    @InitState(name = "Wait")
    public void waitInit(){
        timer.start();
        follower.setTrajectory("Taxi");
    }

    @RunState(name = "Wait")
    public void waitRun(){
        if (timer.get() > timerDelay){
            setState("Taxi");
        }
    }

    @InitState(name = "Taxi")
    public void taxiInit(){
        follower.startFollowing();
        timer.stop();
    }

    @RunState(name = "Taxi")
    public void taxiRun(){
        follower.follow();
    }
}
