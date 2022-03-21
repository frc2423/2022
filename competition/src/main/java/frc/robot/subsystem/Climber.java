package frc.robot.subsystem;

import frc.robot.Devices;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;

public class Climber extends StateMachine {

    private NeoMotor leftMotor;
    private NeoMotor rightMotor;
    private final double LOW_POSITION = 80;
    private final double MEDIUM_POSITION = 160;
    private final double CLIMB_POSITION = 25;
    private final double BOTTOM_POSITION = 10;
    private double desiredPosition = 0;

    public Climber() {
        super("stop");
        leftMotor = Devices.climberLeftMotor;
        rightMotor = Devices.climberRightMotor;
        setState("stop");
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
        setState("stop");
    }

    public String getDesiredState() {
        return NtHelper.getString(NtKeys.CLIMBER_DESIRED_STATE, "stop");
    }

    public Boolean isMediumBar() {
        return NtHelper.getBoolean(NtKeys.CLIMB_MEDIUM_BAR, true);
    }

    public void setDesiredState(String name) {
        NtHelper.setString(NtKeys.CLIMBER_DESIRED_STATE, name);
    }

    @Override
    public void setState(String name) {
        super.setState(name);
        NtHelper.setString(NtKeys.CLIMBER_CURRENT_STATE, name);
        setDesiredState(name);
    }

    public void setDesiredPosition(double position){
        leftMotor.setDistance(position);
        rightMotor.setDistance(position);
        desiredPosition = position;
        NtHelper.setDouble(NtKeys.CLIMBER_DESIRED_POSITION, desiredPosition);
    }
        
    @RunState(name = "stop")
    public void stop() {
        leftMotor.setPercent(0);
        rightMotor.setPercent(0);
        if (getDesiredState().equals("up")) {
            setState("up");
        } else {
            setDesiredState("stop");
        }
    }

    public double getBarPosition() {
        return isMediumBar() ? MEDIUM_POSITION : LOW_POSITION;
    }

    @RunState(name = "up")
    public void goUp() {
        setDesiredPosition(getBarPosition());
        if (getDesiredState().equals("stop")) {
            setState("stop");
        } else if (getDesiredState().equals("climb")) {
            setState("climb");
        } else if (getDesiredState().equals("down")) {
            setState("down");
        } else {
            setDesiredState("stop");
        }
    }

    @RunState(name = "climb")
    public void climb() {
        setDesiredPosition(CLIMB_POSITION);

        if (getDesiredState().equals("up")) {
            setState("up");
        } else {
            setDesiredState("climb");
        }
    }

    @RunState(name = "down")
    public void down() {
        setDesiredPosition(0);
        boolean isDown = leftMotor.getDistance() < BOTTOM_POSITION && rightMotor.getDistance() < BOTTOM_POSITION;
        if (isDown || getDesiredState().equals("stop")) {
            setState("stop");
        } else if (getDesiredState().equals("up")) {
            setState("up");
        } else {
            setDesiredState("down");
        }
    }

    public void climberReset(){
        leftMotor.resetEncoder(0);
        rightMotor.resetEncoder(0);
    }

    public void climberInfo(){
        NtHelper.setDouble(NtKeys.CLIMBER_LEFT_POSITION, leftMotor.getDistance());
        NtHelper.setDouble(NtKeys.CLIMBER_RIGHT_POSITION, rightMotor.getDistance());
    }
}
