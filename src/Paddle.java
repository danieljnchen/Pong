import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle extends UIObject {
    private static final double width = 10;
    private static final double height = 60;
    private static final double speed = 1;
    private static final double distFromSide = 30;
    private boolean side;
    private Point2D position;

    public Paddle(boolean side) {
        this.side = side;
        reset();
    }

    public void move(boolean direction) {
        position = position.add(0,speed*(direction?1:-1));
    }
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(position.getX()-width/2, position.getY()-height/2, width, height);
    }

    public void reset() {
        if(side) {
            position = new Point2D(Main.width-distFromSide, Main.height/2);
        } else {
            position = new Point2D(distFromSide, Main.height/2);
        }
    }
}
