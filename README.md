# Text-Based-Game
A Text Based Game I've been working on in Java

ABOUT
This is a text based game I've been working on in Java. It features a cool GUI. The main premise of any of the games is to move between
rooms, interacting with objects inside of them. The main gameplay will be exploration based and puzzle solving.

HOW IT WORKS
The game starts with the a level selection window. This searches the src/Levels directory for xml files that will be read as levels
The game then loads the selected level using the LevelConstructorXMLParser.java
Each level has a bunch of rooms. Each room contains objects and exits. Objects exist within rooms, and exits lead between rooms.

When the player enters something into the input box on the GUI and presses go, the GUI passes this into the Input function of the
game. The game then runs this text through the CommandInterpereter. This tries to figure out what the player wants to do to what.
The game then tries to do the action on the thing. If the thing can do the action, text is outputted to the GUI. Otherwise, an 
error message pops up.

HOW TO CREATE A LEVEL
Coming soon...

NOTE
Not everything is working at the moment as described or implied
