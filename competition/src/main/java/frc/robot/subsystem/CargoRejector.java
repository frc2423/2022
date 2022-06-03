package frc.robot.subsystem;

import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.util.stateMachine.StateMachine;
import frc.robot.util.AverageFinder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Devices;

public class CargoRejector extends StateMachine {

    private String otherColor;
    private AverageFinder otherColorAverage = new AverageFinder(10);
    private double colorConfidenceThreshhold = 0.7;

    public CargoRejector(){
        super("inactive");
        if (DriverStation.getAlliance() == Alliance.Blue) {
            otherColor = "red";
        } else {
            otherColor = "blue";
        }
    }

    @State (name = "inactive")
    private void inactive(StateContext ctx) {
        // otherColorAverage.addSample(Devices.colourSensor.isColor(otherColor) ? 1 : 0);
        // if (otherColorAverage.getAverage() > colorConfidenceThreshhold){
        //     otherColorAverage.resetSamples();
        //     setState("rejection");


        // }
    }

    @State (name = "rejection")
    private void rejection(StateContext ctx) {
        if (ctx.getTime() > .75){
            setState("forward");
        }
    }

    @State (name = "forward")
    private void forward(StateContext ctx) {
        if (ctx.getTime() > .75){
            setState("inactive");                                     // avocado >~<
        }
    }

    public boolean isRejecting(){
        return getState() == "rejection";
    }

    public boolean isForwarding(){
        return getState() == "forward";
    }
}