import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class dataInterpreter {

    private HashMap<Integer, Dot> dotList = new HashMap<Integer, Dot>();

    // these values are for debugging, we estimate move speed at 100mm/s and turn speed at 180 deg/s
    private final double robotMoveSpeed = 257.577; // mm per second - 257.577
    private final double robotTurnSpeed = 130; // degrees per second  - 130

    private final double robotWidth = 140; // mm 
    private final double robotHeight = 200; // mm

    private final int maxScanDist = 1200; // in mm

    public void parseJSON(String s) {
        // string must contain the keyword "Dot" and a number represesnting distance, then a number representing angle, OR
        // keyword "moveForward", "moveBackward", "rotateRight", or "rotateLeft", and a number representing the duration of the movement.
        // the string must contain no more than one keyword 

        Pattern pattern = Pattern.compile("[0-9.-]+"); // regex for any number
        Matcher matcher = pattern.matcher(s);

        if (s.contains("Dot")) {
            
            matcher.find(); // we are just taking the first two numbers we find
            double dist = Double.parseDouble(s.substring(matcher.start(), matcher.end()));
            
            if (dist <= maxScanDist) {

                matcher.find();
                int angle = Integer.parseInt(s.substring(matcher.start(), matcher.end())); 

                angle -= 90; // 90 degrees is straight ahead
                angle -= 2 * angle; // flip it over y axis because I probably messed up somewhere else :)
                while (angle < 360) 
                    angle += 360; // negative angle -> positive
                angle %= 360; 

                double x = dist * Math.sin(Math.toRadians(angle)); // polar coords -> cartesian 
                double y = dist * Math.cos(Math.toRadians(angle));

                dotList.put(angle, new Dot(x, y)); // add dot to list
            } 
        }

        else if (s.contains("moveForward")) {
            matcher.find(); // just taking the first number we find for all of these
            updateListMovement(robotMoveSpeed * Double.parseDouble(s.substring(matcher.start(), matcher.end())));
        }

        else if (s.contains("moveBackward")) {
            matcher.find();
            updateListMovement(-robotMoveSpeed * Double.parseDouble(s.substring(matcher.start(), matcher.end())));
        }

        else if (s.contains("rotateRight")) {
            matcher.find();
            updateListAngle((int) Math.round(robotTurnSpeed * Double.parseDouble(s.substring(matcher.start(), matcher.end()))));
        }

        else if (s.contains("rotateLeft")) {
            matcher.find();
            updateListAngle((int) Math.round(-robotTurnSpeed * Double.parseDouble(s.substring(matcher.start(), matcher.end()))));
        }
    }

    private void updateListMovement(double moved) { 
        // update list with (forward or backward) distance moved 
        HashMap<Integer, Dot> temp = new HashMap<Integer, Dot>();

        for (int i = 0; i < 360; i++) {

            if (dotList.get(i) != null) {

                double x = dotList.get(i).getx(); // extract info from dot
                double y = dotList.get(i).gety() - moved; 

                double newAngle = Math.toDegrees(Math.atan2(x, y));  // cartesian coords -> polar

                while (newAngle < 0) //negative angle -> positive
                    newAngle += 360; 
                newAngle %= 360; 

                double newDist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); 

                double newx = newDist * Math.sin(Math.toRadians(newAngle)); // polar coords -> cartesian 
                double newy = newDist * Math.cos(Math.toRadians(newAngle));

                temp.put((int) (Math.round(newAngle)), new Dot(newx, newy)); 
            }
        }      
        dotList = temp; 
    }

    private void updateListAngle(int turned) { 
        // update list with angle turned in degrees
        // assumes turning left rotates around the left wheel, and vice versa. 
        HashMap<Integer, Dot> temp = new HashMap<Integer, Dot>();

        for (int i = 0; i < 360; i++) {
            Dot d = dotList.get(i);
            if (d != null) {

                double x = turned > 0 ? // subtract point we are rotating around
                    d.getx() - robotWidth / 2 : // we are rotating right
                    d.getx() + robotWidth / 2;  // left
                double y = d.gety() + robotHeight / 2;
                
                double newx = x * Math.cos(Math.toRadians(turned)) - y * Math.sin(Math.toRadians(turned)); // get new coords for dot
                double newy = y * Math.cos(Math.toRadians(turned)) + x * Math.sin(Math.toRadians(turned)); 

                newx += turned > 0 ? robotWidth / 2 : -robotWidth / 2; // add back point 
                newy -= robotHeight / 2;

                int newAngle = (int) Math.round(Math.toDegrees(Math.atan2(newx, newy))); 

                while (newAngle < 0) // negative angle -> positive
                    newAngle += 360; 
                newAngle %= 360; 

                temp.put((int)newAngle, new Dot(newx, newy)); 
            }
        }
        dotList = temp; 
    }

    public void clearDotList() {
        // clears dot list
        dotList.clear();
    }

    public void printDots() { // prints list with angle in deg, x in mm, and y in mm 
        for (int i = 0; i < 360; i++) {
            if (dotList.get(i) != null) {
                System.out.println("Angle " + i + " -- x: " + dotList.get(i).getx() + ", y: " + dotList.get(i).gety());
            }
        }
    }

    public Dot[] getDots() {
        // returns array of dots relative to the robot in mm
        Dot[] array = new Dot[dotList.size()];
        int index = 0;

        for (Dot d : dotList.values()) {
            array[index] = new Dot(d.getx(), d.gety());
            index++;
        }
        return array; 
    }

    public Dot[] getScaledList(int panelHeight, int panelWidth) {
        // returns an array of dots, scaled to panel in pixels
        Dot[] scaledList = new Dot[dotList.size()];

        int index = 0;
        for (Dot d : dotList.values()) {
            double x = d.getx(); double y = d.gety(); 
            x /= maxScanDist; y /= maxScanDist; // get a double between 0 and 1, 1 being the max scan dist
            x *= panelWidth / 2; y *= panelHeight / 2; // scale to size of panel 
            x += panelWidth / 2; y += panelHeight / 2;  // move to middle of panel 

            scaledList[index] = new Dot(x, y);
            index++; 
        } 

        return scaledList; 
    }
}




