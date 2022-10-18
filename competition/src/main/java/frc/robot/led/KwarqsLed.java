package frc.robot.led;

public class KwarqsLed {
    private LedController ledController = new LedController(60);

    public KwarqsLed() {
        ledController.add("halfAndHalf", new HalfAndHalf());
        ledController.add("dark", new Dark());
        ledController.add("snake", new Snake());
        ledController.add("rainbow", new rainbow());
        ledController.add("morsecode", new morsecode());
        ledController.add("LedRotating", new LedRotating());
        ledController.add("LedFade", new LedFade());
        ledController.add("LedAlternate", new LedAlternate());
    }

    public void disable() {
        ledController.set("dark");
    }

    private String getRandomLedMode() {
        var randomLed = 0;

        switch (randomLed) {
            case 0:
                return "rainbow";
            case 1:
                return "LedRotating";
            case 2:
                return "LedFade";
            default:
                return "LedAlternate";
        }
    }

    public void setRandom() {
        ledController.set(getRandomLedMode());
    }

    public void run() {
        ledController.run();
    }

}
