package frc.robot.auto;

import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;

public class Auto extends StateMachine {

    StateMachine selectedAutonomous;
    StateMachine taxi;
    StateMachine shootOneAndTaxi;
    StateMachine shootTwoTaxi;
    StateMachine shootOneAndShootTwo;
    StateMachine shootTwoAndShootOne;
    
    public Auto(){
        super("init");
        taxi = new Taxi();
        shootOneAndTaxi = new ShootOneAndTaxi();
        shootTwoTaxi = new ShootTwoTaxi();
        shootOneAndShootTwo = new ShootOneAndShootTwo();
        shootTwoAndShootOne = new ShootTwoAndShootOne();

        NtHelper.setString("/robot/auto/name", "shootOneAndTaxi");
    }

    public void getAuto(){
        String name = NtHelper.getString("/robot/auto/name", "taxi");
        System.out.println(name);
        switch (name) {
            case "taxi":
                selectedAutonomous = taxi;
                break;
            case "shootOneAndTaxi":
                selectedAutonomous = shootOneAndTaxi;
                break;
            case "shootTwoTaxi":
                selectedAutonomous = shootTwoTaxi;
                break;
            case "shootOneAndShootTwo":
                selectedAutonomous = shootOneAndShootTwo;
                break;
            case "shootTwoAndShootOne":
                selectedAutonomous = shootTwoAndShootOne;
                break;
            default:
                selectedAutonomous = taxi; // I'm sure this will cause no problems in the future :)
                break; // Adrien, you evil evil man
        }
    }

    @RunState(name = "init")
    public void runInit(){
        setState("run");
    }

    @InitState(name = "run")
    public void initRun(){
        getAuto();
        selectedAutonomous.setState(selectedAutonomous.getDefaultState());
    }

    @RunState(name = "run")
    public void runRun(){
        selectedAutonomous.run();
    }
}
