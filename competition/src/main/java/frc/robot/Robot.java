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
import frc.robot.constants.NtKeys;

public class Robot extends TimedRobot {

  private RateLimiter speedLimiter = new RateLimiter(0.7, 1.2);
  private RateLimiter turnLimiter = new RateLimiter(2, 3.5);

  private double slowCoefficient = .6;

  @Override
  public void robotInit() {
    Devices.init();
    Subsystems.init();
    CameraServer.startAutomaticCapture();
    NtHelper.setBoolean(NtKeys.IS_AUTO_AIM, true);
  }

  @Override
  public void robotPeriodic() {
    if (this.isDisabled()) {
      return;
    }
    Subsystems.drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());
    Subsystems.shooter.run();
    Subsystems.climber.run();
    Subsystems.belt.runStorage();
    Subsystems.climber.preventClimberFromBreaking();
    telemetry();

    Subsystems.intake.runIntake();
  }

  @Override
  public void autonomousPeriodic() {
    Subsystems.auto.run();
  }

  @Override
  public void autonomousInit() {
    resetRobot();
  }

  @Override
  public void teleopInit() {
    NtHelper.setDouble(NtKeys.CARGO_COUNT, 0);
    resetRobot();
  }

  @Override
  public void teleopPeriodic() {


    //Targeting Code
    if (Devices.controller.getRightTriggerAxis() > 0.2) {
      Subsystems.shooter.setAuto(NtHelper.getBoolean(NtKeys.IS_AUTO_AIM, false));
      Subsystems.shooter.shoot();
    } else if (Devices.controller.getLeftTriggerAxis() > 0.2) {
      Subsystems.shooter.setAuto(false);
      Subsystems.shooter.shoot(false);
    }
    else {
      Subsystems.shooter.stop();
      if (Devices.controller.getRightBumper()){
        slowCoefficient = .8;
      } else {
        slowCoefficient = .6;
      }
      double turnRate = turnLimiter.calculate(DriveHelper.applyDeadband(-Devices.controller.getRightX())) * slowCoefficient;
      double ySpeed = speedLimiter.calculate(DriveHelper.applyDeadband(-Devices.controller.getLeftY()));
  
      double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(ySpeed, -turnRate, false);

      double leftSpeed;
      double rightSpeed;

     
      leftSpeed = arcadeSpeeds[0] * Units.feetToMeters(constants.maxSpeedo);
      rightSpeed = arcadeSpeeds[1] * Units.feetToMeters(constants.maxSpeedo);
      

      double[] motorValues = Subsystems.drivetrain.getMotorValues(new DifferentialDriveWheelSpeeds(leftSpeed, rightSpeed));
  
      Devices.leftMotor.setPercent(motorValues[0]);
      Devices.rightMotor.setPercent(motorValues[1]);
    }

    if (Devices.controller.getAButton()){
      Subsystems.intake.goDown();
    } else if (Devices.controller.getYButtonPressed() && Devices.controller.getStartButton()) {
      Subsystems.intake.unCalibrate();
    } else if (Devices.controller.getYButton()) {
      Subsystems.intake.goUp();
    }

    if (Devices.climbController.getAButtonPressed()) {
      NtHelper.setString(NtKeys.CLIMBER_DESIRED_STATE, "down");
    } else if (Devices.climbController.getYButtonPressed()) {
      NtHelper.setString(NtKeys.CLIMBER_DESIRED_STATE, "up");
    }
  }

  @Override
  public void disabledPeriodic() {
    // resetRobot();
  }

  public void resetRobot() {
    Devices.leftMotor.resetEncoder(0);
    Devices.rightMotor.resetEncoder(0);
    Devices.gyro.reset();
    Subsystems.drivetrain.odometryReset(new Pose2d(), Devices.gyro.getRotation());
    Targeting.init();
    Subsystems.climber.calibrate();
    Subsystems.auto.setState("init");
    Subsystems.intake.unCalibrate();
  }

  public void telemetry() {
    NtHelper.setDouble (NtKeys.LEFT_INTAKE_POSITION, Devices.intakeArmMotor.getDistance());
    NtHelper.setDouble (NtKeys.RIGHT_INTAKE_POSITION, Devices.intakeArmFollowerMotor.getDistance());
    NtHelper.setDouble(NtKeys.GYRO_ANGLE, Devices.gyro.getAngle());  

    NtHelper.setString(NtKeys.SVG_ALLIANCE_COLOR, DriverStation.getAlliance().toString());
    NtHelper.setDouble(NtKeys.SVG_ROTATIONS_PER_SECOND, Devices.leftMotor.getSpeed()/(2 * Math.PI * Units.inchesToMeters(3)));
    NtHelper.setDouble (NtKeys.SVG_INTAKE_POSITION, Devices.intakeArmMotor.getDistance());


    Subsystems.shooter.shooterInfo();
    Subsystems.climber.climberInfo();
  }
}