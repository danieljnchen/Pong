import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends UIObject {
    private static final double side = 10;
    private Point2D position;
    private Point2D velocity;

    public Ball() {
        reset();
    }

    public void addVelocity(Point2D delta) {
        velocity = velocity.add(delta);
    }
    private void update() {
        if(position.getY()>Main.height-Main.yBound || position.getY()<Main.yBound) {
            velocity = velocity.subtract(0,2*velocity.getY());
        }
        position = position.add(velocity);
    }
    public int getOutOfBounds() {
        if(position.getX()<Main.xBound) {
            return -1;
        } else if(position.getX()>Main.width-Main.xBound) {
            return 1;
        } else {
            return 0;
        }
    }
    public void reset() {
        position = new Point2D(Main.width/2, Main.height/2);
        velocity = new Point2D(0,0);
    }

    public void draw(GraphicsContext gc) {
        update();
        gc.setFill(Color.ORANGE);
        gc.fillRect(position.getX(), position.getY(), side, side);
    }
}
