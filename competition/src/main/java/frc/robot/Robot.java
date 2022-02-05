// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;

public class Robot extends TimedRobot {

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
    double leftSpeed = arcadeSpeeds[0];
    double rightSpeed = arcadeSpeeds[1];
    Devices.leftMotor.setPercent(leftSpeed);
    Devices.rightMotor.setPercent(rightSpeed);
  }

}
