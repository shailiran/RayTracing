package src;

public interface Surfaces {
    double intersection(Ray ray);
    Vector calcSurfaceNormal(Vector intersectionPoint, Ray ray);
    int getMaterialIndex();


}
