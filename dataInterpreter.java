import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


class dataInterpreter {
    private LinkedList<Dot> dotList = new LinkedList<Dot>();

    private final double robotMoveSpeed = 1; // mm per second 
    private final double robotTurnSpeed = 1; // degrees per second 

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
            double angle = Math.toRadians(Double.parseDouble(s.substring(matcher.start(), matcher.end())));

            double x = dist*Math.sin(angle);
            double y = dist*Math.cos(angle);

            addDot(new Dot(x, y));
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
            updateListAngle(robotTurnSpeed * Double.parseDouble(s.substring(matcher.start(), matcher.end())));
        }

        else if (s.contains("rotateLeft")) {
            matcher.find();
            updateListAngle(-robotTurnSpeed * Double.parseDouble(s.substring(matcher.start(), matcher.end())));
        }
    }

    private void updateListMovement(double dist) { 
        // update list with (forward or backward) distance moved 
        for (Dot d : dotList) {
            d.update(d.getx(), d.gety() - dist);
            }      
    }

    private void updateListAngle(double angle) { 
        // update list with angle turned in degrees
        angle = Math.toRadians(angle);
        for (Dot d : dotList) {
            d.update(d.getx() * Math.cos(angle) + d.gety() * Math.sin(angle), 
                d.gety() * Math.cos(angle) - d.getx() * Math.sin(angle));
            }      
    }

    public void printList() { // prints dot list 
        for (int i = 0; i < dotList.size(); i++) {
            Dot d = dotList.get(i);
            System.out.println("Dot " + i + " -- x: " + d.getx() + ", y: " + d.gety());
        }
        System.out.println();
    }

    public LinkedList<Dot> getList() {
        // returns dot list 
        return dotList;
    }

    private void addDot(Dot d) { 
        // adds dot to dot list 
        dotList.add(d);
    }
}




