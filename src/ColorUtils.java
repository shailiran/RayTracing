package src;

import java.util.List;

public class ColorUtils {

    public static Color calcColor (Intersection intersection, Ray ray, Scene scene) {
        Color color;
        if (scene.getSet().getMaxRecursionLevel() == 0) {
            color = scene.getSet().getBackgroundColor();
            return color;
        }
        Vector intersectionPoint = ray.getBase().addVectors(ray.getDirection()).multByScalar(intersection.getMinT());
        Vector N = intersection.getMinSurface().calcSurfaceNormal(intersectionPoint);
        //checking if the angle between the N and the ray is smaller than 90
        //and if so change the direction of N.
        if (N.dotProduct(ray.getDirection()) > 0) { //TODO: check if < 0 or >0
            N = N.multByScalar(-1);
        }
        N.normalizeInPlace();
        Materials material = scene.getMaterials().get(intersection.getMinSurface().getMaterialIndex());
        color = new Color(0,0,0);

        for (Light light: scene.getLights()) {
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

            //TODO:Soft Shadow


        }

        // Color transparencyColor
        // output color = (background color) * transparency + (diffuse + specular) * (1 - transparency) + (reflection color)
        double transparency = 0;

//        color.setRed(scene.getSet().getBackgroundColor().getRed() * transparency + );


        return color;
    }

}
