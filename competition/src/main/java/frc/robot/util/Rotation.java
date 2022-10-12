package frc.robot.util;

/**
 * A Rotation object for calculating turn rate values based on an error between an actual and desired rotation
 */
public class Rotation {

  private double minTurnSpeedo = 0.03;
  private double maxTurnSpeedo = 0.05;
  private double onTargetMargin = 6;
  private double maxTurnSpeedThreshold = 25;


  /**
   * Initializes a new Rotation object
   * @param minTurnSpeedo The minimum rotation speed that the object will ever return
   * @param maxTurnSpeedo The maximum rotation speed that the object will ever return
   * @param onTargetMargin The maximum angle error allowed in either direction to consider us at the target rotation
   * @param maxTurnSpeedThreshold The angle error threshold beyond which the min/max turn speedo will be returned
   */
  public Rotation(double minTurnSpeedo, double maxTurnSpeedo, double onTargetMargin, double maxTurnSpeedThreshold) {
    this.minTurnSpeedo = minTurnSpeedo;
    this.maxTurnSpeedo = maxTurnSpeedo;
    this.onTargetMargin = onTargetMargin;
    this.maxTurnSpeedThreshold = maxTurnSpeedThreshold;
  }
  
  /**
   * Takes in an angle error and returns a proper rotation speed to reduce the error.
   * @param angleError The angle error
   * @return The rotation speed to provide to the robot.
   */
  public double calculate(double angleError) { 
    
    //the two line bellow are just setting a reverse deadband
    if (angleError >= maxTurnSpeedThreshold) {
      return maxTurnSpeedo;
    }
    if (angleError <= -maxTurnSpeedThreshold) {
      return -maxTurnSpeedo;
    }
    
    //checks if we are at the target
    if (angleError >= -onTargetMargin && angleError <= onTargetMargin) {
      return 0;

    }

    //Sets the speed depending on the distance of target
    double turnSpeed = (maxTurnSpeedo - minTurnSpeedo) / (maxTurnSpeedThreshold - onTargetMargin) * (angleError - onTargetMargin) + minTurnSpeedo;
    if (angleError > onTargetMargin) {
      return turnSpeed;
    }

    return -turnSpeed;
  }

  public boolean isDone(double x){
    return x >= -onTargetMargin && x <= onTargetMargin;
    //return (angle < maxX && angle > minX);
  }
}
