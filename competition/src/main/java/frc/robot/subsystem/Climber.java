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
    private boolean safe = false;

    public Climber() {
        super("down");
        leftMotor = Devices.climberLeftMotor;
        rightMotor = Devices.climberRightMotor;
        setState("down");
    }

    public void preventClimberFromBreaking() {
        double leftPosition = leftMotor.getDistance();
        double rightPosition = rightMotor.getDistance();
        double bottomPosition = -5;

        if (leftPosition < bottomPosition || rightPosition < bottomPosition){
            safe = true;
            setState("down");
        }
    }

    public void calibrate() {
        setState("down");
    }

    public String getDesiredState() {
        return NtHelper.getString(NtKeys.CLIMBER_DESIRED_STATE, "down");
    }

    public Boolean isMediumBar() {
        return NtHelper.getBoolean(NtKeys.CLIMB_MEDIUM_BAR, true);
    }
    
    // ok it's fine - alexandra
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
    
    @RunState(name = "up")
    public void goUp() {
        setDesiredPosition(MEDIUM_POSITION);
        if (getDesiredState().equals("down")) {
            setState("down");
        }
    }

    @RunState(name = "down")
    public void down() {
        boolean isDown = (leftMotor.getDistance() < BOTTOM_POSITION && rightMotor.getDistance() < BOTTOM_POSITION) || safe;
        if (isDown) {
            leftMotor.setPercent(0);
            rightMotor.setPercent(0);
        } else {
            setDesiredPosition(0);
        }
        if (getDesiredState().equals("up")) {
            safe = false;
            setState("up");
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
