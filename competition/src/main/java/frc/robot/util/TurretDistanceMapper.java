package frc.robot.util;

import java.util.Map;
import java.util.HashMap;

public class TurretDistanceMapper {

    private Map<Double, Double> distxAngleMap = new HashMap<Double, Double>();
    private Map<Double, Double> distxSpeedMap = new HashMap<Double, Double>(); // shooter
    private Map<Double, Double> distxSkpeedMap = new HashMap<Double, Double>(); // kicker

    public double getShootValue(double value) {
        return value + NtHelper.getDouble("/robot/mapper/offsetSpeed", 2.5);
    }

    public TurretDistanceMapper() {
        NtHelper.setDouble("/robot/mapper/offsetSpeed", 0);

        distxAngleMap.put(0.0, 0.0); // distance, encoder value ([0, -15])
    
        distxAngleMap.put(2.59, 0.0); //4.0 ft.
        distxAngleMap.put(3.5, -0.8); //6.0 ft.
        distxAngleMap.put(4.38, -1.9); //8.0 ft.
        distxAngleMap.put(5.3, -2.0); 
        distxAngleMap.put(7.07, -2.35);
        distxAngleMap.put(8.48, -2.6); //14.0 ft
        distxAngleMap.put(100.0, -2.6);

        distxSpeedMap.put(0.0, -55.0); // feet, voltage? shooter motor
        distxSpeedMap.put(2.59, -55.0);
        distxSpeedMap.put(3.5, -57.5);
        distxSpeedMap.put(4.38, -60.0);
        distxSpeedMap.put(5.3, -65.0);
        distxAngleMap.put(7.07, -65.0);
        distxAngleMap.put(8.48, -70.0);
        distxAngleMap.put(100.0, -70.0);

        distxSkpeedMap.put(0.0, -25.0); // feet, voltage? kicker motor
        distxSkpeedMap.put(2.59, -25.0);
        distxSkpeedMap.put(3.5, -30.0);
        distxSkpeedMap.put(4.38, -30.0);
        distxSkpeedMap.put(5.3, -35.0);
        distxAngleMap.put(7.07, -40.0);
        distxAngleMap.put(8.48, -45.0);
        distxAngleMap.put(100.0, -45.0);
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
        double boundedDistance = Math.max(0, distance);
        double lessThan = getClosestDistanceLessThan(boundedDistance);
        double greaterThan = getClosestDistanceGreaterThan(boundedDistance);
        double lessThanAngle = getAngleFromDistance(lessThan);
        double greaterThanAngle = getAngleFromDistance(greaterThan);
        return lerp(lessThan, greaterThan, lessThanAngle, greaterThanAngle, boundedDistance);
    }

    public double getShooterSpeed(double distance) {
        double boundedDistance = Math.max(0, distance);
        double slessThan = getClosestDistanceLessThan(boundedDistance);
        double sgreaterThan = getClosestDistanceGreaterThan(boundedDistance);
        double slessThanSpeedo = getSpeedFromDistance(slessThan);
        double sgreaterThanSped = getSpeedFromDistance(sgreaterThan);
        double value = lerp(slessThan, sgreaterThan, slessThanSpeedo, sgreaterThanSped, boundedDistance);
        return getShootValue(value);
    }

    public double getKickerSpeed(double distance) {
        double boundedDistance = Math.max(0, distance);
        double klessThan = getClosestDistanceLessThan(boundedDistance);
        double kgreaterThan = getClosestDistanceGreaterThan(boundedDistance);
        double klessThanSpeed = getSkpeedFromDistance(klessThan);
        double kgreaterThanSkpeed = getSkpeedFromDistance(kgreaterThan);
        return lerp(klessThan, kgreaterThan, klessThanSpeed, kgreaterThanSkpeed, boundedDistance);
    }

}