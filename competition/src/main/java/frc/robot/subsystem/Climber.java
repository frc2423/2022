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
    private final double LOW_POSITION = 10;
    private final double MEDIUM_POSITION = 20;
    private final double CLIMB_POSITION = 5;
    private final double BOTTOM_DISTANCE = 1;

    public Climber() {
        super("stop");
        // leftMotor = Devices.climberLeftMotor;
        // rightMotor = Devices.climberRightMotor;
    }

    public void resetClimber() {
        leftMotor.resetEncoder(0);
        rightMotor.resetEncoder(0);
    }

    public String getNtState() {
        return NtHelper.getString("/robot/climber/state", "stop");
    }

    public Boolean isMediumBar() {
        return NtHelper.getBoolean("/robot/climber/isMediumBar", true);
    }

    @Override
    public void setState(String name) {
        super.setState(name);
        NtHelper.setString("robot/climber/state", name);
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
        if (isMediumBar()) {
            desiredPosition = LOW_POSITION; // !!!PLACEHOLDER VALUE!!!
        } else {
            desiredPosition = MEDIUM_POSITION; // !!!PLACEHOLDER VALUE!!!
        }
    }

    @RunState(name = "up")
    public void goUp() {
        leftMotor.setDistance(desiredPosition);
        rightMotor.setDistance(desiredPosition);
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
        desiredPosition = CLIMB_POSITION;
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
        leftMotor.setDistance(desiredPosition);
        rightMotor.setDistance(desiredPosition);

        if (getNtState().equals("up")) {
            setState("up");
        } else {
            NtHelper.setString("robot/climber/state", "climb");
        }
    }

    @RunState(name = "down")
    public void down() {
        leftMotor.setDistance(0);
        rightMotor.setDistance(0);
        boolean isDown = leftMotor.getDistance() < BOTTOM_DISTANCE && rightMotor.getDistance() < BOTTOM_DISTANCE;
        if (isDown || getNtState().equals("stop")) {
            setState("stop");
        } else if (getNtState().equals("up")) {
            setState("up");
        } else {
            NtHelper.setString("robot/climber/state", "down");
        }
    }
}
