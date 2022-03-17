# java-letterrecognizer
Java app with GUI that uses a previously trained neural network to recognize drawn uppercase letters

The application is composed of three Java files (`LetterRecognizer.java`, `RecognizerNeuralNetwork.java`, `DrawArea.java`) and two CSV files which are the trained neural network model (`w_ih_1000x784.csv`, `w_ho_26x1000.csv`).

Command to run application: 
>$ java LetterRecognizer

**DrawArea.java**:
 - Contains the _JComponent_ class for drawing canvas.
 - Added dimension constrains to make canvas 280x280, so image can be proportionally resized to 28x28 pixels. 
 - `getImage()` method that saves the drawn image to the directory and returns the File of the saved image.

**LetterRecognizer.java**:
 - Contains the main method to run application.
 - Builds and displays GUI using _JFrame_ and _ActionListener_.
 - Creates DrawArea class for drawing canvas.
 - Contains the GUI ActionEvent that:
   - Calls `getImage()` method from DrawArea class to save drawn image as .png file to directory.
   - Passes the File of the drawing to _LetterRecognizerNeuralNetwork_ class to query the network. 
   - Calls `getAnswer()` method from _LetterRecognizerNeuralNetwork_ class which is then used to display result to GUI.

**RecognizerNeuralNetwork.java**
 - Class constructor takes BufferedImage as argument.
 -	Contains methods to resize image and turn image into array of pixel densities
 -	Contains method for loading the model that is saved as CSV files. 
 -	Contains methods that perform linear algebra operations such as transposing vectors and matrix multiplication.
 -	Takes image File and queries the neural network by:
    -	Resizing image to 28x28 pixels
    -	Turning image into 1D array of pixel densities.
    -	Transposing 1D array so it can be multiplied by model weights.
    -	Loades model weights from CSV files as 2D arrays.
    -	Multiplies array of pixel densities by 2D array (1000x784) representing weights for signal between input layer and hidden layer.
          - Returns 1D array that is 1000 numbers in length.
    -	Applies sigmoid function to array.
    -	Multiplies array by 2D array (26x1000) representing weights for signal between hidden layer and output layer.
          -	Returns 1D array that is 26 numbers in length.
    -	Applies sigmoid function to array to produce final output array.
    -	Interprets the output signal
          -	The letter represented by the largest number in the network’s answer
    -	 Contains `getAnswer()` method that returns the network’s prediction.

**w_ih_1000x784.csv**
  - 1000x784 matrix of weights for the trained model that is applied when the signal goes from the input-layer to the hidden-layer.

**w_ho_26x1000.csv**
  - 26x1000 matrix of weights for the trained model that is applied when the signal goes from the hidden-layer to the output layer.

**preview of app**<br>
![app preview](https://github.com/talalifer/java-letterrecognizer/blob/7571c617d3bb1a34250ecea64ca2a1233f5695fc/Testing%20Files/appExample.png)

**example of singal output that's printed to console**<br>
```
NEURAL NETWORK SIGNAL OUTPUT:

A: [1.522049548030378E-4]
B: [1.408054711431656E-5]
C: [0.0014990289132300082]
D: [1.8551830699346734E-4]
E: [7.176077776972993E-4]
F: [0.0024556400354707867]
G: [2.6354954307582795E-5]
H: [0.9992693395774176]
I: [0.026993373960593044]
J: [0.0012844638431190212]
K: [9.632588423256176E-4]
L: [7.866615079987571E-4]
M: [5.698155082425804E-4]
N: [1.4249427242699539E-4]
O: [1.2511429640719331E-5]
P: [0.0012985146918581066]
Q: [3.486350564083017E-5]
R: [8.273238196313768E-5]
S: [4.351438273103093E-4]
T: [0.004998689717544974]
U: [5.460152924550774E-4]
V: [1.0386448623340737E-4]
W: [2.1785424218218389E-4]
X: [3.510644412371039E-5]
Y: [0.017585624386353346]
Z: [0.00771696600215225]
```
In this example, you can see that the signal for the H node is the highest, so H is used as the recognition result.
