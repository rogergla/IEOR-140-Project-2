
import lejos.nxt.*;
import lejos.util.*;
import java.io.*;
import lejos.robotics.navigation.DifferentialPilot;

/**
 *
 * @author D-MAN
 */
public class GridNavMain {

    /**
     * 
     * @param args
     */
    public static void main(String args[]) {
        System.out.println("Press Enter");
        Button.waitForAnyPress();
        GridNavigation myRobot = new GridNavigation();
        ButtonCounter myInput = new ButtonCounter();
        myInput.promptUser();
        //get these from computer
        int x1 = myInput.getXOrig();
        int y1 = myInput.getYOrig();
        int x2 = myInput.getXDest();
        int y2 = myInput.getYDest();
        System.out.println("Press Enter");
        Button.waitForAnyPress();
        //end get from computer
        myRobot.setOrigin(x1, y1);
        myRobot.setDestination(x2, y2);
        LCD.clear();
        myRobot.Instantiate(); //instantiate the pilot.
        //find shortest path in navigate, go to it. probably arraylist the path
        myRobot.navigate();
        while (true) {
            LCD.clear();
            myInput.askXDest(); //overwrite the variables to set new Dest.
            myInput.askYDest();
            myRobot.setDestination(myInput.getXDest(), myInput.getYDest()); //set new dest.
            Delay.msDelay(1000); //delay the navigation a bit so it doesnt jerk.
            myRobot.navigate();
        }

    }
}
