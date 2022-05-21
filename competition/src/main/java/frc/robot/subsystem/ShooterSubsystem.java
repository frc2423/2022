package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Subsystems;
import frc.robot.constants.NtKeys;
import frc.robot.subsystem.util.TurretDistanceMapper;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.util.Targeting;
import frc.robot.util.Targeting;

public class ShooterSubsystem extends StateMachine {
    private Shooter shooter;

    private int shooterSpeed = -60;
    private Timer timer = new Timer();
    private boolean autoAim = false;
    private double revDuration = 1;
    // speed, timer
    private double speedBeltBackwards = 0.2;
    private double timeBeltBackwards = 0.2;
    private TurretDistanceMapper angleFinder = new TurretDistanceMapper();

    public ShooterSubsystem() {
        super("stop");
        shooter = Subsystems.shooter;
    }

    public void setAuto(boolean bool) {
        autoAim = bool;
    }

    public void stop() {
        this.setState("stop");
        shooter.stop();
    }

    public String getState() {
        return super.getState();
    }

    public void shoot(boolean isHighGoal) {
        shooter.shoot(isHighGoal);
        if (getState() == "stop") {
            this.setState("backwardsBelt");
        }
    }

    public void shoot() {
        shoot(true);
    }

    @State(name = "stop")
    public void runStopped(StateContext ctx) {
        if (ctx.isInit()) {
            shooter.kickerStop();
            shooter.shooterStop();
            timer.stop();
            shooter.setIsShoot(false);
        }
        shooter.calibrateHood();
    }

    // @State(name = "find")
    // public void runFind(StateContext ctx) {
    // /* using camera to detect the relective tapes on upper hub
    // if it sees targets goes to aiming
    // if it doesn't see targets- find by moving turret left and right

    // */
    // }

    // @State(name ="aim")
    // public void runAim(StateContext ctx) {
    // /**
    // * pointing turret until the desired target is in desirable position then goes
    // to hood adjustments
    // */
    // }

    @State(name = "backwardBelt")
    public void backwardBelt(StateContext ctx){
        if (ctx.isInit()) {
            shooter.backwardIsSet(true);
        }
        if (ctx.getTime() > timeBeltBackwards) {
            shooter.backwardIsSet(false);
            this.setState("preShooting");
        }
    }
   
    @State(name = "preShooting")
    public void preShooting(StateContext ctx) {
        double distance = Targeting.getDistance();
        
        shooter.aim(autoAim);
        shooter.skRev(distance);
        shooter.setHoodAngle(distance);

        if (ctx.getTime() > this.revDuration && shooter.isAimed(autoAim) && distance != -1) {
            this.setState("shoot");
            Subsystems.drive.isTargeting(false, autoAim);
        }
    }

    @State(name = "shoot")
    public void runShoot(StateContext ctx) {
        /**
         * Add code for running accelerator as well as kicker
         */
        if (ctx.isInit()) {
            shooter.setIsShoot(true);
            NtHelper.setDouble(NtKeys.CARGO_COUNT, 0);
        }
        shooter.kicker();
        //Subsystems.drive.setSpeeds(0, 0);
        shooter.setShooterVolt(shooterSpeed);
    }

    public void shooterInfo() {
        NtHelper.setString(NtKeys.SHOOTER_STATE, getState());
        shooter.shooterInfo();
    }

}
