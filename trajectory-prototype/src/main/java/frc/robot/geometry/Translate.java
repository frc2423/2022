package frc.robot.geometry;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.geometry.Translation2d;


public class Translate {
    private double x;
    private double y;

    public Translate(double xFT, double yFT){
        x = Units.feetToMeters(xFT);
        y = Units.feetToMeters(yFT);
    }
    
    public Translation2d getTranslation() {
        return new Translation2d(x, y);
    }

    public double getXFeet(){
        return Units.metersToFeet(x);
    }

    public double getXMeters(){
        return x;
    }

    public double getYFeet(){
        return Units.metersToFeet(y);
    }

    public double getYMeters(){
        return y;
    }
}
