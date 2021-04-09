package src;

public class Ray {
    private Vector base;
    private Vector direction;

    public Ray(Vector base, Vector direction) {
        this.base = base;
        this.direction = direction;
    }
    public Vector getBase() { return base; }

    public Vector getDirection() { return direction; }
}
