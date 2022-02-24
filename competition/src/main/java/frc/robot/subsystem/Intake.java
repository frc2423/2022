package frc.robot.subsystem;

import frc.robot.Devices;
import frc.robot.devices.NeoMotor;


public class Intake {

    private NeoMotor armMotor;
    private NeoMotor armMotorLeft;
    private NeoMotor rollerMotor;
    private double desiredPosition;

    private double topPosition = 0;
    private double bottomPosition = -10.5;
    private double rollerSpeed = 0.25;

    public Intake(){
        armMotor = Devices.intakeArmMotor;
        armMotorLeft = Devices.intakeArmFollowerMotor;
        rollerMotor = Devices.intakeRollerMotor;
      //  intakeUp();
        zero();
        stop();
    }

    //sets position to current minus something
    public void stepUp(){
        rollerMotor.setPercent(0);
        desiredPosition -= 2; 
        armMotor.setDistance(desiredPosition);
        armMotorLeft.setDistance(desiredPosition);
        //Temporarily arbitrary
        
    }

    //sets position to current plus something
    public void stepDown(){
        rollerMotor.setPercent(0);
        desiredPosition += 2; 
        armMotor.setDistance(desiredPosition);
        armMotorLeft.setDistance(desiredPosition);
    }
        //Temporarily arbitrary

    //sets position to its up position
    public void intakeUp(){
        rollerMotor.setPercent(0);
        desiredPosition = topPosition;
        armMotor.setDistance(desiredPosition);
        armMotorLeft.setDistance(desiredPosition);
    }

    //sets position to its down position
      public void intakeDown(){
        rollerMotor.setPercent(rollerSpeed);
        desiredPosition = bottomPosition;
        armMotor.setDistance(desiredPosition);
        armMotorLeft.setDistance(desiredPosition);
    }

    //sets setpoint to current postion
    public void holdInPlace(){
        rollerMotor.setPercent(0);
        desiredPosition = armMotor.getEncoderCount();
        armMotor.setDistance(desiredPosition);
        armMotorLeft.setDistance(desiredPosition);
    }

    public void stop() {
        rollerMotor.setPercent(0);
        armMotor.setPercent(0);
        armMotorLeft.setPercent(0);
    }

    public void reverse(){
        rollerMotor.setPercent(-rollerSpeed);
    }

    public void zero(){
        armMotor.resetEncoder(0.00000);
        armMotorLeft.resetEncoder(0.0000);
        //0000000000000000000000000000000.0
    }
}