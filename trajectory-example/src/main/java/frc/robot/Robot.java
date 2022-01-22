// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;

import java.util.List;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;

public class Robot extends TimedRobot {

  // The gains calculated by the system id tool
  final double kS = 1;
  final double kV = 1;
  final double kA = 1;

  //the message we dont need
  final double maxaccelaration = 2;
  final double maxvelocity = 2;

  // odometry
  DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));

  // kinematics
  DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(0.43815);

  @Override
  public void robotInit() {
    var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
      new SimpleMotorFeedforward(
          kS,
          kV,
          kA
      ),
      kinematics,
      10
    );
   
    // Create config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(
        maxvelocity,
        maxaccelaration)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(kinematics)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    // An example trajectory to follow. All units in meters.
    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        // Pass through these two interior waypoints, making an 's' curve path
        List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(3, 0, new Rotation2d(0)),
        // Pass config
        config);
  }

  @Override
  public void robotPeriodic() {

  }

  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {
    // Calculate odometry and get current pose

    // get the desired drivetrain state (pose, velocity, acceleteration, etc) from
    // the trajectory
    // based on how much time has passed

    // calculate desired drivetrain speeds from the ramsete controller based on
    // current pose and desired state

    // calculate motor voltages based on desired drivetrain speeds and gains
    // calculated by system id tool

    // calculate individual motor speeds based desired drivetrain velocity and
    // acceleration using kinematics

    // calculate desired acceleration for each motor

    // calculate
  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {

  }
}
