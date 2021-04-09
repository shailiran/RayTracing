package src;

public class Box implements Surfaces {
    private Vector center;
    private double edgeLen;
    private int materialIndex;

    public Box(String [] params) {
        this.center = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.edgeLen = Double.parseDouble(params[3]);
        this.materialIndex = Integer.parseInt(params[4]);
    }

    //credit: https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-box-intersection
    @Override
    public double intersection(Ray ray) {
        return 0;
    }
}
