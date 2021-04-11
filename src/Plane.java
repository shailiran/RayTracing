package src;

public class Plane implements Surfaces {
    private Vector normal;
    private double offset;
    private int materialIndex;

    public Plane(String [] params) {
        this.normal = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.offset = Double.parseDouble(params[3]);
        this.materialIndex = Integer.parseInt(params[4]);
    }
    public Plane (Vector normal, double offset, int materialIndex) {
        this.normal = normal;
        this.offset = offset;
        this.materialIndex = materialIndex;
    }

    @Override
    public double intersection(Ray ray) {
        double t = -1 * (ray.getBase().dotProduct(normal) + offset) / (ray.getDirection().dotProduct(normal));
        return t;
    }
}
