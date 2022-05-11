package frc.robot;

import frc.robot.devices.NeoMotor;
import frc.robot.devices.SimMotor;
import frc.robot.util.ColourSensor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.math.util.Units;
import frc.robot.devices.*;
import edu.wpi.first.wpilibj.Relay;

public class Devices {
    public static XboxController driverController = new XboxController(0);
    public static XboxController operatorController = new XboxController(1);
    public static IGyro gyro;
    // Drivetrain motors
    public static  IMotor leftMotor; // front left
    private static IMotor leftFollowerMotor; // back left
    public static  IMotor rightMotor; // front right
    private static IMotor rightFollowerMotor; // back right
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
    public static NeoMotor climberLeftMotor = new NeoMotor(12);
    public static NeoMotor climberRightMotor = new NeoMotor(11);
    //relay stuff for LEDs on stinky (the camera) - please don't lose it again
    public static final Relay camLed = new Relay(0);
    //color sensor thing
    public static ColourSensor colourSensor = new ColourSensor();

    public static DigitalInput leftLimitSwitchClimber = new DigitalInput(3); // needs actual ports
    public static DigitalInput rightLimitSwitchClimber = new DigitalInput(4);
    //place holders for future beam break sensors
    public static BeamBreak intakeBeamBrake = new BeamBreak(new DigitalInput(5)); 
    public static BeamBreak shooterBeamBrake = new BeamBreak(new DigitalInput(6));

    //turret - three motors; turret, accelerator, hoof - two limit switches
    // public static NeoMotor turretMotor = new NeoMotor(13);
    // public static NeoMotor accelerateMotor = new NeoMotor(14);
    public static NeoMotor hoofMotor = new NeoMotor(15);
    
    // public static DigitalInput turretLeftLimitSwitch = new DigitalInput(7);
    // public static DigitalInput turretRightLimitSwitch = new DigitalInput(8);
    public static DigitalInput hoodLimitSwitch = new DigitalInput(9);

    static void init(boolean isSimulation) {

        if (!isSimulation) {
            gyro = new Gyro();
            leftMotor = new NeoMotor(1);
            leftFollowerMotor = new NeoMotor (2);
            rightMotor = new NeoMotor(3);
            rightFollowerMotor = new NeoMotor (4);
          
            rightMotor.setInverted(true);
            rightFollowerMotor.setInverted(true);
        } else {
            gyro = new SimGyro(0);
            leftMotor = new SimMotor(1, 5, 6);
            leftFollowerMotor = new SimMotor (2, 7, 8);
            rightMotor = new SimMotor(3, 9, 10);
            rightFollowerMotor = new SimMotor (4, 11, 12);
      
        }

        rightFollowerMotor.follow(rightMotor);
        leftFollowerMotor.follow(leftMotor);
        //this is because the motors are put in the opposite way, so the wheels move in the opposite direction of the motor. 
        rightMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);
        leftMotor.setConversionFactor(2 * Math.PI * Units.inchesToMeters(3) / 10.7);

        leftMotor.setBrakeMode(false);
        leftFollowerMotor.setBrakeMode(false);
        rightMotor.setBrakeMode(false);
        rightFollowerMotor.setBrakeMode(false);


        intakeArmMotor.setPid(.035, 0.00025, 0);
        intakeArmFollowerMotor.setPid(.035, 0.00025, 0);
        intakeArmMotor.setIZone(2);
        intakeArmFollowerMotor.setIZone(2);
        intakeArmFollowerMotor.setInverted(true);
        
        colourSensor.addColor("red", .475, .382, .142);
        colourSensor.addColor("blue", .17, .42, .39);
    
        Devices.camLed.set(Relay.Value.kForward);
        climberLeftMotor.setPid(.03, 0.0000, 0);
        climberRightMotor.setPid(.03, 0.0000, 0);

        climberLeftMotor.resetEncoder(0);
        climberRightMotor.resetEncoder(0);

        hoofMotor.setPid(.035, 0.00025, 0);
        hoofMotor.setIZone(2);
    }
}
