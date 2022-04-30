package frc.robot.subsystem;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.Devices;
import frc.robot.util.AverageFinder;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;

public class CargoCounter extends StateMachine {
    
    private int ballCount = 0;
    private double thresholdBig = .7;
    private double thresholdsmol = .4;

    private static AverageFinder averageFinder = new AverageFinder(20);

    public int getBallCount(){
        return ballCount;
    }

    public void setBallCount(int value){
        ballCount = value;
    }

    public void resetBallCount(){
        ballCount = 0;
    }

    public CargoCounter(){
        super("wait");
    }

    @State(name = "wait") //wait until initial see
    public void runWait(StateContext ctx) {
        averageFinder.addSample(Devices.intakeBeamBrake.get()? 1 : 0); // -1 point Amory :<

        if (averageFinder.getAverage() > thresholdBig){
            this.setState("see");
        }
    }

    @State(name = "see") //wait until no see
    public void runDetect(StateContext ctx) {
        averageFinder.addSample(Devices.intakeBeamBrake.get()? 1 : 0);

        if (averageFinder.getAverage() < thresholdsmol){
            this.setState("add");
        }
    }

    @State(name = "add") //after plus one, wait
    public void runAdd(StateContext ctx) {
        ballCount ++;

        this.setState("wait");
    }
}
