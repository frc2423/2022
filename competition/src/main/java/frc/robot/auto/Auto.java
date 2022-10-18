package frc.robot.auto;

import frc.robot.constants.NtKeys;
import frc.robot.util.NtHelper;
import frc.robot.util.stateMachine.State;
import frc.robot.util.stateMachine.StateContext;
import frc.robot.util.stateMachine.StateMachine;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto extends StateMachine {

    StateMachine selectedAutonomous;
    StateMachine taxi;
    StateMachine shootOneAndTaxi;
    StateMachine shootTwoTaxi;
    StateMachine shootOneAndShootTwo;
    StateMachine shootTwoAndShootOne;
    StateMachine testAuto;
    StateMachine shootTwoShootOne;
    StateMachine shootOneGetTwo;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();

    public Auto() {
        super("run");
        taxi = new Taxi();
        shootOneAndTaxi = new ShootOneAndTaxi();
        shootTwoTaxi = new ShootTwoTaxi();
        shootOneAndShootTwo = new ShootOneAndShootTwo();
        testAuto = new TestAuto();
        shootOneGetTwo = new ShootOneGetTwo();

        NtHelper.setString(NtKeys.AUTO_MODE_NAME, "shootOneAndShootTwo");

        m_chooser.setDefaultOption("shootOneAndTaxi", "shootOneAndTaxi");
        m_chooser.addOption("taxi", "taxi");
        m_chooser.addOption("shootOneAndTaxi", "shootOneAndTaxi");
        m_chooser.addOption("shootTwoTaxiTop", "shootTwoTaxiTop");
        m_chooser.addOption("shootTwoTaxiMiddle", "shootTwoTaxiMiddle");
        m_chooser.addOption("shootTwoTaxiBottom", "shootTwoTaxiBottom");
        m_chooser.addOption("shootOneAndShootTwo", "shootOneAndShootTwo");        
        m_chooser.addOption("shootOneGetTwo", "shootOneGetTwo");


        SmartDashboard.putData("Auto choices", m_chooser);
    }

    public void getAuto() {
        String name = m_chooser.getSelected();


        switch (name) {
            case "taxi": //0 ball
                selectedAutonomous = taxi;
                break;
            case "shootOneAndTaxi": //1 ball
                selectedAutonomous = shootOneAndTaxi;
                break;
            case "shootTwoTaxiTop": //two ball
                NtHelper.setString("/robot/position", "top");
                selectedAutonomous = shootTwoTaxi;
                break;
            case "shootTwoTaxiMiddle": //two ball
                NtHelper.setString("/robot/position", "middle");
                selectedAutonomous = shootTwoTaxi;
                break;
            case "shootTwoTaxiBottom": //two ball
                NtHelper.setString("/robot/position", "bottom");
                selectedAutonomous = shootTwoTaxi;
                break;
            case "shootOneAndShootTwo": //3 ball
                selectedAutonomous = shootOneAndShootTwo;
                break;
            case "shootOneGetTwo":
                selectedAutonomous = shootOneGetTwo;
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
