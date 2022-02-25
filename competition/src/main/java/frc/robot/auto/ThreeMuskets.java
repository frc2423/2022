package frc.robot.auto;

import frc.robot.util.stateMachine.StateMachine;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;

import frc.robot.util.TrajectoryFollower;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.constants.constants;
import frc.robot.subsystem.Intake;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;

import java.util.List;
public class ThreeMuskets extends StateMachine{

    private Intake intake = new Intake();
    Trajectory IntakeAimTrajectory = TrajectoryGenerator.generateTrajectory(
        //the line is going along the x axis
        new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
        List.of(),
        new Pose2d(Units.feetToMeters(10), 0, Rotation2d.fromDegrees(0)),
        new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );
    Trajectory TaxiTrajectory = TrajectoryGenerator.generateTrajectory(
        //the line is going along the x axis
        new Pose2d(0, 0, Rotation2d.fromDegrees(0)),
        List.of(),
        new Pose2d(Units.feetToMeters(10), 0, Rotation2d.fromDegrees(0)),
        new TrajectoryConfig(Units.feetToMeters(constants.maxSpeedo), Units.feetToMeters(constants.maxAccel))
    );

    private TrajectoryFollower follower = new TrajectoryFollower();
    private Timer timer = new Timer();

    public ThreeMuskets() {
        super("FirstShot");
        //
    }

    @InitState(name = "FirstShot")
    public void firstShotInit(){
        timer.reset();
        timer.start();
        //TODO: Implement shooter code following general shooter implementation
    }

    @RunState(name = "FirstShot")
    public void firstShotRun(){
        //Shoot primary initialization cargo
        if (timer.get() > 4){
            timer.stop();
            setState ("CargoAdvance");
        }
        //Seconds subject to change upon testing
    }

    @InitState(name = "CargoAdvance")
    public void cargoAdvanceInit(){
        follower.setTrajectory("IntakeAimTrajectory");
        follower.resetPosition();
        intake.intakeDown();
    }

    @RunState(name = "CargoAdvance")
    public void cargoAdvanceRun(){
        follower.follow();
        if (follower.isDone()) {
            setState("ShootTwo");
        }
    }

    @InitState(name = "ShootTwo")
    public void shootTwoInit(){
        timer.reset();
        timer.start();
        intake.intakeUp();
    }

    @RunState(name = "ShootTwo")
    public void shootTwoRun(){
        //Shoot both cargos
        if (timer.get() > 4){
            timer.stop();
            setState ("TaxiBack");
        }
        //Seconds subject to change upon testing
    }

    @InitState (name = "TaxiBack")
    public void taxiBackInit(){
        follower.setTrajectory("TaxiTrajectory");
        follower.resetPosition();
    }

    @RunState (name = "TaxiBack")
    public void taxiBackRun (){
        follower.follow ();
    }
}
