package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Devices;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;
import frc.robot.Subsystems;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

//we are children dont judge us :P

public class Intake {

    public enum IntakeState {
        ShallIntake,
        ShallReject,
        ShallStop,
    }

    private NeoMotor armMotor;
    private NeoMotor armMotorLeft;
    private NeoMotor rollerMotor;

    private DigitalInput leftLimit;
    private DigitalInput rightLimit;

    private double topPosition = 1;
    private double bottomPosition = -14.5;
    private double rollerSpeed = 0.50;

    private boolean calibrated = false;
    private boolean isDown = false;
    private Timer spacingTimer = new Timer();
    private Timer rejectionTimer = new Timer();
    private double separationDelay = 0.5; // extra seconds to run intake to separate cargo
    private double rejectionDelay = 1; // seconds to run belt motor backwards on bad cargo
    private IntakeState intakeState = IntakeState.ShallStop;

    public Intake() {
        NtHelper.setDouble(NtKeys.SVG_INTAKE_MAX_POSITION, bottomPosition);
        armMotor = Devices.intakeArmMotor;
        armMotorLeft = Devices.intakeArmFollowerMotor;
        rollerMotor = Devices.intakeRollerMotor;
        leftLimit = Devices.leftLimit;
        rightLimit = Devices.rightLimit;

        armMotor.resetEncoder(0);
        armMotorLeft.resetEncoder(0);
    }

    public boolean isRightPressed() {
        return !rightLimit.get();
    }

    public boolean isLeftPressed() {
        return !leftLimit.get();
    }

    private void calibrate() {
        if (isLeftPressed()) {
            armMotorLeft.setPercent(0);
            armMotorLeft.resetEncoder(0);
        } else {
            armMotorLeft.setPercent(0.1);
        }
        if (isRightPressed()) {
            armMotor.setPercent(0);
            armMotor.resetEncoder(0);
        } else {
            armMotor.setPercent(0.1);
        }
        if (isLeftPressed() && isRightPressed()) {
            calibrated = true;
        }
    }

   

    private void intakeUp(){
        rollerMotor.setPercent(0);
        if(isLeftPressed() || armMotorLeft.getDistance() > topPosition) {
            armMotorLeft.setPercent(0);
        } else {
            armMotorLeft.setPercent(0.15); 
        }
        if(isRightPressed() || armMotor.getDistance() > topPosition) {
            armMotor.setPercent(0);
        } else {
            armMotor.setPercent(0.15); 
        }
    }

    private void intakeDown() {
        System.out.println("Intake down");
        rollerMotor.setPercent(intakeState == IntakeState.ShallReject ? -rollerSpeed : rollerSpeed);
        armMotor.setDistance(bottomPosition);
        armMotorLeft.setDistance(bottomPosition);       
    }

    public boolean isDown() {
        return isDown;
    }

    public void goUp() {
        isDown = false;
    }

    public void goDown() {
        isDown = true;
    }

    public void unCalibrate() {
        calibrated = false;
    }

    public void runIntake() {
        
        if (!calibrated) {
            calibrate();
        } else if (!isDown()) {
            intakeUp();
        } else {
            intakeDown();
        }
        
        NtHelper.setString(NtKeys.DETECTED_CARGO_COLOR, 
        Subsystems.cargoDetector.isDetected(true) ? Alliance.Blue.toString() 
        : Subsystems.cargoDetector.isDetected(false) ? Alliance.Red.toString() 
        : "no color");
        NtHelper.setString( NtKeys.INTAKE_STATE, intakeState.toString() );
        NtHelper.setString(NtKeys.INTAKE_ARM_STATE, isDown ? "down" : "up");
        NtHelper.setDouble(NtKeys.CARGO_COUNT, Subsystems.cargoCounter.getNumCargo());
        NtHelper.setDouble("robot/intake/rollerMotorSpeed", rollerMotor.getSpeed());

    }

    public IntakeState getIntakeState() {
        return intakeState;
    }

    public void runCargoDetection() {
        if(intakeState == IntakeState.ShallReject) {

            if(rejectionTimer.get() > rejectionDelay) {
                rejectionTimer.stop();
                rejectionTimer.reset();
                intakeState = IntakeState.ShallStop;
            }
            return;
        }

        if (Subsystems.cargoDetector.hasChanged()) {
            System.out.println("Cargo state changed");
            if (Subsystems.cargoDetector.isDetected(true)) {
                System.out.println("GOOD color");
                int cargoCount = Subsystems.cargoCounter.getNumCargo();
                // have target color. let the intake belt run if it's the first ball
                if (cargoCount == 0) {
                    System.out.println("Add first cargo and intake");
                    Subsystems.cargoCounter.addCargo();
                    intakeState = IntakeState.ShallIntake;
                    spacingTimer.start();
                } else if (cargoCount == 1) {
                    // on the second cargo, we want to stop the belt
                    // as soon as we see it (rising edge counting)
                    System.out.println("Add second cargo and stop");
                    Subsystems.cargoCounter.addCargo();
                    intakeState = IntakeState.ShallStop;
                }
            } else if (Subsystems.cargoDetector.isDetected(false)) {
                // has other color. abort! (not sure if this is needed)
                System.out.println("BAD color");
                spacingTimer.stop();
                spacingTimer.reset();
                intakeState = IntakeState.ShallReject;
               
                rejectionTimer.reset();
                rejectionTimer.start();
            }
            
        }

        // in the 1-cargo case, stop the belt motor after a bit of a delay
        if (spacingTimer.get() > separationDelay) {
            spacingTimer.stop();
            spacingTimer.reset();
            intakeState = IntakeState.ShallStop;
        }
    }

}