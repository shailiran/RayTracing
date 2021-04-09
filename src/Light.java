package src;

public class Light {
    Vector position;
    Color color;
    double specularIntensity;
    double shadowIntensity;
    double lightRadius;

    public Light(String[] params) {
       this.position = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
               Double.parseDouble(params[2]));
       this.color = new Color(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
               Double.parseDouble(params[5]));
       this.specularIntensity = Double.parseDouble(params[6]);
       this.shadowIntensity = Double.parseDouble(params[7]);
       this.lightRadius = Double.parseDouble(params[8]);
    }

}
