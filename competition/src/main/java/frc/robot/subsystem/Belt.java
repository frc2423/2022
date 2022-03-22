package frc.robot.subsystem;
import frc.robot.Subsystems;
import frc.robot.Devices;

public class Belt {

    
    public Belt() {
        
    }

    public void run_storage(){
        if (Subsystems.intake.isDown() || Subsystems.shooter.isShoot() || Devices.controller.getXButton()){
            Devices.beltMotor.setSpeed(-0.2);
        }
        else if (Devices.controller.getBButton()){ 
            Devices.beltMotor.setSpeed(0.2);
        }
        else{ 
            Devices.beltMotor.setSpeed(0);
        }
        
    }




}
