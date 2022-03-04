package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Devices;

import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.Targeting;

public class Shooter extends StateMachine{
    private NeoMotor beltMotor;
    private NeoMotor kickerMotor;
    private NeoMotor shooterMotor;

    private Timer timer = new Timer();

    private double beltSpeed = -0.2;
    private double kickerSpeed = -0.2;
    private double shooterSpeed = -.2; //-0.8;

    private double revDuration = 1;

    private boolean autoMode = false;
    //Motor values subject to change following implementation and testing

    public Shooter() {
        super("stop");
        beltMotor = Devices.beltMotor;
        kickerMotor = Devices.kickerMotor;
        shooterMotor = Devices.shooterMotor;
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
        return super.state;
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
        shooterMotor.setPercent(shooterSpeed);
    }

    @RunState(name="rev") 
    public void runRev() {
        if (autoMode == true){
            double rotationSpeed = Targeting.calculate();

            // arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
            // leftSpeed = arcadeSpeeds[0];
            // rightSpeed = arcadeSpeeds[1];
            // Devices.leftMotor.setPercent(leftSpeed);
            // Devices.rightMotor.setPercent(rightSpeed); 
        }
        
        shooterMotor.setPercent(shooterSpeed);

        if (timer.get() > this.revDuration) this.setState("shoot");

    }

    @InitState(name="shoot")
    public void runShootInit() {
    }
    
    @RunState(name="shoot")
    public void runShoot() {
        beltForward();
        kicker();
        shooterMotor.setPercent(shooterSpeed);

        // run shooting
    }


    public void shooterInfo(){
        NtHelper.setString("/robot/shooter/state", getState());
        NtHelper.setDouble("/robot/shooter/beltspeed", beltMotor.getSpeed());
        NtHelper.setDouble("/robot/shooter/kickerspeed", kickerMotor.getSpeed());
        NtHelper.setDouble("/robot/shooter/shooterspeed", shooterMotor.getSpeed());
    }



}
