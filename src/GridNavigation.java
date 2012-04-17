
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
public class GridNavigation {

    public static void main(String args[]) {
        Tracker robot = new Tracker();
        DifferentialPilot pilot = new DifferentialPilot(5.6f, 11.8f, Motor.A, Motor.B);
        pilot.setTravelSpeed(15);
        Button.waitForAnyPress();
        robot.calibrate();

//        Point start = new Point(0, 2); //take out when we right button class
//        Point destination = new Point(2, 4);

        for (int i = 0; i < 8; i++) { //move forward and turn left (2 squares)
            robot.trackline(pilot,2);
            robot.turn(1, pilot);
        }

        robot.turn(1, pilot); 
        
        for (int i = 0; i < 8; i++) { //move forward and turn right (2 squares)
            robot.trackline(pilot,2);
            robot.turn(-1, pilot);
        }

        robot.turn(-1,pilot);
        
        for (int i = 0; i < 4; i++) { //shuffle turning left

            robot.trackline(pilot,2);
            robot.turn(2, pilot);
        }

        robot.turn(-2,pilot);
        
        for (int i = 0; i < 4; i++) { //shuffle turning right

            robot.trackline(pilot,2);
            robot.turn(-2, pilot);
        }

    }
}
