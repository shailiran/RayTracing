package src;
import java.lang.Math;

public class Vector {
    private double x;
    private double y;
    private double z;
    private double norm;

    public Vector(){}

    public Vector (double x, double y,double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.norm = calcNorm(x,y,z);
    }

    public void setX(double x){ this.x = x; }

    public void setY(double y){ this.y = y; }

    public void setZ(double z){ this.z = z; }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }




    private double calcNorm(double x, double y, double z){
        double sum, res;
        sum = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) ;
        res = Math.sqrt(sum);
        return res;
    }

    public Vector subVectors(Vector v) {
        Vector subVector = new Vector(this.x - v.getX(), this.y - v.getY(), this.z - v.getZ());
        return subVector;
    }

    public Vector addVectors(Vector v) {
        Vector addVector = new Vector(this.x + v.getX(), this.y + v.getY(), this.z + v.getZ());
        return addVector;
    }

    public Vector normalizeVector() {
        Vector normalize = new Vector();
        normalize.setX(this.x / this.norm);
        normalize.setY(this.y / this.norm);
        normalize.setZ(this.z / this.norm);
        return normalize;
    }

    public double dotProduct (Vector v) {
        double sum;
        sum = v.x*this.x + v.y*this.y + v.z*this.z;
        return sum;
    }

    public double calcAngle(Vector v) {
        double res;
        double dotProductV = this.dotProduct(v);
        res = Math.acos(dotProductV / (this.norm * v.norm));
        return res;
    }

    //the next equation is from wikipedia: https://en.wikipedia.org/wiki/Cross_product
    public Vector crossProduct (Vector v) {
       Vector cross = new Vector();
       cross.setX(this.y*v.z - this.z*v.y);
       cross.setY(this.z*v.x - this.x*v.z);
       cross.setZ(this.x*v.y - this.y*v.x);

       return cross;
    }

}
