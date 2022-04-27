package frc.robot.subsystem;
import frc.robot.Subsystems;
import frc.robot.Devices;

public class Belt {
    /*
     * Counting sensors: color sensor at the entrance to the storage; break beam sensor farther in
     * No cargo: run the belts to pull in (if the intake is down)
     * First cargo hits color sensor:
     * Determine color; if correct then go into store_first_cargo mode, if wrong, reject
     * Store First Cargo mode: do NOT run the intake, but run the belts until later break beam is hit, 
     *  increase counter as soon as correct color is determined
     * Has one cargo fully stored mode: go back to trying to intake, until the next one hits the color sensor
     * Second cargo hits color sensor: count it and stop if correct color, run in reverse until First Cargo hits the color sensor again otherwise
     * Zero on shooting is still fine (even if we delay the shot, we're not moving the intake until the shots are done)
     * 
     * States:
     * Intake: belt pulls in + rollers pulling in (if intake is down)
     * Spacing: belt pulls in + rollers NOT moving
     * ShootEject: belt pulls in + kicker runs + intake can still move + keep looking for a right ball to count
     * Eject: belt and rollers in reverse
     * Full: Stopped
     * Shoot: Belts run, kicker runs, flywheel runs
     * 
     * Transitions:
     * First state: Intake
     * Intake -> Spacing on First Correct Ball
     * Intake -> Full on Second Correct Ball
     * Intake -> ShootEject on Wrong First Ball
     * Intake -> ReverseEject on Wrong Second Ball
     * 
     * Spacing -> Intake on Break Beam Tripped
     * 
     * ShootEject -> Intake on successful shot (add third sensor?)
     * ShootEject -> Spacing if we found a correct ball in the middle
     * ShootEject -> Intake if we see a ball at break beam AND our count is 1
     * 
     * ReverseEject -> Intake on finding previously grabbed color of correct color
     * 
     * Every state -> Shoot on operator button held
     * 
     * Shoot -> Intake on operator button released
     */
    public void runStorage(){
        if (Subsystems.intake.isDown() || Subsystems.shooter.isShoot() || Devices.driverController.getXButton()){
            Devices.beltMotor.setPercent(-0.2);
        }
        else if (Devices.driverController.getBButton()){ 
            Devices.beltMotor.setPercent(0.2);
        }
        else{ 
            Devices.beltMotor.setPercent(0);
        }
        
    }
}
