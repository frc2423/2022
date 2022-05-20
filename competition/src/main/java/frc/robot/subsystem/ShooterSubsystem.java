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
    private boolean autoMode = false;
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
        autoMode = bool;
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
            this.setState("rev");
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

    @State(name = "rev")
    public void runRev(StateContext ctx) {
        if (autoMode == true) {
            shooter.aim();
        } else {
            Subsystems.drive.setSpeeds(0, 0);
        }

        double distance = Targeting.getDistance();
            
        if (distance != -1) {
            double position = angleFinder.getHoodAngle(distance); 
            shooter.setHoodfPosition(position);
        }
        if (ctx.getTime() < timeBeltBackwards) {
            shooter.backwardIsSet(true);

        } else {
            shooter.kicker();
            shooter.backwardIsSet(false);
        }

        shooter.setShooterVolt(shooterSpeed);

        if (autoMode == true) {
            if (ctx.getTime() > this.revDuration && shooter.isAimed() && distance != -1)
            this.setState("shoot");
        } else {
            if (ctx.getTime() > this.revDuration && distance != -1)
            this.setState("shoot");
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
