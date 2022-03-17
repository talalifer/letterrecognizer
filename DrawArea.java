/**
 * Talal Ahmed
 
 * Handwritten Uppercase Letter Recognizer
 *
 * JComponent used for drawing.
 *
 * Majority of this code is not mine.
 * It was borrowed from http://www.ssaurel.com/blog/learn-how-to-make-a-swing-painting-and-drawing-application/
 *
 * Added dimension constriants to make it 280x280, so dimension propotions will be kept the same when resized
 * down to 28x28pixels
 *
 * Added getImage() for saving and accessing the drawn image.
*/

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

class DrawArea extends JComponent {

    // Image in which we're going to draw
    private Image image;
    // Graphics2D object ==> used to draw on
    private Graphics2D g2;
    // Mouse coordinates
    private int currentX, currentY, oldX, oldY;

    public DrawArea() {
        setDoubleBuffered(false);
        setMinimumSize(new Dimension(280, 280));
        setMaximumSize(new Dimension(280, 280));
        setPreferredSize(new Dimension(280, 280));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // save coord x,y when mouse is pressed
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                // coord x,y when drag mouse
                currentX = e.getX();
                currentY = e.getY();

                if (g2 != null)
                {
                    // draw line if g2 context not null
                    g2.setStroke(new BasicStroke(25, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                    g2.drawLine(oldX, oldY, currentX, currentY);
                    // refresh draw area to repaint
                    repaint();
                    // store current coords x,y as olds x,y
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            // image to draw null ==> we create
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            // enable antialiasing
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // clear draw area
            clear();
        }

        g.drawImage(image, 0, 0, null);
    }

    // now we create exposed methods
    public void clear()
    {
        g2.setPaint(Color.white);
        // draw white on entire draw area to clear
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    public File getImage()
    {
        BufferedImage drawnImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        File imageFile = new File("drawnImage.png");
        this.paint(drawnImage.getGraphics());
        try {
            ImageIO.write(drawnImage, "png", imageFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}