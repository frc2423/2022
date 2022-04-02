package frc.robot.auto;

import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.NtHelper;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.NtKeys;
import frc.robot.util.DriveHelper;
import frc.robot.util.Rotation;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;

/* Move towards cargo in straight line
 * Intake cargo
 * Aim and shoot both cargos at upper hub (testing necessary)
 * Move backwards to taxi
 */

public class ShootTwoTaxi extends StateMachine {
    // TODO: Values subject to change upon completed trajcetory integration
    private double angle;
    private Rotation rotate = new Rotation(.15, .3, 5, 150);;

    public ShootTwoTaxi() {
        super("IntakeDown");
        NtHelper.setString(NtKeys.AUTO_MODE_ROBOT_POSITION, "middle");
    }

    @State(name = "IntakeDown")
    public void intakeDown(StateContext ctx) {
        Subsystems.intake.goDown();
        if (ctx.getTime() > .5) {
            setState("CargoAdvance2");
        }
    }

    @State(name = "CargoAdvance2")
    public void CargoAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            String position = NtHelper.getString(NtKeys.AUTO_MODE_ROBOT_POSITION, "bottom");
            if (position.equals("top")) {
                Subsystems.follower.setTrajectory("TopTarmacToTopCargo");
            } else if (position.equals("middle")) {
                Subsystems.follower.setTrajectory("MiddleTarmacToMiddleCargo");
            } else { // bottom
                Subsystems.follower.setTrajectory("BottomTarmacToBottomCargo");
            }
            Subsystems.follower.startFollowing();
        }
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("Intake");
        }
    }

    @State(name = "Intake")
    public void Intake(StateContext ctx) {
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0);
        if (ctx.getTime() > 1.5) {
            setState("Rotate");
        }
    }

    private double getAngleErrorRadians(double errorDegrees) {
        return Units.radiansToDegrees(MathUtil.angleModulus(Units.degreesToRadians(errorDegrees)));
    }

    @State(name = "Rotate")
    public void rotate(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.intake.goUp();
            angle = Devices.gyro.getAngle() + 180;
        }
        double angleError = getAngleErrorRadians(angle - Devices.gyro.getAngle());
        double rotationSpeed = rotate.calculate(angleError);
        double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
        double leftSpeed = arcadeSpeeds[0];
        double rightSpeed = arcadeSpeeds[1];
        Devices.leftMotor.setPercent(leftSpeed);
        Devices.rightMotor.setPercent(rightSpeed);
        if (rotate.isDone(angleError)) {
            setState("ShooterAdvance");
        }
    }

    @State(name = "ShooterAdvance")
    public void ShooterAdvance(StateContext ctx) {
        if (ctx.isInit()) {
            String position = NtHelper.getString(NtKeys.AUTO_MODE_ROBOT_POSITION, "bottom");
            if (position.equals("top")) {
                Subsystems.follower.setTrajectory("TopCargoToHub", false);
            } else if (position.equals("middle")) {
                Subsystems.follower.setTrajectory("MiddleCargoToHub", false);
            } else { // bottom
                Subsystems.follower.setTrajectory("BottomCargoToHub", false);
            }
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
            Subsystems.shooter.stop();
        }
    }

    @State(name = "TaxiBack")
    public void TaxiBack(StateContext ctx) {
        if (ctx.isInit()) {
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
