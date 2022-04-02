package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.Subsystems;

public class ShootOneAndShootTwo extends StateMachine {

    public ShootOneAndShootTwo() {
        super("FirstShot");
    }

    @State(name = "FirstShot")
    public void firstShot(StateContext ctx) {
        NtHelper.setString("/robot/auto/state", "FirstShot");
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("BottomTarmacToCargosToHub");
        }
        Subsystems.shooter.shoot();
        if (ctx.getTime() > 2.5) {
            Subsystems.shooter.stop();
            setState("CargoAdvance");
        }
        // Seconds subject to change upon testing
    }

    @State(name = "CargoAdvance")
    public void cargoAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.startFollowing();
            Subsystems.intake.goDown();
        }
        Subsystems.follower.follow();
        NtHelper.setString("/robot/auto/state", "CargoAdvance");
        if (Subsystems.follower.isDone()) {
            setState("ShootTwo");
        }
    }

    @State(name = "ShootTwo")
    public void shootTwo(StateContext ctx) {
        NtHelper.setString("/robot/auto/state", "ShootTwo");
        if (ctx.isInit()) {
            Subsystems.intake.goUp();
        }
        Subsystems.shooter.shoot();

        if (ctx.getTime() > 3) {
            Subsystems.shooter.stop();
            setState("TaxiBack");
        }
        // Seconds subject to change upon testing
    }

    @State(name = "TaxiBack")
    public void taxiBack(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("Taxi");
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
    }
}
