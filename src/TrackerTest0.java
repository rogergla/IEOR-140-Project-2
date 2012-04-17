import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;


/**
 * Test of tracker.trackline() method - runs around oval track indefinitely
 * @author glassey
 */

public class TrackerTest0 {

     public static void main(String[] args)
  {

     DifferentialPilot pilot = new DifferentialPilot(5.6f, 13.8f, Motor.A, Motor.C);
     LightSensor left = new LightSensor(SensorPort.S1);
    LightSensor right = new LightSensor(SensorPort.S4);
    Tracker0 tracker = new Tracker0(pilot, left, right);
     pilot.setTravelSpeed(20);
     System.out.println("GO");
     Button.waitForAnyPress();

     tracker.calibrate();
     tracker.trackLine();
    
  }

}

