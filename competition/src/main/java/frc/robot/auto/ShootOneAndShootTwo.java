package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.Subsystems;

public class ShootOneAndShootTwo extends StateMachine {

    public ShootOneAndShootTwo() {
        super("FirstShot");
    }

    @State(name = "FirstShot")
    public void firstShot(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("BottomTarmacToCargosToHub");
        }
        // shooter.shootOne();
        if (ctx.getTime() > 4) {
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
        if (Subsystems.follower.isDone()) {
            setState("ShootTwo");
        }
    }

    @State(name = "ShootTwo")
    public void shootTwo(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.intake.goUp();
        }
        // shooter.shootTwo();
        if (ctx.getTime() > 4) {
            setState("TaxiBack");
        }
        // Seconds subject to change upon testing
    }

    @State(name = "TaxiBack")
    public void taxiBack(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("Taxi4");
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
    }
}
