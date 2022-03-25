package frc.robot.constants;

public class NtKeys {
    // Shooter
    public static final String IS_AUTO_AIM = "/robot/shooter/isAuto";
    public static final String SHOOTER_STATE = "/robot/shooter/state";
    public static final String SHOOTER_SPEED = "/robot/shooter/shooterspeed";
    public static final String DESIRED_SHOOTER_SPEED = "/robot/shooter/desiredshooterspeed";
    public static final String DESIRED_KICKER_SPEED = "/robot/shooter/desiredkickerspeed";
    
    // Drivetrain
    public static final String GYRO_ANGLE = "/robot/gyro";
    // SVG robot for dashboard
    public static final String SVG_ALLIANCE_COLOR = "/robot/svg/allianceColor";
    public static final String SVG_CARGO_COUNT = "/robot/svg/ballCount";
    public static final String SVG_ROTATIONS_PER_SECOND = "/robot/svg/rotationsPerSecond";
    public static final String SVG_INTAKE_POSITION = "/robot/svg/robotArmSetpoint";
    public static final String SVG_INTAKE_MAX_POSITION = "/robot/svg/robotArmSetpointMax";
    // Auto mode
    public static final String AUTO_MODE_NAME = "/robot/auto/name";
    public static final String AUTO_MODE_ROBOT_POSITION = "/robot/auto/position";
    // Climber
    public static final String CLIMBER_DESIRED_STATE = "/robot/climber/desiredState";
    public static final String CLIMBER_CURRENT_STATE = "/robot/climber/state";
    public static final String CLIMB_MEDIUM_BAR = "/robot/climber/isMediumBar";
    public static final String CLIMBER_DESIRED_POSITION = "/robot/climber/desiredPosition";
    public static final String CLIMBER_LEFT_POSITION = "/robot/climber/leftPosition";
    public static final String CLIMBER_RIGHT_POSITION = "/robot/climber/rightPosition";
    public static final String CLIMBER_IS_LEFT_LIMIT_PRESSED = "/robot/climber/isLeftLimitPressed";
    public static final String CLIMBER_IS_RIGHT_LIMIT_PRESSED = "/robot/climber/isRightLimitPressed";
    // Intake
    public static final String CARGO_COUNT = "/robot/cargoCount";
    public static final String LEFT_INTAKE_POSITION = "/robot/intake/leftdistance";
    public static final String RIGHT_INTAKE_POSITION = "/robot/intake/rightdistance";
    public static final String INTAKE_STATE = "/robot/intake/state";
    public static final String INTAKE_LEFT_LIMIT_PRESSED = "/robot/intake/leftLimitPressed";
    public static final String INTAKE_RIGHT_LIMIT_PRESSED = "/robot/intake/rightLimitPressed";
    public static final String DETECTED_CARGO_COLOR = "/robot/intake/cargoColor";
    public static final String INTAKE_ARM_STATE = "/robot/intake/armState";
  
}
