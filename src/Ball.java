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
            System.out.println("Hit horizontal bound");
            velocity = velocity.subtract(0,2*velocity.getY());
        } else if((position.getX()<Main.xBound && Math.abs(Main.leftPaddle.getYPos()-position.getY())<20) || (position.getX()>Main.width-Main.xBound && Math.abs(Main.rightPaddle.getYPos()-position.getY())<20)) {
            System.out.println("Hit vertical bound");
            velocity = velocity.subtract(2*velocity.getX(),0);
        }
        position = position.add(velocity);
    }
    public int getOutOfBounds() {
        if(position.getX()<Main.xOutBound) {
            return 1;
        } else if(position.getX()>Main.width-Main.xOutBound) {
            return 0;
        } else {
            return -1;
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
