package frc.robot.auto;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.NtHelper;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.util.DriveHelper;
import frc.robot.util.Rotation;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;

/* Move towards cargo in straight line
 * Intake cargo
 * Aim and shoot both cargos at upper hub (testing necessary)
 * Move backwards to taxi
 */ 

public class ShootTwoTaxi extends StateMachine{
    //TODO: Values subject to change upon completed trajcetory integration
    private Timer timer = new Timer();
    private double angle;
    private Rotation rotate;

    public ShootTwoTaxi() {
        super("Stop");
        NtHelper.setString("/robot/auto/name", "shootTwoTaxi3");
        Subsystems.follower.setTrajectory("BottomTarmacToBottomCargo");

    }

    @RunState(name = "Stop")
    public void stopState(){
        NtHelper.setString("/robot/auto/state", "stop");
        NtHelper.setString("/robot/auto/postiion", "bottom");

        setState("CargoAdvance");
    }

    @InitState(name = "CargoAdvance")
    public void CargoAdvanceInit (){
        String position = NtHelper.getString("/robot/auto/position", "bottom");
        if (position.equals("high")){
            Subsystems.follower.setTrajectory("TopTarmacToTopCargo");
        } else if (position.equals("middle")) {
            Subsystems.follower.setTrajectory("MiddleTarmacToMiddleCargo");
        } else {
            Subsystems.follower.setTrajectory("BottomTarmacToBottomCargo");
        }
        //Subsystems.follower.resetPosition();
        Subsystems.follower.startFollowing();
    }

    @RunState(name = "CargoAdvance")
    public void CargoAdvanceRun(){
        NtHelper.setString("/robot/auto/state", "cargoAdvance");
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("Rotate");
        }
    
    }
    @RunState(name="end") public void stoppppp(){}

    @InitState(name = "Intake")
    public void IntakeInit (){
        Subsystems.intake.setDownState();
        timer.reset ();
        timer.start ();
    }

    @RunState(name = "Intake")
    public void Intake (){
        NtHelper.setString("/robot/auto/state", "intake");

        if (timer.get() > 2){
            Subsystems.intake.setUpState();
            setState("Rotate");
        }
        //Seconds; subject to change

    }


    @InitState(name = "Rotate")
    public void rotateInit(){
        angle = Devices.gyro.getAngle() + 180;
        rotate = new Rotation(.2, .4, 5, 150);
    }

    private double getAngleErrorRadians(double errorDegrees) {
        return Units.radiansToDegrees(MathUtil.angleModulus(Units.degreesToRadians(errorDegrees)));
    }

    @RunState(name = "Rotate")
    public void rotate(){
        NtHelper.setString("/robot/auto/state", "rotate");

        double angleError = getAngleErrorRadians(-90  - Devices.gyro.getAngle());
        double rotationSpeed = rotate.calculate(angleError);
        double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
        double leftSpeed = arcadeSpeeds[0];
        double rightSpeed = arcadeSpeeds[1];
        Devices.leftMotor.setPercent(leftSpeed);
        Devices.rightMotor.setPercent(rightSpeed); 
        if(rotate.isDone(Devices.gyro.getAngle())){
            setState("ShooterAdvance");
        }
    }

    @InitState(name = "ShooterAdvance")
    public void ShooterAdvanceInit (){
        String position = NtHelper.getString("/robot/auto/postion", "middle");
        if (position.equals("high")){
            Subsystems.follower.setTrajectory("TopCargoToHub");
        } else if (position.equals("midde")) {
            Subsystems.follower.setTrajectory("MiddleCargoToHub");
        } else {
            Subsystems.follower.setTrajectory("BottomCargoToHub");
        }
        //Subsystems.follower.resetPosition();
    }

    @RunState(name = "ShooterAdvance")
    public void ShooterAdvance (){
        NtHelper.setString("/robot/auto/state", "shooteradvacnce");
        Subsystems.follower.follow ();
        if (Subsystems.follower.isDone()){
            setState ("ShootTwo");
        }

    }

    @InitState(name = "ShootTwo")
    public void ShootTwoInit (){
        timer.reset();
        timer.start();
    }

    @RunState(name = "ShootTwo")
    public void ShootTwo (){
        NtHelper.setString("/robot/auto/state", "shoottwo");
        Subsystems.shooter.shoot();
        if (timer.get() > 4){
            setState ("TaxiBack");
        }
        //Seconds subject to change upon testing
    
    }

    @InitState(name = "TaxiBack")
    public void TaxiBackInit (){
        Subsystems.follower.setTrajectory ("CargoAdvance");
       // Subsystems.follower.resetPosition();
        timer.stop();
    }

    @RunState(name = "TaxiBack")
    public void TaxiBack (){
        Subsystems.follower.follow ();
    }


}
