package frc.robot.devices;
import frc.robot.util.AverageFinder;

import edu.wpi.first.wpilibj.DigitalInput;


public class BeamBreak {
    private AverageFinder average;
    private DigitalInput beamBreak;
    private double thresholdBig = .7;

    public BeamBreak(DigitalInput device){
      average = new AverageFinder(10);
      beamBreak = device;
    }

    public boolean get(){
      average.addSample(beamBreak.get()? 1 : 0);

      if (average.getAverage() > thresholdBig){
        return true;
      } else {
        return false;
      }
    }


}
