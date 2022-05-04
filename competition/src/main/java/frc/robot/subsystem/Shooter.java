package frc.robot.subsystem;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
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
import edu.wpi.first.wpilibj.RobotController;

public class Shooter extends StateMachine {
    private NeoMotor kickerMotor;
    private NeoMotor shooterMotor;

    private Timer timer = new Timer();

    private double kickerSpeed = -0.3;
    private double highGoalSpeed = -60; // for upper hub
    private double lowGoalShooterSpeed = -38; // -42; //for lower hub
    private double shooterSpeed = highGoalSpeed;

    private double revDuration = 1;
    private SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(0.10397, 0.12786, 0.0085994);
    private boolean autoMode = false;
    private boolean isShoot = false;

    public Shooter() {
        super("stop");
        kickerMotor = Devices.kickerMotor;
        shooterMotor = Devices.shooterMotor;
    }

    public boolean isShoot() {
        return isShoot;
    }

    public void setAuto(boolean bool) {
        autoMode = bool;
    }

    public void shoot(boolean isHighGoal) {
        if (isHighGoal) {
            setShooterSpeed(highGoalSpeed);
        } else {
            setShooterSpeed(lowGoalShooterSpeed);
        }

        if (getState() == "stop") {
            this.setState("rev");
        }
    }

    public void shoot() {
        shoot(true);
    }

    public void stop() {
        this.setState("stop");
    }

    public String getState() {
        return super.getState();
    }

    public void setShooterSpeed(double speed) {
        shooterSpeed = speed;
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

    public void setShooterVolt(double speed) {
        double voltage = feedForward.calculate(speed);
        double percent = voltage / RobotController.getBatteryVoltage();
        shooterMotor.setPercent(percent);
    }

    @State(name = "stop")
    public void runStopped(StateContext ctx) {
        if (ctx.isInit()) {
            kickerStop();
            shooterStop();
            timer.stop();
            isShoot = false;
        }
    }

    @State(name = "find")
    public void runFind(StateContext ctx) {
        /* using camera to detect the relective tapes on upper hub
            if it sees targets goes to aiming
            if it doesn't see targets- find by moving turret left and right

        */
    }

    @State(name ="aim") 
    public void runAim(StateContext ctx) {
        /**
         * pointing turret until the desired target is in desirable position then goes to hood adjustments
         */
    }

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

        setShooterVolt(shooterSpeed);

        if (ctx.getTime() > this.revDuration && isAimed)
            this.setState("shoot");

    }

    @State(name = "shoot")
    public void runShoot(StateContext ctx) {
        /**
         * Add code for running accelerator as well as kicker
         */
        if (ctx.isInit()) {
            isShoot = true;
            NtHelper.setDouble(NtKeys.CARGO_COUNT, 0);
        }
        kicker();
        Subsystems.drive.setSpeeds(0, 0);
        setShooterVolt(shooterSpeed);
    }



    public void shooterInfo() {
        NtHelper.setString(NtKeys.SHOOTER_STATE, getState());
        NtHelper.setDouble(NtKeys.SHOOTER_SPEED, shooterMotor.getSpeed());
        NtHelper.setDouble(NtKeys.DESIRED_SHOOTER_SPEED, shooterSpeed);
        NtHelper.setDouble(NtKeys.DESIRED_KICKER_SPEED, kickerSpeed);
    }
}
