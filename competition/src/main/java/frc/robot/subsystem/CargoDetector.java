package frc.robot.subsystem;

import frc.robot.Devices;
import frc.robot.util.AverageFinder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class CargoDetector {

    private String allianceColor;
    private String otherColor;
    private AverageFinder allianceColorAverage = new AverageFinder(10);
    private AverageFinder otherColorAverage = new AverageFinder(10);
    private double colorConfidenceThreshhold = 0.7;
    private double colorUnconfidenceThreshhold = 0.5;

    private String currentValue = "none";
    private String previousValue = "none";

    public CargoDetector() {
        if (DriverStation.getAlliance() == Alliance.Blue) {
            allianceColor = "blue";
            otherColor = "red";
        } else {
            allianceColor = "red";
            otherColor = "blue";
        }
    }

    public boolean isDetected(boolean isAllianceColor) {
        double average = isAllianceColor
                ? allianceColorAverage.getAverage()
                : otherColorAverage.getAverage();

        String colorType = isAllianceColor ? "alliance" : "other";

        if (currentValue.equals(colorType)) {
            return average > colorUnconfidenceThreshhold;
        } else {
            return average > colorConfidenceThreshhold;
        }
    }

    public boolean hasChanged() {
        return !currentValue.equals(previousValue);
    }

    public void run() {
        allianceColorAverage.addSample(Devices.colourSensor.isColor(allianceColor) ? 1 : 0);
        otherColorAverage.addSample(Devices.colourSensor.isColor(otherColor) ? 1 : 0);
        previousValue = currentValue;
        if (isDetected(true)) {
            currentValue = "alliance";
        } else if (isDetected(false)) {
            currentValue = "other";
        } else {
            currentValue = "none";
        }
    }
}
