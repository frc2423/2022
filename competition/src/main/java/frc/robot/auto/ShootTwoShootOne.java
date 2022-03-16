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

/* Move towards cargo in straight line
 * Intake cargo
 * Aim and shoot both cargos at upper hub (testing necessary)
 * Move backwards to taxi
 */ 

public class ShootTwoShootOne extends StateMachine{
    //TODO: Values subject to change upon completed trajcetory integration
    private Timer timer = new Timer();
    private double angle;
    private Rotation rotate;
    private int rotations = 0;

    public ShootTwoShootOne() {
        super("Stop");
        NtHelper.setString("/robot/auto/name", "shootTwoTaxi3");
    }

    @RunState(name = "Stop")
    public void stopState(){
        setState("CargoAdvance");
    }

    @InitState(name = "CargoAdvance")
    public void CargoAdvanceInit (){
        String position = NtHelper.getString("/robot/auto/postion", "middle");
        Subsystems.follower.setTrajectory("BottomTarmacToBottomCargo");
        Subsystems.follower.resetPosition();
    }

    @RunState(name = "CargoAdvance")
    public void CargoAdvanceRun(){
        Subsystems.follower.follow();
        if (Subsystems.follower.isDone()) {
            setState("Intake");
        }
    
    }

    @InitState(name = "Intake")
    public void IntakeInit (){
        Subsystems.intake.intakeDown();
        timer.reset ();
        timer.start ();
    }

    @RunState(name = "Intake")
    public void Intake (){
        if (timer.get() > 2){
            Subsystems.intake.intakeUp();
            setState("Rotate");
        }
        //Seconds; subject to change

    }

    @InitState(name = "Rotate")
    public void rotateInit(){
        angle = Devices.gyro.getAngle() + 180;
        rotate = new Rotation(.1, .3, angle - 5, angle + 5);
    }

    @RunState(name = "Rotate")
    public void rotate(){
        double rotationSpeed = rotate.calculate(Devices.gyro.getAngle());
        double[] arcadeSpeeds = DriveHelper.getArcadeSpeeds(0, rotationSpeed, false);
        double leftSpeed = arcadeSpeeds[0];
        double rightSpeed = arcadeSpeeds[1];
        Devices.leftMotor.setPercent(leftSpeed);
        Devices.rightMotor.setPercent(rightSpeed); 
        if(rotate.isDone(Devices.gyro.getAngle())){
            if(rotations == 0){
                setState("ShooterAdvance");
            } else if (rotations == 1){
                setState("SecondAdvanceCargo");
            }
        }
    }

    @InitState(name = "ShooterAdvance")
    public void ShooterAdvanceInit (){
        Subsystems.follower.setTrajectory("BottomCargoToHub");
        Subsystems.follower.resetPosition();
    }

    @RunState(name = "ShooterAdvance")
    public void ShooterAdvance (){
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
    public void ShootTwo(){
        Subsystems.shooter.shoot();
        if (timer.get() > 4){
            setState ("TaxiBack");
        }
        //Seconds subject to change upon testing
    
    }

    @InitState(name = "BackUp")
    public void TaxiBackInit (){
        Subsystems.follower.setTrajectory ("BottomHubBackUp");
        Subsystems.follower.resetPosition();
    }

    @RunState(name = "TaxiBack")
    public void TaxiBack (){
        Subsystems.follower.follow ();
        if (Subsystems.follower.isDone()){
            setState("Rotate");
        }
    }

    @InitState(name = "SecondCargoAdvance")
    public void initSecondCargoAdvance(){
        Subsystems.follower.setTrajectory("BottomtarmacToMiddleCargo");
        Subsystems.follower.resetPosition();
    }

    @RunState(name = "SecondCargoAdvance")
    public void secondCargoAdvance(){
        Subsystems.follower.follow ();
        if (Subsystems.follower.isDone()){
            setState("SecondShooterAdvance");
        }
    }

    @InitState(name = "SecondShooterAdvance")
    public void initSecondShooterAdvance(){
        Subsystems.follower.setTrajectory("MiddleCargoToBottomHub");
        Subsystems.follower.resetPosition();
    }

    @RunState(name = "SecondShooterAdvance")
    public void secondShooterAdvance(){
        Subsystems.follower.follow ();
        if (Subsystems.follower.isDone()){
            setState ("ShootOne");
        }
    }

    @InitState(name = "ShootOne")
    public void initShootOne(){
        timer.reset();
        timer.start();
    }

    @RunState(name = "ShootOne")
    public void ShootOne(){
        Subsystems.shooter.shoot();
        if (timer.get() > 4){
        }
        //Seconds subject to change upon testing
    
    }

}
