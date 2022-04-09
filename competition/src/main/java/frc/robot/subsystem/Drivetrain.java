package frc.robot.subsystem;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.RobotController;

import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N2;

import edu.wpi.first.math.controller.LinearPlantInversionFeedforward;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.system.LinearSystem;

import frc.robot.constants.constants;

public class Drivetrain {

    private DifferentialDriveOdometry odometryFinder;
    private DifferentialDriveKinematics kinematics;
    private Field2d field = new Field2d();

    private final LinearSystem<N2, N2, N2> m_drivetrainSystem = LinearSystemId.identifyDrivetrainSystem(constants.Kv,
            constants.Ka, constants.KvAngular, constants.KaAngular);
    private final LinearPlantInversionFeedforward<N2, N2, N2> feedforward = new LinearPlantInversionFeedforward<>(
            m_drivetrainSystem, .05);

    public Drivetrain(double trackWidth, double Ks, double Kv, Rotation2d initialRotation) {
        odometryFinder = new DifferentialDriveOdometry(initialRotation);
        kinematics = new DifferentialDriveKinematics(trackWidth);
        // feedforward = new SimpleMotorFeedforward(Ks, Kv);
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

    public DifferentialDriveWheelSpeeds getWheelSpeeds(ChassisSpeeds refChassisSpeeds) {
        return kinematics.toWheelSpeeds(refChassisSpeeds);
    }

    public double[] getMotorValues(ChassisSpeeds refChassisSpeeds) {
        var wheelSpeeds = kinematics.toWheelSpeeds(refChassisSpeeds);
        return getMotorValues(wheelSpeeds);
    }

    public double[] getMotorValues(DifferentialDriveWheelSpeeds wheelSpeeds) {
        Matrix<N2, N1> voltages = feedforward.calculate(
                new MatBuilder<>(
                        Nat.N2(),
                        Nat.N1())
                                .fill(
                                        wheelSpeeds.leftMetersPerSecond,
                                        wheelSpeeds.rightMetersPerSecond));
        double leftVoltage = voltages.get(0, 0);
        double rightVoltage = voltages.get(1, 0);

        double leftPercent = leftVoltage / RobotController.getBatteryVoltage();
        double rightPercent = rightVoltage / RobotController.getBatteryVoltage();

        double[] returnArray = { leftPercent, rightPercent };
        return returnArray;
    }

    public void setTrajectory(String name, Trajectory trajectory) {
        field.getObject(name).setTrajectory(trajectory);
    }
}
