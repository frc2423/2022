// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;
import frc.robot.constants.constants;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;


public class Robot extends TimedRobot {

  public SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(constants.Ks, constants.Kv);
  private Trajectory m_trajectory;
  private Timer timer;
  private final RamseteController m_ramseteController = new RamseteController();


  @Override
  public void robotInit() {

    Devices.init();

    m_trajectory =
        TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
            List.of(new Translation2d(1, 0),new Translation2d(2, 0),new Translation2d(3, 0), new Translation2d(4, 0),new Translation2d(5, 0),new Translation2d(6, 0),new Translation2d(7, 0)),
            new Pose2d(7, 0, Rotation2d.fromDegrees(0)),
            new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel)));



  }

  @Override
  public void robotPeriodic() {
  }

  public void autonomousInit() {

    timer =new Timer();
    timer.start();
  }

  @Override
  public void autonomousPeriodic() {

    var currTime = timer.get();
    var desiredPose = m_trajectory.sample(timer.get());

    var refChassisSpeeds = m_ramseteController.calculate(desiredPose.poseMeters, desiredPose);
    double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(refChassisSpeeds.vxMetersPerSecond, -refChassisSpeeds.omegaRadiansPerSecond, false);

    double leftSpeed = arcadeSpeeds[0] * edu.wpi.first.math.util.Units.feetToMeters(constants.maxSpeedo);
    double rightSpeed = arcadeSpeeds[1] * edu.wpi.first.math.util.Units.feetToMeters(constants.maxSpeedo);

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
