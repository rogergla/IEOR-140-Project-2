
import lejos.nxt.LCD;
import lejos.nxt.Button;


/**
 *
 * @author owner.GLASSEY
 */
public class CommTester
{

  public static void main(String[] args)
  {
    System.out.println("Comm Tester  Press any");
    Button.waitForAnyPress();
    LCD.clear();
    Communicator com = new Communicator();
    com.connect();
    int x,y;
    int[] xy;
    for (int i = 0; i<4; i++)
    {
      xy = com.getDestination();
      x = xy[0];
      y = xy[1];
      LCD.drawString("x "+x+" y "+y,0,3);
      com.sendData(i%2, x+1, y-1);     
    }
  }
}
