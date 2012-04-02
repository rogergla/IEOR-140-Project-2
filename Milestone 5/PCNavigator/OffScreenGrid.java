package pcnavigator;


import java.awt.*;
import javax.swing.JTextField;
import java.awt.event.*;

/*
 * OffScreenGrid.java ; manages drawing of grid and robot path on an Image 
 * which is displayed when  repaint() is called. 
 * Mouse listener used    
 * updated 10/13   2011
 * @author  Roger
 */
/**
 * 
 * @author duttasho
 */
public class OffScreenGrid extends javax.swing.JPanel
{

   /** Creates new form OffScreenGrid */
   public OffScreenGrid()
   {
      initComponents();
      setBackground(Color.white);
      System.out.println(" OffScreen Drawing constructor ");
   }

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      if (offScreenImage == null)
      {
         makeImage();
      }
      g.drawImage(offScreenImage, 0, 0, this);  //Writes the Image to the screen
   }

   /**
    * Create the offScreenImage, 
    */
   public void makeImage()
   {
      imageWidth = getSize().width;// size from the panel
      imageHeight = getSize().height;
      yOrigin = imageHeight - xOrigin;
      robotPrevX = xpixel(0);
      robotPrevY = ypixel(0);
      offScreenImage = createImage(imageWidth, imageHeight);// the container can make an image
      osGraphics = (Graphics2D) offScreenImage.getGraphics();
      osGraphics.setColor(getBackground());
      osGraphics.fillRect(0, 0, imageWidth, imageHeight);// erase everything
      drawGrid();
   }

   /**
    *draws the grid with labels; draw robot at 0,0
    */
   public void drawGrid()
   {
      int xmax = 5;
      int ymax = 7;
      osGraphics.setColor(Color.green); // Set the line color
      for (int y = 0; y <= ymax; y++)
      {
         osGraphics.drawLine(xpixel(0), ypixel(y), xpixel(xmax), ypixel(y));//horizontal lines
      }
      for (int x = 0; x <= xmax; x++)
      {
         osGraphics.drawLine(xpixel(x), ypixel(0), xpixel(x), ypixel(ymax));// vertical lines
      }
      osGraphics.setColor(Color.black); //set number color 	
      for (int y = 0; y <= ymax; y++) // number the  y axis
      {
         osGraphics.drawString(y + "", xpixel(-0.5f), ypixel(y));
      }
      for (int x = 0; x <= xmax; x++) // number the x axis
      {
         osGraphics.drawString(x + "", xpixel(x), ypixel(-0.5f));
      }
      drawRobotPath(0, 0);

   }

   /**
    *clear the screen and draw a new grid
    */
   public void clear()
   {
     System.out.println(" clear called ");
      osGraphics.setColor(getBackground());
      osGraphics.fillRect(0, 0, imageWidth, imageHeight);// clear the image
      drawGrid();
      repaint();
   }

   /**
    *Obstacles shown as magenta dot
    * @param x 
    * @param y 
    */
   public void drawObstacle(int x, int y)
   {
      x = xpixel(x); // coordinates of intersection
      y = ypixel(y);
      block = true;
      osGraphics.setColor(Color.magenta);
      osGraphics.fillOval(x - 5, y - 5, 10, 10);//bounding rectangle is 10 x 10
      repaint();
   }

   /**
    * 
    * @param x
    * @param y
    */
   public void drawDest(int x, int y)
   {
      clearSpot(destX0, destY0, Color.green);
      x = xpixel(x); // coordinates of intersection
      y = ypixel(y);
      osGraphics.setColor(Color.cyan);
      osGraphics.fillOval(x - 5, y - 5, 10, 10);//bounding rectangle is 10 x 10
      repaint();
      destX0 = x;
      destY0 = y;
   }

   /**
    *blue line connects current robot position to last position if adjacent to current position
    * @param xx 
    * @param yy 
    */
   public void drawRobotPath(int xx, int yy)
   {
      int x = xpixel(xx); // coordinates of intersection
      int y = ypixel(yy);
      if (checkContinuity(x, y))
      {
         osGraphics.setColor(Color.blue);
         osGraphics.drawLine(robotPrevX, robotPrevY, x, y);
      }
      if (block)
      {
         block = false;
      } else
      {
         clearSpot(robotPrevX, robotPrevY, Color.BLUE);
      }
      osGraphics.setColor(Color.blue);
      osGraphics.fillOval(x - 5, y - 5, 10, 10);  //show robot position
      robotPrevX = x;
      robotPrevY = y;
      repaint();
   }

   /**
    * clear the old robot position, arg pixels
    */
   private void clearSpot(int x, int y, Color c)
   {
      osGraphics.setColor(Color.white);
      osGraphics.fillOval(x - 5, y - 5, 10, 10);
      osGraphics.setColor(c);
      osGraphics.drawLine(x - 5, y, x + 5, y);
      osGraphics.drawLine(x, y - 5, x, y + 5);
   }

   /**
    *see of robot has moved to adjacent node - used by drawRobotPath
    */
   private boolean checkContinuity(int x, int y)
   {
      if ((abs(x - robotPrevX) == 0 && abs(y - robotPrevY) == gridSpacing) ||
              (abs(x - robotPrevX) == gridSpacing && abs(y - robotPrevY) == 0) ||
              (abs(x - robotPrevX) == 0 && abs(y - robotPrevY) == 0))
      {
         return true;
      } else
      {
         return false;
      }
   }

   /**
    * 
    * @param a
    * @return
    */
   public int abs(int a)
   {
      return (a < 0 ? (-a) : (a));
   }

   /**
    *convert grid coordinates to pixels
    */
   private int xpixel(float x)
   {
      return xOrigin + (int) (x * gridSpacing);
   }

  /**
    * 
    * @param xpix  mouse cleck x in pixels
    * @return   grid  x
    */
   private int gridX(int xpix)
   {
     float x = (xpix - xOrigin)/(1.0f*gridSpacing);
     return Math.round(x);
   }
   private int ypixel(float y)
   {
      return yOrigin - (int) (y * gridSpacing);
   }
  /*
    * return  grid Y 
    * @param  mouse click y in pixels
    */
 private int gridY(int ypix)
   {
     float y = (yOrigin - ypix)/(1.0f*gridSpacing);
     return Math.round(y);
   }
   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clearB = new javax.swing.JButton();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        clearB.setText("Clear Map");
        clearB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(clearB)
                .addContainerGap(169, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(clearB)
                .addContainerGap(412, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void clearBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBActionPerformed

  clear();
}//GEN-LAST:event_clearBActionPerformed

private void formMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseClicked
{//GEN-HEADEREND:event_formMouseClicked
  System.out.println("click "+ evt.getX()+" "+evt.getY());
  int x = gridX(evt.getX());
  int y = gridY(evt.getY());
  textX.setText(""+x);
  textY.setText(""+y);   
  drawDest(x,y);
}//GEN-LAST:event_formMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearB;
    // End of variables declaration//GEN-END:variables
   /**
    *The robot path is drawn and updated on this object. <br>
    *created by makeImage which is called by paint(); guarantees image always exists before used; 
    */
   Image offScreenImage;
   /**
    *width of the dawing area;set by makeImage,used by clearImage
    */
   int imageWidth;
   /**
    *height of the dawing are; set by  makeImage,used by clearImage
    */
   int imageHeight;
   /** 
    *the graphics context of the image; set by makeImage, used by all methods that draw on the image
    */
   private Graphics2D osGraphics;
   /**
    * y origin in pixels
    */
   public int yOrigin;
   /**
    * line spacing in  pixels
    */
   public final int gridSpacing = 50;
   /**
    * origin in pixels from corner of drawing area
    */
   public final int xOrigin = 50;
   /**
    *robot position ; used by checkContinuity, drawRobotPath
    */
   private int robotPrevX = xpixel(0);
   /**
    * robot position; used by checkContinuity, drawRobotPath
    */
   private int robotPrevY = ypixel(0);
   private int destX0 = xpixel(0);
   private int destY0 = ypixel(0);
   /**
    * node status - true if blocked; set by drawObstacle, used by drawRobotPath
    */
   private boolean block = false;
   /**
    * 
    */
   public JTextField textX;
   /**
    * 
    */
   public JTextField textY;
}
