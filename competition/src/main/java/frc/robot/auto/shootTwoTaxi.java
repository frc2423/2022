package frc.robot.auto;
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
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.util.Targeting;
import frc.robot.util.TrajectoryFollower;

import frc.robot.util.TrajectoryFollower;

import java.util.List;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import frc.robot.subsystem.Intake;

/* Move towards cargo in straight line
 * Intake cargo
 * Aim and shoot both cargos at upper hub (testing necessary)
 * Move backwards to taxi
 */ 

public class shootTwoTaxi extends StateMachine{

    private Intake intake = new Intake ();
    Trajectory CargoAdvanceTrajectory = TrajectoryGenerator.generateTrajectory(
            //the line is going along the x axis
            new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
            List.of(),
            new Pose2d(Units.feetToMeters(10), 0, Rotation2d.fromDegrees(0)),
            new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
        );
    Trajectory ShooterAdvanceTrajectory = TrajectoryGenerator.generateTrajectory(
        //the line is going along the x axis
        new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
        List.of(),
        new Pose2d(Units.feetToMeters(10), 0, Rotation2d.fromDegrees(0)),
        new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );
    Trajectory TaxiBackTrajectory = TrajectoryGenerator.generateTrajectory(
        //the line is going along the x axis
        new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
        List.of(),
        new Pose2d(Units.feetToMeters(10), 0, Rotation2d.fromDegrees(0)),
        new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );
    private TrajectoryFollower follower = new TrajectoryFollower();
    private Timer timer = new Timer();


    public shootTwoTaxi() {
        super("CargoAdvance");
        follower.addTrajectory("CargoAdvance", CargoAdvanceTrajectory);
        follower.addTrajectory("ShooterAdvance", ShooterAdvanceTrajectory);
        follower.addTrajectory("TaxiBack", TaxiBackTrajectory);
        follower.setTrajectory("CargoAdvance");
    }

    @InitState(name = "CargoAdvance")
    public void CargoAdvanceInit (){
        follower.setTrajectory("CargoAdvance");
        follower.resetPosition();
    }

    @RunState(name = "CargoAdvance")
    public void CargoAdvanceRun(){
        follower.follow();
        if (follower.isDone()) {
            setState("Intake");
        }
    
    }

    @InitState(name = "Intake")
    public void IntakeInit (){
        intake.intakeDown();
        timer.reset ();
        timer.start ();
    }

    @RunState(name = "Intake")
    public void Intake (){
        if (timer.get() > 2){
            setState("ShooterAdvance");
        }
        //Seconds; subject to change

    }

    @InitState(name = "ShooterAdvance")
    public void ShooterAdvanceInit (){
        follower.setTrajectory("ShooterAdvance");
        follower.resetPosition();
    }

    @RunState(name = "ShooterAdvance")
    public void ShooterAdvance (){
        follower.follow ();
        if (follower.isDone()){
            setState ("ShootTwo");
        }

    }

    @InitState(name = "ShootTwo")
    public void ShootTwoInit (){
        timer.reset();
        timer.start();
    //Shooter initialization
    }

    @RunState(name = "ShootTwo")
    public void ShootTwo (){
        //Shoot both cargos
        if (timer.get() > 4){
            setState ("TaxiBack");
        }
        //Seconds subject to change upon testing
    
    }

    @InitState(name = "TaxiBack")
    public void TaxiBackInit (){
        follower.setTrajectory ("CargoAdvance");
        follower.resetPosition();
    }

    @RunState(name = "TaxiBack")
    public void TaxiBack (){
        follower.follow ();
    }


}
