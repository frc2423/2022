package frc.robot.util;

public class Targeting {


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
    
}
