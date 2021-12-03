# Robot-Radar
This is a project for CSC 380-Software Engineering.
<hr>
Goal<br>
To create a robot radar that will detect objects and display them on the GUI screen as the robot moves.
<hr>
Project Details<br>
Please see document specification file for a detail infomation about this project
<hr>
<pre>
classes overview:
  Robot driver: TCP server and also handling backend function for the server side
  Robot Manager: TPC client and also the main controller for the cilent side (main method)
  Robot data interpreter: handle incoming and outcoming data
  Robot command interpreter: handle packing command
  UserInterface: main GUI for the User
  Dot: basic structure of a data point to be stored 
</pre>
<hr>

<hr>
<pre>
In order to make sure everything works properly, Java version 8 and up is required, Python version 2.7 is required, and bash is required.
This guild is made for the Raspbian Dexter OS, and requires a gopigo2 and gopigo3 software as well and corresponding GoPiGo robot, with a distance sensor and raspberry pi camera.  

Server Download and Build:
Clone git repository 
Go into Robot-Radar Directory
Open command terminal in directory 
Make ustreamer.sh executable by running: chmod u+xr ustreamer.sh
Make install_ustreamer.sh executable by running: chmod u+xr install_ustreamer.sh
Build server dependencies by running install_ustreamer.sh: ./install_ustreamer.sh
Run server
To run server run python robotDriverServer.py in the command terminal in the cloned repository
The default port for the video is 8080 and can be modified in the ustreamer.sh. Camera rotation and  resolution can also be changed in this file. Once the video starts It will run until the Raspberry pi is restarted.

Client Download, Build and run:
Clone git repository
Go into Robot-Radar Directory
Raw command terminal solution
Open command terminal in directory 
To compile Program use javac *.java (Guild here:https://stackoverflow.com/questions/6623161/javac-option-to-compile-all-java-files-under-a-given-directory-recursively)
On terminal type java RobotManager then run
Run .jar file solution
On window make sure to set up java run path correctly (Guild here : https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html)
Open release folder
Double click/run RobotClient.jar
If on linux command line is also an option (Guild here : https://askubuntu.com/questions/101746/how-can-i-execute-a-jar-file-from-the-terminal)

<note: if after double click, program not running then please check java version or use command line to find the problem>
</pre>
<hr>
