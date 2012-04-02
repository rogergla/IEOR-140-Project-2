package pcnavigator;

import java.io.*;
import lejos.pc.comm.*;

/**
 *Communicator for use with GridNavigationControl
 * Needs a bit more work - student start
 * updated 10/13   2011
 * @author Roger
 */
public class GridControlComm {

    /**
     * 
     * @param control
     */
    public GridControlComm(GridNavigationControl control) {
        this.control = control; //callback path
        System.out.println(" Control Comm built ***");
    }

    /**
     * connects establish bluetooth connection to named robot
     * @param name 
     */
    public void connect(String name) {
        try {
            connector.close();
        } catch (Exception e) {
            System.out.println(e);
        }
//      String NXT = name; //name_addr.getText();
        System.out.println(" conecting to " + name);
        if (connector.connectTo("btspp://" + name)) {
            control.setMessage("Connected to " + name);
            System.out.println(" connected !!");
            dataIn = new DataInputStream(connector.getInputStream());
            dataOut = new DataOutputStream(connector.getOutputStream());
            if (dataIn == null) {
                System.out.println(" no Data  ");
            } else if (!reader.isRunning) {
                reader.start();
            }
        } else {
            System.out.println(" no connection ");
        }
    }

    /**
     * sends robot coordinates to the PC
     * @param x
     * @param y
     * @throws Exception  
     */
    public void send(int x, int y) throws Exception {
        System.out.println(" Comm send " + x + " " + y);
        dataOut.writeInt(x);
        dataOut.writeInt(y);
        dataOut.flush();
    }

    /**
     * reads the  data input stream, and calls DrawRobotPath() and DrawObstacle()
     * uses OffScreenDrawing,  dataIn
     * @author Roger Glassey
     */
    class Reader extends Thread {

        int count = 0;
        boolean isRunning = false;

        public void run() {
            System.out.println(" reader started ");
            isRunning = true;
            String message = "";

            while (isRunning) {
                try {
                    // your code here to read the incoming message
                    int header = dataIn.readInt();
                    int x = dataIn.readInt();
                    int y = dataIn.readInt();
                    if (header > 0) {
                        control.drawObstacle(x, y);
                    }
                    else {
                        //this is where robot is
                        control.drawRobotPath(x, y);
                    }

                    // your code here to read the rest of the message
                } catch (IOException e) {
                    System.out.println("Read Exception in GridControlComm");
                    count++;
                }
                // code to make the message informative
                control.setMessage(message);
                // call the appropriate method on control
            }
        }
    }
    /**
     * call back reference; calls  setMessage, dreawRobotPositin, drasObstacle;
     */
    GridNavigationControl control;
    /**
     * default bluetooth address. used by reader
     */
//   String address = "";
    /**
     * connects to NXT using bluetooth.  Provides data input stream and data output stream
     */
    private NXTConnector connector = new NXTConnector();
    /**
     * used by reader
     */
    private DataInputStream dataIn;
    /**
     * used by send()
     */
    private DataOutputStream dataOut;
    /**
     * inner class extends Thread; listens  for incoming data from the NXT
     */
    private Reader reader = new Reader();
}
