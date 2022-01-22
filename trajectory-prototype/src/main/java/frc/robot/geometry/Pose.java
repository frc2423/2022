package frc.robot.geometry;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class Pose {
    private double x; //stored in meters
    private double y;
    private Rotation2d rot; //negates the input value

    public Pose(double xFt, double yFt, Rot rotValue){
        x = Units.feetToMeters(xFt);
        y = Units.feetToMeters(yFt);
        rot = rotValue.getRotation();
    }

    public Pose2d getPose(){
        return new Pose2d(x, y, rot);
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

    public Rotation2d getRotation() {
        return rot;
    }

}
