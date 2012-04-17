
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
        //initialize button counter
        ButtonCounter myInput = new ButtonCounter();
        myInput.promptUser();
        int x1 = myInput.getXOrig();
        int y1 = myInput.getYOrig();
        int x2 = myInput.getXDest();
        int y2 = myInput.getYDest();
        System.out.println("Press Enter");
        Button.waitForAnyPress();
        myRobot.setOrigin(x1, y1);
        myRobot.setDestination(x2, y2);
        LCD.clear();
        myRobot.Instantiate(); //instantiate the pilot.
        myRobot.navigate();
        while (true) {
            LCD.clear();
            myRobot.setOrigin(myInput.getXDest(), myInput.getYDest()); //set the new orig to be old dest
            myInput.askXDest(); //overwrite the variables to set new Dest.
            myInput.askYDest();
            myRobot.setDestination(myInput.getXDest(), myInput.getYDest()); //set new dest.
            Delay.msDelay(1000); //delay the navigation a bit so it doesnt jerk.
            myRobot.navigate();
        }

    }
}
