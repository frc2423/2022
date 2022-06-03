package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.Subsystems;
import frc.robot.Devices;

public class ShootOneAndTaxi extends StateMachine {

    public ShootOneAndTaxi() {
        super("shooter");
    }

    @State(name = "shooter")
    public void Shooter(StateContext ctx) {
        Subsystems.shooter.skRev(3.47);
        Subsystems.shooter.setHoodAngle(3.47);

        if (ctx.getTime() > 2) {
            Devices.beltMotor.setPercent(-0.2);
        }

        if (ctx.getTime() > 4) {
            Devices.beltMotor.setPercent(0);
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
        Subsystems.drive.setSpeeds(0, 0);
    }
}
