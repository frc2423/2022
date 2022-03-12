package frc.robot.auto;

import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.InitState;
import frc.robot.util.stateMachine.RunState;
import frc.robot.util.stateMachine.StateMachine;

public class Auto extends StateMachine {

    StateMachine selectedAutonomous;
    StateMachine taxi;
    StateMachine simple1;
    StateMachine shootTwo;
    StateMachine threeMuskets;
    StateMachine fiveMuskets;
    
    public Auto(){
        super("init");
        taxi = new taxi();
        simple1 = new SimpleAuto();
        shootTwo = new shootTwoTaxi();
        threeMuskets = new ThreeMuskets();
        fiveMuskets = new ThreeMuskets5();

        NtHelper.setString("/robot/auto/name", "noAuto");
    }

    public void getAuto(){
        String name = NtHelper.getString("/robot/auto/name", "taxi1");
        System.out.println(name);
        switch (name) {
            case "taxi1":
                selectedAutonomous = taxi;
                break;
            case "simpleAuto2":
                selectedAutonomous = simple1;
                break;
            case "shootTwoTaxi3":
                selectedAutonomous = shootTwo;
                break;
            case "threeMuskets4":
                selectedAutonomous = threeMuskets;
                break;
            case "threeMuskets5ive":
                selectedAutonomous = fiveMuskets;
                break;
            default:
                selectedAutonomous = taxi; // I'm sure this will cause no problems in the future :)
                break; //you evil evil man.... Adrien
        }
    }

    @RunState(name = "init")
    public void runInit(){
        setState("run");
    }

    @InitState(name = "run")
    public void initRun(){
        getAuto();
        System.out.println (selectedAutonomous);
        selectedAutonomous.setState(selectedAutonomous.getDefaultState());
    }

    @RunState(name = "run")
    public void runRun(){
        selectedAutonomous.run();
    }
}
