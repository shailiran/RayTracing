package src;

public class Sphere implements Surfaces {
    private Vector center;
    private double radius;
    private int materialIndex;

    public Sphere(String [] params) {
        this.center = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.radius = Double.parseDouble(params[3]);
        this.materialIndex = Integer.parseInt(params[4]);
    }

    @Override
    public double intersection(Ray ray) {
        Vector L = center.subVectors(ray.getBase());
        double t_ca = L.dotProduct(ray.getDirection());
        if (t_ca < 0) {
            return 0;
        }
        double dSquare = L.dotProduct(L) - Math.pow(t_ca,2);
        if (dSquare > Math.pow(radius, 2)) {
            return 0;
        }
        double t_hc = Math.sqrt(Math.pow(radius, 2) - dSquare);
        double t = Math.min(t_ca - t_hc, t_ca + t_hc);
        return t;
    }
}
