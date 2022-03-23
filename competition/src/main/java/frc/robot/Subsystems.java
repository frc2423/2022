package frc.robot;
import frc.robot.constants.constants;
import frc.robot.subsystem.*;
import frc.robot.auto.Auto;
import frc.robot.auto.Trajectories;
import frc.robot.util.Targeting;

/**
 * Static accessor for all robot subsystems.
 */
public class Subsystems {
    public static Climber climber;
    public static Drivetrain drivetrain;
    public static Intake intake;
    public static Shooter shooter;
    public static TrajectoryFollower follower;
    public static Auto auto;
    public static Belt belt;
    
    static void init() {
        climber = new Climber();
        drivetrain = new Drivetrain(
            constants.trackWidth, 
            constants.Ks, 
            constants.Kv, 
            Devices.gyro.getRotation()
        );
        intake = new Intake();
        shooter = new Shooter();
        follower = new TrajectoryFollower(Trajectories.getTrajectories());
        auto = new Auto();
        belt = new Belt();
        Targeting.init();
    }
}
