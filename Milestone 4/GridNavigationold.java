
import lejos.nxt.*;
import lejos.util.*;
import java.io.*;
import lejos.robotics.navigation.DifferentialPilot;
import java.awt.Point;
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author D-MAN
 */
public class GridNavigationold {

    Node origin;
    Node dest;
    Node whereAmI;
    Node nextNode;
    Tracker robot = new Tracker();
    int direction = 0;
    int GridSizeX = 6;
    int GridSizeY = 8;
    GridBackup findPath = new GridBackup(GridSizeX, GridSizeY);
    UltrasonicSensor ear = new UltrasonicSensor(SensorPort.S4);

    /**
     * Sets original coordinates for robot position
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void setOrigin(int x, int y) {

        origin = findPath.nodes[x][y];
        whereAmI = origin;
    }

    /**
     * Sets point robot will travel to
     * @param x X-coordinate
     * @param y Y-coordinate
     */
    public void setDestination(int x, int y) {
        dest = findPath.nodes[x][y];
    }

    /**
     * Initializes the DifferentialPilot variable, sets travel speed, and calibrates the robot
     */
    public void Instantiate() {
        DifferentialPilot pilot = new DifferentialPilot(5.6f, 13.5f, Motor.A, Motor.B);
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
        ear.continuous();
        findPath.setDestination(dest.getX(), dest.getY());
        while (whereAmI != dest) {
            //find next node to go to
            findPath.recalc();
            for (int i = 0; i < 4; i++) {
                Node neighbor = findPath.neighbor(whereAmI, i);
                if (neighbor != null && whereAmI != null) {
                    int distance_neighbor = neighbor.getDistance();
                    if (distance_neighbor < whereAmI.getDistance()) {
                        nextNode = neighbor;
                    }
                }
            }
            //System.out.print("NEXT NODE: " + nextNode.getX() + " " + nextNode.getY());
            //end find node
            double xtravel = nextNode.getX() - whereAmI.getX();
            double ytravel = nextNode.getY() - whereAmI.getY();
            //robot moves in the x-axis
            if (xtravel != 0) {
                align_direction(xtravel, 'x');
            }
            checkObstacle();
            if (!nextNode.isBlocked()) {
                move(xtravel, true, 0);
            }
            //robot moves in y-axis
            if (ytravel != 0) {
                align_direction(ytravel, 'y');
            }
            checkObstacle();
            if (!nextNode.isBlocked()) {
                move(ytravel, false, (int) xtravel);
            }

        }
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
                if (Math.abs(3 - direction) < Math.abs(-1 - direction)) {
                    desired_direction = 3;
                } else {
                    desired_direction = -1;
                }
            }
        }
        robot.turn(desired_direction - direction);
        direction = desired_direction;
        if (direction == -1) {
            direction = 3;
        }
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
            whereAmI = nextNode;
            LCD.drawString("(" + whereAmI.getX() + "," + whereAmI.getY() + ")", 0, offset + i);
        }
    }

    public void checkObstacle() {
        if (ear.getDistance() < 35) {
            if (direction == 0) {
                findPath.nodes[whereAmI.getX() + 1][whereAmI.getY()].blocked();
            } else if (direction == 1) {
                findPath.nodes[whereAmI.getX()][whereAmI.getY() + 1].blocked();
            } else if (direction == 2) {
                findPath.nodes[whereAmI.getX() - 1][whereAmI.getY()].blocked();
            } else if (direction == 3) {
                findPath.nodes[whereAmI.getX()][whereAmI.getY() - 1].blocked();
            }
        }
    }
}
