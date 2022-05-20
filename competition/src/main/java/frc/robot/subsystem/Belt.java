package frc.robot.subsystem;
import frc.robot.Subsystems;
import frc.robot.Devices;

public class Belt {
    public void runStorage(){
        if (Devices.driverController.getBButton() || Subsystems.cargoRejector.isRejecting() || Subsystems.shooter.backwardIs()){
            Devices.beltMotor.setPercent(0.2);
        }
        else if (Subsystems.intake.isDown() || Subsystems.shooter.isShoot() || Devices.driverController.getXButton() || Subsystems.cargoRejector.isForwarding() || isNotLoaded()){ 
            Devices.beltMotor.setPercent(-0.2);
        }
        else{ 
            Devices.beltMotor.setPercent(0);
        }
        
    }

    public boolean isNotLoaded(){ // returns if 
        // if (Subsystems.counter.getBallCount() != 1){
        //     return false;
        // } else if (Devices.shooterBeamBrake.get()) {
        //     return true;
        // }
        return ((Subsystems.counter.getBallCount() == 1) && !(Devices.shooterBeamBrake.get()));
    }
}
