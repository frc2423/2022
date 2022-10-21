package frc.robot.led;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;

public class morsecode implements Led {
    private Timer timer = new Timer();
    private int step = 0;
    public void start(AddressableLEDBuffer buffer, int length) {
        timer.start();
        timer.reset();
    }

     public void run(AddressableLEDBuffer buffer, int length) {
        if (true) {
        if (timer.get() > 3) {
            step++;
            for (var i = 0; i < buffer.getLength(); i++) {
                buffer.setRGB(i,0,125,0);
            }
            timer.reset();
        }
        if (timer.get() > 3) {
            step++;
            for (var i = 0; i < buffer.getLength(); i++) {
                buffer.setRGB(i,100,100,0);
            }
            timer.reset();
        }
        }

    }

    public void end(AddressableLEDBuffer buffer, int length) {
        timer.stop();
    }
}
