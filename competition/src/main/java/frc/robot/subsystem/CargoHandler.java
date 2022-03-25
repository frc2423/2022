package frc.robot.subsystem;

import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Subsystems;

public class CargoHandler extends StateMachine {

    private final double REJECTION_TIME = 1; // seconds to run belt motor backwards on bad cargo
    private Timer rejectionTimer = new Timer();

    public CargoHandler() {
        super("belowCapacity");
    }

    @RunState(name = "belowCapcity")
    public void belowCapcity() {
        Subsystems.cargoDetector.runDetection();

        if (Subsystems.cargoDetector.hasChanged()) {
            if (Subsystems.cargoDetector.isDetected(true)) {
                Subsystems.cargoCounter.addCargo();
            } else if (Subsystems.cargoDetector.isDetected(false)) {
                setState("reject");
                return;
            }
        }

        if (isAtCapacity()) {
            setState("atCapicity");
        }
    }

    @RunState(name = "atCapacity")
    public void atCapacity() {
        if (!isAtCapacity()) {
            setState("belowCapcity");
        }
    }

    @InitState(name = "reject")
    public void initReject() {
        rejectionTimer.reset();
        rejectionTimer.start();
    }

    @RunState(name = "reject")
    public void reject() {
        if (rejectionTimer.get() > REJECTION_TIME) {
            if (isAtCapacity()) {
                setState("atCapacity");
            } else {
                setState("belowCapacity");
            }
        }
    }

    public boolean isAtCapacity() {
        return Subsystems.cargoCounter.getNumCargo() > 1;
    }
}
