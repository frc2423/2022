package frc.robot.util;
import frc.robot.devices.Camera;

public class Targeting {
  private static Camera camera = new Camera("Microsoft_LifeCam_HD-3000 (1)"); // aka greg
  static double minTurn = .03;
  static double maxTurn = 0.05;

  static double minX = 6;
  static double maxX = 25;
  
  public static void init() { 
    camera.setPipeline(0);
  }

  //returns value 0-1
  public static double calculate() {
    var result = camera.getLatestResult();
    double rotationSpeed = 0.0;
    NtHelper.setBoolean("/robot/aiming/haveTargets", result.hasTargets()); //puts whether we do or don't have targets in the table 

    if (result.hasTargets()) {
      // really really has targets? 
      // Calculate angular turn power
      var bestYaw = camera.FindAverage(result.getTargets());
      rotationSpeed = getTurn(bestYaw);
      //this gets best yaw
      
    } else {
      // If we have no targets, stay still.
      rotationSpeed = 0;
    }
    return rotationSpeed;
  }

  public static double getTurn(double x) { 
    
    //the two line bellow are just setting a reverse deadband
    if (x >= maxX) {
      return maxTurn;
    }
    if (x <= -maxX) {
      return -maxTurn;
    }
    
    //checks if we are at the target
    if (x >= -minX && x <= minX) {
      return 0;
    }

    //Sets the speed depending on the distance of target
    if (x > minX) {
      return (maxTurn - minTurn) / (maxX - minX) * (x - minX) + minTurn;
    }

    return (maxTurn - minTurn) / (maxX - minX) * (x + minX) - minTurn;
  }


  public void setTurnSpeed(double min, double max) {
    minTurn = min;
    maxTurn = max;
  }

  public void setTurnx(double min, double max) {
    minX = min;
    maxX = max;
  }
  
}
