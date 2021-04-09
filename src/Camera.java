package src;

public class Camera {
    Vector position;
    Vector lookAtPoint;
    Vector upVector;
    double screenDistance;
    double screenWidth;
    boolean fishEyeLens;

    public Camera(String[] params) {
        this.position = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.lookAtPoint = new Vector(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
                Double.parseDouble(params[5]));
        this.upVector = new Vector(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
                Double.parseDouble(params[8]));
        this.screenDistance = Double.parseDouble(params[9]);
        this.screenWidth = Double.parseDouble(params[10]);
        this.fishEyeLens = Boolean.parseBoolean(params[11]);
    }



}
