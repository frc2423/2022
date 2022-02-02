package frc.robot.devices;
import java.util.List;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

public class Camera {

    public double FindAverage(List<PhotonTrackedTarget> reflectiveTargets){
    
        double sum = 0;
        for(int i=0; i<reflectiveTargets.size();i++){
          PhotonTrackedTarget target = reflectiveTargets.get(i);
          sum += target.getYaw();
        }
        
        
        return sum / reflectiveTargets.size();
      }
    
    public boolean haveTargets(){

    }

    public double getAverageYaw(){

    }

    public void setPipeline(String shiny){

    }
}
