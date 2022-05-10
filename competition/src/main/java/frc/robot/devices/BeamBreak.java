package frc.robot.devices;
import frc.robot.util.AverageFinder;

public class BeamBreak {
    AverageFinder average;
    public BeamBreak(int n){
      average = new AverageFinder(10);
    }

    
}
