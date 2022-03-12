package frc.robot.subsystem;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Devices;

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
    private double kickerSpeed = -0.2;
    private double shooterSpeed = -60;//-3500; //-10; //-.2; //-0.8; //-3500?

    private double revDuration = 1;
    private SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(0.10397, 0.12786, 0.0085994);
    private boolean autoMode = false;
    //Motor values subject to change following implementation and testing

    public Shooter() {
        super("stop");
        beltMotor = Devices.beltMotor;
        kickerMotor = Devices.kickerMotor;
        shooterMotor = Devices.shooterMotor;

        NtHelper.setDouble("/robot/shooter/shootermotorp", 0.0);
        NtHelper.setDouble("/robot/shooter/shootermotori", 0.0); 
        NtHelper.setDouble("/robot/shooter/shootermotord", 0.0);
        NtHelper.setDouble("/robot/shooter/shootermotorf", 0.0);
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
            System.out.println("doing the thing "+ rotationSpeed);
            isAimed = rotationSpeed == 0 && Targeting.hasTargets();
        } else {
            Devices.leftMotor.setPercent(0);
            Devices.rightMotor.setPercent(0);
        }
        
       // shooterMotor.setSpeed(shooterSpeed);
       setShooterVolt(shooterSpeed);

        if (timer.get() > this.revDuration && isAimed) this.setState("shoot");

    }

    @InitState(name="shoot")
    public void runShootInit() {
        NtHelper.setDouble("/robot/cargocount", 0);
    }
    
    @RunState(name="shoot")
    public void runShoot() {
        beltForward();
        kicker();
        Devices.leftMotor.setPercent(0);
        Devices.rightMotor.setPercent(0); 
        // run shooting
        //shooterMotor.setPercent(-Devices.controller.getRightTriggerAxis() * 0.65);
        setShooterVolt(shooterSpeed);
    }


    public void shooterInfo(){
        NtHelper.setString("/robot/shooter/state", getState());
        // NtHelper.setDouble("/robot/shooter/beltspeed", beltMotor.getSpeed());
        // NtHelper.setDouble("/robot/shooter/kickerspeed", kickerMotor.getSpeed());
        NtHelper.setDouble("/robot/shooter/shooterspeed", shooterMotor.getSpeed());
        NtHelper.setDouble("/robot/shooter/desiredshooterspeed", shooterSpeed);

        shooterMotor.setPidf(NtHelper.getDouble("/robot/shooter/shootermotorp", 0.0),
        NtHelper.getDouble("/robot/shooter/shootermotori", 0.0), 
        NtHelper.getDouble("/robot/shooter/shootermotord", 0.0),
        NtHelper.getDouble("/robot/shooter/shootermotorf", 0.0));

        NtHelper.setDouble("/robot/shooter/foundMotorP", NtHelper.getDouble("/robot/shooter/shootermotorp", 0.0));
    }



}
