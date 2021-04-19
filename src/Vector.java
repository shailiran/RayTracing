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

    public double getNorm() {return norm; }




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

    public Vector multByScalar(double s) {
        Vector res = new Vector();
        double x = this.x * s;
        double y = this.y * s;
        double z = this.z * s;
        res.setX(x);
        res.setY(y);
        res.setZ(z);
        res.norm = calcNorm(x,y,z);

        return res;
    }

    public void normalizeInPlace() {
        if (this.norm == 0) {
            this.setX(0);
            this.setY(0);
            this.setZ(0);
            this.norm = 0;
        } else {
            this.setX(this.x / this.norm);
            this.setY(this.y / this.norm);
            this.setZ(this.z / this.norm);
            this.norm = 1;
        }
    }

    public Vector normalizeVector() {
        Vector normalize = new Vector();
        if (this.norm == 0) {
            normalize.setX(0);
            normalize.setY(0);
            normalize.setZ(0);
            normalize.norm = 0;
        } else {
            normalize.setX(this.x / this.norm);
            normalize.setY(this.y / this.norm);
            normalize.setZ(this.z / this.norm);
            normalize.norm = 1;
        }
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
       double x = this.y*v.z - this.z*v.y;
       double y = this.z*v.x - this.x*v.z;
       double z = this.x*v.y - this.y*v.x;
       cross.setX(x);
       cross.setY(y);
       cross.setZ(z);
       cross.norm = calcNorm(x, y, z);

       return cross;
    }

}
