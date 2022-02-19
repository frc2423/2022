package frc.robot.subsystem;

import frc.robot.Devices;
import frc.robot.devices.NeoMotor;


public class Intake {

    private NeoMotor armMotor;
    private NeoMotor rollerMotor;
    private double desiredPosition;

    private double topPosition = 0;
    private double bottomPosition = 90;

    public Intake(){
        armMotor = Devices.intakeArmMotor;
        rollerMotor = Devices.intakeRollerMotor;
        intakeUp();
    }

    //sets position to current minus something
    public void stepUp(){
        rollerMotor.setPercent(0);
        desiredPosition -= 2; 
        armMotor.setDistance(desiredPosition);
        //Temporarily arbitrary
        
    }

    //sets position to current plus something
    public void stepDown(){
        rollerMotor.setPercent(0);
        desiredPosition += 2; 
        armMotor.setDistance(desiredPosition);
    }
        //Temporarily arbitrary

    //sets position to its up position
    public void intakeUp(){
        rollerMotor.setPercent(0);
        desiredPosition = topPosition;
        armMotor.setDistance(desiredPosition);
    }

    //sets position to its down position
      public void intakeDown(){
        rollerMotor.setPercent(0.1);
        desiredPosition = bottomPosition;
        armMotor.setDistance(desiredPosition);
    }

    //sets setpoint to current postion
    public void halt(){
        rollerMotor.setPercent(0);
        desiredPosition = armMotor.getEncoderCount();
        armMotor.setDistance(desiredPosition);
    }
}