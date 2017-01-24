# Systemic Ranking

## Description
Upon inputs from the user, systematically add rankings by comparing with binary search, assuming strict preferences.


## Configuration
The ranker assumes two inputs: the first input is the path of the file with the objects, and the second inpt is the file in which the processed rankings will be written.

The input file must contain text with each object on its own line. There are two supported configurations for this file.

The first configuration assumes that the objects in the input file are currently unranked. If so, just list the items normally.

The second configuration assumes that a ranked list already exists, but the user will just add to it. If so, the input file should contain a single line with the "START" keyword to separate the existing rankings with the new conditions.

When the program has finished running, the output file will contain the processed rankings. If the output file had not existed before, it will be created at the specified path. If the output file had existed, it will be overwritten.


## Data Structure
The data structure chosen to represent the rankings is a linked list. This choice was made because every object in the list has a direct preference relationship with the objects both preceding and following it.


## Algorithm and Runtimes
When a new object is first added to the ranked linked list, it is compared to the existing object at the midpoint of the list. If the old object is preferred, the program knows the object belongs on the bottom half of the ranked list. If the new object is preferred, the prgram knows the object belongs on the top half of the ranked list. In a process resembling binary search, the program runs until it determines at which index the new object should be added.

While comparing takes O(1) time, finding the old object takes O(n) time. Since we do O(log n) comparisons, the entire process of adding a new object is O(nlog(n)).


## Graphical User Interface
The user is presented with two choices. Since we assume strict preferences, the user clicks the button corresponding to the preferred object. The user continues clicking until the entire file has been processed.


## Output
Finally, the interpreter traverses the linked list and adds the ranked objects to the file specified, ordered from most preferred to least preferred.