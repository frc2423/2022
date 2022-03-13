// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;
import frc.robot.constants.constants;
import frc.robot.subsystem.Drivetrain;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import frc.robot.util.Targeting;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import frc.robot.auto.Auto;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Relay;
import frc.robot.util.RateLimiter;


public class Robot extends TimedRobot {

  private Drivetrain drivetrain = new Drivetrain(
    constants.trackWidth, 
    constants.Ks, 
    constants.Kv, 
    Devices.gyro.getRotation()
  );
  private Intake intake = new Intake ();
  public static Shooter shooter = new Shooter ();
  private Auto auto = new Auto();
  private RateLimiter speedLimiter = new RateLimiter(0.7, 1.2);
  private RateLimiter turnLimiter = new RateLimiter(2, 3.5);


  @Override
  public void robotInit() {
    intake.zero();
    intake.stop();
    Devices.init();
    CameraServer.startAutomaticCapture();
    Devices.camLed.set(Relay.Value.kOn);
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
    NtHelper.setDouble("/robot/cargocount", 0);

  }

  @Override
  public void teleopPeriodic() {
    drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());


    //Targeting Code
    double rotationSpeed = 0;
    if (Devices.controller.getRightTriggerAxis() > 0.2){
      System.out.println("SHOOT!");
      shooter.setAuto(NtHelper.getBoolean("/robot/shooter/isAuto", true));
      shooter.shoot();

    }
    else {
      shooter.stop();
      double turnRate = turnLimiter.calculate(DriveHelper.applyDeadband(-Devices.controller.getRightX()));
      double ySpeed = speedLimiter.calculate(DriveHelper.applyDeadband(-Devices.controller.getLeftY()));
  
      double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(ySpeed, -turnRate, false);
  
      double leftSpeed = arcadeSpeeds[0] * Units.feetToMeters(constants.maxSpeedo);
      double rightSpeed = arcadeSpeeds[1] * Units.feetToMeters(constants.maxSpeedo);
  
      double[] motorValues = drivetrain.getMotorValues(new DifferentialDriveWheelSpeeds(leftSpeed, rightSpeed));
  
      Devices.leftMotor.setPercent(motorValues[0]);
      Devices.rightMotor.setPercent(motorValues[1]);
    }

  
    shooter.run();

    //runs intake
    intake.runIntake();
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
  }

  public void telemetry() {

    //uh oh
    NtHelper.setDouble ("/robot/intake/leftdistance", Devices.intakeArmMotor.getDistance());
    NtHelper.setDouble ("/robot/intake/rightdistance", Devices.intakeArmFollowerMotor.getDistance());

    NtHelper.setDouble ("/robot/intake/leftspeed", Devices.intakeArmFollowerMotor.getSpeed());
    NtHelper.setDouble ("/robot/intake/rightspeed", Devices.intakeArmMotor.getSpeed());
    NtHelper.setDouble("/robot/gyro", Devices.gyro.getAngle());  
    shooter.shooterInfo();
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