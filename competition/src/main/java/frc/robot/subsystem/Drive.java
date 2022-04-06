package frc.robot.subsystem;

import frc.robot.Subsystems;
import frc.robot.util.NtHelper;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import frc.robot.Devices;

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

        Devices.leftMotor.setSpeed(feedForward[0] );
        Devices.rightMotor.setSpeed(feedForward[1]);
    }

}
