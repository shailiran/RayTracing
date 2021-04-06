package src;
import java.lang.Math;

public class Vector {
    double[] values;
    int n;
    double norm;

    public Vector (double[] values) {
        this.values = values;
        this.n = values.length;
        this.norm = calcNorm(values);
    }

    private double calcNorm(double[] values){
        double sum = 0, res;
        for (int i = 0; i < values.length; i++){
            sum += Math.pow(values[i], 2);
        }
        res = Math.sqrt(sum);
        return res;
    }

    public double dotProduct (Vector v) {
        double sum = 0;
        for (int i = 0; i < v.n; i++){
            sum += v.values[i] * this.values[i];
        }
        return sum;
    }

    public double crossProduct (Vector v) {

    return 0;
    }
}
