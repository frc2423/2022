// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.devices.NeoMotor;
import frc.robot.util.DriveHelper;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

public class Robot extends TimedRobot {

  // Constants such as camera and target height stored. Change per robot and goal!
  final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(24);
  final double TARGET_HEIGHT_METERS = Units.feetToMeters(5);
  // Angle between horizontal and the camera.
  final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(0);

  // How far from the target we want to be
  final double GOAL_RANGE_METERS = Units.feetToMeters(3);

  // Change this to match the name of your camera
  PhotonCamera camera = new PhotonCamera("Microsoft_LifeCam_HD-3000"); //aka Greg

  // PID constants should be tuned per robot
  final double LINEAR_P = 0.1;
  final double LINEAR_D = 0.0;
  //PIDController forwardController = new PIDController(LINEAR_P, 0, LINEAR_D);

  final double ANGULAR_P = 0.1;
  final double ANGULAR_D = 0.0;
 // PIDController turnController = new PIDController(ANGULAR_P, 0, ANGULAR_D);

  XboxController xboxController = new XboxController(0);

  // Drive motors
  NeoMotor leftMotor = new NeoMotor(3);
  NeoMotor rightMotor = new NeoMotor(4);

  @Override
  public void robotInit() {
    camera.setPipelineIndex(1);

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


  public double getTurn(double x) {
    double minTurn = .03;
    double maxTurn = 0.05;
    double minX = 6;
    double maxX = 25;

    if (x >= maxX) {
      return maxTurn;
    }
    if (x <= -maxX) {
      return -maxTurn;
    }
    
    if (x >= -minX && x <= minX) {
      return 0;
    }

    if (x > minX) {
      return (maxTurn - minTurn) / (maxX - minX) * (x - minX) + minTurn;
    }

    return (maxTurn - minTurn) / (maxX - minX) * (x + minX) - minTurn;
  }


  public double FindAverage(List<PhotonTrackedTarget> reflectiveTargets){
    
    double sum = 0;
    for(int i=0; i<reflectiveTargets.size();i++){
      PhotonTrackedTarget target = reflectiveTargets.get(i);
      sum += target.getYaw();
    }
    
    
    return sum / reflectiveTargets.size();
  }



  @Override
  public void teleopPeriodic() {
    double forwardSpeed;
    double rotationSpeed;

    forwardSpeed = -xboxController.getRightY();

    if (xboxController.getAButton()) {
       System.out.println("we can game");
        // Vision-alignment mode
        // Query the latest result from PhotonVision
        var result = camera.getLatestResult();

        if (result.hasTargets()) {
          // really really has targets? 
            // Calculate angular turn power
            // -1.0 required to ensure positive PID controller effort _increases_ yaw
            var bestYaw = FindAverage(result.getTargets());
            System.out.println("Best Yaw: " + bestYaw);
            rotationSpeed = getTurn(bestYaw);
            //hi this gets the best boy exept the yaw
          
            double range =
              PhotonUtils.calculateDistanceToTargetMeters(
                      CAMERA_HEIGHT_METERS,
                      TARGET_HEIGHT_METERS,
                      CAMERA_PITCH_RADIANS,
                      Units.degreesToRadians(result.getBestTarget().getPitch()));
        } else {
            // If we have no targets, stay still.
            rotationSpeed = 0;
            System.out.println("nope sorry :c");
        }
    } else {
        // Manual Driver Mode
        rotationSpeed = xboxController.getLeftX();
    }
    double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, -rotationSpeed, false);
    double leftSpeed = arcadeSpeeds[0];
    double rightSpeed = arcadeSpeeds[1];
    leftMotor.setPercent(-leftSpeed);
    rightMotor.setPercent(rightSpeed);

    if (xboxController.getAButton()) {
      System.out.println ("RotationSpeed: " + rotationSpeed+" LeftSpeed: "+leftSpeed+" RightSpeed:"+rightSpeed);
    }
  }
}
