package frc.robot.subsystem;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Devices;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.DriveHelper;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.Targeting;
import edu.wpi.first.wpilibj.RobotController;


public class Shooter extends StateMachine{
    private NeoMotor beltMotor;
    private NeoMotor kickerMotor;
    private NeoMotor shooterMotor;

    private Timer timer = new Timer();

    private double beltSpeed = -0.2;
    private double kickerSpeed = -0.3;
    private double shooterSpeed = -60;

    private double revDuration = 1;
    private SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(0.10397, 0.12786, 0.0085994);
    private boolean autoMode = false;

    public Shooter() {
        super("stop");
        beltMotor = Devices.beltMotor;
        kickerMotor = Devices.kickerMotor;
        shooterMotor = Devices.shooterMotor;
        NtHelper.setDouble(NtKeys.SHOOTER_GOAL_SPEED, this.shooterSpeed);
    }

    public void setAuto(boolean bool){
        autoMode = bool;
    }

    public void shoot() {
        if (getState() == "stop"){
            this.setState("rev");
        }
    }

    public void stop() {
        this.setState("stop");
    }

    public String getState() {
        return super.getState();
    }

    public void setShooterSpeed(double speed){
        shooterSpeed = speed;
    }
    
    public double getGoalSpeed() {
        return NtHelper.getDouble(NtKeys.SHOOTER_GOAL_SPEED, this.shooterSpeed);
    }

    public void beltForward() {
        beltMotor.setPercent(beltSpeed);
    }

    public void beltStop() {
        beltMotor.setPercent(0);
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

    public void setShooterVolt(double speed){
        double voltage = feedForward.calculate(speed);
        double percent = voltage / RobotController.getBatteryVoltage();
        shooterMotor.setPercent(percent);
    }


    @InitState(name="stop") 
    public void runStoppedInit() {
        // initialize stopped (runs once)
        beltStop();
        kickerStop();
        shooterStop();
        timer.stop();
    }

    @RunState(name="stop") 
    public void runStopped() {
        // run stopped (runs a bunch)
    }

    @InitState(name="rev")
    public void runRevInit() {
        timer.reset();
        timer.start();
        // initialize rev
    }

    @RunState(name="rev") 
    public void runRev() {
        boolean isAimed = true;
        if (autoMode == true){
            double rotationSpeed = Targeting.calculate();
            double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
            double leftSpeed = arcadeSpeeds[0];
            double rightSpeed = arcadeSpeeds[1];
            Devices.leftMotor.setPercent(leftSpeed);
            Devices.rightMotor.setPercent(rightSpeed); 
            isAimed = rotationSpeed == 0 && Targeting.hasTargets();
        } else {
            Devices.leftMotor.setPercent(0);
            Devices.rightMotor.setPercent(0);
        }
        
        setShooterVolt(getGoalSpeed());

        if (timer.get() > this.revDuration && isAimed) this.setState("shoot");

    }

    @InitState(name="shoot")
    public void runShootInit() {
        NtHelper.setDouble(NtKeys.CARGO_COUNT, 0);
    }
    
    @RunState(name="shoot")
    public void runShoot() {
        beltForward();
        kicker();
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0); 
        setShooterVolt(getGoalSpeed());
    }


    public void shooterInfo(){
        NtHelper.setString(NtKeys.SHOOTER_STATE, getState());
        NtHelper.setDouble(NtKeys.SHOOTER_SPEED, shooterMotor.getSpeed());
        NtHelper.setDouble(NtKeys.DESIRED_SHOOTER_SPEED, shooterSpeed);
        NtHelper.setDouble(NtKeys.DESIRED_KICKER_SPEED, kickerSpeed);
    }
}
