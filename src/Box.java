package src;

public class Box {
    private Point center;
    private double edgeLen;
    private int materialIndex;

    public Box(String [] params) {
        this.center = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.edgeLen = Double.parseDouble(params[3]);
        this.materialIndex = Integer.parseInt(params[4]);
    }
}
