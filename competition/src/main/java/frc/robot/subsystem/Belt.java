package frc.robot.subsystem;
import frc.robot.Subsystems;
import frc.robot.Devices;

public class Belt {
    public void runStorage(){
        if (Devices.controller.getBButton() || Subsystems.cargoRejector.isRejecting()){
            Devices.beltMotor.setPercent(-0.2);
        }
        else if (Subsystems.intake.isDown() || Subsystems.shooter.isShoot() || Devices.controller.getXButton() || Subsystems.cargoRejector.isForwarding() || !isLoaded()){ 
            Devices.beltMotor.setPercent(0.2);
        }
        else{ 
            Devices.beltMotor.setPercent(0);
        }
        
    }

    public boolean isLoaded(){ // returns if 
        // if (Subsystems.counter.getBallCount() != 1){
        //     return false;
        // } else if (Devices.shooterBeamBrake.get()) {
        //     return true;
        // }
        return ((Subsystems.counter.getBallCount() == 1) && !(Devices.shooterBeamBrake.get()));
    }
}
