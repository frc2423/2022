package frc.robot.subsystem;

import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Devices;
import frc.robot.Subsystems;
import frc.robot.constants.NtKeys;
import frc.robot.devices.NeoMotor;
import frc.robot.util.ColourSensor;
import frc.robot.util.NtHelper;
//we are children dont judge us :P

public class Intake {

    private NeoMotor armMotor;
    private NeoMotor armMotorLeft;
    private NeoMotor rollerMotor;

    private DigitalInput leftLimit;
    private DigitalInput rightLimit;

    private double topPosition = 1;
    private double bottomPosition = -14.5;
    private double rollerSpeed = 0.50;
    
    private boolean isDown = false;
    private boolean shouldIntake = false;

    private double colorConfidenceThreshhold = 0.7;
    private double separationDelay = 0.5; // extra seconds to run intake to separate cargo
    private String allianceColor;
    private String oppositionColor;
    private MedianFilter allianceColorFilter = new MedianFilter(10);
    private MedianFilter oppositionColorFilter = new MedianFilter(10);
    private ColourSensor colourSensor;
    private Timer timer;

    private Alliance currentAlliance = DriverStation.getAlliance();

    public Intake() {
        NtHelper.setDouble(NtKeys.SVG_INTAKE_MAX_POSITION, bottomPosition);
        armMotor = Devices.intakeArmMotor;
        armMotorLeft = Devices.intakeArmFollowerMotor;
        rollerMotor = Devices.intakeRollerMotor;
        leftLimit = Devices.leftLimit;
        rightLimit = Devices.rightLimit;
        colourSensor = Devices.colourSensor;
        timer = new Timer();

        initColors(); 
    }

    private void initColors() {
        if (currentAlliance == Alliance.Blue) {
            allianceColor = "blue";
            oppositionColor = "red";
        }
        else {
            allianceColor = "red";
            oppositionColor = "blue";
        }
    }


    public void intakeUp(){
        rollerMotor.setPercent(0);
        if(!leftLimit.get()) {
            armMotorLeft.setPercent(0);
            armMotorLeft.resetEncoder(0);
        } else {
            armMotorLeft.setDistance(topPosition);
        }
        if(!rightLimit.get()) {
            armMotor.setPercent(0);
            armMotor.resetEncoder(0);
        } else {
            armMotor.setDistance(topPosition); 
        }
        isDown = false;
    }

    public void intakeDown(){
        rollerMotor.setPercent(rollerSpeed);
        armMotor.setDistance(bottomPosition);
        armMotorLeft.setDistance(bottomPosition);
        isDown = true;
    }

    public boolean isDown(){
        return isDown;
    }

    public boolean shouldIntake(){
        return shouldIntake;
    }

    public void run(){
        // detect current ball color
        double allianceColorConfidence = allianceColorFilter.calculate(colourSensor.isColor(allianceColor) ? 1 : 0);
        double oppositionColorConfidence = oppositionColorFilter.calculate(colourSensor.isColor(oppositionColor) ? 1 : 0);
        
        if(allianceColorConfidence  >= colorConfidenceThreshhold) {
            // have target color. let the intake belt run if it's the first ball
            if(Subsystems.cargoCounter.getNumCargo() == 0) {
                Subsystems.cargoCounter.addCargo();
                shouldIntake = true;
                timer.start();
            }
            else if (Subsystems.cargoCounter.getNumCargo() == 1) {
                // on the second cargo, we want to stop the belt
                // as soon as we see it (rising edge counting)
                Subsystems.cargoCounter.addCargo();
                shouldIntake = false;
            }
            else {
                // more than 2 cargo, should not be here!
            }
        }
        else if (oppositionColorConfidence >= colorConfidenceThreshhold ){
            // have opposite color. abort! (not sure if this is needed)
            timer.stop();
            timer.reset();
            shouldIntake = false;
        }

        // in the 1-cargo case, stop the belt motor after a bit of a delay
        if(timer.get() > separationDelay) {
            timer.stop();
            timer.reset();
            shouldIntake = false;
        }

    }

}