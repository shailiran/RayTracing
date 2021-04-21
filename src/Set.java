package src;

public class Set {
    private final Color backgroundColor;
    private int NumberOfShadowsRays;
    private int maxRecursionLevel;

    public Set(String[] params) {
        this.backgroundColor = new Color(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.NumberOfShadowsRays = Integer.parseInt(params[3]);
        this.maxRecursionLevel = Integer.parseInt(params[4]);
    }

    public Color getBackgroundColor(){
        return this.backgroundColor;
    }

    public int getMaxRecursionLevel() {
        return maxRecursionLevel;
    }

    public int getNumberOfShadowsRays() {
        return NumberOfShadowsRays;
    }
}
