package frc.robot.subsystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Devices;

public class Drivetrain {
    private  DifferentialDriveOdometry odometryFinder;

    public Drivetrain() {
        odometryFinder = new DifferentialDriveOdometry(Devices.gyro.getRotation());

    }

    public void updateOdometry(){
        odometryFinder.update(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());
    }

    public void odometryReset(Pose2d pose){
        Devices.leftMotor.resetEncoder(0);
        Devices.rightMotor.resetEncoder(0);
        Devices.gyro.reset();
        odometryFinder.resetPosition(pose, Devices.gyro.getRotation());

    }

    public Pose2d getPose() {
        return odometryFinder.getPoseMeters();
    }
}
