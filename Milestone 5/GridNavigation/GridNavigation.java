
import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author D-MAN
 */
public class GridNavigation {

    Node origin;
    Node dest;
    Node whereAmI;
    Node nextNode;
    Tracker robot = new Tracker();
    int direction = 0;
    int GridSizeX = 6;
    int GridSizeY = 8;
    int desired_direction;
    Grid findPath = new Grid(GridSizeX, GridSizeY);
    UltrasonicSensor ear = new UltrasonicSensor(SensorPort.S4);
    DifferentialPilot pilot;

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
    public GridNavigation() {
        pilot = new DifferentialPilot(5.6f, 13.5f, Motor.A, Motor.B);
        pilot.setTravelSpeed(15);
        robot.setPilot(pilot);
        LCD.clear();
        robot.calibrate();
    }

    /**
     * Finds shortest path to destination, finds next node to move to, moves
     */
    public void navigate() {
        ear.continuous();
        findPath.setDestination(dest.getX(), dest.getY());
        while (whereAmI != dest) {
            nextNode = findNextNode();
            System.out.print("Next Node: " + nextNode.getX() + " " + nextNode.getY());
            if (desired_direction != direction) {
                align_direction();
            }
            checkObstacle();
            if (!nextNode.isBlocked()) {
                move();
            }
        }
    }

    /**
     * Makes robot face correct direction
     */
    public void align_direction() {
        while (pilot.isMoving()) {
        }
        int direction_difference = desired_direction - direction;
        if (Math.abs(direction_difference) > 2) {
            direction_difference = direction_difference - (int) Math.signum(direction_difference) * 4;
        }
        robot.turn(direction_difference);
        direction = desired_direction;
    }

    /**
     * Tells robot to move to next Node
     */
    public void move() { //x-axis true, y-axis false
        robot.trackline();
        whereAmI = nextNode;
        System.out.print("Pos: " + whereAmI.getX() + " " + whereAmI.getY());
        sendLocation(whereAmI);
    }

    /**
     * Checks for an obstacle in front of robot and if found, marks node
     * in front as blocked
     */
    public void checkObstacle() {
        if (ear.getDistance() < 35) {
            Node n = findPath.neighbor(whereAmI, direction);
            if (n != null) {
                n.blocked();
                sendBlocked(n);
            }

        }
    }

    /**
     * Dummy method for use in subclass
     * @param n
     */
    public void sendBlocked(Node n) {
    }

    /**
     * Dummy method for use in subclass
     * @param n
     */
    public void sendLocation(Node n) {
    }

    /** 
     * Finds the neighboring node that has the shortest distance to destination
     * Prefers to go straight if equivalent distances are found
     * @return Returns node that robot will go to next
     */
    public Node findNextNode() {
        findPath.recalc();
        nextNode = whereAmI;
        if (findPath.neighbor(whereAmI, direction) != null) {
            nextNode = findPath.neighbor(whereAmI, direction);
        }
        for (int i = 0; i < 4; i++) {
            Node neighbor = findPath.neighbor(whereAmI, i);
            if (neighbor != null && whereAmI != null) {
                int distance_neighbor = neighbor.getDistance();
                if (distance_neighbor < nextNode.getDistance()) {
                    nextNode = neighbor;
                    desired_direction = i;
                }
            }
        }
        return nextNode;
    }
}
