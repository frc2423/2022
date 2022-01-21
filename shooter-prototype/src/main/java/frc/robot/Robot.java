// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;

public class Robot extends TimedRobot {

  NeoMotor topMotor = new NeoMotor(3);
  NeoMotor bottomMotor = new NeoMotor(7);

  @Override
  public void robotInit() {
    NtHelper.setDouble("/topSpeed", 0);
    NtHelper.setDouble("/bottomSpeed", 0);
    NtHelper.setBoolean("/enabled", false);
    topMotor.setInverted(true);
  }

  @Override
  public void robotPeriodic() {
    // get user input for motor speeds
    double topSpeed = NtHelper.getDouble("/topSpeed", 0);
    double bottomSpeed = NtHelper.getDouble("/bottomSpeed", 0);
    boolean enabled = NtHelper.getBoolean("/enabled", false);

    // set motor speeds here
    if (enabled) {
      topMotor.setPercent(topSpeed);
      bottomMotor.setPercent(bottomSpeed);
    } else {
      topMotor.setPercent(0);
      bottomMotor.setPercent(0);
    }
  }
}
