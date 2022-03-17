/**
 * Talal Ahmed
 
 * Handwritten Uppercase Letter Recognizer
 *
 * Class with Main method.
 * Creates the JFrame GUI using DrawArea class as drawing canvas. Outputs BufferedImage of drawing.
 * Drawing File is passed to RecognizerNeuralNetwork class via ActionEvent to query the neural network model.
*/

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

public class LetterRecognizer extends JFrame implements ActionListener
{
    JFrame frame;
    JPanel canvas;
    JPanel buttonsPan;
    JPanel rightside;
    JPanel leftside;
    JPanel result;
    JPanel instructPan;
    DrawArea drawArea;
    JButton clear;
    JButton recognize;
    JLabel resultLab;
    JLabel instructions;

    public LetterRecognizer()
    {
        //Gridlayout where leftside is for the drawing and buttons and right where the result is displayed
        frame = new JFrame();
        frame.setLayout(new GridLayout(1, 2));
        frame.setTitle("Hand Written Uppercase Letter Recognizer");

        leftside = new JPanel();
        leftside.setBorder(BorderFactory.createTitledBorder("Draw Area"));

        leftside.setLayout(new BorderLayout());

        instructPan = new JPanel();
        instructions = new JLabel();
        instructions.setText("\nDraw an uppercase letter. Large and Centered.");
        instructPan.add(instructions);
        leftside.add(instructPan, BorderLayout.NORTH);

        canvas = new JPanel();
        drawArea = new DrawArea();
        canvas.add(drawArea);
        leftside.add(canvas, BorderLayout.CENTER);

        buttonsPan = new JPanel();
        clear = new JButton("Clear");
        clear.addActionListener(this);
        recognize = new JButton("Recognize");
        recognize.addActionListener(this);
        buttonsPan.add(clear);
        buttonsPan.add(recognize);
        leftside.add(buttonsPan, BorderLayout.SOUTH);

        frame.add(leftside);

        rightside = new JPanel();
        rightside.setBorder(BorderFactory.createTitledBorder("Recognition Results"));
        result = new JPanel();
        resultLab = new JLabel();
        resultLab.setFont(new Font("Helvitica", Font.PLAIN, 120));
        resultLab.setForeground(Color.BLUE);
        result.add(resultLab);
        rightside.add(result);

        frame.add(rightside);

        frame.setMinimumSize(new Dimension(700, 400));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae)
    {
        JButton b = (JButton) ae.getSource();
        if (b == clear)
        {
            drawArea.clear();
        }
        else if (b == recognize)
        {
            try
            {
                //Using method from DrawArea class to save drawn image.
                File letterFile = drawArea.getImage();
                BufferedImage drawnImage = ImageIO.read(letterFile);

                //Creating RecognizerNeuralNetwork to pass the drawn image to for querying the network
                RecognizerNeuralNetwork recogNN = new RecognizerNeuralNetwork(drawnImage);

                //Get answer back from querying the network. Get the predicted letter.
                String ans = recogNN.getAnswer();

                //Set the answer as the text for result JLabel.
                resultLab.setText(ans);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main (String[] args)
    {
        new LetterRecognizer();
    }
}