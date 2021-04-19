package src;
import java.util.List;

public class Intersection {
    private double minT;
    private Surfaces minSurface;
    private boolean hasIntersection;

    public Intersection(double minT, Surfaces minSurface, boolean hasIntersection) {
        this.minT = minT;
        this.minSurface = minSurface;
        this.hasIntersection = hasIntersection;
    }

    public double getMinT() {
        return minT;
    }

    public Surfaces getMinSurface() {
        return minSurface;
    }

    public boolean getHasIntersection() {
        return this.hasIntersection;
    }

    public static Intersection findIntersection(Ray ray, Scene scene, boolean flagTransparency) {
        double t;
        double minT = Double.MAX_VALUE;
        List<Surfaces> surfaces;
        if (flagTransparency) {
            surfaces = scene.getTransparencySurfaces();
        } else {
            surfaces = scene.getSurfaces();
        }
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

    public static boolean checkIntersection(Ray ray, Scene scene, double norm) {
        double t;
        for (Surfaces surface : scene.getSurfaces()) {
            t = surface.intersection(ray);
            if (t > 0 && t < norm) {
                return true;
            }
        }
        return false;
    }


}
