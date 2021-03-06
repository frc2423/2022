package frc.robot.subsystem;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Devices;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.State;
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
    
    @State(name = "up")
    public void goUp() {
        setDesiredPosition(MEDIUM_POSITION);
        if (getDesiredState().equals("down")) {
            setState("down");
        } else if (getDesiredState().equals("manual")) {
            setState("manual");
        }
    }

    private boolean isLeftLimitPressed() {
        return leftLimitSwitch.get();
    }

    private boolean isRightLimitPressed() {
        return rightLimitSwitch.get();
    }


    @State(name = "down")
    public void down() {
        if (isLeftLimitPressed() || isRightLimitPressed()) {
            if (isLeftLimitPressed()) {
                leftMotor.setPercent(0);
            }
            if (isRightLimitPressed()) {
                rightMotor.setPercent(0);
            }
        } else {
            setDesiredPosition(0);
        }
        if (getDesiredState().equals("up")) {
            setState("up");
        } else if (getDesiredState().equals("manual")) {
            setState("manual");
        }
    }

    @State(name = "manual")
    public void manual() {
        var left = -MathUtil.applyDeadband(Devices.operatorController.getLeftY(), .1) * .2;
        var right = -MathUtil.applyDeadband(Devices.operatorController.getRightY(), .1) * .2;
        leftMotor.setPercent(left);
        rightMotor.setPercent(right);

        if (getDesiredState().equals("down")) {
            setState("down");
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
