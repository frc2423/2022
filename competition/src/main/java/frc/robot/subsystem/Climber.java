package frc.robot.subsystem;

import frc.robot.Devices;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;

public class Climber extends StateMachine {
    
    private NeoMotor leftMotor;
    private NeoMotor rightMotor;
    private double desiredPosition;

    public Climber(){
        super("Stop");
    }

    //@RunState(name = "Stop")
    //public void stop{
    //    leftMotor.setPercent(0);
    //    rightMotor.setPercent(0);
        // if (NtHelper.getString()
    //}


    //@RunState(name = "GoUp")
    //public void goUp(){
    //    leftMotor.setDistance(desiredPosition);
    //    rightMotor.setDistance(desiredPosition);
    //}

    //@RunState(name = "")
}
