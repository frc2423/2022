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


public class taxi extends StateMachine {
    //Values subject to change upon completed trajectory integration
    Trajectory cabTrajectory = PathPlanner.loadPath("TaxiTaxi", constants.maxSpeedo, constants.maxAccel);
    TrajectoryFollower follower = new TrajectoryFollower();

    private Timer timer = new Timer();
    private double timerDelay = 0;

    public taxi(){
        super("Wait");
        follower.addTrajectory("CabRoute", cabTrajectory);
        follower.setTrajectory("CabRoute");
        NtHelper.setString("/robot/auto/name", "taxi1");
    }

    @InitState(name = "Wait")
    public void waitInit(){
        timer.start();
        timerDelay = NtHelper.getDouble("/robot/auto/delay", 0);
    }

    @RunState(name = "Wait")
    public void waitRun(){
        if (timer.get() > timerDelay){
            setState("Taxicab");
        }
    }

    @InitState(name = "Taxicab")
    public void taxicabinit(){
        follower.startFollowing();
        timer.stop();
    }

    @RunState(name = "Taxicab")
    public void taxicabrun(){
        follower.follow();
    }
}