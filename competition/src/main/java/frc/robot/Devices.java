package frc.robot;

import frc.robot.devices.NeoMotor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.math.util.Units;
import frc.robot.devices.*;

public class Devices {
    public static XboxController controller = new XboxController(0);
    public static NeoMotor leftMotor = new NeoMotor(1); // front left
    private static NeoMotor leftFollowerMotor = new NeoMotor (2); // back left
    public static NeoMotor rightMotor = new NeoMotor(3); // front right
    private static NeoMotor rightFollowerMotor = new NeoMotor (4); // back right
    public static NeoMotor intakeArmMotor = new NeoMotor (5);
    public static NeoMotor intakeArmFollowerMotor = new NeoMotor (6);
    public static NeoMotor intakeRollerMotor = new NeoMotor (7);
    // i don't understand this :( yelling out commands and collecting information each time around the loop, or so they say. 
    public static Gyro gyro = new Gyro();

    public static NeoMotor beltMotor = new NeoMotor(8);
    public static NeoMotor kickerMotor = new NeoMotor(10);
    public static NeoMotor shooterMotor = new NeoMotor(9);

    public static DigitalInput leftLimit = new DigitalInput(1);
    public static DigitalInput rightLimit = new DigitalInput(2);

    static void init() {
        rightMotor.setInverted(true);
        rightFollowerMotor.setInverted(true);
        rightFollowerMotor.follow(rightMotor);
        leftFollowerMotor.follow(leftMotor);
        //this is because the motors are put in the opposite way, so the wheels move in the opposite direction of the motor. 
        rightMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);
        leftMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);

        intakeArmMotor.setPid(.02, 0.0001, 0);
        intakeArmFollowerMotor.setPid(.02, 0.0001, 0);

        intakeArmMotor.setIZone(2);
        intakeArmFollowerMotor.setIZone(2);

        intakeArmFollowerMotor.setInverted(true);
        

        // shooterMotor.setPid(0.003, 0, 0);
        //intakeArmFollowerMotor.follow(intakeArmMotor);
        shooterMotor.setPidf(0, 0, 0, .000);
    }
}
