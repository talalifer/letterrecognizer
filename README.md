# java-letterrecognizer
Java app with GUI that uses a previously trained neural network to recognize drawn uppercase letters

Command to run application: $ java LetterRecognizer

##DrawArea.java
•	Contains the JComponent class for drawing canvas.
•	Added dimension constrains to make canvas 280x280, so image can be proportionally resized to 28x28 pixels. 
•	getImage() method that saves the drawn image to the directory and returns the File of the saved image.
