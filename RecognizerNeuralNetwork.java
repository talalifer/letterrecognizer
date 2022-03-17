/**
 * Talal Ahmed
 
 * Handwritten Uppercase Letter Recognizer
 *
 * This class is the brains of the operation (pun intended). It takes a BufferedImage as an argument,
 * resizes the image to 28x28 pixels, turns image to a 1 dimensional array of grayscale pixel densities, loads
 * the saved neural network model, and queries the model with the image array.
 *
 * The output of the network is an array of 26 numbers where each number corresponds to a letter of the alphebet.
 * The letter that corresponds to the largest number is the network's prediction.
 *
 * Several methods that were used during the initial build and troubleshooting were not needed in the final
 * implementation. These methods are found at the end and are kept for future reference.
*/

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.lang.Math.*;

class RecognizerNeuralNetwork
{
    public String answer;

    //Constructor takes a BufferedImage as argument
    public RecognizerNeuralNetwork (BufferedImage drawnImage)
    {
        try
            {
                //resizes the drawn image to 28x28 pixels
                BufferedImage reScaled = resize(drawnImage, 28,28);

                //turns image into 1D array
                double [] imageVec = imageToOneDimArray(reScaled);

                //transposes vector to allow for multiplication with matrices from model
                double [][] imageVecT = transposeVec(imageVec);

                //Loading model saved as CSV. Weight applied when signal goes from input layer to hidden layer.
                //This is a 1000x784 matrix. 784 = number of pixels in 28x28 image. 1000 = number of hidden nodes in network.
                double[][] w_ih = csvToTwoDimArray("w_ih_1000x784.csv", 1000, 784);

                //Loading model saved as CSV. Weight applied when signal goes from hidden layer to output layer.
                //This is a 26x1000 matrix. 26 = number of letters in alphabet which is also number of output layer nodes.
                //1000 = number of hidden nodes in network.
                double[][] w_ho = csvToTwoDimArray("w_ho_26x1000.csv", 26, 1000);

                //Signal from input nodes (image array) to hidden layer nodes.
                //Input-Hidden model weights multiplied by 1D array of image densities
                double[][] inputHiddenVec = multMatrix(w_ih, imageVecT);

                //Signal leaving hidden layer nodes
                //Applying sigmoid function to vector of hidden nodes.
                double[][] hiddenNodeSig = matrixSig(inputHiddenVec);

                //Signal going from hidden layer nodes to output layer nodes
                //Hidden-Output model weights multiplied by sigmoid vector at hidden layer
                double[][] hiddenOutputVec = multMatrix(w_ho, hiddenNodeSig);

                //Signal leaving output layer. End result signal.
                //Sigmoid function applied to output layer nodes.
                double[][] outputNodeSig = matrixSig(hiddenOutputVec);

                //Print output signal as visualization of what the final result looks like.
                System.out.println("\nNEURAL NETWORK SIGNAL OUTPUT:\n");

                char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
                int alphaLoc = 0;
                for (double[] row : outputNodeSig)
                {
                    System.out.println(Character.toUpperCase(alphabet[alphaLoc]) + ": " + Arrays.toString(row));
                    alphaLoc++;
                }
                
                //Determining which output node signal is the highest.
                //26 node signal correspond to each letter of the alphebet. Highest value is predicted letter
                answer = letterOfIndex(strongestSignal(outputNodeSig));

            }

            catch (IOException e)
            {
                e.printStackTrace();
            }
    }

    public String getAnswer()
    {
        return this.answer;
    }

    private static String letterOfIndex(int index)
    {
        switch(index)
        {
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            case 7: return "H";
            case 8: return "I";
            case 9: return "J";
            case 10: return "K";
            case 11: return "L";
            case 12: return "M";
            case 13: return "N";
            case 14: return "O";
            case 15: return "P";
            case 16: return "Q";
            case 17: return "R";
            case 18: return "S";
            case 19: return "T";
            case 20: return "U";
            case 21: return "V";
            case 22: return "W";
            case 23: return "X";
            case 24: return "Y";
            case 25: return "Z";
        }
        return "Error";
    }


    //Method finds the index of the largest number of the 26 number output array.
    private static int strongestSignal(double[][] outputSignal)
    {
        int rows = outputSignal.length;
        int cols = outputSignal[0].length;

        int highestValIndex = 0;
        double highestVal = outputSignal[0][0];
        for (int i = 0; i < rows; i++)
        {
            if (outputSignal[i][0] > highestVal)
            {
                highestValIndex = i;
                highestVal = outputSignal[i][0];
            }
        }
        return highestValIndex;
    }

    // Parses through CSV file and returns a 2D array of same dimensions.
    private static double[][] csvToTwoDimArray(String fileName, int row, int col) throws IOException
    {
        File csvFile = new File(fileName);
        Scanner sc = new Scanner(csvFile);

        double[][] csvArray = new double[row][col];
        String[] csvLine = new String[col-1];

        int i = 0;
        while(sc.hasNextLine())
        {
            //for each line, numbers in line are split by ","
            String line = sc.nextLine();
            csvLine = line.split(",");

            //numbers are cast as double and added to array row by row
            for (int num = 0; num < col; num++)
            {
                csvArray[i][num] = Double.parseDouble(csvLine[num]);
            }
            i++;
        }
        return csvArray;
    }

    //Method for multiplying two matricies together. MatA*MatB
    private static double[][] multMatrix(double[][] matrixA, double[][] matrixB)
    {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int rowsB = matrixB.length;
        int colsB = matrixB[0].length;

        //Output matrix will have rows as long as MatrixA and columns as long as MatrixB
        double[][] matrixC = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++)
        {
            for (int j = 0; j < colsB; j++)
            {
                for (int n = 0; n < colsA; n++)
                {
                    //Matrix multiplication math. Row of A multiplied and summed by Column of B.
                    matrixC[i][j] += matrixA[i][n] * matrixB[n][j];
                }
            }
        }
        return matrixC;
    }

    //Takes 1D array arranged as double[n] and returns 2D array as double[n][1]. Turns rows into columns.
    private static double[][] transposeVec(double [] array)
    {
        int cols = array.length;
        double[][] transVec = new double[cols][1];

        for (int i = 0; i < cols; i++)
        {
            transVec[i][0] = array[i];
        }
        return transVec;
    }

    //Takes a double number as arugment and applied sigmoid function to it
    private static double sigmoid(double x)
    {
        return 1/(1 + Math.exp(-x));
    }

    //Applies the sigmoid function to each number of matrix.
    //Iterates through 2D array and applied sigmoid function to each number.
    private static double[][] matrixSig(double[][] matrix)
    {
        int rows = matrix.length;
        int cols = matrix[0].length;

        double[][] matrixS = new double[rows][cols];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                matrixS[i][j] = sigmoid(matrix[i][j]);
            }
        }
        return matrixS;
    }

    //Method takes a BufferedImage and width and height (pixel length) as arguments,
    //and resizes the image. Returns the resized image as BufferedImage.
    // Method derived from https://memorynotfound.com/java-resize-image-fixed-width-height-example/
    private static BufferedImage resize (BufferedImage image, int newW, int newH)
    {
        Image temp = image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage reSizedImage = new BufferedImage(newW, newH, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g2d = reSizedImage.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        return reSizedImage;
    }

    //Takes BufferedImage, turns pixels into grayscale and returns 1D array of pixel densities where
    //Grayscaled pixel densities are proportionally scaled to work with sigmoid function.
    //Image should already by grayscale, but this will work incase image of letter is not grayscale.
    private static double[] imageToOneDimArray(BufferedImage image)
    {
        int w = image.getWidth();
        int h = image.getHeight();

        double[] imageVec = new double[w*h];
        
        int index = 0;
        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                
                //Grayscale is the average of the RGB numbers.
                //Each Pixel is represented by 3 values (RGB). We need to flatten it to 1 value.
                //Need to get color numbers for R, G, B and then take average
                Color color = new Color(image.getRGB(i, j), true);
                double red = (color.getRed());
                double green = (color.getGreen());
                double blue = (color.getBlue());
                double v = 255 - (red + green + blue) / 3;

                //Need to proportionally scale grayscale pixel densities so numbers work with sigmoid function.
                double d = ((v/255.0) * 0.99) + 0.01;
                imageVec[index] = d;
                index++;
            }
        }
        
        return imageVec;
    }
}

/**
 * Following methods used during troubleshooting that are not in final build.
 *
 * Kept for future reference.


    //Method used during troubleshooting. Transposes a 2D array. Columns into rows and rows into columns.
    public static double[][] transposeMatrix(double [][] matrix)
    {
        int rows = matrix.length;
        int cols = matrix[0].length;

        double[][] matrixT = new double[cols][rows];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                matrixT[j][i] = matrix[i][j];
            }
        }
        return matrixT;
    }

    //Method used during troubleshooting. Applied sigmoid function to each number of 1D array.
    private static double[] vecSig(double[] vector)
    {
        int cols = vector.length;

        double[] vectorS = new double[cols];

        for (int i = 0; i < cols; i++)
        {
            vectorS[i] = sigmoid(vector[i]);
        }
        return vectorS;
    }

    //Method used during build and troubleshooting. Multiplies vector by matrix.
    //Multiplies 1D array of length N by 2D array with N rows.
    public static double[] multVecMat(double[] vector, double[][] matrix)
    {
        int rows = matrix.length;
        int cols = matrix[0].length;

        double[] newVec = new double[rows];

        for (int row = 0; row < rows; row++) {
            double sum = 0;
            for (int column = 0; column < cols; column++)
            {
                sum += vector[column] * matrix[row][column];
            }
            newVec[row] = sum;
        }
        return newVec;
    }

    //Method used when building and troubleshooting. Takes BufferedImage, turns pixels into grayscale
    //and returns 2D array of pixel densities
    //Grayscaled pixel densities are proportionally scaled to work with sigmoid function.
    private static double[][] pixelMatrix(BufferedImage image)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        double[][] matrix = new double[width][height];

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                //Grayscale is the average of the RGB numbers.
                //Need to get color numbers for R, G, B and then take average
                Color color = new Color(image.getRGB(i, j), true);
                double red = (color.getRed());
                double green = (color.getGreen());
                double blue = (color.getBlue());
                double v = 255 - (red + green + blue) / 3d;

                //Need to proportionally scale grayscale pixel densities so numbers work with sigmoid function.
                double d = ((v/255.0) * 0.99) + 0.01;
                matrix[i][j] = d;
            }
        }
        return matrix;
    }

    //Method used during build and troubleshooting. Turns matrix to vector.
    //Takes 2D array and turns into 1D array where numbers from row 2 come right after row1 numbers, etc.
    private static double [] matrixToVec (double[][] matrix)
    {
        int rows = matrix.length;
        int cols = matrix[0].length;

        double [] vector = new double[rows*cols];
        int index = 0;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                vector[index] = matrix[i][j];
                index++;
            }
        }
        return vector;
    }

*/