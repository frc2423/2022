package frc.robot.subsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.constants;
import frc.robot.util.NtHelper;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

import edu.wpi.first.wpilibj.Timer;
import java.util.HashMap;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class TrajectoryFollower {
    private Timer timer = new Timer ();
    private Trajectory trajectory;
    private final RamseteController m_ramseteController = new RamseteController();

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
        NtHelper.setString("/robot/auto/currTrajectory", name);
        trajectory = trajectoryMap.get(name);
        var initialPose = trajectory.getStates().get(0).poseMeters;
        Subsystems.drivetrain.odometryReset(initialPose, initialPose.getRotation());
        Devices.gyro.setAngle(-initialPose.getRotation().getDegrees());
        Subsystems.drivetrain.setTrajectory("traj", trajectory);
        
    }

    public void follow (){
        var currTime = timer.get();
        var desiredPose = trajectory.sample(currTime);

        Subsystems.drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());
        ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(Subsystems.drivetrain.getPose(), desiredPose);
        double[] motorValues = Subsystems.drivetrain.getMotorValues(refChassisSpeeds);
        Devices.leftMotor.setPercent(motorValues[0]);
        Devices.rightMotor.setPercent(motorValues[1]);
        NtHelper.setDouble("/robot/auto/leftmotor", motorValues[0]);
        NtHelper.setDouble("/robot/auto/rightmotor", motorValues[1]);

        if (isDone()) timer.stop();
    }

    public void resetPosition (){
        timer.stop();
        Devices.leftMotor.resetEncoder(0);
        Devices.rightMotor.resetEncoder(0);
        Devices.gyro.reset();
        Subsystems.drivetrain.odometryReset(new Pose2d(0,0,Rotation2d.fromDegrees(0)), Devices.gyro.getRotation());
    }

    public boolean isDone(){
        if (timer.get() > trajectory.getTotalTimeSeconds()){
            return true;
        }
        return false;
    }
    
}
