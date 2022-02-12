package frc.robot;

import frc.robot.devices.NeoMotor;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.util.Units;
import frc.robot.devices.*;

public class Devices {
    static XboxController controller = new XboxController(0);
    public static NeoMotor leftMotor = new NeoMotor(1); // front left
    private static NeoMotor leftFollowerMotor = new NeoMotor (2); // back left
    public static NeoMotor rightMotor = new NeoMotor(3); // front right
    private static NeoMotor rightFollowerMotor = new NeoMotor (4); // back right
    // i don't understand this :( yelling out commands and collecting information each time around the loop, or so they say. 
    public static Gyro gyro = new Gyro();

    static void init() {
        rightMotor.setInverted(true);
        rightFollowerMotor.setInverted(true);
        rightFollowerMotor.follow(rightMotor);
        leftFollowerMotor.follow(leftMotor);
        //this is because the motors are put in the opposite way, so the wheels move in the opposite direction of the motor. 
        rightMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);
        leftMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);

    }
}
