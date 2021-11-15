import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class dataInterpreter {

    private HashMap<Integer, Dot> dotList = new HashMap<Integer, Dot>();

    // these values are for debugging, we estimate move speed at 100mm/s and turn speed at 180 deg/s
    private final double robotMoveSpeed = 100; // mm per second 
    private final double robotTurnSpeed = 180; // degrees per second 

    private final int maxScanDist = 400; // in mm

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
                Integer angle = (int) (Double.parseDouble(s.substring(matcher.start(), matcher.end())) + 0.5); 

                angle -= 90; // 90 degrees is straight ahead 
                while (angle < 360) 
                    angle += 360; // negative angle -> positive
                angle %= 360; 

                double x = dist * Math.sin(Math.toRadians(angle)); // polar coords -> cartesian 
                double y = dist * Math.cos(Math.toRadians(angle)); // transform

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
            updateListAngle((int) (robotTurnSpeed * Double.parseDouble(s.substring(matcher.start(), matcher.end())) + 0.5));
        }

        else if (s.contains("rotateLeft")) {
            matcher.find();
            updateListAngle((int) (-robotTurnSpeed * Double.parseDouble(s.substring(matcher.start(), matcher.end())) + 0.5));
        }
    }

    private void updateListMovement(double moved) { 
        // update list with (forward or backward) distance moved 
        HashMap<Integer, Dot> temp = new HashMap<Integer, Dot>();

        for (int i = 0; i < 360; i++) {

            if (dotList.get(i) != null) {

                double x = dotList.get(i).getx(); // extract info from dot
                double y = dotList.get(i).gety() - moved; 

                int newAngle = (int) Math.round(Math.toDegrees(Math.atan2(x, y)));  // cartesian coords -> polar

                while (newAngle < 0) //negative angle -> positive
                    newAngle += 360; 
                newAngle %= 360; 

                double newDist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); 

                double newx = newDist * Math.sin(Math.toRadians(newAngle)); // polar coords -> cartesian 
                double newy = newDist * Math.cos(Math.toRadians(newAngle));

                temp.put(newAngle, new Dot(newx, newy)); 
            }
        }      
        dotList = temp; 
    }

    private void updateListAngle(int turned) { 
        // update list with angle turned in degrees
        HashMap<Integer, Dot> temp = new HashMap<Integer, Dot>();

        for (int i = 0; i < 360; i++) {
            Dot d = dotList.get(i);
            if (d != null) {

                int newAngle = i - turned; 

                while (newAngle < 0) // negative angle -> positive
                    newAngle += 360; 
                newAngle %= 360; 

                double newx = d.getx() * Math.cos(Math.toRadians(turned)) - d.gety() * Math.sin(Math.toRadians(turned));
                double newy = d.gety() * Math.cos(Math.toRadians(turned)) + d.getx() * Math.sin(Math.toRadians(turned)); // get new coords for dot

                temp.put(newAngle, new Dot(newx, newy)); 
            }
        }
        dotList = temp; 
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




