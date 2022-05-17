package frc.robot.subsystem.util;

import java.util.Map;
import java.util.HashMap;

public class TurretDistanceMapper {

    private Map<Double, Double> distxAngleMap = new HashMap<Double, Double>();

    public TurretDistanceMapper() {
        distxAngleMap.put(5.0, 60.0);
        distxAngleMap.put(10.0, 45.0);
        distxAngleMap.put(15.0, 40.0);
        distxAngleMap.put(20.0, 37.0);
    }

    private double lerp(double x1, double x2, double y1, double y2, double x) {
        return 0;
    }

    private double getClosestDistanceLessThan(double distance) {
        return 0;
    }    
    
    private double getClosestDistanceGreaterThan(double distance) {
        return 0;
    }

    private double getAngleFromDistance(double distance) {
        return 0;
    }

    public double getHoodAngle(double distance) {
        double lessThan = getClosestDistanceLessThan(distance);
        double greaterThan = getClosestDistanceGreaterThan(distance);
        double lessThanAngle = getAngleFromDistance(lessThan);
        double greaterThanAngle = getAngleFromDistance(greaterThan);
        return lerp(lessThan, greaterThan, lessThanAngle, greaterThanAngle, distance);
    }
}
