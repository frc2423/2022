package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.DriveHelper;
import frc.robot.util.NtHelper;
import frc.robot.util.Rotation;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import frc.robot.Devices;
import frc.robot.Subsystems;

public class ShootOneGetTwo extends StateMachine {
    
    private double angle;
    private Rotation rotate = new Rotation(.15, .3, 5, 150);;

    public ShootOneGetTwo() {
        super("FirstShot");
    }

    @State(name = "FirstShot")
    public void firstShot(StateContext ctx) {
        NtHelper.setString("/robot/auto/state", "FirstShot");
        Subsystems.shooter.shoot();

        if (ctx.getTime() > 2.5) {
            Subsystems.shooter.stop();
            setState("rotate");
        }
        // Seconds subject to change upon testing
    }

    private double getAngleErrorRadians(double errorDegrees) {
        return Units.radiansToDegrees(MathUtil.angleModulus(Units.degreesToRadians(errorDegrees)));
    }

    @State(name = "rotate")
    public void rotate(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.intake.goUp();
            angle = Devices.gyro.getAngle()-180;
        }

        double angleError = getAngleErrorRadians(angle - Devices.gyro.getAngle());
        double rotationSpeed = rotate.calculate(angleError);
        double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
        double leftSpeed = arcadeSpeeds[0];
        double rightSpeed = arcadeSpeeds[1];
        Devices.leftMotor.setPercent(leftSpeed);
        Devices.rightMotor.setPercent(rightSpeed);

        if (rotate.isDone(angleError)) {
            setState("get");
        }
    }

    @State(name = "get")
    public void get(StateContext ctx){
        if (ctx.isInit()){
            Subsystems.follower.setTrajectory("GoHummusPlayer"); // exept its the correct trajectory
            Subsystems.follower.startFollowing();
            Subsystems.intake.goDown();
        }

        Subsystems.follower.follow();
        NtHelper.setString("/robot/auto/state", "GoHummusPlayer");
        
        if (Subsystems.follower.isDone()) {
            setState("rotateTwo");
        }
    }

    @State(name = "rotateTwo")
    public void rotateTwo(StateContext ctx) {
        if (ctx.isInit()) {
            Subsystems.intake.goUp();
            angle = Devices.gyro.getAngle()-180;
        }

        double angleError = getAngleErrorRadians(angle - Devices.gyro.getAngle());
        double rotationSpeed = rotate.calculate(angleError);
        double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
        double leftSpeed = arcadeSpeeds[0];
        double rightSpeed = arcadeSpeeds[1];
        Devices.leftMotor.setPercent(leftSpeed);
        Devices.rightMotor.setPercent(rightSpeed);

        if (rotate.isDone(angleError)) {
            setState("return");
        }
    }

    @State(name = "return")
    public void returnHome(StateContext ctx){
        if (ctx.isInit()){
            Subsystems.follower.setTrajectory("HPToHome"); // exept its the correct trajectory
            Subsystems.follower.startFollowing();
            Subsystems.intake.goDown();
        }

        Subsystems.follower.follow();
        NtHelper.setString("/robot/auto/state", "HPToHome");
        
        if (Subsystems.follower.isDone()) {
         //   setState("rotateTwo");
        }
    }

}
