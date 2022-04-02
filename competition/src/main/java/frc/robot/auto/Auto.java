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

    public Auto() {
        super("run");
        taxi = new Taxi();
        shootOneAndTaxi = new ShootOneAndTaxi();
        shootTwoTaxi = new ShootTwoTaxi();
        shootOneAndShootTwo = new ShootOneAndShootTwo();
        shootTwoAndShootOne = new ShootTwoAndShootOne();
        testAuto = new TestAuto();
        NtHelper.setString(NtKeys.AUTO_MODE_NAME, "shootTwoTaxi");
    }

    public void getAuto() {
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
            default:
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
