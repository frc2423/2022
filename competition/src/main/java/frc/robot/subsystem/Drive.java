package frc.robot.subsystem;

import frc.robot.Subsystems;
import frc.robot.util.NtHelper;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import frc.robot.Devices;
import frc.robot.Robot;

public class Drive {

    private PIDController leftPid = new PIDController(1, 0, 0);
    private PIDController rightPid = new PIDController(1, 0, 0);

    private DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds();

    public void setSpeeds(double leftMetersPerSecond, double rightMetersPerSecond) {
        wheelSpeeds = new DifferentialDriveWheelSpeeds(leftMetersPerSecond, rightMetersPerSecond);
    }

    public void setSpeeds(DifferentialDriveWheelSpeeds wheelSpeeds) {
        this.wheelSpeeds = wheelSpeeds;
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return wheelSpeeds;
    }

    public void run() {
        double[] feedForward = Subsystems.drivetrain.getMotorValues(wheelSpeeds);

        double leftFeedback = leftPid.calculate(Devices.leftMotor.getSpeed(), wheelSpeeds.leftMetersPerSecond);
        double rightFeedback = rightPid.calculate(Devices.rightMotor.getSpeed(), wheelSpeeds.rightMetersPerSecond);

        // NtHelper - print out actual left value
        NtHelper.setDouble("/robot/PIDControllers/Actual", Devices.leftMotor.getSpeed());
        NtHelper.setDouble("/robot/PIDControllers/Desired", wheelSpeeds.leftMetersPerSecond);
        // NtHelper - print desired left value
         
        Devices.leftMotor.setPercent(feedForward[0] + leftFeedback);
        Devices.rightMotor.setPercent(feedForward[1] + rightFeedback);
    }

}
