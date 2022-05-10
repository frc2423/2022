package frc.robot.subsystem;
import frc.robot.Subsystems;
import frc.robot.Devices;

public class Belt {
    public void runStorage(){
        if (Devices.controller.getBButton() || Subsystems.cargoRejector.isRejecting()){
            Devices.beltMotor.setPercent(-0.2);
        }
        else if (Subsystems.intake.isDown() || Subsystems.shooter.isShoot() || Devices.controller.getXButton()){ 
            Devices.beltMotor.setPercent(0.2);
        }
        else{ 
            Devices.beltMotor.setPercent(0);
        }
        
    }
}
