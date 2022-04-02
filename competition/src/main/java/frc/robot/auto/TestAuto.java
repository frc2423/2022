package frc.robot.auto;

import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.Subsystems;
import frc.robot.Devices;

public class TestAuto extends StateMachine {

    public TestAuto() {
        super("Start");
    }

    @State(name = "Start")
    public void start(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("TestCurvyPath");
            Subsystems.follower.startFollowing();
        }
        if (!Subsystems.follower.isDone()) {
            Subsystems.follower.follow();
        } else {
            setState("Stop");
        }
    }

    @State(name = "Stop")
    public void stop() {
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0);
    }
}
