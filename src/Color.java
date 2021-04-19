package src;

public class Color {
    private double red;
    private double green;
    private double blue;

    public Color(){}

    public Color(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double getBlue() {
        return blue;
    }

    public double getGreen() {
        return green;
    }

    public double getRed() {
        return red;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public void setToZero() {
        this.red = 0;
        this.green = 0;
        this.blue = 0;
    }
}
