package frc.robot;

import frc.robot.devices.NeoMotor;
import frc.robot.util.ColourSensor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.math.util.Units;
import frc.robot.devices.*;
import edu.wpi.first.wpilibj.Relay;

public class Devices {
    public static XboxController controller = new XboxController(0);
    public static Gyro gyro = new Gyro();
    // Drivetrain motors
    public static NeoMotor leftMotor = new NeoMotor(1); // front left
    private static NeoMotor leftFollowerMotor = new NeoMotor (2); // back left
    public static NeoMotor rightMotor = new NeoMotor(3); // front right
    private static NeoMotor rightFollowerMotor = new NeoMotor (4); // back right
    // Intake motors
    public static NeoMotor intakeArmMotor = new NeoMotor (5);
    public static NeoMotor intakeArmFollowerMotor = new NeoMotor (6);
    public static NeoMotor intakeRollerMotor = new NeoMotor (7);
    // Intake limit switches
    public static DigitalInput leftLimit = new DigitalInput(1);
    public static DigitalInput rightLimit = new DigitalInput(2);
    // Belt and shooter motors
    public static NeoMotor beltMotor = new NeoMotor(8);
    public static NeoMotor kickerMotor = new NeoMotor(10);
    public static NeoMotor shooterMotor = new NeoMotor(9);
    // Climber motors
    //public static NeoMotor climberLeftMotor = new NeoMotor(???);
    //public static NeoMotor climberRightMotor = new NeoMotor(???);
    //relay stuff for LEDs on stinky (the camera) - please don't lose it again
    public static final Relay camLed = new Relay(0);
    //color sensor thing
    public static ColourSensor colourSensor = new ColourSensor();


    static void init() {
        rightMotor.setInverted(true);
        rightFollowerMotor.setInverted(true);
        rightFollowerMotor.follow(rightMotor);
        leftFollowerMotor.follow(leftMotor);
        //this is because the motors are put in the opposite way, so the wheels move in the opposite direction of the motor. 
        rightMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);
        leftMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);

        intakeArmMotor.setPid(.03, 0.0002, 0);
        intakeArmFollowerMotor.setPid(.03, 0.0002, 0);
        intakeArmMotor.setIZone(2);
        intakeArmFollowerMotor.setIZone(2);
        intakeArmFollowerMotor.setInverted(true);
        
        colourSensor.addColor("red", .475, .382, .142);
        colourSensor.addColor("blue", .17, .42, .39);
        
        // climberLeftMotor.setPid(.02, 0.0001, 0);
        // climberRightMotor.setPid(.02, 0.0001, 0);
    }
}
