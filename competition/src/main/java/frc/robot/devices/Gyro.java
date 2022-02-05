package frc.robot.devices;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI.Port;

public class Gyro implements IGyro{

    private AHRS gyro;

    public Gyro(){
        gyro = new AHRS(Port.kMXP);
    }

    public Gyro(Port channel){
        gyro = new AHRS(channel);
    }

    public void reset(){
        gyro.reset();
    }

    public double getAngle(){
        return gyro.getAngle();
    }

    public void setAngle(double angle){
        gyro.reset();
        gyro.setAngleAdjustment(angle);
    }

    public double getRate(){
        return gyro.getRate();
    }

    public void calibrate(){
        gyro.calibrate();
    }

}