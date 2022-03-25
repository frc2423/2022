package frc.robot.auto;

import frc.robot.constants.NtKeys;
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
    StateMachine testAuto;
    StateMachine shootOneAndShootTwoSeparate;

    
    public Auto(){
        super("init");
        taxi = new Taxi();
        shootOneAndTaxi = new ShootOneAndTaxi();
        shootTwoTaxi = new ShootTwoTaxi();
        shootOneAndShootTwo = new ShootOneAndShootTwo();
        shootTwoAndShootOne = new ShootTwoAndShootOne();
        testAuto = new TestAuto();
        shootOneAndShootTwoSeparate = new ShootOneAndShootTwoSeparate();
        NtHelper.setString(NtKeys.AUTO_MODE_NAME, "shootTwoTaxi");
    }

    public void getAuto(){
        String name = NtHelper.getString(NtKeys.AUTO_MODE_NAME, "taxi");
        // selectedAutonomous = testAuto;
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
            case "shootOneAndShootTwoSeparate":
                selectedAutonomous = shootOneAndShootTwoSeparate;
                break;
            default:
                selectedAutonomous = taxi;
                break;
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
