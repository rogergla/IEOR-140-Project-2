
import lejos.nxt.*;
import lejos.util.*;
import java.io.*;
import lejos.robotics.navigation.DifferentialPilot;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author duttasho
 */
public class Tracker {

    LightSensor leftEye = new LightSensor(SensorPort.S1);
    LightSensor rightEye = new LightSensor(SensorPort.S2);
    int black_reading;
    int blue_reading;

  public void calibrate()
  {
      System.out.println("Calibrate Tracker");
   
 
      while (0 < Button.readButtons())
      {
        Thread.yield();// wait for release
      }
      for (byte i = 0; i < 3; i++)
      {
        while (0 == Button.readButtons())//wait for press
        {
          LCD.drawInt(leftEye.getLightValue(), 4, 6, 1 + i);
          LCD.drawInt(rightEye.getLightValue(), 4, 12, 1 + i);
          if (i == 0)
          {
            LCD.drawString("LOW", 0, 1 + i);
          } else if (i == 1)
          {
            LCD.drawString("HIGH", 0, 1 + i);
          } else
          {
            LCD.drawString("BLACK", 0, 1 + i);
            black_reading = (leftEye.getLightValue() + rightEye.getLightValue())/2;
            
          }
        }
        Sound.playTone(1000 + 200 * i, 100);
        if (i == 0)
        {
          leftEye.calibrateLow();
          rightEye.calibrateLow();
          blue_reading = (leftEye.getLightValue() + rightEye.getLightValue())/2;
        } else if (i == 1)
        {
          rightEye.calibrateHigh();
          leftEye.calibrateHigh();
        } 
        while (0 < Button.readButtons())
        {
          Thread.yield();//button released
        }
       
    }
    while (0 == Button.readButtons())// while no press
    {
      int lval = leftEye.getLightValue();
      int rval = rightEye.getLightValue();
      LCD.drawInt(lval, 4, 0, 5);
      LCD.drawInt(rval, 4, 4, 5);
      LCD.drawInt(CLDistance(lval, rval), 4, 12, 5);
      LCD.refresh();
    }
    LCD.clear();
  }
  
  int CLDistance(int left, int right)
  {
     return 0;
  }

    public void trackline(DifferentialPilot pilot, int xCounter) {

        pilot.forward();
        int black = 0;
        int k = 1;
        while (black < xCounter) {
            int zone_center = rightEye.getLightValue() - leftEye.getLightValue();

            //steers depending on left and right eye light values at rate depending on constant k
            if (leftEye.getLightValue() != rightEye.getLightValue()) {
                pilot.steer((zone_center) * k);
            }
            
            if (leftEye.getLightValue() < (black_reading + blue_reading)/2 
                    || rightEye.getLightValue() < (black_reading + blue_reading)/2) {
                pilot.travel(7); //travel further to center robot on intersection
                Sound.playTone(1000 + 200, 100);
                black++;
                continue;
            }
            
            if (Motor.A.isMoving() || Motor.B.isMoving()) { //exit anytime button pressed
                if (Button.readButtons() == Button.ESCAPE.getId()) {
                    Motor.A.stop();
                    Motor.B.stop();
                    return;
                }
            }
        }
        pilot.stop();
        return;
    }

    public void turn(int turn_angle, DifferentialPilot pilot) {

        pilot.rotate(turn_angle * 90);
    }


}
