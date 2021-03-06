package src;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private Set set;
    private List<Surfaces> surfaces;
    private List<Surfaces> transparencySurfaces;
    private List<Materials> materials;
    private List<Light> lights;

    public Scene(Camera camera, Set set, List<Surfaces> surfaces, List<Materials> materials,
                 List<Light> lights) {
        this.camera = camera;
        this.set = set;
        this.surfaces = surfaces;
        this.transparencySurfaces = List.copyOf(surfaces);
        this.materials = materials;
        this.lights = lights;
    }

    public List<Surfaces> getSurfaces() {
        return this.surfaces;
    }

    public List<Surfaces> getTransparencySurfaces() {
        return this.transparencySurfaces;
    }
    
    public boolean isValid() {
        if (camera != null && set != null && surfaces != null
                && materials != null && lights != null)
            return true;
        return false;
    }

    public Set getSet() {
        return set;
    }

    public Camera getCamera() {
        return camera;
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Materials> getMaterials() {
        return materials;
    }
}
