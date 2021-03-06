package frc.robot.devices;
import java.util.List;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import edu.wpi.first.math.util.Units;

public class Camera {

  // Constants such as camera and target height stored. Change per robot and goal!
  double CAMERA_HEIGHT_METERS = Units.inchesToMeters(24);
  double TARGET_HEIGHT_METERS = Units.feetToMeters(5);

  // Angle between horizontal and the camera.
  double CAMERA_PITCH_RADIANS = Units.degreesToRadians(0);

  // How far from the target we want to be
  double GOAL_RANGE_METERS = Units.feetToMeters(3);

  // Change this to match the name of your camera
  public PhotonCamera camera;

  public Camera(String name) {
    camera = new PhotonCamera(name);

  }
  
  public void cameraSetUp(double cameraHeight, double cameraPitch, double targetHight, double goalRange){
    CAMERA_HEIGHT_METERS = cameraHeight;
    TARGET_HEIGHT_METERS = targetHight;
    CAMERA_PITCH_RADIANS = cameraPitch;
    GOAL_RANGE_METERS = goalRange;
  }

  public double FindAverage(List<PhotonTrackedTarget> reflectiveTargets){
      double sum = 0;
      for(int i=0; i<reflectiveTargets.size();i++){
        PhotonTrackedTarget target = reflectiveTargets.get(i);
        sum += target.getYaw();
      }
      
      return sum / reflectiveTargets.size();
    }
    
    public boolean haveTargets(){
      var result = camera.getLatestResult();
      return result.hasTargets();
    }

    public void setPipeline(int shiny){
      camera.setPipelineIndex(shiny);
    }

    public PhotonPipelineResult getLatestResult(){
      return camera.getLatestResult();
    }
}
