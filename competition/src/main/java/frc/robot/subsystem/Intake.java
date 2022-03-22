package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Devices;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;

//we are children dont judge us :P

public class Intake {

    private NeoMotor armMotor;
    private NeoMotor armMotorLeft;
    private NeoMotor rollerMotor;

    private DigitalInput leftLimit;
    private DigitalInput rightLimit;

    private double topPosition = 1;
    private double bottomPosition = -14.5;
    private double rollerSpeed = 0.50;


    private boolean isDown = false;

    public Intake() {
        NtHelper.setDouble(NtKeys.SVG_INTAKE_MAX_POSITION, bottomPosition);
        armMotor = Devices.intakeArmMotor;
        armMotorLeft = Devices.intakeArmFollowerMotor;
        rollerMotor = Devices.intakeRollerMotor;
        leftLimit = Devices.leftLimit;
        rightLimit = Devices.rightLimit;
 
    }


    public void intakeUp(){
        rollerMotor.setPercent(0);
        if(!leftLimit.get()) {
            armMotorLeft.setPercent(0);
            armMotorLeft.resetEncoder(0);
        } else {
            armMotorLeft.setDistance(topPosition);
        }
        if(!rightLimit.get()) {
            armMotor.setPercent(0);
            armMotor.resetEncoder(0);
        } else {
            armMotor.setDistance(topPosition); 
        }
        isDown = false;
    }

    public void intakeDown(){
        rollerMotor.setPercent(rollerSpeed);
        armMotor.setDistance(bottomPosition);
        armMotorLeft.setDistance(bottomPosition);
        isDown = true;
    }

    public boolean isDown(){
        return isDown;
    }

}