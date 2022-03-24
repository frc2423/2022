package frc.robot.subsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.constants;
import frc.robot.util.NtHelper;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.util.Rotation;
import frc.robot.util.DriveHelper;
import edu.wpi.first.wpilibj.Timer;
import java.util.HashMap;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;

public class TrajectoryFollower {
    private Timer timer = new Timer ();
    private Trajectory trajectory;
    private final RamseteController m_ramseteController = new RamseteController();
    // private Rotation rotate = new Rotation(.15, .3, 5, 150);

    private Rotation rotate = new Rotation(.1, .3, 5, 150);

    private HashMap<String, Trajectory> trajectoryMap;

    public TrajectoryFollower(HashMap<String, Trajectory> trajectoryMap) {
        this.trajectoryMap = trajectoryMap;
    }

    public void startFollowing() {
        timer.reset();
        timer.start();
    }

    public void addTrajectory(String name, Trajectory trajectoryName){
        trajectoryMap.put(name, trajectoryName);
    }

    public void setTrajectory (String name){
        setTrajectory(name, true);
    }

    public void setTrajectory (String name, boolean moveToStart){
        NtHelper.setString("/robot/auto/currTrajectory", name);
        trajectory = trajectoryMap.get(name);
        System.out.println("trajectory time: " + trajectory.getTotalTimeSeconds());
        if (moveToStart) {
            var initialPose = trajectory.getStates().get(0).poseMeters;
            Devices.leftMotor.resetEncoder(0);
            Devices.rightMotor.resetEncoder(0);
            Subsystems.drivetrain.odometryReset(initialPose, initialPose.getRotation());
            Devices.gyro.setAngle(-initialPose.getRotation().getDegrees());
        }
        Subsystems.drivetrain.setTrajectory("traj", trajectory);
        
    }

    public void follow (){
        if (timer.get() < trajectory.getTotalTimeSeconds()) {
            var currTime = timer.get();
            var desiredPose = trajectory.sample(currTime);
    
            Subsystems.drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(Subsystems.drivetrain.getPose(), desiredPose);
            double[] motorValues = Subsystems.drivetrain.getMotorValues(refChassisSpeeds);

            Devices.leftMotor.setPercent(motorValues[0]);
            Devices.rightMotor.setPercent(motorValues[1]);

            NtHelper.setDouble("/robot/auto/leftmotor", motorValues[0]);
            NtHelper.setDouble("/robot/auto/rightmotor", motorValues[1]);

        } else if (!isDoneRotating()) {
            rotateToEndHeading();
        }

        if (timer.get() > trajectory.getTotalTimeSeconds()) {
            timer.stop();
        }
    }

    private double getAngleErrorRadians(double errorDegrees) {
        return Units.radiansToDegrees(MathUtil.angleModulus(Units.degreesToRadians(errorDegrees)));
    }

    public boolean isDoneRotating() {
        var lastState = trajectory.getStates().get( trajectory.getStates().size() - 1);
        var angle = -lastState.poseMeters.getRotation().getDegrees();
        double angleError = getAngleErrorRadians(angle - Devices.gyro.getAngle());
        return rotate.isDone(angleError);
    }

    public void rotateToEndHeading() {
        var lastState = trajectory.getStates().get( trajectory.getStates().size() - 1);
        var angle = -lastState.poseMeters.getRotation().getDegrees();

        double angleError = getAngleErrorRadians(angle - Devices.gyro.getAngle());

        double rotationSpeed = rotate.calculate(angleError);
        double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);

        double leftSpeed = arcadeSpeeds[0];
        double rightSpeed = arcadeSpeeds[1];

        Devices.leftMotor.setPercent(leftSpeed);
        Devices.rightMotor.setPercent(rightSpeed);
    }

    public void resetPosition (){
        timer.stop();
        Devices.leftMotor.resetEncoder(0);
        Devices.rightMotor.resetEncoder(0);
        Devices.gyro.reset();
        Subsystems.drivetrain.odometryReset(new Pose2d(0,0,Rotation2d.fromDegrees(0)), Devices.gyro.getRotation());
    }

    public boolean isDone(){
        // Maybe we should also use  m_ramseteController.atReference()
        if (timer.get() > trajectory.getTotalTimeSeconds() && isDoneRotating()){
            return true;
        }
        return false;
    }
    
}
