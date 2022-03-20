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
    private double barPosition;
    private final double LOW_POSITION = 80;
    private final double MEDIUM_POSITION = 160;
    private final double CLIMB_POSITION = 25;
    private final double BOTTOM_POSITION = 10;
    private double desiredPosition = 0;

    public Climber() {
        super("stop");
        leftMotor = Devices.climberLeftMotor;
        rightMotor = Devices.climberRightMotor;
        NtHelper.setString("/robot/climber/desiredState", "stop");

        NtHelper.setDouble("/robot/climber/ntPosition", 50);
    }

    public void preventClimberFromBreaking() {
        double leftPosition = leftMotor.getDistance();
        double rightPosition = rightMotor.getDistance();
        double bottomPosition = -5;

        if (leftPosition < bottomPosition || rightPosition < bottomPosition){
            setState("stop");
        }
    }

    public void calibrate() {
        // setState("stop");
        NtHelper.setString("robot/climber/desiredState", "stop");
    }

    public String getNtState() {
        return NtHelper.getString("/robot/climber/desiredState", "stop");
    }

    public double getNtPosition() {
        return NtHelper.getDouble("/robot/climber/ntPosition", 50);
    }

    public Boolean isMediumBar() {
        return NtHelper.getBoolean("/robot/climber/isMediumBar", true);
    }

    @Override
    public void setState(String name) {
        super.setState(name);
        NtHelper.setString("robot/climber/state", name);
    }

    public void setDesiredPosition(double position){
        leftMotor.setDistance(position);
        rightMotor.setDistance(position);
        desiredPosition = position;
    }
        
    @RunState(name = "stop")
    public void stop() {
        leftMotor.setPercent(0);
        rightMotor.setPercent(0);
        if (getNtState().equals("up")) {
            setState("up");
        } else {
            NtHelper.setString("robot/climber/state", "stop");
        }
    }

    @InitState(name = "up")
    public void initUp() {
        if (!isMediumBar()) {
            barPosition = LOW_POSITION; // !!!PLACEHOLDER VALUE!!!
        } else {
            barPosition = MEDIUM_POSITION; // !!!PLACEHOLDER VALUE!!!
        }
    }

    @RunState(name = "up")
    public void goUp() {
        setDesiredPosition(barPosition);
        // setDesiredPosition(getNtPosition());
        if (getNtState().equals("stop")) {
            setState("stop");
        } else if (getNtState().equals("climb")) {
            setState("climb");
        } else if (getNtState().equals("down")) {
            setState("down");
        } else {
            NtHelper.setString("robot/climber/state", "up");
        }
    }

    @InitState(name = "climb")
    public void initClimb() {
        barPosition = CLIMB_POSITION;
    }

    @RunState(name = "climb")
    public void climb() {
        /*
         * I don't exactly know what to do here
         * it for sure needs to move down slowly,
         * how it does that I'm not entirely sure;
         * Maybe a setpoint that is some difference
         * from desired position or it's own
         * position for a setDistance, but you would
         * need to slow down the motor speed somehow
         * and I don't know exactly how to do that :)
         */
        setDesiredPosition(CLIMB_POSITION);


        if (getNtState().equals("up")) {
            setState("up");
        } else {
            NtHelper.setString("robot/climber/state", "climb");
        }
    }

    @RunState(name = "down")
    public void down() {
        setDesiredPosition(0);
        boolean isDown = leftMotor.getDistance() < BOTTOM_POSITION && rightMotor.getDistance() < BOTTOM_POSITION;
        if (isDown || getNtState().equals("stop")) {
            setState("stop");
        } else if (getNtState().equals("up")) {
            setState("up");
        } else {
            NtHelper.setString("robot/climber/state", "down");
        }
    }

    public void climberReset(){
        leftMotor.resetEncoder(0);
        rightMotor.resetEncoder(0);
    }


    public void climberInfo(){
        NtHelper.setDouble("/robot/climber/barPosition", barPosition);
        NtHelper.setString("/robot/climber/currentState", getState());
        NtHelper.setDouble("/robot/climber/desiredPosition", desiredPosition);
        NtHelper.setDouble("/robot/climber/leftPosition", leftMotor.getDistance());
        NtHelper.setDouble("/robot/climber/rightPosition", rightMotor.getDistance());

    }
}
