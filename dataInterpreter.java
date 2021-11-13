import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


class dataInterpreter {
    private HashMap<Integer, Dot> dotList = new HashMap<Integer, Dot>();

    private final double robotMoveSpeed = 1166.66666667; // mm per second 
    private final double robotTurnSpeed = 180; // degrees per second 

    // private final double robotMoveSpeed = 1; // debugging speeds 
    // private final double robotTurnSpeed = 1; 

    public void parseJSON(String s) {
        // string must contain the keyword "Dot" and a number represesnting distance, then a number representing angle, OR
        // keyword "moveForward", "moveBackward", "rotateRight", or "rotateLeft", and a number representing the duration of the movement.
        // the string must contain no more than one keyword 

        Pattern pattern = Pattern.compile("[0-9.-]+"); // regex for any number
        Matcher matcher = pattern.matcher(s);

        if (s.contains("Dot")) {
            
            matcher.find(); // we are just taking the first two numbers we find
            double dist = Double.parseDouble(s.substring(matcher.start(), matcher.end()));
            
            matcher.find();
            Integer angle = (int) (Double.parseDouble(s.substring(matcher.start(), matcher.end())) + 0.5);

            angle -= 90; // 90 degrees is straight ahead 
            while (angle < 0) // if angle is negative, make it positive
                angle += 360; 
            angle %= 360; 

            double x = dist * Math.sin(Math.toRadians(angle)); // polar coords -> cartesian 
            double y = dist * Math.cos(Math.toRadians(angle)); // transform

            dotList.put(angle, new Dot(x, y));
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

                int newAngle = (int) Math.round((Math.toDegrees(Math.atan2(x, y)) + 360000) % 360); // cartesian coords -> polar
                double newDist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); 

                double newx = newDist * Math.sin(Math.toRadians(newAngle)); // polar coords -> cartesian 
                double newy = newDist * Math.cos(Math.toRadians(newAngle)); // transform

                temp.put(newAngle, new Dot(newx, newy)); 
            }
        }      
        dotList = temp; 
    }

    private void updateListAngle(int angle) { 
        // update list with angle turned in degrees
        HashMap<Integer, Dot> temp = new HashMap<Integer, Dot>();

        for (int i = 0; i < 360; i++) {
            if (dotList.get(i) != null) {
                temp.put((i - angle + 360) % 360, dotList.get(i)); 
            }
        }
        dotList = temp; 
    }

    public void printList() {
        for (int i = 0; i < 360; i++) {
            if (dotList.get(i) != null) {
                System.out.println("Angle " + i + " -- x: " + dotList.get(i).getx() + ", y: " + dotList.get(i).gety());
            }
        }
    }


    public Dot[] getList() {
        // returns array of dots (w/ +250 to x and y)
        Dot[] array = new Dot[dotList.size()];
        int index = 0;

        for (Dot d : dotList.values()) {
            array[index] = new Dot(d.getx() + 250, d.gety() + 250);
        }
        return array; 
    }
}




