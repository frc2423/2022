package frc.robot.subsystem;

import frc.robot.Subsystems;
import frc.robot.constants.constants;
import frc.robot.util.DriveHelper;
import frc.robot.util.NtHelper;
import frc.robot.util.Targeting;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import frc.robot.Devices;

public class Drive {

    private PIDController leftPid = new PIDController(0.1, 0, 0.001); // .001 p
    private PIDController rightPid = new PIDController(0.1, 0, 0.001);
    private boolean isAimed = false;
    private DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds();
    private boolean isTargeting = false;

    public Drive() {
        leftPid.setIntegratorRange(0, .1);
        rightPid.setIntegratorRange(0, .1);

    }

    public void setSpeeds(double leftMetersPerSecond, double rightMetersPerSecond) {
        wheelSpeeds = new DifferentialDriveWheelSpeeds(leftMetersPerSecond, rightMetersPerSecond);
    }

    public void setSpeeds(DifferentialDriveWheelSpeeds wheelSpeeds) {
        this.wheelSpeeds = wheelSpeeds;
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return wheelSpeeds;
    }

    public void runTargeting() {
        double rotationSpeed = Targeting.calculate();
        double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
        double leftSpeed = arcadeSpeeds[0];
        double rightSpeed = arcadeSpeeds[1];

        Subsystems.drive.setSpeeds(leftSpeed * Units.feetToMeters(constants.maxSpeedo),
                rightSpeed * Units.feetToMeters(constants.maxSpeedo));
        isAimed = rotationSpeed == 0 && Targeting.hasTargets();
    }

    public boolean getIsAimed() {
        return isAimed;
    }

    public void isTargeting(boolean value) { //"not quoting things here" -Alexandra 5/20/2022
        isTargeting = value;
    }

    public void run() {
        double[] feedForward = Subsystems.drivetrain.getMotorValues(wheelSpeeds);

        // double leftFeedback = leftPid.calculate(Devices.leftMotor.getSpeed(), wheelSpeeds.leftMetersPerSecond);
        // double rightFeedback = rightPid.calculate(Devices.rightMotor.getSpeed(), wheelSpeeds.rightMetersPerSecond);

        // // NtHelper - print out actual left value
        NtHelper.setDouble("/robot/PIDControllers/Actual", Devices.leftMotor.getSpeed());
        NtHelper.setDouble("/robot/PIDControllers/Desired", wheelSpeeds.leftMetersPerSecond);
        // NtHelper.setDouble("/robot/PIDControllers/Feedback", leftFeedback);
        // NtHelper - print desired left value
        
        if (isTargeting){
            runTargeting();
        }

        else {
            isAimed = false;
            Devices.leftMotor.setPercent(feedForward[0]);
            Devices.rightMotor.setPercent(feedForward[1]);
        }
    }

}
