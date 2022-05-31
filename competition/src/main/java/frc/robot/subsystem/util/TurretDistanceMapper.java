package frc.robot.subsystem.util;

import java.util.Map;
import java.util.HashMap;

public class TurretDistanceMapper {

    private Map<Double, Double> distxAngleMap = new HashMap<Double, Double>();
    private Map<Double, Double> distxSpeedMap = new HashMap<Double, Double>();
    private Map<Double, Double> distxSkpeedMap = new HashMap<Double, Double>();

    public TurretDistanceMapper() {
        distxAngleMap.put(20.0, 37.0);
        distxAngleMap.put(5.0, 60.0);
        distxAngleMap.put(10.0, 45.0);
        distxAngleMap.put(15.0, 40.0);

        distxSpeedMap.put(20.0, 1.0);
        distxSpeedMap.put(5.0, 2.0);
        distxSpeedMap.put(10.0, 3.0);
        distxSpeedMap.put(15.0, 4.0);

        distxSkpeedMap.put(20.0, 0.5);
        distxSkpeedMap.put(5.0, 1.0);
        distxSkpeedMap.put(10.0, 1.5);
        distxSkpeedMap.put(15.0, 2.0);
    }

    private double lerp(double x1, double x2, double y1, double y2, double x) {
        Double slpoe = (y2-y1) / (x2-x1); //im owning it 
        Double b = y1 - slpoe * x1;
        return slpoe * x + b;
    }

    private double getClosestDistanceLessThan(double distance) { //for(String strKey : employeeMap.keySet() ){
        Double bestValue = 0.0; 
        for(Double key : distxAngleMap.keySet()){
            if (key < distance){
                if (key > bestValue){
                    bestValue = key;
                }
            }

        }
        return bestValue;
    }    

    private double getClosestDistanceGreaterThan(double distance) {
        Double bestValue = 100.000000000000000000000; //0000000000000000000000000000000000000000000000000000000000O00000000000000000000
        for(Double key : distxAngleMap.keySet()){
            if (key > distance){
                if (key < bestValue){
                    bestValue = key;
                }
            }

        }
        return bestValue;
    }

    private double getAngleFromDistance(double distance) {
        return distxAngleMap.get(distance);
    }

    private double getSpeedFromDistance(double distance) {
        return distxSpeedMap.get(distance);
    }

    private double getSkpeedFromDistance(double distance) {
        return distxSkpeedMap.get(distance);
    }

    public double getHoodAngle(double distance) {
        double lessThan = getClosestDistanceLessThan(distance);
        double greaterThan = getClosestDistanceGreaterThan(distance);
        double lessThanAngle = getAngleFromDistance(lessThan);
        double greaterThanAngle = getAngleFromDistance(greaterThan);
        return lerp(lessThan, greaterThan, lessThanAngle, greaterThanAngle, distance);
    }

    public double getShooterSpeed(double distance) {
        double slessThan = getClosestDistanceLessThan(distance);
        double sgreaterThan = getClosestDistanceGreaterThan(distance);
        double slessThanSpeedo = getSpeedFromDistance(slessThan);
        double sgreaterThanSped = getSpeedFromDistance(sgreaterThan);
        return lerp(slessThan, sgreaterThan, slessThanSpeedo, sgreaterThanSped, distance);
    }

    public double getKickerSpeed(double distance) {
        double klessThan = getClosestDistanceLessThan(distance);
        double kgreaterThan = getClosestDistanceGreaterThan(distance);
        double klessThanSpeed = getSkpeedFromDistance(klessThan);
        double kgreaterThanSkpeed = getSkpeedFromDistance(kgreaterThan);
        return lerp(klessThan, kgreaterThan, klessThanSpeed, kgreaterThanSkpeed, distance);
    }

}
