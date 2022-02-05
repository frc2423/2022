package frc.robot;

import frc.robot.devices.NeoMotor;

import edu.wpi.first.wpilibj.XboxController;

public class Devices {
    static XboxController controller = new XboxController(0);
    static NeoMotor leftMotor = new NeoMotor(1); // front left
    private static NeoMotor leftFollowerMotor = new NeoMotor (2); // back left
    static NeoMotor rightMotor = new NeoMotor(3); // front right
    private static NeoMotor rightFollowerMotor = new NeoMotor (4); // back right
    // i don't understand this :( yelling out commands and collecting information each time around the loop, or so they say. 

    static void init() {
        rightMotor.setInverted(true);
        rightFollowerMotor.setInverted(true);
        rightFollowerMotor.follow(rightMotor);
        leftFollowerMotor.follow(leftMotor);
        //this is because the motors are put in the opposite way, so the wheels move in the opposite direction of the motor. 
    }
}