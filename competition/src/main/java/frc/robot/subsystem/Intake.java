package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Devices;
import frc.robot.devices.NeoMotor;


public class Intake {

    private NeoMotor armMotor;
    private NeoMotor armMotorLeft;
    private NeoMotor rollerMotor;
    private double desiredPosition;

    private DigitalInput leftLimit;
    private DigitalInput rightLimit;

    private double topPosition = 0;
    private double bottomPosition = -10.5;
    private double rollerSpeed = 0.25;

    private String state = "Stop";

    public Intake(){
        armMotor = Devices.intakeArmMotor;
        armMotorLeft = Devices.intakeArmFollowerMotor;
        rollerMotor = Devices.intakeRollerMotor;
        leftLimit = Devices.leftLimit;
        rightLimit = Devices.rightLimit;
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

    public boolean calibrate(){

        if (!rightLimit.get()){
            armMotor.setPercent(0);
            armMotor.resetEncoder(0);
        } 
        else {
           armMotor.setPercent(-0.1);
        }

        if (!leftLimit.get()){
            armMotorLeft.setPercent(0);
            armMotorLeft.setPercent(0);
        }
        else {
            armMotorLeft.setPercent(-0.1);

        }

        return !rightLimit.get() && !leftLimit.get();
    }

    public void runIntake(){
        switch (state){
            case "Stop":
                stop();
                if (Devices.controller.getAButton()){
                    state = "Calibrate";
                }
                break;
            case "Calibrate":
                if (calibrate()){
                    state = "Down";
                }
                break;
            case "Down":
                intakeDown();
                if (Devices.controller.getYButton()){
                    state = "Up";
                }
                break;
            case "Up":
                intakeUp();
                if (leftLimit.get()){
                    armMotorLeft.setPercent(0);
                }
                if (rightLimit.get()){
                    armMotor.setPercent(0);
                }
                if (leftLimit.get() && rightLimit.get()){
                    state = "Stop";
                }
                break;
        }
    }


    
}