package frc.robot.subsystem;

import frc.robot.Devices;
import frc.robot.devices.NeoMotor;


class Intake {

    private NeoMotor armMotor;
    private NeoMotor rollerMotor;
    private double kP;
    private double kI;
    private double kD;
    private double desiredPosition;

    private double topPosition;
    private double bottomPosition;

    public Intake(double kP, double kI, double kD){
        armMotor = Devices.intakeArmMotor;
        rollerMotor = Devices.intakeRollerMotor;

        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        desiredPosition = armMotor.getDistance();
    }

    //go up until hard stop and set encoder 0
    public void calibrate(){
        armMotor.setPercent(-0.4);
        while (! isStopped()); // TODO Add limit switch and timeout
        topPosition = armMotor.getDistance();
        //bottomPosition = 
    }

        //case 1 : stop when hits limit switch
        //case 2 : stop when encoder value stops changing

    //return true if motor is set to move but encoder isn't changing
    //isStopped()

    //sets position to current minus something
    public void stepUp(){
        desiredPosition -= 2; 
        armMotor.setDistance(desiredPosition);
        //Temporarily arbitrary
        
    }

    //sets position to current plus something
    public void stepDown(){
        desiredPosition += 2; 
        armMotor.setDistance(desiredPosition);
    }
        //Temporarily arbitrary

    //sets position to its up position
    public void intakeUp(){
        desiredPosition = topPosition;
        armMotor.setDistance(desiredPosition);
    }

    //sets position to its down position
      public void intakeDown(){
        desiredPosition = bottomPosition;
        armMotor.setDistance(desiredPosition);
    }

    //sets setpoint to current postion
    //halt()
}