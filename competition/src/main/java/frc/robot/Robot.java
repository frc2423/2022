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
import edu.wpi.first.cameraserver.CameraServer;
import frc.robot.util.RateLimiter;
import edu.wpi.first.wpilibj.DriverStation;

public class Robot extends TimedRobot {

  private RateLimiter speedLimiter = new RateLimiter(0.7, 1.2);
  private RateLimiter turnLimiter = new RateLimiter(2, 3.5);

  @Override
  public void robotInit() {
    Devices.init();
    Subsystems.init();
    CameraServer.startAutomaticCapture();
    NtHelper.setBoolean("/robot/shooter/isAuto", true);
  }

  @Override
  public void robotPeriodic() {
    Subsystems.drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());
    Subsystems.shooter.run();
    Subsystems.climber.run();
    Subsystems.intake.runIntake();
    telemetry();
  }

  @Override
  public void autonomousPeriodic() {
    Subsystems.auto.run();
  }

  @Override
  public void teleopInit() {
    NtHelper.setDouble("/robot/cargoCount", 0);
  }

  @Override
  public void teleopPeriodic() {

    //Targeting Code
    if (Devices.controller.getRightTriggerAxis() > 0.2){
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

    if (Devices.climbController.getYButtonPressed()) {
      NtHelper.setString("/robot/climber/desiredState", "up");
    } else if (Devices.climbController.getAButtonPressed()) {
      NtHelper.setString("/robot/climber/desiredState", "down");
    } else if (Devices.climbController.getRightBumperPressed()){
      NtHelper.setString("/robot/climber/desiredState", "climb");
    }
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
    Subsystems.climber.calibrate();
    Subsystems.auto.setState("init");
  }

  public void telemetry() {
    NtHelper.setDouble ("/robot/intake/leftdistance", Devices.intakeArmMotor.getDistance());
    NtHelper.setDouble ("/robot/intake/rightdistance", Devices.intakeArmFollowerMotor.getDistance());
    NtHelper.setDouble ("/robot/intake/leftspeed", Devices.intakeArmFollowerMotor.getSpeed());
    NtHelper.setDouble ("/robot/intake/rightspeed", Devices.intakeArmMotor.getSpeed());
    NtHelper.setDouble("/robot/gyro", Devices.gyro.getAngle());  

    NtHelper.setString("/robot/svg/allianceColor", DriverStation.getAlliance().toString());
    NtHelper.setDouble("/robot/svg/ballCount", Subsystems.intake.getBallCount());
    NtHelper.setDouble("/robot/svg/rotationsPerSecond", Devices.leftMotor.getSpeed()/(2 * Math.PI * Units.inchesToMeters(3)));
    NtHelper.setDouble ("/robot/svg/robotArmSetpoint", Devices.intakeArmMotor.getDistance());


    Subsystems.shooter.shooterInfo();
    Subsystems.climber.climberInfo();
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