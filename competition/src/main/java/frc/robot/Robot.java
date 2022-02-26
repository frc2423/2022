// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;
import frc.robot.auto.shootTwoTaxi;
import frc.robot.constants.constants;
import frc.robot.subsystem.Drivetrain;
import frc.robot.subsystem.Intake;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.util.Targeting;
import java.util.List;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;

public class Robot extends TimedRobot {

  private Trajectory m_trajectory;
  private Drivetrain drivetrain = new Drivetrain(
    constants.trackWidth, 
    constants.Ks, 
    constants.Kv, 
    Devices.gyro.getRotation()
  );
  private Field2d m_field;
  private shootTwoTaxi auto = new shootTwoTaxi();
  private Intake intake = new Intake ();

  @Override
  public void robotInit() {
    intake.zero();
    intake.stop();
  }

  @Override
  public void robotPeriodic() {
    telemetry();
  }

  public void autonomousInit() {
    intake.zero();
    intake.stop();
  }

  @Override
  public void autonomousPeriodic() {
    auto.run();
  }

  @Override
  public void teleopInit() {
    intake.zero();
    intake.stop();
    Devices.leftMotor.resetEncoder(0);
    Devices.rightMotor.resetEncoder(0);
    Devices.gyro.reset();
    drivetrain.odometryReset(new Pose2d(), Devices.gyro.getRotation());
    Targeting.init();

  }

  @Override
  public void teleopPeriodic() {
    double turnRate = DriveHelper.applyDeadband(-Devices.controller.getLeftX());
    double ySpeed = DriveHelper.applyDeadband(-Devices.controller.getLeftY());

    double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(ySpeed, -turnRate, false);

    double leftSpeed = arcadeSpeeds[0] * Units.feetToMeters(constants.maxSpeedo);
    double rightSpeed = arcadeSpeeds[1] * Units.feetToMeters(constants.maxSpeedo);

    double[] motorValues = drivetrain.getMotorValues(new DifferentialDriveWheelSpeeds(leftSpeed, rightSpeed));

    Devices.leftMotor.setPercent(motorValues[0]);
    Devices.rightMotor.setPercent(motorValues[1]);

    NtHelper.setDouble("/robot/gyro", Devices.gyro.getAngle());
    drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());
    m_field.setRobotPose(drivetrain.getPose());


    //Targeting Code
    // double rotationSpeed = 0;
    // if (Devices.controller.getAButton()){
    //   rotationSpeed = Targeting.calculate();
    // }
    // double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
    // double leftSpeed = arcadeSpeeds[0];
    // double rightSpeed = arcadeSpeeds[1];
    // Devices.leftMotor.setPercent(leftSpeed);
    // Devices.rightMotor.setPercent(rightSpeed);

    //runs intake
    intake.runIntake();

    telemetry();

  }


  @Override
  public void disabledPeriodic() {
    intake.zero();
    intake.stop();
    Devices.leftMotor.resetEncoder(0);
    Devices.rightMotor.resetEncoder(0);
    Devices.gyro.reset();
    drivetrain.odometryReset(new Pose2d(), Devices.gyro.getRotation());
    Targeting.init();
    telemetry();
  }

  public void telemetry() {
    NtHelper.setDouble ("/robot/intake/leftdistance", Devices.intakeArmMotor.getDistance());
    NtHelper.setDouble ("/robot/intake/rightdistance", Devices.intakeArmFollowerMotor.getDistance());

    NtHelper.setDouble ("/robot/intake/leftspeed", Devices.intakeArmFollowerMotor.getSpeed());
    NtHelper.setDouble ("/robot/intake/rightspeed", Devices.intakeArmMotor.getSpeed());
  }

  public void telementryAuto(){
    // NtHelper.setDouble("/robot/Desired/x", desiredPose.poseMeters.getX());
    // NtHelper.setDouble("/robot/Desired/y", desiredPose.poseMeters.getY());
    // NtHelper.setDouble("/robot/Desired/angle", desiredPose.poseMeters.getRotation().getDegrees());
    // NtHelper.setDouble("/robot/Current/x", drivetrain.getPose().getX());
    // NtHelper.setDouble("/robot/Current/y", drivetrain.getPose().getY());
    // NtHelper.setDouble("/robot/Current/angle", drivetrain.getPose().getRotation().getDegrees()); 
  }
}