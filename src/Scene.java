package src;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private Set set;
    private List<Surfaces> surfaces;
    private List<Materials> materials;
    private List<Light> lights;

    public Scene(Camera camera, Set set, List<Surfaces> surfaces, List<Materials> materials,
                 List<Light> lights) {
        this.camera = camera;
        this.set = set;
        this.surfaces = surfaces;
        this.materials = materials;
        this.lights = lights;
    }

    public List<Surfaces> getSurfaces() {
        return this.surfaces;
    }
    
    public boolean isValid() {
        if (camera != null && set != null && surfaces != null
                && materials != null && lights != null)
            return true;
        return false;
    }

}
