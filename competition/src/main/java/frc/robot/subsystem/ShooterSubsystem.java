package frc.robot.subsystem;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.NtKeys;
import frc.robot.constants.constants;
import frc.robot.devices.NeoMotor;
import frc.robot.util.DriveHelper;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.util.Targeting;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotController;

public class ShooterSubsystem extends StateMachine {
    private Shooter shooter;

    private int shooterSpeed =  -60;
    private Timer timer = new Timer();
    private boolean autoMode = false;
    private double revDuration = 1;


    public ShooterSubsystem() {
        super("stop");
        shooter = Subsystems.shooter;
    }
    
    public void setAuto(boolean bool) {
        autoMode = bool;
    }

    public void stop(){
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
    }

    // @State(name = "find")
    // public void runFind(StateContext ctx) {
    //     /* using camera to detect the relective tapes on upper hub
    //         if it sees targets goes to aiming
    //         if it doesn't see targets- find by moving turret left and right

    //     */
    // }

    // @State(name ="aim") 
    // public void runAim(StateContext ctx) {
    //     /**
    //      * pointing turret until the desired target is in desirable position then goes to hood adjustments
    //      */
    // }

    @State(name="hood") 
    public void runHood(StateContext ctx) {
        /**
         * - get distance from the camera 
         * - get desired angle for hood
         * - adjusts angle to desired angle
         * - goes to rev
         */
    }

    @State(name = "rev")
    public void runRev(StateContext ctx) {
        boolean isAimed = true;
        if (autoMode == true) {
            double rotationSpeed = Targeting.calculate();
            double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
            double leftSpeed = arcadeSpeeds[0];
            double rightSpeed = arcadeSpeeds[1];

            Subsystems.drive.setSpeeds(leftSpeed * Units.feetToMeters(constants.maxSpeedo),
                    rightSpeed * Units.feetToMeters(constants.maxSpeedo));
            isAimed = rotationSpeed == 0 && Targeting.hasTargets();

        } else {
            Subsystems.drive.setSpeeds(0, 0);

        }

        shooter.setShooterVolt(shooterSpeed);

        if (ctx.getTime() > this.revDuration && isAimed)
            this.setState("shoot");

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
        Subsystems.drive.setSpeeds(0, 0);
        shooter.setShooterVolt(shooterSpeed);
    }

    public void shooterInfo() {
        NtHelper.setString(NtKeys.SHOOTER_STATE, getState());
        shooter.shooterInfo();
    }

  
}
