import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


class dataInterpreter {
    private static LinkedList<Dot> DotList = new LinkedList<Dot>();

    public static void main(String[] args) {
        parseJSON("{\"Dot\":{\"distance\":78.45,\"angle\":26.56}}");
        printList(DotList);
    }

    public static void parseJSON(String s) {
        // parse JSON - either a new dot, or a previous movement or turn
        // {"Dot":{"distance":78.45,"angle":26.56}}

        // TODO: right now it works assuming a distance is passed, but will have to calculate distance travelled using time in future

        Pattern pattern = Pattern.compile("[0-9|.]+"); // regex for any number
        Matcher matcher = pattern.matcher(s);

        if (s.contains("Dot")) {

            matcher.find(); // we are just taking the first two numbers we find

            double dist = Double.parseDouble(s.substring(matcher.start(), matcher.end()));

            matcher.find();

            double angle = Double.parseDouble(s.substring(matcher.start(), matcher.end()));

            double x = dist*Math.sin(angle);
            double y = dist*Math.cos(Math.toRadians(angle));

            addDot(new Dot(x, y));
        }

        else if (s.contains("move")) {
            matcher.find();
            updateListMovement(Double.parseDouble(s.substring(matcher.start(), matcher.end())));
        }

        else if (s.contains("angle")) {
            matcher.find();
            updateListAngle(Double.parseDouble(s.substring(matcher.start(), matcher.end())));
        }
    }

    private static void updateListMovement(double dist) { 
        // update list with (forward or backward) distance moved 
        for (Dot d : DotList) {
            d.update(d.getx(), d.gety() - dist);
            }      
    }

    private static void updateListAngle(double angle) { 
        // update list with angle turned in degrees
        for (Dot d : DotList) {
            d.update(d.getx() * Math.cos(Math.toRadians(angle)) + d.gety() * Math.sin(Math.toRadians(angle)), 
                -d.getx() * Math.sin(Math.toRadians(angle)) + d.gety() * Math.cos(Math.toRadians(angle)));
            }      
    }

    private static void printList(LinkedList<Dot> ll) { // prints dot list 
        for (int i = 0; i < ll.size(); i++) {
            Dot d = ll.get(i);
            System.out.println("Dot " + i + " -- x: " + d.getx() + ", y: " + d.gety());
        }
        System.out.println();
    }

    public static LinkedList<Dot> getList() {
        // returns dot list 
        return DotList;
    }

    private static void addDot(Dot d) { 
        // adds dot to dot list 
        DotList.add(d);
    }
}



