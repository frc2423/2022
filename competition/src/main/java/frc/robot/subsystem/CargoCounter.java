package frc.robot.subsystem;

public class CargoCounter {

    private int numCargo = 0;
    
    public CargoCounter() {
        numCargo = 0;
    }

    public void addCargo() {
        numCargo += 1;
    }

    public int getNumCargo() {
        return numCargo;
    }
}
