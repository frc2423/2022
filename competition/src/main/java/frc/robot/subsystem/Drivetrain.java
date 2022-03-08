package frc.robot.subsystem;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {
    private DifferentialDriveOdometry odometryFinder;
    private DifferentialDriveKinematics kinematics;
    private SimpleMotorFeedforward feedforward;
    private Field2d field = new Field2d();

    public Drivetrain(double trackWidth, double Ks, double Kv, Rotation2d initialRotation) {
        odometryFinder = new DifferentialDriveOdometry(initialRotation);
        kinematics = new DifferentialDriveKinematics(trackWidth);
        feedforward = new SimpleMotorFeedforward(Ks, Kv);
        SmartDashboard.putData("Field", field);
    }

    public void updateOdometry(Rotation2d rotation, double leftdistance, double rightdistance) {
        odometryFinder.update(rotation, leftdistance, rightdistance);
        field.setRobotPose(getPose());
    }

    public void odometryReset(Pose2d pose, Rotation2d rotation) {
        odometryFinder.resetPosition(pose, rotation);
        field.setRobotPose(getPose());
    }

    public Pose2d getPose() {
        return odometryFinder.getPoseMeters();
    }

    public double[] getMotorValues(ChassisSpeeds refChassisSpeeds) {
        var wheelSpeeds = kinematics.toWheelSpeeds(refChassisSpeeds);
        return getMotorValues(wheelSpeeds);
    }

    public double[] getMotorValues(DifferentialDriveWheelSpeeds wheelSpeeds) {
        double leftSpeed = wheelSpeeds.leftMetersPerSecond;
        double rightSpeed = wheelSpeeds.rightMetersPerSecond;
        double leftVoltage = feedforward.calculate(leftSpeed); // add acceleration at some point
        double rightVoltage = feedforward.calculate(rightSpeed);
        double leftPercent = leftVoltage / RobotController.getBatteryVoltage();
        double rightPercent = rightVoltage / RobotController.getBatteryVoltage();
        
        double[] returnArray = { leftPercent, rightPercent };
        return returnArray;
    }
}
