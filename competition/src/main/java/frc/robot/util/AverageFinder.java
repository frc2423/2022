package frc.robot.util;

import java.util.ArrayList;

public class AverageFinder {

    private int sampleCount = 0;
    private ArrayList<Double> samples;

    public AverageFinder(int sampleCount) {
        this.sampleCount = sampleCount;
        samples = new ArrayList<Double>();
    }

    public void addSample(double sample) {
        samples.add(sample);
        if (samples.size() > sampleCount) {
            samples.remove(0);
        }
    }

    public double getAverage() {
        double total = 0;
        for (int i = 0; i < samples.size(); i++) {
            total = total + samples.get(i);
        }
        return total / samples.size();
    }
}