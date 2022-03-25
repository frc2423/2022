package frc.robot.subsystem;
import frc.robot.Subsystems;
import frc.robot.Devices;

public class Belt {
    public void runStorage() {
        if (Subsystems.shooter.isShoot()) {
            Devices.beltMotor.setPercent(-0.2);
        } else if (Subsystems.cargoHandler.getState().equals("reject")) {
            Devices.beltMotor.setPercent(0.2);
        } else if (Devices.controller.getXButton()) {
            Devices.beltMotor.setPercent(-0.2);
        } else if (Subsystems.intake.isDown()) {
            boolean atCapacity = Subsystems.cargoHandler.getState().equals("atCapacity");
            Devices.beltMotor.setPercent(atCapacity ? 0 : -0.2);
        } else if (Devices.controller.getBButton()) {
            Devices.beltMotor.setPercent(0.2);
        } else {
            Devices.beltMotor.setPercent(0);
        }

    }
}
