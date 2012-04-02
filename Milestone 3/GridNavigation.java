
import lejos.nxt.*;
import lejos.util.*;
import java.io.*;
import lejos.robotics.navigation.DifferentialPilot;
import java.awt.Point;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author D-MAN
 */
public class GridNavigation {

    Point origin;
    Point dest;
    Point whereAmI = new Point(0, 0);
    Tracker robot = new Tracker();
    int direction = 0;

    /**
     * Sets original coordinates for robot position
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setOrigin(int x, int y) {

        origin = new Point(x, y);
    }

    /**
     * Sets point robot will travel to
     * @param x X-coordinate
     * @param y Y-coordinate
     */
    public void setDestination(int x, int y) {
        dest = new Point(x, y);
    }

    /**
     * Initializes the DifferentialPilot variable, sets travel speed, and calibrates the robot
     */
    public void Instantiate() {
        DifferentialPilot pilot = new DifferentialPilot(5.6f, 11f, Motor.A, Motor.B);
        pilot.setTravelSpeed(15);
        robot.setPilot(pilot);
        Button.waitForAnyPress();
        LCD.clear();
        robot.calibrate();
    }

    /**
     * Tells the robot to align itself to the appropriate x direction, move forward that far, align to y direction, move
     */
    public void navigate() {
        Point temp = new Point((int) origin.getX(), (int) origin.getY());
        whereAmI = temp;
        double xtravel = dest.getX() - origin.getX();
        double ytravel = dest.getY() - origin.getY();
        //robot moves in the x-axis
        if (xtravel != 0) {
            align_direction(xtravel, 'x');
        }
        move(xtravel, true, 0);
        //robot moves in y-axis
        if (ytravel != 0) {
            align_direction(ytravel, 'y');
        }
        move(ytravel, false, (int) xtravel);
    }

    /**
     * Makes robot face correct direction
     * @param distance Amount Robot will travel (negative or positive values denote direction along axis)
     * @param axis X or Y axis
     */
    public void align_direction(double distance, char axis) {
        int desired_direction = 0;
        if (axis == 'x') {
            if (distance > 0) {
                desired_direction = 0;
            } else {
                desired_direction = 2;
            }
        }
        if (axis == 'y') {
            if (distance > 0) {
                desired_direction = 1;
            } else {
                desired_direction = 3;
            }
        }
        robot.turn(desired_direction - direction);
        direction = desired_direction;

    }

    /**
     * Tells robot to move number of intersections and displays current position on screen
     * @param steps number of intersections
     * @param axis  X or Y
     * @param offset Where to display on screen
     */
    public void move(double steps, boolean axis, int offset) { //x-axis true, y-axis false
        for (int i = 0; i < Math.abs(steps); i++) {
            robot.trackline();
            if (axis) { //update x-value
                whereAmI.x += (int) steps / Math.abs(steps);
            } else {
                whereAmI.y += (int) steps / Math.abs(steps);
            }
            LCD.drawString("(" + whereAmI.getX() + "," + whereAmI.getY() + ")", 0, offset + i);
        }
    }
}
