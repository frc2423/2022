package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Devices;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;

public class Climber extends StateMachine {

    private NeoMotor leftMotor;
    private NeoMotor rightMotor;
    private final double MEDIUM_POSITION = 160;
    private double desiredPosition = 0;

    private DigitalInput leftLimitSwitch; 
    private DigitalInput rightLimitSwitch;

    public Climber() {
        super("down");
        leftMotor = Devices.climberLeftMotor;
        rightMotor = Devices.climberRightMotor;

        leftLimitSwitch = Devices.leftLimitSwitchClimber;
        rightLimitSwitch = Devices.rightLimitSwitchClimber;
        setState("down");

        NtHelper.setDouble("/climberDownPosition", 0);
    }

    private double getClimberDownPosition() {
        return NtHelper.getDouble("/climberDownPosition", 0);

    }

    public void preventClimberFromBreaking() {
        double leftPosition = leftMotor.getDistance();
        double rightPosition = rightMotor.getDistance();
        double bottomPosition = -5;

        if (leftPosition < bottomPosition || rightPosition < bottomPosition){
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

    private boolean isLeftLimitPressed() {
        return leftLimitSwitch.get();
    }

    private boolean isRightLimitPressed() {
        return rightLimitSwitch.get();
    }


    @RunState(name = "down")
    public void down() {
        if (isLeftLimitPressed() || isRightLimitPressed()) {
            if (isLeftLimitPressed()) {
                leftMotor.setPercent(0);
            }
            if (isRightLimitPressed()) {
                rightMotor.setPercent(0);
            }
        } else {
            // setDesiredPosition(getClimberDownPosition());
            setDesiredPosition(0);

        }
        if (getDesiredState().equals("up")) {
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
        NtHelper.setBoolean(NtKeys.CLIMBER_IS_LEFT_LIMIT_PRESSED, isLeftLimitPressed());
        NtHelper.setBoolean(NtKeys.CLIMBER_IS_RIGHT_LIMIT_PRESSED, isRightLimitPressed());
    }
}
