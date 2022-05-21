package frc.robot.subsystem;

import frc.robot.util.stateMachine.StateMachine;
import frc.robot.Devices;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;

public class CargoCounter extends StateMachine {
    
    private int ballCount = 0;
    private boolean enabled = true;
    private int beamsBroken;

    public int getBallCount(){
        beamsBroken = 0;
        if (Devices.intakeBeamBrake.get())
        beamsBroken ++;
        if (Devices.shooterBeamBrake.get())
        beamsBroken ++;
        return beamsBroken;
    }

    public void setBallCount(int value){
        ballCount = value;
    }

    public void resetBallCount(){
        ballCount = 0;
    }

    public CargoCounter(){
        super("see");
    }

    public void enable(){
        enabled = true;
    }

    public void disable(){
        enabled = false;
    }

    @State(name = "wait") //wait until initial see
    public void runWait(StateContext ctx) {
        if (Devices.intakeBeamBrake.get()){
            this.setState("add");
        }
    }

    @State(name = "see") //wait until no see
    public void runDetect(StateContext ctx) {
        if (!Devices.intakeBeamBrake.get()){
            this.setState("wait");
        }
    }

    @State(name = "add") //after plus one, wait
    public void runAdd(StateContext ctx) {
        if (enabled){
            ballCount ++;
        }

        this.setState("see");
    }
}
