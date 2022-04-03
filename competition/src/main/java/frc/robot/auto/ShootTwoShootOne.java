package frc.robot.auto;

import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.constants;
import frc.robot.util.DriveHelper;
import frc.robot.util.NtHelper;
import frc.robot.util.Rotation;

/* Move towards cargo in straight line
 * Intake cargo
 * Aim and shoot both cargos at upper hub (testing necessary)
 * Move backwards to taxi
 */

//CAUTION: THIS DOESN'T WORK DO NOT USE

public class ShootTwoShootOne extends StateMachine {
    // TODO: Values subject to change upon completed trajcetory integration
    private double angle;
    private Rotation rotate = new Rotation(.1, .3, angle - 5, angle + 5);
    private int rotations = 0;

    public ShootTwoShootOne() {
        super("CargoAdvance");
    }

    @State(name = "CargoAdvance")
    public void CargoAdvanceRun(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("BottomTarmacToBottomCargo");
            Subsystems.follower.startFollowing();
        }
        NtHelper.setString("/robot/auto/state", "cargoAdvance");

        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("Intake");
        }

    }

    @State(name = "Intake")
    public void Intake(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.intake.goDown();
        }
        NtHelper.setString("/robot/auto/state", "instake");

        if (ctx.getTime() > 2) {
            Subsystems.intake.goUp();
            setState("Rotate");
        }
    }

    @State(name = "Rotate")
    public void rotate(StateContext ctx) {
        if (ctx.isInit()) {
            angle = Devices.gyro.getAngle() + 180;
        }
        double rotationSpeed = rotate.calculate(Devices.gyro.getAngle());
        double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
        double leftSpeed = arcadeSpeeds[0];
        double rightSpeed = arcadeSpeeds[1];
        
        Subsystems.desiredWheelSpeeds = new DifferentialDriveWheelSpeeds(
                leftSpeed * Units.feetToMeters(constants.maxSpeedo),
                rightSpeed * Units.feetToMeters(constants.maxSpeedo));
                
        if (rotate.isDone(Devices.gyro.getAngle())) {
            if (rotations == 0) {
                setState("ShooterAdvance");
            } else if (rotations == 1) {
                setState("SecondAdvanceCargo");
            }
        }
    }

    @State(name = "ShooterAdvance")
    public void ShooterAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("BottomCargoToHub");
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("ShootTwo");
        }

    }

    @State(name = "ShootTwo")
    public void ShootTwo(StateContext ctx) {
        Subsystems.shooter.shoot();
        if (ctx.getTime() > 4) {
            setState("TaxiBack");
        }
    }

    @State(name = "TaxiBack")
    public void TaxiBack(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("BottomHubBackUp");
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("Rotate");
        }
    }

    @State(name = "SecondCargoAdvance")
    public void secondCargoAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("BottomtarmacToMiddleCargo");
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("SecondShooterAdvance");
        }
    }

    @State(name = "SecondShooterAdvance")
    public void secondShooterAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.follower.setTrajectory("MiddleCargoToBottomHub");
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("ShootOne");
        }
    }

    @State(name = "ShootOne")
    public void ShootOne(StateContext ctx) {
        Subsystems.shooter.shoot();
        if (ctx.getTime() > 4) {
            Subsystems.shooter.stop();

        }
        // Seconds subject to change upon testing
    }
}
