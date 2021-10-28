import java.util.LinkedList;

class dataInterpreter {
    static LinkedList<Dot> ll = new LinkedList<Dot>();
    // this will be a ref to the linked list somewhere else later, but I'm making it here for now

    public static void main(String[] args) {
        ll.add(new Dot(0, 5));

        printList(ll);
        updateListMovement(1);
        printList(ll);
        updateListAngle(90);
        printList(ll);
    }

    public Dot parseJSON(String s) {
        // TODO
        return new Dot(0, 0);
    }

    public static void updateListMovement(double dist) { 
        // update list with distance moved
        for (Dot d : ll) {
            d.update(d.getx(), d.gety() - dist);
            }      
    }

    public static void updateListAngle(double angle) { 
        // update list with distance moved
        for (Dot d : ll) {
            d.update(d.getx() * Math.cos(Math.toRadians(angle)) + d.gety() * Math.sin(Math.toRadians(angle)), 
                -d.getx() * Math.sin(Math.toRadians(angle)) + d.gety() * Math.cos(Math.toRadians(angle)));
            }      
    }

    public static void printList(LinkedList<Dot> ll) {
        for (int i = 0; i < ll.size(); i++) {
            Dot d = ll.get(i);
            System.out.println("Dot " + i + " -- dist: " + Math.round(d.getx()) + ", angle: " + Math.round(d.gety()));
        }
        System.out.println();
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
