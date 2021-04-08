package src;

public class Sphere implements Surfaces {
    private Point center;
    private double radius;
    private int materialIndex;

    public Sphere(String [] params) {
        this.center = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.radius = Double.parseDouble(params[3]);
        this.materialIndex = Integer.parseInt(params[4]);
    }


}
