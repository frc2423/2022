package frc.robot.util;
//imports 
import frc.robot.devices.Camera;

//hi Adrien (that was on purpose)

public class Targeting {
  private Camera camera = new Camera("Microsoft_LifeCam_HD-300"); // aka greg
  double minTurn = .03;
  double maxTurn = 0.05;

  double minX = 6;
  double maxX = 25;
  
  public void init(){ 
    camera.setPipeline(1);
  }

  public void execute(){

    double rotationSpeed = getRotSped();

    double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, -rotationSpeed, false);
    double leftSpeed = arcadeSpeeds[0];
    double rightSpeed = arcadeSpeeds[1];
    //set motors here
  }

  public double getRotSped(){
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

  public double getTurn(double x) { 
    
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

