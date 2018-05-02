import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public abstract class UIObject {
    public static ArrayList<UIObject> uiObjects = new ArrayList<>();
    public UIObject() {
        uiObjects.add(this);
    }
    public abstract void draw(GraphicsContext gc);
    public abstract void reset();
}
