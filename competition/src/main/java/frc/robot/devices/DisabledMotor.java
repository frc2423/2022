package frc.robot.devices;

public class DisabledMotor implements IMotor {

    public DisabledMotor(int port) {

    }

    public void setIZone(double zone) {

    }

    public void setSpeed(double speed) {
    }

    public double getSpeed() {
        return 0;
    }

    public void setPercent(double percent) {

    }

    public double getPercent() {
        return 0;
    }

    public void setDistance(double dist) {

    }

    public void resetEncoder(double distance) {

    }

    public double getDistance() {
        return 0;
    }

    public void setConversionFactor(double factor) {

    }

    public double getConversionFactor() {
        return 0;
    }

    public void setInverted(boolean isInverted) {

    }

    public boolean getInverted() {
        return false;
    }

    public void setPid(double kP, double kI, double kD) {

    }

    public void setPidf(double kP, double kI, double kD, double kF) {

    }

    public void setP(double kP) {

    }

    public void setI(double kI) {

    }

    public void setD(double kD) {

    }

    public void setF(double kF) {

    }

    public double getP() {
        return 0;
    }

    public double getI() {
        return 0;
    }

    public double getD() {
        return 0;
    }

    public double getF() {
        return 0;
    }

    public void follow(IMotor leader) {

    }

    public void setFollower(NeoMotor follower) {

    }

    public void setEncoderPositionAndRate(double position, double rate) {
    }

    public double getEncoderCount() {
        return 0;
    }
}
