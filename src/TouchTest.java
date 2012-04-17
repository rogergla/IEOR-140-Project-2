
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author glassey
 */
public class TouchTest
{
  public static void main(String[] args)
  {
 
    System.out.println(" go");
    TouchSensor bump = new TouchSensor(SensorPort.S1);
    while(true)
    {
       if(bump.isPressed())   System.out.println( "hit ") ;
    }
  }
}
