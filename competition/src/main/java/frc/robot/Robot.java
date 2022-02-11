// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;
import frc.robot.constants.constants;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import java.util.List;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;


public class Robot extends TimedRobot {

  public SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(constants.Ks, constants.Kv);
  private Trajectory m_trajectory;
  private Timer timer;
  private final RamseteController m_ramseteController = new RamseteController();
  private DifferentialDriveKinematics kinematics;


  @Override
  public void robotInit() {
    kinematics = new DifferentialDriveKinematics(constants.trackWidth);
    Devices.init();

    Trajectory line = TrajectoryGenerator.generateTrajectory(
      //the line is going along the x axis
      new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
      List.of(),
      new Pose2d(Units.feetToMeters(10), 0, Rotation2d.fromDegrees(0)),
      new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );

    Trajectory parabola = TrajectoryGenerator.generateTrajectory(
      new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
      List.of(new Translation2d(Units.feetToMeters(6),Units.feetToMeters(4))),
      new Pose2d(Units.feetToMeters(16), Units.feetToMeters(1), Rotation2d.fromDegrees(0)),
      new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );

    Trajectory curvy = TrajectoryGenerator.generateTrajectory(
      new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
      List.of(
        new Translation2d(Units.feetToMeters(6),Units.feetToMeters(4)),
        new Translation2d(Units.feetToMeters(16),Units.feetToMeters(1)),
        new Translation2d(Units.feetToMeters(27),Units.feetToMeters(1)),
        new Translation2d(Units.feetToMeters(31),Units.feetToMeters(0))
      ),
      new Pose2d(Units.feetToMeters(32), Units.feetToMeters(10), Rotation2d.fromDegrees(90)),
      new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );


    m_trajectory = curvy;

  }

  @Override
  public void robotPeriodic() {
  }

  public void autonomousInit() {
    timer = new Timer();
    timer.start();
  }

  @Override
  public void autonomousPeriodic() {
    var currTime = timer.get();
    var desiredPose = m_trajectory.sample(currTime);

    ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(desiredPose.poseMeters, desiredPose);
    var wheelSpeeds = kinematics.toWheelSpeeds(refChassisSpeeds);


    //double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(refChassisSpeeds.vxMetersPerSecond, -refChassisSpeeds.omegaRadiansPerSecond, false);

    double leftSpeed = wheelSpeeds.leftMetersPerSecond;
    double rightSpeed = wheelSpeeds.rightMetersPerSecond;
    double leftVoltage = feedforward.calculate(leftSpeed); //add acceleration at some point
    double rightVoltage = feedforward.calculate(rightSpeed);
    double leftPercent = leftVoltage / RobotController.getBatteryVoltage();
    double rightPercent = rightVoltage / RobotController.getBatteryVoltage();
    Devices.leftMotor.setPercent(leftPercent);
    Devices.rightMotor.setPercent(rightPercent);
  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {
    double turnRate = DriveHelper.applyDeadband(-Devices.controller.getLeftX());
    double ySpeed = DriveHelper.applyDeadband(-Devices.controller.getLeftY());

    double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(ySpeed, -turnRate, false);

    double leftSpeed = arcadeSpeeds[0] * edu.wpi.first.math.util.Units.feetToMeters(constants.maxSpeedo);
    double rightSpeed = arcadeSpeeds[1] * edu.wpi.first.math.util.Units.feetToMeters(constants.maxSpeedo);

    double leftVoltage = feedforward.calculate(leftSpeed); //add acceleration at some point
    double rightVoltage = feedforward.calculate(rightSpeed);

    double leftPercent = leftVoltage / RobotController.getBatteryVoltage();
    double rightPercent = rightVoltage / RobotController.getBatteryVoltage();

    Devices.leftMotor.setPercent(leftPercent);
    Devices.rightMotor.setPercent(rightPercent);
  }

}
