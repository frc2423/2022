package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.DriveHelper;
import frc.robot.util.NtHelper;
import frc.robot.util.Rotation;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.constants;

public class ShootOneAndShootTwo extends StateMachine {
    private double angle;
    private Rotation rotate = new Rotation(.15, .3, 5, 150);;

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
            setState("rotate");
        }
        // Seconds subject to change upon testing
    }

    private double getAngleErrorRadians(double errorDegrees) {
        return Units.radiansToDegrees(MathUtil.angleModulus(Units.degreesToRadians(errorDegrees)));
    }

    @State(name = "rotate")
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

        Subsystems.desiredWheelSpeeds = new DifferentialDriveWheelSpeeds(
                leftSpeed * Units.feetToMeters(constants.maxSpeedo),
                rightSpeed * Units.feetToMeters(constants.maxSpeedo));

        if (rotate.isDone(angleError)) {
            setState("CargoAdvance");
        }
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
