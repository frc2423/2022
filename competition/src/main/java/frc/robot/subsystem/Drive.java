package frc.robot.subsystem;

import frc.robot.Subsystems;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import frc.robot.Devices;

public class Drive {

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
        double[] motorValues = Subsystems.drivetrain.getMotorValues(wheelSpeeds);
        Devices.leftMotor.setPercent(motorValues[0]);
        Devices.rightMotor.setPercent(motorValues[1]);
    }
}
