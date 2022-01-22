// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.devices.Gyro;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;

public class Robot extends TimedRobot {

  NeoMotor leftMotor = new NeoMotor(3);
  NeoMotor rightMotor = new NeoMotor(4);

  Gyro gyro = new Gyro();

  @Override
  public void robotInit() {
    leftMotor.resetEncoder(0);
    rightMotor.resetEncoder(0);
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
    leftMotor.resetEncoder(0);
    rightMotor.resetEncoder(0);

  }

  @Override
  public void teleopPeriodic() {
    NtHelper.setDouble("/robot/encoderLeft", leftMotor.getDistance());
    NtHelper.setDouble("/robot/encoderRight", rightMotor.getDistance());
    NtHelper.setDouble("/robot/gyro", gyro.getAngle());
  }
}
