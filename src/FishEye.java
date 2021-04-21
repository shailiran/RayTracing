package src;

public class FishEye {
    public static Vector calcFishEyeRayDirection(Vector currentPixel, Vector center, Scene scene) {
        double distanceCurrToCenter = currentPixel.distanceBetweenVectors(center);
        double theta = calcTheta(scene, distanceCurrToCenter);
        if (theta > 90) {
            return null;
        }
        double radiusXip = scene.getCamera().getScreenDistance() * Math.tan(Math.toRadians(theta));
        Vector xifDirection = currentPixel.subVectors(center);
        xifDirection.normalizeInPlace();
        Vector xip = center.addVectors(xifDirection.multByScalar(radiusXip));
        Vector fishEyeDirection = xip.subVectors(scene.getCamera().getPosition());
        fishEyeDirection.normalizeInPlace();
        return fishEyeDirection;
    }

    public static Double calcTheta(Scene scene, double R) {
        double k = scene.getCamera().getK();
        double f = scene.getCamera().getScreenDistance();
        double theta;
        if (k > 0 && k <= 1) {
            theta = Math.toDegrees(Math.atan(R * k / f) / k);
        } else if (k == 0) {
            theta = R / f;
        } else {
            theta = Math.toDegrees(Math.asin(R * k / f) / k);
        }
        return theta;
    }


}
