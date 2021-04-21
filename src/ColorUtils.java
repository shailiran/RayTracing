package src;

import java.util.List;
import java.util.Random;

public class ColorUtils {
    static final double EPSILON = 0.001;
    public static Color calcColor(Intersection intersection, Ray ray, Scene scene, int recLevel) {
        Color color = new Color();
        if (recLevel == 0) {
            color = scene.getSet().getBackgroundColor();
            return color;
        }
        //P = p_0 + tv
        Vector intersectionPoint = ray.getBase().addVectors((ray.getDirection()).multByScalar(intersection.getMinT()));
        Vector N = intersection.getMinSurface().calcSurfaceNormal(intersectionPoint, ray);

        //checking if the angle between the N and the ray is greater than 90
        //and if so change the direction of N.
        if (N.dotProduct(ray.getDirection()) > 0) {
            N = N.multByScalar(-1);
        }
        Materials material = scene.getMaterials().get(intersection.getMinSurface().getMaterialIndex() - 1);
        Color colorWithShadow = new Color(0, 0, 0);
        for (Light light: scene.getLights()) {
            color.setToZero();
            Vector L = light.getPosition().subVectors(intersectionPoint);
            L.normalizeInPlace();
            // ####### Diffuse reflection #######
            // I_diff = K_d * I_p * cos(Theta) = K_d * I_p * (NL)
            double cosTheta = N.dotProduct(L);
            if (cosTheta < 0) {
              continue;
            }
            color.setRed(color.getRed() + (material.getDiffuseColor().getRed() * light.getColor().getRed() * cosTheta));
            color.setGreen(color.getGreen() + (material.getDiffuseColor().getGreen() * light.getColor().getGreen() * cosTheta));
            color.setBlue(color.getBlue() + (material.getDiffuseColor().getBlue() * light.getColor().getBlue() * cosTheta));

            // ####### Ambient - Bonus - TODO #######

            // ####### Specular light #######
            // R = (2LN)N - L
            Vector R = (N.multByScalar(N.dotProduct(L) * 2)).subVectors(L);
            // I_spec = K_s*I_p*(RV)^n
            Vector V = ray.getDirection().multByScalar(-1);
            double cosBeta = Math.pow(R.dotProduct(V), material.getPhongSpecularityCoefficient());
            color.setRed(color.getRed() + (material.getSpecularColor().getRed() * light.getColor().getRed() *
                    cosBeta * light.getSpecularIntensity()));
            color.setGreen(color.getGreen() + (material.getSpecularColor().getGreen() * light.getColor().getGreen() *
                    cosBeta * light.getSpecularIntensity()));
            color.setBlue(color.getBlue() + (material.getSpecularColor().getBlue() * light.getColor().getBlue() *
                    cosBeta * light.getSpecularIntensity()));

            // Soft Shadow
            // The light_intesity only affects the diffuse and specular lighting
            // ligh_intesity = (1 - shadow_intensity) * 1 + shadow_intensity * (%of rays
            // that hit the points from the light source)

            double shadowIntensity = softShadow(intersectionPoint, light, L.multByScalar(-1), scene);
            colorWithShadow.setRed(colorWithShadow.getRed() + color.getRed() * ((1 - light.getShadowIntensity())
                    + light.getShadowIntensity() * shadowIntensity));
            colorWithShadow.setGreen(colorWithShadow.getGreen() + color.getGreen() * ((1 - light.getShadowIntensity())
                    + light.getShadowIntensity() * shadowIntensity));
            colorWithShadow.setBlue(colorWithShadow.getBlue() + color.getBlue() * ((1 - light.getShadowIntensity())
                    + light.getShadowIntensity() * shadowIntensity));
        }

        // Color transparencyColor
        Color transparencyColor = new Color(0,0,0);
        if (material.getTransparency() != 0) {
            transparencyColor = transparencyColor(ray, intersectionPoint, scene, recLevel);
        }

        // Reflection
        Color reflectionColor = new Color(0, 0, 0);
        Color matReflection = material.getReflectionColor();
        if (matReflection.getRed() != 0 || matReflection.getGreen() != 0 || matReflection.getBlue() != 0) {
            reflectionColor = reflectionColor(ray, intersectionPoint, N, scene, material, recLevel);
        }

        // output color = (background color) * transparency + (diffuse + specular) * (1 - transparency) + (reflection color)
//        double transparency = 0;
        Color res = new Color(0, 0, 0);
        res.setRed(colorWithShadow.getRed() * (1 - material.getTransparency()) +
                transparencyColor.getRed() * material.getTransparency() + reflectionColor.getRed());
        res.setGreen(colorWithShadow.getGreen() * (1 - material.getTransparency()) +
                transparencyColor.getGreen() * material.getTransparency() + reflectionColor.getGreen());
        res.setBlue(colorWithShadow.getBlue() * (1 - material.getTransparency()) +
                transparencyColor.getBlue() * material.getTransparency() + reflectionColor.getBlue());
        res = updateColor(res);
        return res;
    }

    private static Color reflectionColor(Ray ray, Vector intersectionPoint, Vector N, Scene scene, Materials material,
                                         int recLevel) {
        Vector V = ray.getDirection();

        // R = V - 2 * (VN)N
        Vector R = V.addVectors(N.multByScalar(-2 * V.dotProduct(N)));
        R.normalizeInPlace();
        Vector base = intersectionPoint.addVectors(R.multByScalar(EPSILON));
        Ray rayReflection = new Ray(base, R);
        Intersection intersection = Intersection.findIntersection(rayReflection, scene, false);
        Color reflectionColor = new Color(0, 0, 0);

        if (intersection.getMinT() == Double.MAX_VALUE) {
            Color backgroundColor = scene.getSet().getBackgroundColor();
            reflectionColor.setRed(backgroundColor.getRed() * material.getReflectionColor().getRed());
            reflectionColor.setGreen(backgroundColor.getGreen() * material.getReflectionColor().getGreen());
            reflectionColor.setBlue(backgroundColor.getBlue() * material.getReflectionColor().getBlue());
        } else {
            Color tmp = calcColor(intersection, rayReflection, scene, recLevel - 1);
            reflectionColor.setRed(tmp.getRed() * material.getReflectionColor().getRed());
            reflectionColor.setGreen(tmp.getGreen() * material.getReflectionColor().getGreen());
            reflectionColor.setBlue(tmp.getBlue() * material.getReflectionColor().getBlue());
        }
        Color updated = updateColor(reflectionColor);
        return updated;
    }

    public static Color transparencyColor(Ray ray, Vector intersectionPoint, Scene scene,int recLevel) {
        Vector direction = ray.getDirection();
        Vector base = intersectionPoint.addVectors(direction.multByScalar(EPSILON));
        Ray transparencyRay = new Ray(base, direction);
        Intersection transparencyIntersection = Intersection.findIntersection(transparencyRay, scene,true);
        Color background = scene.getSet().getBackgroundColor();
        Color res = new Color(background.getRed(), background.getGreen(), background.getBlue());
        if (transparencyIntersection.getMinT() != Double.MAX_VALUE) {
            Color color = calcColor(transparencyIntersection, transparencyRay, scene, recLevel -1);
            res.setRed(color.getRed());
            res.setGreen(color.getGreen());
            res.setBlue(color.getBlue());
        }
        Color updated = updateColor(res);
        return updated;
    }

    private static Color updateColor(Color color) {
       Color res = new Color(0,0,0);
       if (color.getRed() >= 0) {
           res.setRed(Math.min(1, color.getRed()));
       } else {
           res.setRed(0);
       }
       if (color.getGreen() >= 0) {
           res.setGreen(Math.min(1, color.getGreen()));
       } else {
           res.setGreen(0);
       }
       if (color.getBlue() >= 0) {
           res.setBlue(Math.min(1, color.getBlue()));
       } else {
            res.setBlue(0);
       }
       return res;
    }

    private static double softShadow(Vector intersectionPoint,
                                    Light light, Vector planeNormal, Scene scene) {

//      1. Find a plane which is perpendicular to the ray.
        double offset = planeNormal.dotProduct(light.getPosition());
        Plane lightPlane = new Plane(planeNormal, offset);

//      2. Define a rectangle on that plane, centered at the light source and as wide as the
//         defined light radius.
        Vector tmp = lightPlane.calcVectorOnThePlane();
        Vector v = tmp.addVectors(light.getPosition().multByScalar(-1));
        //Vector v = planeNormal.crossProduct(tmp).normalizeVector();
        v.normalizeInPlace();
        Vector u = planeNormal.crossProduct(v).normalizeVector();
        Vector topLeft = light.getPosition().addVectors(v.multByScalar(-0.5 * light.getLightRadius()))
                .addVectors(u.multByScalar(-0.5 * light.getLightRadius()));

//        3. Divide the rectangle into a grid of N*N cells, where N is the number of shadow
//        rays from the scene parameters.
        double delta = 1.0 / scene.getSet().getNumberOfShadowsRays();
        Vector rectangleV = (topLeft.addVectors(v.multByScalar(light.getLightRadius())))
                .subVectors(topLeft);
        Vector rectangleU = (topLeft.addVectors(u.multByScalar(light.getLightRadius())))
                .subVectors(topLeft);
        Vector delta_v = rectangleV.multByScalar(delta);
        Vector delta_u = rectangleU.multByScalar(delta);

//        4. If we shoot a ray from the center of each cell, we will get banding artifacts (you
//        can test it and see for yourself). To avoid banding we select a random point in each
//        cell (by uniformly sampling x value and y value using rnd.nextDouble() function), and
//        shoot the ray from the selected random point to the light.
//        5. Aggregate the values of all rays that were cast and count how many of them hit
//        the required point on the surface.
        double sum = 0;
        for (int i = 0; i < scene.getSet().getNumberOfShadowsRays(); i++) {
            for (int j = 0; j < scene.getSet().getNumberOfShadowsRays(); j++){
                sum += pointToLightRay(scene, topLeft, delta_v, delta_u, i, j, intersectionPoint);
            }
        }
        double denominator = Math.pow(scene.getSet().getNumberOfShadowsRays(), 2);
        return sum / denominator;
    }

    private static int pointToLightRay (Scene scene, Vector topLeft, Vector delta_v,
                                           Vector delta_u, int i, int j, Vector intersectionPoint) {
        Random random = new Random();
        double x = random.nextDouble();
        double y = random.nextDouble();
        Vector p = topLeft.addVectors(delta_v.multByScalar(i + x).addVectors(delta_u.multByScalar(j + y)));
        Vector direction = p.subVectors(intersectionPoint);
        double directionNorm = direction.getNorm();
        direction.normalizeInPlace();
        Vector rayBase = intersectionPoint.addVectors(direction.multByScalar(0.001));
        Ray ray = new Ray(rayBase, direction);
        boolean result = Intersection.checkIntersection(ray, scene, directionNorm);

        if (result) {
            return 0;
        }
        return 1;
    }


}
