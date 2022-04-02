package frc.robot.auto;

import frc.robot.util.stateMachine.StateContext;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.Subsystems;

public class Taxi extends StateMachine {
    
    private double timerDelay = 5;

    public Taxi(){
        super("Wait");
    }

    @State(name = "Wait")
    public void waitRun(StateContext ctx){
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("Taxi");   
        }

        if (ctx.getTime() > timerDelay){
            setState("Taxi");
        }
    }

    @State(name = "Taxi")
    public void taxiRun(StateContext ctx){
        if (ctx.isInit()) {
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
    }
}
