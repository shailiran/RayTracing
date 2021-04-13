package src;

public class Materials {
    private Color diffuseColor;
    private Color specularColor;
    private Color reflectionColor;
    private double phongSpecularityCoefficient;
    private double transparency;

    public Materials(String[] params) {
        this.diffuseColor = new Color(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.specularColor = new Color(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
                Double.parseDouble(params[5]));
        this.reflectionColor =  new Color(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
                Double.parseDouble(params[8]));
        this.phongSpecularityCoefficient = Double.parseDouble(params[9]);
        this.transparency = Double.parseDouble(params[10]);
    }

    public Color getDiffuseColor() {
        return diffuseColor;
    }

    public Color getReflectionColor() {
        return reflectionColor;
    }

    public Color getSpecularColor() {
        return specularColor;
    }

    public double getPhongSpecularityCoefficient() {
        return phongSpecularityCoefficient;
    }

    public double getTransparency() {
        return transparency;
    }


}
