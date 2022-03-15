// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;
import frc.robot.constants.constants;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import frc.robot.util.Targeting;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import frc.robot.auto.Auto;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Relay;
import frc.robot.util.RateLimiter;


public class Robot extends TimedRobot {

  private Auto auto = new Auto();
  private RateLimiter speedLimiter = new RateLimiter(0.7, 1.2);
  private RateLimiter turnLimiter = new RateLimiter(2, 3.5);


  @Override
  public void robotInit() {
    Devices.init();
    Subsystems.init();
    CameraServer.startAutomaticCapture();
    Devices.camLed.set(Relay.Value.kOn);
  }

  @Override
  public void robotPeriodic() {
    telemetry();
  }

  public void autonomousInit() {
    Subsystems.intake.zero();
    Subsystems.intake.stop();
  }

  @Override
  public void autonomousPeriodic() {
    auto.run();
  }

  @Override
  public void teleopInit() {
    Subsystems.intake.zero();
    Subsystems.intake.stop();
    Devices.leftMotor.resetEncoder(0);
    Devices.rightMotor.resetEncoder(0);
    Devices.gyro.reset();
    Subsystems.drivetrain.odometryReset(new Pose2d(), Devices.gyro.getRotation());
    Targeting.init();
    NtHelper.setDouble("/robot/cargocount", 0);

  }

  @Override
  public void teleopPeriodic() {
    Subsystems.drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());


    //Targeting Code
    double rotationSpeed = 0;
    if (Devices.controller.getRightTriggerAxis() > 0.2){
      System.out.println("SHOOT!");
      Subsystems.shooter.setAuto(NtHelper.getBoolean("/robot/shooter/isAuto", true));
      Subsystems.shooter.shoot();

    }
    else {
      Subsystems.shooter.stop();
      double turnRate = turnLimiter.calculate(DriveHelper.applyDeadband(-Devices.controller.getRightX()));
      double ySpeed = speedLimiter.calculate(DriveHelper.applyDeadband(-Devices.controller.getLeftY()));
  
      double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(ySpeed, -turnRate, false);
  
      double leftSpeed = arcadeSpeeds[0] * Units.feetToMeters(constants.maxSpeedo);
      double rightSpeed = arcadeSpeeds[1] * Units.feetToMeters(constants.maxSpeedo);
  
      double[] motorValues = Subsystems.drivetrain.getMotorValues(new DifferentialDriveWheelSpeeds(leftSpeed, rightSpeed));
  
      Devices.leftMotor.setPercent(motorValues[0]);
      Devices.rightMotor.setPercent(motorValues[1]);
    }

  
    Subsystems.shooter.run();
    Subsystems.intake.runIntake();
  }


  @Override
  public void disabledPeriodic() {
    Subsystems.intake.zero();
    Subsystems.intake.stop();
    Devices.leftMotor.resetEncoder(0);
    Devices.rightMotor.resetEncoder(0);
    Devices.gyro.reset();
    Subsystems.drivetrain.odometryReset(new Pose2d(), Devices.gyro.getRotation());
    Targeting.init();
  }

  public void telemetry() {

    //uh oh
    NtHelper.setDouble ("/robot/intake/leftdistance", Devices.intakeArmMotor.getDistance());
    NtHelper.setDouble ("/robot/intake/rightdistance", Devices.intakeArmFollowerMotor.getDistance());

    NtHelper.setDouble ("/robot/intake/leftspeed", Devices.intakeArmFollowerMotor.getSpeed());
    NtHelper.setDouble ("/robot/intake/rightspeed", Devices.intakeArmMotor.getSpeed());
    NtHelper.setDouble("/robot/gyro", Devices.gyro.getAngle());  
    Subsystems.shooter.shooterInfo();
  }

  public void telementryAuto(){
    // NtHelper.setDouble("/robot/Desired/x", desiredPose.poseMeters.getX());
    // NtHelper.setDouble("/robot/Desired/y", desiredPose.poseMeters.getY());
    // NtHelper.setDouble("/robot/Desired/angle", desiredPose.poseMeters.getRotation().getDegrees());
    // NtHelper.setDouble("/robot/Current/x", Subsystems.drivetrain.getPose().getX());
    // NtHelper.setDouble("/robot/Current/y", Subsystems.drivetrain.getPose().getY());
    // NtHelper.setDouble("/robot/Current/angle", Subsystems.drivetrain.getPose().getRotation().getDegrees()); 
  }
}