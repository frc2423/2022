package frc.robot.subsystem;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;

import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;
import edu.wpi.first.wpilibj.RobotController;

public class Shooter  {
    private NeoMotor kickerMotor;
    private NeoMotor shooterMotor;
    // private NeoMotor turretMotor;
    private NeoMotor hoofMotor;
    // private NeoMotor accelMotor;


    private double kickerSpeed = -0.3;
    private double highGoalSpeed = -60; // for upper hub
    private double lowGoalShooterSpeed = -38; // -42; //for lower hub
    private double shooterSpeed = highGoalSpeed;
    // private double turretSpeed = .25;

    private SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(0.10397, 0.12786, 0.0085994);
    private boolean isShoot = false;
    private boolean backwardsIs = false;

    public Shooter() {
        kickerMotor = Devices.kickerMotor;
        shooterMotor = Devices.shooterMotor;
        // turretMotor = Devices.turretMotor;
        hoofMotor = Devices.hoofMotor;

    }

    public boolean isShoot() {
        return isShoot;
    }

    public void setIsShoot(boolean value) {
        isShoot = value;
    }

    public boolean backwardIs(){
        return backwardsIs;
    }

    public void backwardIsSet(boolean value){
        backwardsIs = value; 
    }

    public void shoot(boolean isHighGoal) {
        if (isHighGoal) {
            setShooterSpeed(highGoalSpeed);
        } else {
            setShooterSpeed(lowGoalShooterSpeed);
        }
    }

    public void shoot() {
        shoot(true);
    }

    public void stop() {
        kickerStop();
        shooterStop();
    }

    public void setShooterSpeed(double speed) {
        shooterSpeed = speed;
    }

    
    public void setShooterVolt(double speed) {
        double voltage = feedForward.calculate(speed);
        double percent = voltage / RobotController.getBatteryVoltage();
        shooterMotor.setPercent(percent);
    }

    public void setHoodfPosition(double position){
        hoofMotor.setDistance(position);
    }

    public void aim(){
        Subsystems.drive.isTargeting(true);
    }
 
    public boolean isAimed(){
        return Subsystems.drive.getIsAimed();
    }
    

    public void calibrateHood() {
        if (Math.abs(hoofMotor.getSpeed()) < 0.05){
            hoofMotor.setSpeed(0);    
            hoofMotor.resetEncoder(0);
        }
        else {
            hoofMotor.setSpeed(.1);
        }
    }

    public void kicker() {
        kickerMotor.setPercent(kickerSpeed);
    }

    public void kickerStop() {
        kickerMotor.setPercent(0);
    }

    public void shooterStop() {
        shooterMotor.setPercent(0);
    }
    public void shooterInfo() {
        NtHelper.setDouble(NtKeys.SHOOTER_SPEED, shooterMotor.getSpeed());
        NtHelper.setDouble(NtKeys.DESIRED_SHOOTER_SPEED, shooterSpeed);
        NtHelper.setDouble(NtKeys.DESIRED_KICKER_SPEED, kickerSpeed);
    }
   
}
