// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;
import frc.robot.constants.constants;
import edu.wpi.first.wpilibj.RobotController;


public class Robot extends TimedRobot {
  public SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(constants.Ks, constants.Kv);
  @Override
  public void robotInit() {
    Devices.init();
  }

  @Override
  public void robotPeriodic() {
  }

  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {
    double turnRate = DriveHelper.applyDeadband(-Devices.controller.getLeftX());
    double ySpeed = DriveHelper.applyDeadband(-Devices.controller.getLeftY());

    double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(ySpeed *.3, -turnRate *.3, false);

    double leftSpeed = arcadeSpeeds[0] * edu.wpi.first.math.util.Units.feetToMeters(constants.maxSpeedo);
    double rightSpeed = arcadeSpeeds[1] * edu.wpi.first.math.util.Units.feetToMeters(constants.maxSpeedo);

    double leftVoltage = feedforward.calculate(leftSpeed);
    double rightVoltage = feedforward.calculate(rightSpeed);

    double leftPercent = leftVoltage * RobotController.getBatteryVoltage();
    double rightPercent = rightVoltage * RobotController.getBatteryVoltage();

    Devices.leftMotor.setPercent(leftPercent);
    Devices.rightMotor.setPercent(rightPercent);
  }

}
