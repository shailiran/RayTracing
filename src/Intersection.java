package src;
import java.util.List;

public class Intersection {
    double minT;
    Surfaces minSurface;
    boolean hasIntersection;

    public Intersection(double minT, Surfaces minSurface, boolean hasIntersection) {
        this.minT = minT;
        this.minSurface = minSurface;
        this.hasIntersection = hasIntersection;
    }

    public boolean getHasIntersection() {
        return this.hasIntersection;
    }
    public static Intersection findIntersection(Ray ray, Scene scene) {
        double t, minT = Double.MAX_VALUE;
        List<Surfaces> surfaces = scene.getSurfaces();
        Surfaces closestSurface = null;
        boolean hasIntersection = false;
        for (Surfaces surface: surfaces) {
            t = surface.intersection(ray);
            if (t > 0 && t < minT) {
                minT = t;
                closestSurface = surface;
                hasIntersection = true;
            }
        }
        return new Intersection(minT, closestSurface, hasIntersection);
    }


}
