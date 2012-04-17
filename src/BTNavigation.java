/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Subclass of GridNavigation
 * @author duttasho
 */
public class BTNavigation extends GridNavigation {

    Communicator communicator = new Communicator();

    /**
     * 
     */
    public BTNavigation() {
        super();

    }

    /**
     * Sends location of detected obstacle to PC
     * @param n
     */
    public void sendBlocked(Node n) {
        communicator.sendData(1, n.getX(), n.getY());
    }

    /**
     * Sends location of robot to PC
     * @param n
     */
    public void sendLocation(Node n) {
        communicator.sendData(0, n.getX(), n.getY());
    }

    /**
     * Sets origin at 0,0 then calls go()
     * @param args
     */
    public static void main(String[] args) {
        
        BTNavigation myRobot = new BTNavigation();
        myRobot.setOrigin(0, 0);
        myRobot.go();
    }

    /**
     * Connects to PC, gets destination coordinates, sets destination, moves to it
     */
    public void go() {
        communicator.connect();
        while (true) {
            int[] xy = communicator.getDestination();
            setDestination(xy[0], xy[1]);
            navigate();
        }
    }
}
