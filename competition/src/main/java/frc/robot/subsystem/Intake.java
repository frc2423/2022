package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Devices;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;

//we are children dont judge us :P

public class Intake extends StateMachine{

    private NeoMotor armMotor;
    private NeoMotor armMotorLeft;
    private NeoMotor rollerMotor;
    private double desiredPosition;

    private DigitalInput leftLimit;
    private DigitalInput rightLimit;

    private double topPosition = .5;
    private double bottomPosition = -13.5;
    private double belowPosition = -13.5;
    private double rollerSpeed = 0.45;
    private double calibrateSped = 0.1;

    private String state = "Stop";

    public Intake(){
        super("Stop");
        armMotor = Devices.intakeArmMotor;
        armMotorLeft = Devices.intakeArmFollowerMotor;
        rollerMotor = Devices.intakeRollerMotor;
        leftLimit = Devices.leftLimit;
        rightLimit = Devices.rightLimit;
      //  intakeUp();
        // zero(); //0.0000
        // stop();
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

       /* if (Devices.controller.getXButton()){
            reverse();
        }
        else {
            rollerMotor.setPercent(rollerSpeed);
        }*/
        rollerMotor.setPercent(rollerSpeed);

        desiredPosition = /*(Devices.controller.getLeftBumper()) ? belowPosition :*/ bottomPosition;
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
           armMotor.setPercent(calibrateSped);
        }

        if (!leftLimit.get()){
            armMotorLeft.setPercent(0);
            armMotorLeft.resetEncoder(0);
        }
        else {
            armMotorLeft.setPercent(calibrateSped);
        }

        if (!rightLimit.get() || !leftLimit.get()){ //"||"just for testing because there is the bar in the way
            armMotor.resetEncoder(0);
            armMotorLeft.resetEncoder(0);
            return true;
        } 
        else {
            return false;
        }
    }

    @InitState(name="Stop")
    public void stopInit(){

    }

    @RunState(name="Stop")
    public void stopRun(){
        stop();
        if (Devices.controller.getAButton()){
            setState("Calibrate");
        }
    }

    @InitState(name="Calibrate")
    public void calibrateInit(){

    }

    @RunState(name="Calibrate")
    public void calibrateRun(){
        if (calibrate()){
            setState("Down");
        }
    }

    @InitState(name="Down")
    public void downInit(){

    }

    @RunState(name="Down")
    public void runDown(){
        intakeDown();
            if (Devices.controller.getYButton()){
                setState("Up");
            }
    }

    @InitState(name="Up")
    public void upInit(){

    }

    @RunState(name="Up")
    public void upRun(){
        intakeUp();
            if (!leftLimit.get()){
                armMotorLeft.setPercent(0);
            }
            if (!rightLimit.get()){
                armMotor.setPercent(0);
            }
            if (!leftLimit.get() || !rightLimit.get()){ //"||"just for testing because there is the bar in the way
                setState("Stop");
            }
            if (Devices.controller.getAButton()){
                setState("Down");
            }
    }




    public void runIntake(){
        NtHelper.setString("/robot/intake/state", state);
        NtHelper.setBoolean("/robot/intake/leftLimit", leftLimit.get());
        NtHelper.setBoolean("/robot/intake/rightLimit", rightLimit.get());

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
                if (!leftLimit.get()){
                    armMotorLeft.setPercent(0);
                }
                if (!rightLimit.get()){
                    armMotor.setPercent(0);
                }
                if (!leftLimit.get() || !rightLimit.get()){ //"||"just for testing because there is the bar in the way
                    state = "Stop";
                }
                if (Devices.controller.getAButton()){
                    state = "Down";
                }
                break;
        }
    }


    
}