package frc.robot.util;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.util.NtHelper;
import frc.robot.util.DriveHelper;
import frc.robot.Devices;
import frc.robot.constants.constants;
import frc.robot.subsystem.Drivetrain;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.util.Targeting;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import frc.robot.subsystem.Intake;

public class TrajectoryFollower {
    public Timer timer = new Timer ();
    public Trajectory trajectory;
    private final RamseteController m_ramseteController = new RamseteController();
    private Drivetrain drivetrain = new Drivetrain(
      constants.trackWidth, 
      constants.Ks, 
      constants.Kv, 
      Devices.gyro.getRotation()
    );
    private Field2d m_field;

    private HashMap<String, Trajectory> trajectoryMap = new HashMap<String, Trajectory>();

    public TrajectoryFollower() {
    }

    public void startFollowing() {
        timer.reset();
        timer.start();
    }

    public void addTrajectory(String name, Trajectory trajectoryName){
        trajectoryMap.put(name, trajectoryName);
    }

    public void setTrajectory (String name){
        trajectory = trajectoryMap.get(name);
    }

    public void follow (){
        var currTime = timer.get();
        var desiredPose = trajectory.sample(currTime);
       

        drivetrain.updateOdometry(Devices.gyro.getRotation(), Devices.leftMotor.getDistance(), Devices.rightMotor.getDistance());
        m_field.setRobotPose(drivetrain.getPose());
        ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);
        double[] motorValues = drivetrain.getMotorValues(refChassisSpeeds);
        Devices.leftMotor.setPercent(motorValues[0]);
        Devices.rightMotor.setPercent(motorValues[1]);
    }

    public void resetPosition (){
        Devices.leftMotor.resetEncoder(0);
        Devices.rightMotor.resetEncoder(0);
        Devices.gyro.reset();
        drivetrain.odometryReset(new Pose2d(0,0,Rotation2d.fromDegrees(0)), Devices.gyro.getRotation());
        m_field.setRobotPose(drivetrain.getPose());
    }

    public boolean isDone (){

        if (timer.get() > trajectory.getTotalTimeSeconds()){
            return true;
        }
        return false;
    }
    
}
