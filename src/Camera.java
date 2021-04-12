package src;

public class Camera {
    private Vector position;
    private Vector lookAtPoint;
    private Vector upVector;
    private Vector rightVector;
    private Vector towardsVector;
    private double screenDistance;
    private double screenWidth;
    private boolean fishEyeLens;

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

        //credit: https://en.wikipedia.org/wiki/Ray_tracing_(graphics)
        this.towardsVector = lookAtPoint.subVectors(position);
        this.towardsVector.normalizeInPlace();
        this.rightVector = upVector.crossProduct(towardsVector);
        this.rightVector.normalizeInPlace();
        this.upVector = towardsVector.crossProduct(rightVector);
    }

    public Vector getPosition(){
        return this.position;
    }

    public Vector getLookAtPoint() {
        return this.lookAtPoint;
    }

    public Vector getUpVector() {
        return upVector;
    }

    public Vector getTowardsVector() { return towardsVector; }

    public Vector getRightVector() { return rightVector; }

    public double getScreenDistance() {
        return screenDistance;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public boolean isFishEyeLens() {
        return fishEyeLens;
    }



}
