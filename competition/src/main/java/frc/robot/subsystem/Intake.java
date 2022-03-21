package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Devices;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.ColourSensor;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

//we are children dont judge us :P

public class Intake extends StateMachine {

    private NeoMotor armMotor;
    private NeoMotor armMotorLeft;
    private NeoMotor rollerMotor;
    private double desiredPosition;

    private DigitalInput leftLimit;
    private DigitalInput rightLimit;

    private double topPosition = 1;
    private double bottomPosition = -14.5;
    private double belowPosition = -13.5;
    private double rollerSpeed = 0.50;
    private double calibrateSpeed = 0.1;

    private String state = "Calibrate";
    private NeoMotor beltMotor;
    private double beltSpeed = -0.2;
    private ColourSensor colourSensor;
    private Alliance currentAlliance = DriverStation.getAlliance();
    private String desiredColor;
    private String otherColor;
    private boolean colorLog = false;
    private String colorState = "No ball";
    private Timer timer = new Timer();

    private boolean ballRejection;
    private boolean filterColor;

    public Intake() {
        super("Stop");

        NtHelper.setDouble("/robot/svg/robotArmSetpointMax", bottomPosition);

        armMotor = Devices.intakeArmMotor;
        armMotorLeft = Devices.intakeArmFollowerMotor;
        rollerMotor = Devices.intakeRollerMotor;
        leftLimit = Devices.leftLimit;
        rightLimit = Devices.rightLimit;
        beltMotor = Devices.beltMotor;
        colourSensor = Devices.colourSensor;
        setCargoCount(0);

        if (currentAlliance == Alliance.Blue) {
            desiredColor = "blue";
            otherColor = "red";
        } else {
            desiredColor = "red";
            otherColor = "blue";
        }

        //TEMPORARY ~~~~~ UNTIL WORKING
        doBallRejection(false);
        doColorFiltering(false);

        // intakeUp();
        // zero(); //0.0000
        // stop();
    }

    public void doBallRejection(boolean reject){
        ballRejection = reject;
    }

    public void doColorFiltering(boolean filter){
        filterColor = filter;
    }

    public double getCargoCount(){
        return NtHelper.getDouble(NtKeys.CARGO_COUNT, 0);
    }

    public void setCargoCount(double count) {
        NtHelper.setDouble(NtKeys.CARGO_COUNT, count);
    }

    private void beltForward() {
        NtHelper.setString("/robot/intake/currentcolorstate", colorState);

        switch (colorState) {
            case "No ball":
                NtHelper.setString("/robot/intake/currentcolor", "No color detected");
                beltMotor.setPercent(beltSpeed);

                if (colourSensor.isColor(desiredColor)) {
                    colorState = "DesiredBall";
                    setCargoCount(getCargoCount() + 1);
                }
                if (colourSensor.isColor(otherColor) && filterColor) {
                    colorState = "OtherBall";
                    timer.reset();
                    timer.start();
                    rollerSpeed = (-rollerSpeed);
                }
                break;
            case "DesiredBall":
                NtHelper.setString("/robot/intake/currentcolor", desiredColor);

                if (getCargoCount() == 0 || getCargoCount() == 1) {
                    beltMotor.setPercent(beltSpeed);
                } else if (getCargoCount() > 1 && ballRejection) {
                    beltMotor.setPercent(0);
                }

                if (!colourSensor.isColor(desiredColor)) {
                    colorState = "No ball";
                }

                if (colourSensor.isColor(otherColor) && filterColor) {
                    colorState = "OtherBall";
                    rollerSpeed = (-rollerSpeed);
                    timer.reset();
                    timer.start();

                }

                break;
            case "OtherBall":
                NtHelper.setString("/robot/intake/currentcolor", otherColor);
                beltMotor.setPercent(-beltSpeed);
                if ((getCargoCount() == 1 && colourSensor.isColor(desiredColor)) || timer.get() > 2) {
                    colorState = "No ball";
                    rollerSpeed = (-rollerSpeed);
                    timer.stop();
                }
                break;
        }
    }

    public void beltStop() {
        beltMotor.setPercent(0);
    }

    // sets position to current minus something
    public void stepUp() {
        rollerMotor.setPercent(0);
        desiredPosition -= 2;
        armMotor.setDistance(desiredPosition);
        armMotorLeft.setDistance(desiredPosition);
    }

    // sets position to current plus something
    public void stepDown() {
        rollerMotor.setPercent(0);
        desiredPosition += 2;
        armMotor.setDistance(desiredPosition);
        armMotorLeft.setDistance(desiredPosition);
    }

    // sets position to its up position
    private void intakeUp() {
        rollerMotor.setPercent(0);
        desiredPosition = topPosition;
        armMotor.setDistance(desiredPosition);
        armMotorLeft.setDistance(desiredPosition);
        runOuttake();

    }

    private void runOuttake() {
        boolean isOuttaking = Devices.controller.getBButton();
        if (isOuttaking) {
            Devices.beltMotor.setPercent(-beltSpeed);
        } else {
            Devices.beltMotor.setPercent(0);

        }

    }

    // sets position to its down position
    private void intakeDown() {
        rollerMotor.setPercent(rollerSpeed);
        beltForward();

        desiredPosition = /* (Devices.controller.getLeftBumper()) ? belowPosition : */ bottomPosition;
        armMotor.setDistance(desiredPosition);
        armMotorLeft.setDistance(desiredPosition);
    }

    // sets setpoint to current postion
    public void holdInPlace() {
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

    public void reverse() {
        rollerMotor.setPercent(-rollerSpeed);
    }

    public void zero() {
        armMotor.resetEncoder(0.00000);
        armMotorLeft.resetEncoder(0.0000);
    }

    public boolean calibrate() {

        if (!isRightPressed()) {
            armMotor.setPercent(calibrateSpeed);
        } else {
            armMotor.setPercent(0);
            armMotor.resetEncoder(0);
        }

        if (!isLeftPressed()) {
            armMotorLeft.setPercent(calibrateSpeed);
        } else {
            armMotorLeft.setPercent(0);
            armMotorLeft.resetEncoder(0);
        }
        return (isRightPressed() && isLeftPressed());

    }

    @InitState(name = "Stop")
    public void stopInit() {
        beltStop();

    }

    @RunState(name = "Stop")
    public void stopRun() {
        // System.out.println("color " + colourSensor.getRawColor().red + " " +
        // colourSensor.getRawColor().green + " " + colourSensor.getRawColor().blue);

        stop();
        if (Devices.controller.getAButton()) {
            setState("Calibrate");
        }
    }

    @InitState(name = "Calibrate")
    public void calibrateInit() {

    }

    @RunState(name = "Calibrate")
    public void calibrateRun() {
        if (calibrate()) {
            setState("Down");
        }
    }

    @InitState(name = "Down")
    public void downInit() {

    }

    @RunState(name = "Down")
    public void runDown() {
        intakeDown();
        if (Devices.controller.getYButton()) {
            setState("Up");
        }
    }

    @InitState(name = "Up")
    public void upInit() {

    }

    public boolean isLeftPressed() {
        return !leftLimit.get();
    }

    public boolean isRightPressed() {
        return !rightLimit.get();
    }

    @RunState(name = "Up")
    public void upRun() {

        intakeUp();
        if (isLeftPressed()) {
            armMotorLeft.setPercent(0);
        }
        if (isRightPressed()) {
            armMotor.setPercent(0);
        }
        if (!leftLimit.get() && !rightLimit.get()) {
            setState("Stop");
        }
        if (Devices.controller.getAButton()) {
            setState("Down");
        }
    }
    public void setDownState(){
        state="Down";
    }
    public void setUpState(){
        state="Up";
    }

    public void runIntake() {
        // String rawColor = colourSensor
        // NtHelper.setString("/robot/intake/rawColorValue", colourSensor.getColor().green
        NtHelper.setString("/robot/intake/state", state);
        NtHelper.setBoolean("/robot/intake/leftLimit", leftLimit.get());
        NtHelper.setBoolean("/robot/intake/rightLimit", rightLimit.get());

        switch (state) {
            case "Calibrate":
                if (calibrate()) {
                    System.out.println("calibrated");
                    state = "Stop";
                }
                break;
            case "Stop":
                // System.out.println("color " + colourSensor.getRawColor().red + " " +
                // colourSensor.getRawColor().green + " " + colourSensor.getRawColor().blue);

                stop();
                if (Devices.controller.getAButton()) {
                    state = "Down";
                }
                break;
            case "Down":
                intakeDown();
                if (Devices.controller.getYButton()) {
                    state = "Up";
                }
                break;
            case "Up":
                intakeUp();
                if (isLeftPressed()) {
                    armMotorLeft.setPercent(0);
                }
                if (isRightPressed()) {
                    armMotor.setPercent(0);
                }
                if (isLeftPressed() && isRightPressed()) { // "||"just for testing because there is the bar in the way
                    state = "Stop";
                }
                if (Devices.controller.getAButton()) {
                    state = "Down";
                }
                break;
        }
    }

}