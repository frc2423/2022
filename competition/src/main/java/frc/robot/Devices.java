package frc.robot;

import frc.robot.devices.NeoMotor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.math.util.Units;
import frc.robot.devices.*;
import edu.wpi.first.wpilibj.Relay;

public class Devices {
    public static XboxController controller = new XboxController(0);
    public static XboxController climbController = new XboxController(1);
    public static Gyro gyro = new Gyro();
    // Drivetrain motors
    public static NeoMotor leftMotor = new NeoMotor(1); // front left
    private static NeoMotor leftFollowerMotor = new NeoMotor(2); // back left
    public static NeoMotor rightMotor = new NeoMotor(3); // front right
    private static NeoMotor rightFollowerMotor = new NeoMotor(4); // back right
    // Intake motors
    public static IMotor intakeArmMotor;
    public static IMotor intakeArmFollowerMotor;
    public static IMotor intakeRollerMotor;
    // Intake limit switches
    public static DigitalInput leftLimit = new DigitalInput(1);
    public static DigitalInput rightLimit = new DigitalInput(2);
    // Belt and shooter motors
    public static IMotor beltMotor;
    public static IMotor kickerMotor;
    public static IMotor shooterMotor;
    // Climber motors
    public static NeoMotor climberLeftMotor = new NeoMotor(12);
    public static NeoMotor climberRightMotor = new NeoMotor(11);

    public static DigitalInput leftLimitSwitchClimber = new DigitalInput(3); // needs actual ports
    public static DigitalInput rightLimitSwitchClimber = new DigitalInput(4);

    private static final boolean IS_DEFENSE_MODE = false;

    static void init() {
        rightMotor.setInverted(true);
        rightFollowerMotor.setInverted(true);
        rightMotor.setFollower(rightFollowerMotor);
        leftMotor.setFollower(leftFollowerMotor);

        // this is because the motors are put in the opposite way, so the wheels move in
        // the opposite direction of the motor.
        rightMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);
        leftMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);

        if (IS_DEFENSE_MODE) {
            intakeArmMotor = new DisabledMotor(5);
            intakeArmFollowerMotor = new DisabledMotor(6);
            intakeRollerMotor = new DisabledMotor(7);

            beltMotor = new DisabledMotor(8);
            kickerMotor = new DisabledMotor(10);
            shooterMotor = new DisabledMotor(9);
        } else {
            intakeArmMotor = new NeoMotor(5);
            intakeArmFollowerMotor = new NeoMotor(6);
            intakeRollerMotor = new NeoMotor(7);

            beltMotor = new NeoMotor(8);
            kickerMotor = new NeoMotor(10);
            shooterMotor = new NeoMotor(9);
        }

        intakeArmMotor.setPid(.035, 0.00025, 0);
        intakeArmFollowerMotor.setPid(.035, 0.00025, 0);
        intakeArmMotor.setIZone(2);
        intakeArmFollowerMotor.setIZone(2);
        intakeArmFollowerMotor.setInverted(true);

        climberLeftMotor.setPid(.03, 0.0000, 0);
        climberRightMotor.setPid(.03, 0.0000, 0);
    }
}
