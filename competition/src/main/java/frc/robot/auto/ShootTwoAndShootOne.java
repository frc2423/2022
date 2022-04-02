package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.Subsystems;

//CAUTION: THIS DOESN'T WORK DO NOT USE

public class ShootTwoAndShootOne extends StateMachine {

    public ShootTwoAndShootOne() {
        super("CargoAdvance");
    }

    @State(name = "CargoAdvance")
    public void CargoAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("CargoAdvance1");
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("FirstIntake");
        }
    }

    @State(name = "FirstIntake")
    public void FirstIntake(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.intake.goDown();
        }
        if (ctx.getTime() > 2) {
            setState("FirstShooterAdvance");
        }
    }

    @State(name = "FirstShooterAdvance")
    public void FirstShooterAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("FirstShooterAdvance");
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("ShootTwo");
        }
    }

    @State(name = "ShootTwo")
    public void ShootTwo(StateContext ctx) {
        // shooter.shootTwo;
        if (ctx.getTime() > 4) {
            setState("SecondCargoAdvance");
        }
    }

    @State(name = "SecondCargoAdvance")
    public void SecondCargoAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("SecondCargoAdvance");
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("SecondIntake");
        }
    }

    @State(name = "SecondIntake")
    public void SecondIntake(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.intake.goDown();
        }
        if (ctx.getTime() > 2) {
            setState("SecondShooterAdvance");
        }
    }

    @State(name = "SecondShooterAdvance")
    public void SecondShooterAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("SecondShooterAdvance");
        }
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("ShootOne");
        }
    }

    @State(name = "ShootOne")
    public void ShootOne(StateContext ctx) {
        // shooter.shootTwo;
        if (ctx.getTime() > 4) {
            setState("Taxi");
        }
    }

    @State(name = "Taxi")
    public void Taxi(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("Taxi");
        }
        Subsystems.follower.follow();
    }

}
