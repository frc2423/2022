package frc.robot.auto;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.wpilibj.Timer;
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
    private Timer timer = new Timer();
    private double angle;
    private Rotation rotate;

    public ShootTwoTaxi() {
        super("Stop");
        Subsystems.follower.setTrajectory("BottomTarmacToBottomCargo");
        NtHelper.setString(NtKeys.AUTO_MODE_ROBOT_POSITION, "bottom");
    }

    public void haltMovement() {
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0);
    }

    @RunState(name = "Stop")
    public void stopState() {
        setState("IntakeDown");
    }

    @InitState(name = "IntakeDown")
    public void initIntakeDown() {
        timer.reset();
        timer.start();
        Subsystems.intake.goDown();
    }

    @RunState(name = "IntakeDown")
    public void runIntakeDown() {
        if (timer.get() > .5) {
            setState("CargoAdvance");
        }
    }

    @InitState(name = "CargoAdvance")
    public void CargoAdvanceInit() {
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

    @RunState(name = "CargoAdvance")
    public void CargoAdvanceRun() {
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("Intake");
        }

    }

    @InitState(name = "Intake")
    public void IntakeInit() {
        haltMovement();
        timer.reset();
        timer.start();
    }

    @RunState(name = "Intake")
    public void Intake() {
        if (timer.get() > 1.5) {
            setState("Rotate");
        }
        // Seconds; subject to change

    }

    @InitState(name = "Rotate")
    public void rotateInit() {
        Subsystems.intake.goUp();
        angle = Devices.gyro.getAngle() + 180;
        rotate = new Rotation(.15, .3, 5, 150);
    }

    private double getAngleErrorRadians(double errorDegrees) {
        return Units.radiansToDegrees(MathUtil.angleModulus(Units.degreesToRadians(errorDegrees)));
    }

    @RunState(name = "Rotate")
    public void rotate() {
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

    @InitState(name = "ShooterAdvance")
    public void ShooterAdvanceInit() {
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

    @RunState(name = "ShooterAdvance")
    public void ShooterAdvance() {
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("ShootTwo");
        }

    }

    @InitState(name = "ShootTwo")
    public void ShootTwoInit() {
        timer.reset();
        timer.start();
    }

    @RunState(name = "ShootTwo")
    public void ShootTwo() {
        Subsystems.shooter.shoot();
        if (timer.get() > 4) {
            setState("TaxiBack");
            Subsystems.shooter.stop();
        }
        // Seconds subject to change upon testing

    }

    @InitState(name = "TaxiBack")
    public void TaxiBackInit() {
        Subsystems.follower.setTrajectory("Taxi");
        Subsystems.follower.startFollowing();
        timer.stop();
    }

    @RunState(name = "TaxiBack")
    public void TaxiBack() {
        if (!Subsystems.follower.isDone()) {
            Subsystems.follower.follow();
        } else {
            setState("done");
        }
    }

    @InitState(name = "done")
    public void done(){
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0);
    }

}
