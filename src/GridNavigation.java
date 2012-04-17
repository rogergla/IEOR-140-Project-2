
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
    public void Instantiate() {
        pilot = new DifferentialPilot(5.6f, 13.5f, Motor.A, Motor.B);
        pilot.setTravelSpeed(15);
        robot.setPilot(pilot);
        Button.waitForAnyPress();
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
     * @param distance Amount Robot will travel (negative or positive values denote direction along axis)
     * @param axis X or Y axis
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
     * @param steps number of intersections
     * @param axis  X or Y
     * @param offset Where to display on screen
     */
    public void move() { //x-axis true, y-axis false
        robot.trackline();
        whereAmI = nextNode;
        System.out.print("Pos: " + whereAmI.getX() + " " + whereAmI.getY());
    }

    public void checkObstacle() {
        if (ear.getDistance() < 35) {
            if (direction == 0 && findPath.nodes[whereAmI.getX() + 1][whereAmI.getY()] != null) {
                findPath.nodes[whereAmI.getX() + 1][whereAmI.getY()].blocked();
            } else if (direction == 1 && findPath.nodes[whereAmI.getX()][whereAmI.getY() + 1] != null) {
                findPath.nodes[whereAmI.getX()][whereAmI.getY() + 1].blocked();
            } else if (direction == 2 && findPath.nodes[whereAmI.getX() - 1][whereAmI.getY()] != null) {
                findPath.nodes[whereAmI.getX() - 1][whereAmI.getY()].blocked();
            } else if (direction == 3 && findPath.nodes[whereAmI.getX()][whereAmI.getY() - 1] != null) {
                findPath.nodes[whereAmI.getX()][whereAmI.getY() - 1].blocked();
            }
        }
    }

    public Node findNextNode() {
        findPath.recalc();
        nextNode = whereAmI;
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
