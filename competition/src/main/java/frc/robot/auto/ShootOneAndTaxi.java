package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.Devices;
import frc.robot.Subsystems;

public class ShootOneAndTaxi extends StateMachine {

    public ShootOneAndTaxi() {
        super("shooter");
    }

    @State(name = "shooter")
    public void Shooter(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.shooter.setAuto(false);
        }
        Subsystems.shooter.shoot();
        if (ctx.getTime() > 4) {
            setState("taxi");
        }
    }

    @State(name = "taxi")
    public void Taxi(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.shooter.stop();
            Subsystems.follower.setTrajectory("Taxi");
            Subsystems.follower.startFollowing();
        }
        if (!Subsystems.follower.isDone()) {
            Subsystems.follower.follow();
        } else {
            setState("done");
        }
    }

    @State(name = "done")
    public void done(StateContext ctx) {
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0);
    }
}
