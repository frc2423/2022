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
    Devices.init(isSimulation());
    Subsystems.init();
    CameraServer.startAutomaticCapture();
    NtHelper.setBoolean(NtKeys.IS_AUTO_AIM, false);
  }

  @Override
  public void robotPeriodic() {
    if (this.isDisabled()) {
      return;
    }

    Subsystems.drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(),
        Devices.rightMotor.getDistance());
    Subsystems.climber.run();
    Subsystems.belt.runStorage();
    Subsystems.intake.runIntake();
    Subsystems.drive.run();
    Subsystems.counter.run();


    telemetry();
  }

  @Override
  public void simulationPeriodic() {
    Subsystems.drivetrainSim.simulate();
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
  public void testPeriodic() {
    
    if (Devices.notAdriansController.getXButton()) {
      Subsystems.shooter.setHoodfPosition(-10);
    } else if (!Devices.notAdriansController.getXButton()) {
      Subsystems.shooter.setHoodfPosition(0.000000);
    }
    
  }

  @Override
  public void teleopPeriodic() {
    Subsystems.cargoRejector.run();

    // Targeting Code
    if (Devices.driverController.getRightTriggerAxis() > 0.2) {
      Subsystems.shooter.shoot();
    } else if (Devices.driverController.getLeftTriggerAxis() > 0.2) {
      Subsystems.shooter.shoot(false);
    } else {
      Subsystems.shooter.stop();
      if (Devices.driverController.getRightBumper()) {
        slowCoefficient = .8;
      } else {
        slowCoefficient = .6;
      }
      double turnRate = turnLimiter.calculate(DriveHelper.applyDeadband(-Devices.driverController.getRightX()))
          * slowCoefficient;
      double ySpeed = speedLimiter.calculate(DriveHelper.applyDeadband(-Devices.driverController.getLeftY()));

      double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(ySpeed, -turnRate, false);

      double leftSpeed;
      double rightSpeed;

      leftSpeed = arcadeSpeeds[0] * Units.feetToMeters(constants.maxSpeedo);
      rightSpeed = arcadeSpeeds[1] * Units.feetToMeters(constants.maxSpeedo);

      Subsystems.drive.setSpeeds(leftSpeed, rightSpeed);
    }

    if (Devices.driverController.getAButton()) {
      Subsystems.intake.goDown();
    } else if (Devices.driverController.getYButtonPressed() && Devices.driverController.getStartButton()) {
      Subsystems.intake.unCalibrate();
    } else if (Devices.driverController.getYButton()) {
      Subsystems.intake.goUp();
    }

    if (Devices.operatorController.getAButtonPressed()) {
      NtHelper.setString(NtKeys.CLIMBER_DESIRED_STATE, "down");
    } else if (Devices.operatorController.getYButtonPressed()) {
      NtHelper.setString(NtKeys.CLIMBER_DESIRED_STATE, "up");
    } else if (Devices.operatorController.getStartButton() && Devices.operatorController.getBackButton()) {
      NtHelper.setString(NtKeys.CLIMBER_DESIRED_STATE, "manual");
    }
  }

  public void resetRobot() {
    Devices.leftMotor.resetEncoder(0);
    Devices.rightMotor.resetEncoder(0);
    Devices.gyro.reset();
    Subsystems.drivetrain.odometryReset(new Pose2d(), Devices.gyro.getRotation());
    Subsystems.drivetrainSim.setPose(new Pose2d(), Devices.gyro.getRotation());
    Targeting.init();
    Subsystems.climber.calibrate();
    Subsystems.auto.restart();
    Subsystems.intake.unCalibrate();
    Subsystems.counter.setBallCount(0);
  }

  public void telemetry() {
    NtHelper.setDouble(NtKeys.LEFT_INTAKE_POSITION, Devices.intakeArmMotor.getDistance());
    NtHelper.setDouble(NtKeys.RIGHT_INTAKE_POSITION, Devices.intakeArmFollowerMotor.getDistance());
    NtHelper.setDouble(NtKeys.GYRO_ANGLE, Devices.gyro.getAngle());

    NtHelper.setString(NtKeys.SVG_ALLIANCE_COLOR, DriverStation.getAlliance().toString());
    NtHelper.setDouble(NtKeys.SVG_ROTATIONS_PER_SECOND,
        Devices.leftMotor.getSpeed() / (2 * Math.PI * Units.inchesToMeters(3)));
    NtHelper.setDouble(NtKeys.SVG_INTAKE_POSITION, Devices.intakeArmMotor.getDistance());

    NtHelper.setBoolean("/robot/intake/isLeftPressed", Subsystems.intake.isLeftPressed());
    NtHelper.setBoolean("/robot/intake/isRightPressed", Subsystems.intake.isRightPressed());

    NtHelper.setBoolean("/robot/intake/shooterBeamBreak", Devices.shooterBeamBrake.get());
    NtHelper.setBoolean("/robot/intake/intakeBeamBreak", Devices.intakeBeamBrake.get());
    NtHelper.setDouble("/robot/intake/BallCount", Subsystems.counter.getBallCount());

    NtHelper.setString("/robot/intake/rejecterState", Subsystems.cargoRejector.getState());
    NtHelper.setBoolean("/robot/intake/colorSeesRed", Devices.colourSensor.isColor("red"));
    NtHelper.setDouble("/robot/intake/ballNum", Subsystems.counter.getBallCount());


    Subsystems.shooter.shooterInfo();
    Subsystems.climber.climberInfo();
  }
}