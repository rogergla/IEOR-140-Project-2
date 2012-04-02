
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

    LightSensor _leftEye = new LightSensor(SensorPort.S1);
    LightSensor _rightEye = new LightSensor(SensorPort.S2);

    public void calibrate() {
        LCD.drawString("calibrate", 0, 0);
        LCD.refresh();
        for (int i = 0; i < 3; i = i + 1) {


            Delay.msDelay(200);
            Button.waitForAnyPress();
            if (i == 0) {
                _leftEye.calibrateLow();
            } else if (i == 1) {
                _rightEye.calibrateLow();
            } else if (i == 2) {
                _leftEye.calibrateHigh();
                _rightEye.calibrateHigh();
            }
            Sound.playTone(800 + 200 * i, 200);
            Sound.pause(200);

        }
        LCD.drawString("Calibration Done", 0, 0);
        Button.waitForAnyPress();
    }

    public void trackline(DifferentialPilot pilot) {

        pilot.forward();
        boolean shouldMove = true;
        int black = 1;
        int k = 5;
        int turn_angle;
        while (shouldMove) {
            
            //define Zones
//            int zone_left = -(_leftEye.getLightValue() + _rightEye.getLightValue());
            int zone_center = _rightEye.getLightValue() - _leftEye.getLightValue();
//            int zone_right = (_leftEye.getLightValue() + _rightEye.getLightValue());

            //steers depending on left and right eye light values at rate depending on constant k

            if (_leftEye.getLightValue() != _rightEye.getLightValue()) {
                pilot.steer((zone_center) * k);
            }
            
            if (_leftEye.getLightValue() < 35 || _rightEye.getLightValue() < 35) {
                if (black % 4 == 2 || black % 4 == 3) {
                    turn_angle = 1;
                } else {
                    turn_angle = -1;
                }
                turn(turn_angle, pilot);
                black = black + 1;
                pilot.forward();

            }
            
            if (Motor.A.isMoving() || Motor.B.isMoving()) {
                if (Button.readButtons() == Button.ESCAPE.getId()) {
                    Motor.A.stop();
                    Motor.B.stop();
                    return;
                }
            }
        }
    }

    public void turn(int turn_angle, DifferentialPilot pilot) {

        pilot.travel(7);
        if (turn_angle == 1) {
            pilot.rotate(90);
        } else {
            pilot.rotate(-90);
        }
    }

    public static void main(String[] args) {
        new Tracker().calibrate();
        DifferentialPilot pilot = new DifferentialPilot(5.6f, 11.8f, Motor.A, Motor.B);
        pilot.setTravelSpeed(15);
        Button.waitForAnyPress();
        new Tracker().trackline(pilot);

    }
}
