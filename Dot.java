public class Dot {
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
