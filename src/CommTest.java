
import lejos.nxt.*;
import lejos.util.*;

public class CommTest {

    Communicator comm = new Communicator();
    ButtonCounter bc = new ButtonCounter();
    int[] _data = {0, 0};

    public void go()
    {
    	comm.connect();
    	boolean more = true;
    	for(int i = 0 ; i<4; i++) {
    		_data = comm.getDestination();
    		int x=_data[0];
    		int y = _data[1];
    		LCD.drawString("any Press", 0,0);
    		Button.waitForAnyPress();
    		LCD.clear();
    		sendLocation();
    	}
    }

    public void sendLocation() {
        LCD.clear();
        bc.count("Location x,y");
        int x = bc.getLeftCount();
        int y = bc.getRightCount();
        LCD.drawString("ESC to send block", 0, 2);
        int code = Button.waitForAnyPress() - 1;
        if (code > 0) {
            code = 1;
        }
        comm.sendData(code, x, y);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
    	System.out.println("Press any");
    	Button.waitForAnyPress();
        new CommTest().go();

    }
}
