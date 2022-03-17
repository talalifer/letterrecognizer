# java-letterrecognizer
Java app with GUI that uses a previously trained neural network to recognize drawn uppercase letters

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
