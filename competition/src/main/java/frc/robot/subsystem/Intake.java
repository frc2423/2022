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

    private boolean calibrated = false;
    private boolean isDown = false;

    public Intake() {
        NtHelper.setDouble(NtKeys.SVG_INTAKE_MAX_POSITION, bottomPosition);
        armMotor = Devices.intakeArmMotor;
        armMotorLeft = Devices.intakeArmFollowerMotor;
        rollerMotor = Devices.intakeRollerMotor;
        leftLimit = Devices.leftLimit;
        rightLimit = Devices.rightLimit;

        armMotor.resetEncoder(0);
        armMotorLeft.resetEncoder(0);
 
    }

    public boolean isRightPressed(){
        return !rightLimit.get();
    }

    public boolean isLeftPressed(){
        return !leftLimit.get();
    }

    private void calibrate(){
        if(isLeftPressed()) {
            armMotorLeft.setPercent(0);
            armMotorLeft.resetEncoder(0);
        } else {
            armMotorLeft.setPercent(0.1); 
        }
        if(isRightPressed()) {
            armMotor.setPercent(0);
            armMotor.resetEncoder(0);
        } else {
            armMotor.setPercent(0.1); 
        }
        if (isLeftPressed() && isRightPressed()){
            calibrated = true;
        }
    }

    private void intakeUp(){
        rollerMotor.setPercent(0);
        if(isLeftPressed()) {
            armMotorLeft.setPercent(0);
        } else {
            armMotorLeft.setDistance(topPosition);
        }
        if(isRightPressed()) {
            armMotor.setPercent(0);
        } else {
            armMotor.setDistance(topPosition); 
        }
    }

    private void intakeDown(){
        rollerMotor.setPercent(rollerSpeed);
        armMotor.setDistance(bottomPosition);
        armMotorLeft.setDistance(bottomPosition);
    }

    public boolean isDown(){
        return isDown;
    }

    public void goUp() {
        isDown = false;
    }

    public void goDown() {
        isDown = true;
    }

    public void unCalibrate(){
        calibrated = false;
    }

    public void runIntake() {
        NtHelper.setBoolean(NtKeys.INTAKE_LEFT_LIMIT_PRESSED, isLeftPressed());
        NtHelper.setBoolean(NtKeys.INTAKE_RIGHT_LIMIT_PRESSED, isRightPressed());
        if (!calibrated) {
            calibrate();
        } else if (!isDown()){
            NtHelper.setString(NtKeys.INTAKE_STATE, "up");
            intakeUp();
        } else {
            NtHelper.setString(NtKeys.INTAKE_STATE, "down");
            intakeDown();
        }
    }

}