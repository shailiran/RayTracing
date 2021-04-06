package src;

public class Light {
    Vector position;
    Color color;
    double specularIntensity;
    double shadowIntensity;
    double lightRadius;

    public Light(Vector position,Color color, double specularIntensity, double shadowIntensity, double lightRadius) {
       this.position = position;
       this.color = color;
       this.specularIntensity = specularIntensity;
       this.shadowIntensity = shadowIntensity;
       this.lightRadius = lightRadius;
    }

}
