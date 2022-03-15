package frc.robot;
import frc.robot.constants.constants;
import frc.robot.subsystem.*;


/**
 * Static accessor for all robot subsystems.
 */
public class Subsystems {
    public static Climber climber = new Climber();
    public static Drivetrain drivetrain = new Drivetrain(
        constants.trackWidth, 
        constants.Ks, 
        constants.Kv, 
        Devices.gyro.getRotation()
    );
    public static Intake intake = new Intake();
    public static Shooter shooter = new Shooter();
    public static TrajectoryFollower follower = new TrajectoryFollower();

    static void init() {
        intake.zero();
        intake.stop();
    }
}
