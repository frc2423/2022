package frc.robot.util;

public class Rotation {

  private double minTurn = .03;
  private double maxTurn = 0.05;
  private double minX = 6;
  private double maxX = 25;

  public Rotation(double minTurn, double maxTurn, double minX, double maxX) {
    this.minTurn = minTurn;
    this.maxTurn = maxTurn;
    this.minX = minX;
    this.maxX = maxX;
  }
  
  public double calculate(double x) { 
    
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

  public boolean isDone(double x){
    return x >= -minX && x <= minX;
    //return (angle < maxX && angle > minX);
  }
}
