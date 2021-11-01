import java.util.LinkedList;
import org.json.JSONObject;


class dataInterpreter {
    static LinkedList<Dot> ll = new LinkedList<Dot>();
    // this will be a ref to the linked list somewhere else later, but I'm making it here for now

    public static void main(String[] args) {
        parseJSON("{\"Dot\":{\"distance\":78.45,\"angle\":26.56}}");
        printList(ll);
    }

    public static void parseJSON(String s) {
        // {"Dot":{"distance":78.45,"angle":26.56}}

        JSONObject jo = new JSONObject(s);

        if (jo.opt("Dot") != "null") {
            double dist = Double.parseDouble(jo.getJSONObject("Dot").get("distance").toString());
            double angle = Double.parseDouble(jo.getJSONObject("Dot").get("angle").toString());

            double x = dist*Math.sin(angle);
            double y = dist*Math.cos(Math.toRadians(angle));

            addDot(new Dot(x, y));
        }

        else if (jo.opt("movement") != "null") {
            // todo
        }

        else if (jo.opt("angle") != "null") {
            // todo
        }
    }

    public static void updateListMovement(double dist) { 
        // update list with (forward or backward) distance moved 
        for (Dot d : ll) {
            d.update(d.getx(), d.gety() - dist);
            }      
    }

    public static void updateListAngle(double angle) { 
        // update list with angle turned in degrees
        for (Dot d : ll) {
            d.update(d.getx() * Math.cos(Math.toRadians(angle)) + d.gety() * Math.sin(Math.toRadians(angle)), 
                -d.getx() * Math.sin(Math.toRadians(angle)) + d.gety() * Math.cos(Math.toRadians(angle)));
            }      
    }

    public static void printList(LinkedList<Dot> ll) {
        for (int i = 0; i < ll.size(); i++) {
            Dot d = ll.get(i);
            System.out.println("Dot " + i + " -- x: " + d.getx() + ", y: " + d.gety());
        }
        System.out.println();
    }

    public static void addDot(Dot d) { 
        ll.add(d);
    }
}


class Dot {
    double x; 
    double y;

    public Dot(double x, double y) {
        this.x = x; this.y = y;
    }

    public void update(double newx, double newy) { 
        x = newx; y = newy;
    }

    public double getx() {
        return x;
    }

    public double gety() {
        return y;
    }
}
