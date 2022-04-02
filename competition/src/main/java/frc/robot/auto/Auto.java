package frc.robot.auto;

import frc.robot.constants.NtKeys;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.util.stateMachine.StateMachine;

public class Auto extends StateMachine {

    StateMachine selectedAutonomous;
    StateMachine taxi;
    StateMachine shootOneAndTaxi;
    StateMachine shootTwoTaxi;
    StateMachine shootOneAndShootTwo;
    StateMachine shootTwoAndShootOne;
    StateMachine testAuto;
    StateMachine shootTwoShootOne;

    public Auto() {
        super("run");
        taxi = new Taxi();
        shootOneAndTaxi = new ShootOneAndTaxi();
        shootTwoTaxi = new ShootTwoTaxi();
        shootOneAndShootTwo = new ShootOneAndShootTwo();
        testAuto = new TestAuto();
        NtHelper.setString(NtKeys.AUTO_MODE_NAME, "taxi");
    }

    public void getAuto() {
        String name = NtHelper.getString(NtKeys.AUTO_MODE_NAME, "taxi");
        // selectedAutonomous = testAuto;
        switch (name) {
            case "taxi": //0 ball
                selectedAutonomous = taxi;
                break;
            case "shootOneAndTaxi": //1 ball
                selectedAutonomous = shootOneAndTaxi;
                break;
            case "shootTwoTaxi": //two ball
                selectedAutonomous = shootTwoTaxi;
                break;
            case "shootOneAndShootTwo": //3 ball
                selectedAutonomous = shootOneAndShootTwo;
                break;
            default: //default, 0 ballll
                selectedAutonomous = taxi;
                break;
        }
    }

    public void restart() {
        setState("run");
    }

    @State(name = "run")
    public void runState(StateContext ctx) {
        if (ctx.isInit()) {
            getAuto();
            selectedAutonomous.setState(selectedAutonomous.getDefaultState());
        }
        selectedAutonomous.run();
    }
}
