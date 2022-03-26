package frc.robot.subsystem;


import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.LinearSystem;
import frc.robot.Devices;

public class DrivetrainSim {

    private final LinearSystem<N2, N2, N2> m_drivetrainSystem = LinearSystemId.identifyDrivetrainSystem(1.98, 0.2, 1.5,
            0.3);
    private final DifferentialDrivetrainSim m_drivetrainSimulator;

    public DrivetrainSim(double trackWidth, double wheelRadius) {
        m_drivetrainSimulator = new DifferentialDrivetrainSim(
                m_drivetrainSystem, DCMotor.getCIM(2), 8, trackWidth, wheelRadius, null);
    }

    /** Update our simulation. This should be run every robot loop in simulation. */
    public void simulate() {
        // To update our simulation, we set motor voltage inputs, update the
        // simulation, and write the simulated positions and velocities to our
        // simulated encoder and gyro. We negate the right side so that positive
        // voltages make the right side move forward.
        m_drivetrainSimulator.setInputs(
                Devices.leftMotor.getPercent() * RobotController.getInputVoltage(),
                Devices.rightMotor.getPercent() * RobotController.getInputVoltage());
        m_drivetrainSimulator.update(0.02);

        Devices.leftMotor.setEncoderPositionAndRate(m_drivetrainSimulator.getLeftPositionMeters(),
                m_drivetrainSimulator.getLeftVelocityMetersPerSecond());
        Devices.rightMotor.setEncoderPositionAndRate(m_drivetrainSimulator.getRightPositionMeters(),
                m_drivetrainSimulator.getRightVelocityMetersPerSecond());
        Devices.gyro.setAngle(-m_drivetrainSimulator.getHeading().getDegrees());
    }

}
