package src;

import java.util.ArrayList;
import java.util.List;

public class Box implements Surfaces {
    private Vector center;
    private double edgeLen;
    private int materialIndex;
    private Plane[] boxPlanes = new Plane[6];

    public Box(String [] params) {
        this.center = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.edgeLen = Double.parseDouble(params[3]);
        this.materialIndex = Integer.parseInt(params[4]);
    }
    public void findBoxPlanes() {
        Vector normalX = new Vector(1,0,0);
        Vector normalY = new Vector(0,1,0);
        Vector normalZ = new Vector(0,0,1);
        double offset;

        offset = this.center.getX() + 0.5 * edgeLen;
        Plane planeYZ1 = new Plane(normalX, offset, this.materialIndex);
        boxPlanes[0] = planeYZ1;

        offset = this.center.getX() - 0.5 * edgeLen;
        Plane planeYZ2 = new Plane(normalX, offset, this.materialIndex);
        boxPlanes[1] = planeYZ2;

        offset = this.center.getY() + 0.5 * edgeLen;
        Plane planeXZ1 = new Plane(normalY, offset, this.materialIndex);
        boxPlanes[2] = planeXZ1;

        offset = this.center.getY() - 0.5 * edgeLen;
        Plane planeXZ2 = new Plane(normalY, offset, this.materialIndex);
        boxPlanes[3] = planeXZ2;

        offset = this.center.getZ() + 0.5 * edgeLen;
        Plane planeXY1 = new Plane(normalZ, offset, this.materialIndex);
        boxPlanes[4] = planeXY1;

        offset = this.center.getZ() - 0.5 * edgeLen;
        Plane planeXY2 = new Plane(normalZ, offset, this.materialIndex);
        boxPlanes[5] = planeXY2;

    }

    @Override
    public double intersection(Ray ray) {
        findBoxPlanes();
        for (Plane plane: boxPlanes) {

        }
        return 0;
    }
}
//
//    public static AbstractMap<Hit, Plane> FindPlaneHitsForBox(Ray ray, List<Plane> planes, int index) {
//        AbstractMap<Hit, Plane> res = new HashMap<>();
//        for(Plane pln: planes){
//            double a = Vector.DotProduct(pln.Normal, ray.Direction);
//            if(Math.abs(a)< 0.001){continue;}//the ray is unified or parallel to the plane
//            Vector temp = Vector.ScalarMultiply(pln.Normal, pln.Offset);
//            temp = Vector.VectorSubtraction(temp, ray.Origin);
//            double t = (Vector.DotProduct(pln.Normal, temp)) / a;
//            if(t<0){continue;}
//            Vector hitPoint = ray.tPointOnRay(t);
//            Hit hit = new Hit(hitPoint, Shapes.Plane, index);
//            res.put(hit, pln);
//        }
//        return res;
//    }
//
//    public static Hit FindClosest(List<Hit> hits, Vector origin){
//        double dist = Double.MAX_VALUE;
//        Hit res = null;
//        for (Hit hit : hits) {
//            if (hit.HitPoint == null)
//                continue;
//            double d = Vector.Distance(hit.HitPoint, origin);
//            if (d < dist) {
//                dist = d;
//                res = hit;
//            }
//        }
//        return res;
//    }
