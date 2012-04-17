import lejos.nxt.*;
import lejos.robotics.navigation.*;

// The  library for this project must include  classes.jar from  NXJ_HOME

/**
 * This class builds and runs a robot.
 * It should be the main class of this project, and probably should be renamed
 * @author owner
 */
// use this  class to build and run your robot
public class TrackerTest1 {

     public static void main(String[] args)
  {
    
    DifferentialPilot pilot = new DifferentialPilot(5.6f, 13.8f, Motor.A, Motor.C);
     LightSensor left = new LightSensor(SensorPort.S1);
    LightSensor right = new LightSensor(SensorPort.S4);
    Tracker tracker = new Tracker(pilot, left, right);
     System.out.println("GO");
     Button.waitForAnyPress();

    tracker.calibrate();
    int direction = 1;
    for (int s = 0; s < 4; s++)
    {
      for (int i = 0; i < 4; i++)
      {
        tracker.trackLine();
        tracker.trackLine();
        tracker.turn(direction);   
      }
        
       direction *= -1;
   
    }
  }

}

