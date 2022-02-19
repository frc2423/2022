// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;
import frc.robot.auto.shootTwoTaxi;
import frc.robot.constants.constants;
import frc.robot.subsystem.Drivetrain;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.util.Targeting;
import java.util.List;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;

public class Robot extends TimedRobot {

  private Trajectory m_trajectory;
  private Timer timer;
  private final RamseteController m_ramseteController = new RamseteController();
  private Drivetrain drivetrain = new Drivetrain(
    constants.trackWidth, 
    constants.Ks, 
    constants.Kv, 
    Devices.gyro.getRotation()
  );
  private Field2d m_field;
  private shootTwoTaxi auto = new shootTwoTaxi();

  @Override
  public void robotInit() {
    Devices.init();

    Trajectory line = TrajectoryGenerator.generateTrajectory(
      //the line is going along the x axis
      new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
      List.of(),
      new Pose2d(Units.feetToMeters(10), 0, Rotation2d.fromDegrees(0)),
      new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );

    Trajectory parabola = TrajectoryGenerator.generateTrajectory(
      new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
      List.of(new Translation2d(Units.feetToMeters(6),Units.feetToMeters(4))),
      new Pose2d(Units.feetToMeters(16), Units.feetToMeters(1), Rotation2d.fromDegrees(0)),
      new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );

    Trajectory curvy = TrajectoryGenerator.generateTrajectory(
      new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
      List.of(
        new Translation2d(Units.feetToMeters(6),Units.feetToMeters(4)),
        new Translation2d(Units.feetToMeters(16),Units.feetToMeters(1)),
        new Translation2d(Units.feetToMeters(27),Units.feetToMeters(1)),
        new Translation2d(Units.feetToMeters(31),Units.feetToMeters(0))
      ),
      new Pose2d(Units.feetToMeters(32), Units.feetToMeters(10), Rotation2d.fromDegrees(90)),
      new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );


    m_trajectory = curvy;
    // Create and push Field2d to SmartDashboard.
    m_field = new Field2d();
    SmartDashboard.putData(m_field);

    // Push the trajectory to Field2d.
    m_field.getObject("traj").setTrajectory(m_trajectory);
      

  }

  @Override
  public void robotPeriodic() {
  }

  public void autonomousInit() {
    // timer = new Timer();
    // timer.start();
    // Devices.leftMotor.resetEncoder(0);
    // Devices.rightMotor.resetEncoder(0);
    // Devices.gyro.reset();
    // drivetrain.odometryReset(new Pose2d(0,0,Rotation2d.fromDegrees(0)), Devices.gyro.getRotation());
    // m_field.setRobotPose(drivetrain.getPose());

  }

  @Override
  public void autonomousPeriodic() {
    auto.run();
    // var currTime = timer.get();
    // var desiredPose = m_trajectory.sample(currTime);
    // drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());
    // m_field.setRobotPose(drivetrain.getPose());
    // ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);
    // double[] motorValues = drivetrain.getMotorValues(refChassisSpeeds);
    // Devices.leftMotor.setPercent(motorValues[0]);
    // Devices.rightMotor.setPercent(motorValues[1]);
    
    // System.out.println("Desired" + desiredPose + " Current:" + drivetrain.getPose());
    // NtHelper.setDouble("/robot/Desired/x", desiredPose.poseMeters.getX());
    // NtHelper.setDouble("/robot/Desired/y", desiredPose.poseMeters.getY());
    // NtHelper.setDouble("/robot/Desired/angle", desiredPose.poseMeters.getRotation().getDegrees());
    // NtHelper.setDouble("/robot/Current/x", drivetrain.getPose().getX());
    // NtHelper.setDouble("/robot/Current/y", drivetrain.getPose().getY());
    // NtHelper.setDouble("/robot/Current/angle", drivetrain.getPose().getRotation().getDegrees()); 

  }


  


  @Override
  public void teleopInit() {
    Devices.leftMotor.resetEncoder(0);
        Devices.rightMotor.resetEncoder(0);
        Devices.gyro.reset();
    drivetrain.odometryReset(new Pose2d(), Devices.gyro.getRotation());
    Targeting.init();

  }

  @Override
  public void teleopPeriodic() {
    // double turnRate = DriveHelper.applyDeadband(-Devices.controller.getLeftX());
    // double ySpeed = DriveHelper.applyDeadband(-Devices.controller.getLeftY());

    // double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(ySpeed, -turnRate, false);

    // double leftSpeed = arcadeSpeeds[0] * Units.feetToMeters(constants.maxSpeedo);
    // double rightSpeed = arcadeSpeeds[1] * Units.feetToMeters(constants.maxSpeedo);

    // double[] motorValues = drivetrain.getMotorValues(new DifferentialDriveWheelSpeeds(leftSpeed, rightSpeed));

    // Devices.leftMotor.setPercent(motorValues[0]);
    // Devices.rightMotor.setPercent(motorValues[1]);

    // NtHelper.setDouble("/robot/gyro", Devices.gyro.getAngle());
    // drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());
    // m_field.setRobotPose(drivetrain.getPose());

    double rotationSpeed = 0;

    if (Devices.controller.getAButton()){
      rotationSpeed = Targeting.calculate();
    }

    double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
    double leftSpeed = arcadeSpeeds[0];
    double rightSpeed = arcadeSpeeds[1];
    Devices.leftMotor.setPercent(leftSpeed);
    Devices.rightMotor.setPercent(rightSpeed);

    NtHelper.setDouble("/robot/aiming/leftSpeed", leftSpeed); 
    NtHelper.setDouble("/robot/aiming/rightSpeed", rightSpeed);

    NtHelper.setBoolean("/robot/aiming/Button", Devices.controller.getAButton()); 

  }




}