# java-letterrecognizer
Java app with GUI that uses a previously trained neural network to recognize drawn uppercase letters

Command to run application: 
>$ java LetterRecognizer

**DrawArea.java**
Contains the JComponent class for drawing canvas.
Added dimension constrains to make canvas 280x280, so image can be proportionally resized to 28x28 pixels. 
getImage() method that saves the drawn image to the directory and returns the File of the saved image.

**LetterRecognizer.java**
Contains the main method to run application.
Builds and displays GUI using >JFrame and >ActionListener.
Creates DrawArea class for drawing canvas.
Contains the GUI ActionEvent that:
  Calls getImage()method from DrawArea class to save drawn image as .png file to directory.
  Passes the File of the drawing to LetterRecognizerNeuralNetwork class to query the network. 
  Calls getAnswer() method from LetterRecognizerNeuralNetwork class which is then used to display result to GUI.
