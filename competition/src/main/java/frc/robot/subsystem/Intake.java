package frc.robot.subsystem;

import frc.robot.Devices;
import frc.robot.devices.NeoMotor;


class Intake {

    private NeoMotor motor;
    private double kP;
    private double kI;
    private double kD;
    private double position;

    private double topPosition;
    private double bottomPosition;

    public Intake(double kP, double kI, double kD){
        motor = Devices.intakeMotor;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        position = motor.getDistance();
    }

    //go up until hard stop and set encoder 0
    public void calibrate(){
        motor.setPercent(-0.4);
        while (! isStopped()); // TODO Add limit switch and timeout
        topPosition = motor.getDistance();
        //bottomPosition = 
    }

        //case 1 : stop when hits limit switch
        //case 2 : stop when encoder value stops changing

    //return true if motor is set to move but encoder isn't changing
    //isStopped()

    //sets position to current minus something
    public void stepUp(){
        position -= 2; 
        motor.setDistance(position);
        //Temporarily arbitrary
        
    }

    //sets position to current plus something
    public void stepDown(){
        position += 2; 
        motor.setDistance(position);
    }
        //Temporarily arbitrary

    //sets position to its up position
    public void intakeUp(){
        position = topPosition;
        motor.setDistance(position);
    }

    //sets position to its down position
      public void intakeDown(){
        position = bottomPosition;
        motor.setDistance(position);
    }

    //sets setpoint to current postion
    //halt()
}