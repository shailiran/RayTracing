package src;

public class Plane {
    private Vector normal;
    private double offset;
    private int materialIndex;

    public Plane(String [] params) {
        this.normal = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.offset = Double.parseDouble(params[3]);
        this.materialIndex = Integer.parseInt(params[4]);
    }
}
