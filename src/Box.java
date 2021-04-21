package src;

import java.util.*;

public class Box implements Surfaces {
    private Vector center;
    private double edgeLen;
    private int materialIndex;
    private Plane planeYZ1;
    private Plane planeYZ2;
    private Plane planeXZ1;
    private Plane planeXZ2;
    private Plane planeXY1;
    private Plane planeXY2;
    private Plane[] boxPlanes;

    public Box(String[] params) {
        this.center = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                Double.parseDouble(params[2]));
        this.edgeLen = Double.parseDouble(params[3]);
        this.materialIndex = Integer.parseInt(params[4]);
        findBoxPlanes();
    }

    public void findBoxPlanes() {
        Vector normalX = new Vector(1, 0, 0);
        Vector normalY = new Vector(0, 1, 0);
        Vector normalZ = new Vector(0, 0, 1);
        double offset;

        offset = this.center.getX() + 0.5 * edgeLen;
        planeYZ1 = new Plane(normalX, offset, this.materialIndex);

        offset = this.center.getX() - 0.5 * edgeLen;
        planeYZ2 = new Plane(normalX, offset, this.materialIndex);

        offset = this.center.getY() + 0.5 * edgeLen;
        planeXZ1 = new Plane(normalY, offset, this.materialIndex);

        offset = this.center.getY() - 0.5 * edgeLen;
        planeXZ2 = new Plane(normalY, offset, this.materialIndex);

        offset = this.center.getZ() + 0.5 * edgeLen;
        planeXY1 = new Plane(normalZ, offset, this.materialIndex);

        offset = this.center.getZ() - 0.5 * edgeLen;
        planeXY2 = new Plane(normalZ, offset, this.materialIndex);

        boxPlanes = new Plane[]{planeYZ1, planeYZ2, planeXZ1, planeXZ2, planeXY1, planeXY2};
    }

//    @Override
//    public double intersection(Ray ray) {
//        double minT = Double.MAX_VALUE;
//        double tmpT;
//        for (Plane plane: boxPlanes) {
//            tmpT = plane.intersection(ray);
//            if (tmpT < minT && tmpT > 0) {
//                if ()
//                minT = tmpT;
//            }
//        }




    @Override
    public double intersection(Ray ray) {
        double maxT = Math.abs(Double.MIN_VALUE);
        double minT = Double.MAX_VALUE;
        Plane entryX, leaveX, entryY, leaveY, entryZ, leaveZ;

        if (ray.getDirection().getX() >= 0) {
            entryX = planeYZ2;
            leaveX = planeYZ1;
        } else {
            entryX =  planeYZ1;
            leaveX = planeYZ2;
        }
        double tEntryX = entryX.intersection(ray);
        if (tEntryX > maxT) {
            maxT = tEntryX;
        }
        double tLeaveX = leaveX.intersection(ray);
        if (tLeaveX < minT) {
            minT = tLeaveX;
        }

        if (ray.getDirection().getY() >= 0) {
            entryY = planeXZ2;
            leaveY = planeXZ1;
        } else {
            entryY = planeXZ1;
            leaveY = planeXZ2;
        }
        double tEntryY = entryY.intersection(ray);
        if (tEntryY > maxT) {
            maxT = tEntryY;
        }
        double tLeaveY = leaveY.intersection(ray);
        if (tLeaveY < minT) {
            minT = tLeaveY;
        }

        if (ray.getDirection().getZ() >= 0) {
            entryZ = planeXY2;
            leaveZ = planeXY1;
        } else {
            entryZ = planeXY1;
            leaveZ = planeXY2;
        }
        double tEntryZ = entryZ.intersection(ray);
        if (tEntryZ > maxT) {
            maxT = tEntryZ;
        }
        double tLeaveZ = leaveZ.intersection(ray);
        if (tLeaveZ < minT) {
            minT = tLeaveZ;
        }

        if (maxT == Math.abs(Double.MIN_VALUE)) {
            return -1;
        }
        if (minT == Double.MAX_VALUE) {
            return -1;
        }
        if (maxT < minT) {
            return maxT;
        }
        return -1;
    }

    @Override
    public Vector calcSurfaceNormal(Vector intersectionPoint, Ray ray) {
        double minT = Double.MAX_VALUE;
        double tmpT;
        Plane minPlane = new Plane();
        for (Plane plane: boxPlanes) {
            tmpT = plane.intersection(ray);
            if (tmpT < minT && tmpT > 0) {
                minT = tmpT;
                minPlane = plane;
            }
        }
        return minPlane.getNormal();
    }

    @Override
    public int getMaterialIndex() {
        return materialIndex;
    }
}

