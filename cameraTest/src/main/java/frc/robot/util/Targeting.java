package frc.robot.util;

import frc.robot.devices.Camera;

public class Targeting {

  double minTurn = .03;
  double maxTurn = 0.05;

  double minX = 6;
  double maxX = 25;

  private Camera camera = new Camera("Microsoft_LifeCam_HD-300"); // aka greg

  public void init (){ 
    camera.setPipeline(1);
  }

  public double getRotSped(){
    var result = camera.getLatestResult();

    double rotationSpeed = 0.0;

    if (result.hasTargets()) {
      // really really has targets? 
        // Calculate angular turn power
        // -1.0 required to ensure positive PID controller effort _increases_ yaw
        var bestYaw = camera.FindAverage(result.getTargets());
        System.out.println("Best Yaw: " + bestYaw);
        rotationSpeed = getTurn(bestYaw);
        //hi this gets the best boy exept the yaw
      
    } else {
        // If we have no targets, stay still.
        rotationSpeed = 0;
        System.out.println("nope sorry :c");
    }
    return rotationSpeed;
  }

  public void setTurnSpeed(double min, double max) {
    minTurn = min;
    maxTurn = max;
  }

  public void setTurnx(double min, double max) {
    minX = min;
    maxX = max;
  }

  public double getTurn(double x) {
   

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

  
}

