package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.NtHelper;

//we are children dont judge us :P

public class Intake {
    private NeoMotor rollerMotor;
    private double rollerSpeed = 0.50;

    private boolean isDown = false;
    private DoubleSolenoid solenoid;

    public Intake() {
        rollerMotor = Devices.intakeRollerMotor;
        solenoid = Devices.solenoid;   
    }


    private void intakeUp() {
        rollerMotor.setPercent(0);
        solenoidUp();
    }

    private void intakeDown() {
        if (Subsystems.counter.getBallCount() == 2) {
            rollerMotor.setPercent(0);
        } else {
            if (Subsystems.cargoRejector.isRejecting()) {
                rollerMotor.setPercent(-rollerSpeed);
            } else {
                rollerMotor.setPercent(rollerSpeed);
            }
        }
       solenoidDown();
    }

    public boolean isDown() {
        return isDown;
    }

    public void goUp() {
        isDown = false;
    }

    public void goDown() {
        isDown = true;
    }

    private void solenoidUp() {
        solenoid.set(DoubleSolenoid.Value.kForward);
    
    }
    private void solenoidDown() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void runIntake() {
         if (!isDown()) {
            intakeUp();
        } else {
            intakeDown();
        }
    }

}
