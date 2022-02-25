package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Devices;

import frc.robot.devices.NeoMotor;

public class Shooter {
    private NeoMotor beltMotor;
    private NeoMotor kickerMotor;
    private NeoMotor shooterMotor;

    private Timer timer = new Timer();

    private double beltSpeed = 0.2;
    private double kickerSpeed = 0.2;
    private double shooterSpeed = 0.8;
    //Motor values subject to change followin implementation and testing

    private String state = "start";

    public Shooter(){
        beltMotor = Devices.beltMotor;
        kickerMotor = Devices.kickerMotor;
        shooterMotor = Devices.shooterMotor;
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

    public void revUp () {
        shooterMotor.setSpeed(shooterSpeed);
    }

    public void kicker() {
        kickerMotor.setPercent(kickerSpeed);
    }

    public void kickerStop() {
        kickerMotor.setPercent(0);
    }

    public void shootOne() {
        switch (state) {
            case "start": 
                timer.reset();
                timer.start();
                state = "rev";
                break;
            case "rev":
                revUp();
                if (timer.get() > 1) state= "shoot";
                break;
            case "shoot":
                beltForward();
                kicker();
                revUp();
                if (timer.get() > 2) state= "shoot";
                break;
            case "stop":
                beltStop();
                kickerStop();
                shootStop();
                timer.stop();
                break;

        }
  
    }
    public void shootTwo() {
        switch (state) {
            case "start": 
                timer.reset();
                timer.start();
                state = "rev";
                break;
            case "rev":
                revUp();
                if (timer.get() > 1) state= "shoot";
                break;
            case "shoot":
                beltForward();
                kicker();
                revUp();
                if (timer.get() > 2) state= "shoot";
                break;
            case "stop":
                beltStop();
                kickerStop();
                shootStop();
                timer.stop();
                break;

        }
  
    }

    public void shootStop() {
        shooterMotor.setPercent(0);
    }


}
