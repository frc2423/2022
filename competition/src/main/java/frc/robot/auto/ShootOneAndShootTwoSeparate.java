package frc.robot.auto;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.NtHelper;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.NtKeys;
import frc.robot.util.DriveHelper;
import frc.robot.util.Rotation;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;


public class ShootOneAndShootTwoSeparate extends StateMachine{
    private Timer timer = new Timer();
    private double angle;
    private Rotation rotate;

    public ShootOneAndShootTwoSeparate() {
        super("FirstShot");
      }

    @InitState(name = "FirstShot")
    public void firstShotInit(){
        timer.reset();
        timer.start();
        //TODO: Implement shooter code following general shooter implementation
    }

    @RunState(name = "FirstShot")
    public void firstShotRun(){
        Subsystems.shooter.shoot();
        if (timer.get() > 4){
            timer.stop();
            Subsystems.shooter.stop();
            setState ("Rotate");
        }
        //Seconds subject to change upon testing
    }

    @InitState(name = "Rotate")
    public void rotateInit() {
        Subsystems.intake.goUp();
        angle = Devices.gyro.getAngle() + 180;
        rotate = new Rotation(.15, .3, 5, 150);
    }

    private double getAngleErrorRadians(double errorDegrees) {
        return Units.radiansToDegrees(MathUtil.angleModulus(Units.degreesToRadians(errorDegrees)));
    }

    @RunState(name = "Rotate")
    public void rotate() {
        double angleError = getAngleErrorRadians(angle - Devices.gyro.getAngle());
        double rotationSpeed = rotate.calculate(angleError);
        double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
        double leftSpeed = arcadeSpeeds[0];
        double rightSpeed = arcadeSpeeds[1];
        Devices.leftMotor.setPercent(leftSpeed);
        Devices.rightMotor.setPercent(rightSpeed);
        if (rotate.isDone(angleError)) {
            setState("CargoAdvance");
        }
    }

    @InitState(name = "CargoAdvance")
    public void cargoAdvanceInit(){
        Subsystems.follower.setTrajectory("BottomTarmacToCargosToHub");

        Subsystems.follower.startFollowing();
    }
    @RunState(name = "CargoAdvance")
    public void cargoAdvanceRun(){
        Subsystems.intake.goDown();
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("MiddleCargoAdvance");
        }
    }

    @InitState(name = "MiddleCargoAdvance")
    public void middleCargoAdvanceInit(){
        Subsystems.follower.setTrajectory("BottomCargoToMiddleCargo");
        Subsystems.follower.startFollowing();
    }

    @RunState(name = "MiddleCargoAdvance")
    public void middleCargoAdvanceRun(){
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("HubAdvance");
        }
    }

    @InitState(name = "HubAdvance")
    public void hubAdvanceInit(){
        Subsystems.follower.setTrajectory("MiddleCargoToHubThreeBall");
        Subsystems.follower.startFollowing();
    }

    @RunState(name = "HubAdvance")
    public void hubAdvanceRun(){
        Subsystems.intake.goUp();
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("ShootTwo");
        }
    }

    @InitState(name = "ShootTwo")
    public void shootTwoInit(){
        timer.reset();
        timer.start();
        Subsystems.intake.goUp();
    }

    @RunState(name = "ShootTwo")
    public void shootTwoRun(){
        Subsystems.shooter.shoot();
        if (timer.get() > 4){
            timer.stop();
            Subsystems.shooter.stop();
            setState ("TaxiBack");
        }
        //Seconds subject to change upon testing
    }

    @InitState (name = "TaxiBack")
    public void taxiBackInit(){
        Subsystems.follower.setTrajectory("Taxi");
        Subsystems.follower.startFollowing();
        timer.stop();
    }

    @RunState (name = "TaxiBack")
    public void taxiBackRun (){
        Subsystems.follower.follow();
    }
}
