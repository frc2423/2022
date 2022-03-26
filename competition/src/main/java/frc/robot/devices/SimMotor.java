
package frc.robot.devices;

import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.Encoder;
import java.util.ArrayList;
import edu.wpi.first.wpilibj.simulation.EncoderSim;

public class SimMotor implements IMotor {

    private PWMVictorSPX motor;
    private Encoder encoder; 
    protected ArrayList<SimMotor> followers = new ArrayList<SimMotor>();
    private double encoderOffset = 0;
    private EncoderSim encoderSim;
    private double desiredPercent = 0;

    public SimMotor(int port, int channelA, int channelB) {
        motor = new PWMVictorSPX(port);
        encoder = new Encoder(channelA, channelB);
        encoderSim = new EncoderSim(encoder);
        setPercent(0);
    }

    public void setSpeed(double speed) {

    }

    public double getSpeed(){
        double rate = encoder.getRate();
        return motor.getInverted() ? -rate : rate;
    }

    public void setPercent(double percent) {
        desiredPercent = percent;
        motor.setVoltage(desiredPercent);

        for (SimMotor follower : followers) {
            follower.setPercent(desiredPercent);
        }
    }

    public double getPercent(){
        return motor.get();
    }


    public double getEncoderCount() {
        return getDistance() / getConversionFactor();
    }

    public void setDistance(double dist) {

    }

    public void resetEncoder(double distance) {
        encoder.reset();
        encoderOffset = distance;
    }

    public double getDistance() {
        double distance =  encoder.getDistance() + encoderOffset;
        return motor.getInverted() ? -distance : distance;
    }

    public void setConversionFactor(double factor){
        encoder.setDistancePerPulse(factor);
    }

    public double getConversionFactor(){
        return encoder.getDistancePerPulse();
    }

    public void setInverted(boolean isInverted) {
        motor.setInverted(isInverted);
    }

    public boolean getInverted() {
        return motor.getInverted();
    }

    public void setPid(double kP, double kI, double kD){
        setP(kP);
        setI(kI);
        setD(kD);       
    }

    public void setPidf(double kP, double kI, double kD, double kF){
        setP(kP);
        setI(kI);
        setD(kD);
        setF(kF); 
    }

    public void setP(double kP){
    }

    public void setI(double kI){
    }

    public void setD(double kD){
    }

    public void setF(double kF) {
        
    }

    public double getP(){
        return 0;
    }

    public double getI(){
        return 0;
    }

    public double getD(){
        return 0;
    }

    public double getF() {
        return 0.0;
    }

    public void follow(IMotor leader){
        if (leader.getClass() == SimMotor.class) {
            SimMotor leadDriveMotor = (SimMotor)leader;
            leadDriveMotor.followers.add(this);
        }
    }

    public void setEncoderPositionAndRate(double position, double rate){
        encoderSim.setDistance(position);
        encoderSim.setRate(rate);
    }

}
