import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


class dataInterpreter {
    private HashMap<Integer, Double> dotList = new HashMap<Integer, Double>();

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

            dotList.put(angle, dist);
        }

        else if (s.contains("moveForward")) {
            matcher.find();
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
        HashMap<Integer, Double> temp = new HashMap<Integer, Double>();

        for (int i = 0; i < 360; i++) {
            if (dotList.get(i) != null) {
                double x = dotList.get(i) * Math.sin(Math.toRadians(i)); 
                double y = dotList.get(i) * Math.cos(Math.toRadians(i)) - moved; 

                int newAngle = (int) Math.round((Math.toDegrees(Math.atan2(x, y)) + 360000) % 360);
                double newDist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); 

                temp.put(newAngle, newDist); 
            }
        }      
        dotList = temp; 
    }

    private void updateListAngle(int angle) { 
        // update list with angle turned in degrees
        HashMap<Integer, Double> temp = new HashMap<Integer, Double>();

        for (int i = 0; i < 360; i++) {
            if (dotList.get(i) != null) {
                temp.put((i - angle + 360000) % 360, dotList.get(i)); 
            }
        }
        dotList = temp; 
    }

    public void printList() {
        for (int i = 0; i < 360; i++) {
            if (dotList.get(i) != null) {
                System.out.println("angle: " + i + ", dist: " + dotList.get(i));
            }
        }
    }


    public HashMap<Integer, Double> getList() {
        // returns dot list 
        return dotList;
    }
}




