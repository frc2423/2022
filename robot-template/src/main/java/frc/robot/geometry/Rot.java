package frc.robot.geometry;

import edu.wpi.first.math.geometry.Rotation2d;

public class Rot {
    double angle;

    public Rot(double angel){
        angle = angel;
    }

    public Rotation2d getRotation(){
        return new Rotation2d(-angle);
    }

    public double getAngle() {
        return angle;
    }
}
