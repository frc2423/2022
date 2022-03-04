package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Devices;

import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;

public class Shooter extends StateMachine{
    private NeoMotor beltMotor;
    private NeoMotor kickerMotor;
    private NeoMotor shooterMotor;

    // private Timer timer = new Timer();

    private double beltSpeed = -0.2;
    private double kickerSpeed = -0.2;
    private double shooterSpeed = -0.8;

    private double shootDuration;
    private double revDuration;
    //Motor values subject to change following implementation and testing


    public Shooter(double revDuration, double shootDuration){
        super("shooter");
        beltMotor = Devices.beltMotor;
        kickerMotor = Devices.kickerMotor;
        shooterMotor = Devices.shooterMotor;
        this.shootDuration = shootDuration;
        this.revDuration = revDuration;
        this.setState("stop");
        this.shooterInfo();
    }

    public String getState() {
        return super.state;
    }

    // public void setShooterSpeed(double speed){
    //     shooterSpeed = speed;
    // }

    // public void beltForward() {
    //     beltMotor.setPercent(beltSpeed);
    // }

    // public void beltStop() {
    //     beltMotor.setPercent(0);
    // }

    public void kicker() {
        kickerMotor.setPercent(kickerSpeed);
    }

    // public void kickerStop() {
    //     kickerMotor.setPercent(0);
    // }

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

    @InitState(name="start")
    public void runStartInit() {
        // initialize shooter
        timer.reset();
        timer.start();
      
        //state = "start";
        
    }

    @RunState(name="start")
    public void runStart() {
        // start shooter sequence
        this.setState("rev");
    }

    @InitState(name="rev")
    public void runRevInit() {
        // initialize rev
        shooterMotor.setPercent(shooterSpeed);
    }

    @RunState(name="rev") 
    public void runRev() {
        // run rev
        if (timer.get() > this.revDuration) this.setState("shoot");

    }

    @InitState(name="shoot")
    public void runShootInit() {
        beltForward();
        kicker();
        //revUp();
        // initialize shooting
    }

    @RunState(name="shoot")
    public void runShoot() {
        if (timer.get() > this.shootDuration + this.revDuration) this.setState("stop");
        // run shooting
    }


    public void shooterInfo(){
        NtHelper.setString("/robot/shooter/state", state);
        NtHelper.setDouble("/robot/shooter/beltspeed", beltMotor.getSpeed());
        NtHelper.setDouble("/robot/shooter/kickerspeed", kickerMotor.getSpeed());
        NtHelper.setDouble("/robot/shooter/shooterspeed", shooterMotor.getSpeed());
    }



}
