
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import java.io.*;
import lejos.nxt.*;

/**
 * test communications with GrigNavControl
 * @author glassey
 */
public class Communicator {

    DataInputStream dataIn;
    DataOutputStream dataOut;
    private int x = 0;
    private int y = 0;

    /**
     * connects to PC via bluetooth
     */
    public void connect() {
// you can copy most of this code from BTRecieve
        BTConnection btc = Bluetooth.waitForConnection();
        LCD.drawString("Connected", 0, 1);
        dataIn = btc.openDataInputStream();
        dataOut = btc.openDataOutputStream();
        Sound.beepSequence();
    }

    /**
     * Gets destination from PC by reading stream
     * @return x and y returns x and y coordinates
     */
    public int[] getDestination() {
        try {
            // get from DataInputStream
            x = dataIn.readInt();
            y = dataIn.readInt();

        } catch (IOException e) {
            System.out.println("InputError");
        }

        LCD.drawString("Read ", 0, 3);
        LCD.drawInt(x, 4, 0, 4);
        LCD.drawInt(y, 4, 8, 4);
        int xy[] = {x, y};
        return xy;
    }

    /**
     * Sends data to PC. Header indicates whether data contains location of
     * obstacle or arrival at node
     * @param header Type of data: 0 for arrival at node, 1 for location of obstacle
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void sendData(int header, int x, int y) {
        try {
            dataOut.writeInt(header);
            dataOut.writeInt(x);
            dataOut.writeInt(y);
            dataOut.flush();
        } catch (Exception e) {
        }
    }
}
