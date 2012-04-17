/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import lejos.nxt.*;
import lejos.util.*;
import java.io.*;

/**
 *
 * @author D-MAN
 */
public class ButtonCounter {

    int x1, y1, x2, y2;

    /** Asks user for Origin and destination coordinates
     */
    void promptUser() {
        LCD.clear();
        System.out.println("Type Origin");
        Button.waitForAnyPress();
        LCD.clear();
        askXOrig();
        askYOrig();
        askXDest();
        askYDest();
        LCD.clear();
    }

    /** 
     * Gets X coordinates of origin
     */
    void askXOrig() {
        LCD.drawString("X: 0", 0, 2);
        int val = getInput(2);
        LCD.clear(2);
        LCD.drawString("X:" + val, 0, 2);
        this.x1 = val;
    }

    /** 
     * Gets Y coordinates of origin
     */
    void askYOrig() {
        LCD.drawString("Y: 0", 0, 3);
        int val = getInput(3);
        LCD.clear(3);
        LCD.drawString("Y:" + val, 0, 3);
        this.y1 = val;
    }

    /** 
     * Gets X coordinates of destination
     */
    void askXDest() {
        LCD.drawString("X: 0", 0, 4);
        int val = getInput(4);
        LCD.clear(4);
        LCD.drawString("X:" + val, 0, 4);
        this.x2 = val;
    }

    /** 
     * Gets X coordinates of destination
     */
    void askYDest() {
        LCD.drawString("Y: 0", 0, 5);
        int val = getInput(5);
        LCD.clear(5);
        LCD.drawString("Y:" + val, 0, 5);
        this.y2 = val;
    }

    /** Button counter: Left is down, Right is up, ENTER to enter
     * 
     * @param coord Where on screen it is displayed: Handled by ask functions
     * @return returns number for use in coordinates
     */
    int getInput(int coord) {
        int counter = 0;
        int buttonId = Button.waitForAnyPress();
        while (buttonId != Button.ID_ENTER) {
            if (buttonId == Button.ID_LEFT) {
                LCD.clear(4, coord, 2); //clear the old value
                counter--;
                LCD.drawInt(counter, 3, coord); // print the new value
                buttonId = Button.waitForAnyPress();
            } else if (buttonId == Button.ID_RIGHT) {
                LCD.clear(4, coord, 2);
                counter++;
                LCD.drawInt(counter, 3, coord);
                buttonId = Button.waitForAnyPress();
            } else {
                System.out.println("Terminating...");
                System.exit(1);
            }
        }
        return counter;
    }

    int getXOrig() {
        return this.x1;
    }

    int getYOrig() {
        return this.y1;
    }

    int getXDest() {
        return this.x2;
    }

    int getYDest() {
        return this.y2;
    }
}
