import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    public static final double width = 1000;
    public static final double height = 800;
    public static final double yBound = 20;
    public static final double xBound = 20;
    private static long lastIteration;
    private static int countdown = 3;
    private static long countdownTime;
    private static double frameRate = 50; // frames per second

    public enum States {
        STARTUP,
        START,
        PLAYING,
        END
    }

    public States state = States.STARTUP;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pong");
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        Paddle leftPaddle = new Paddle(false);
        Paddle rightPaddle = new Paddle(true);
        Ball ball = new Ball();

        int[] score = {0,0};
        Text scoreText = new Text(score[0] + " : " + score[1]);
        scoreText.setLayoutX(width/2);
        scoreText.setLayoutY(30);
        scoreText.setFont(Font.font("Arial",30));
        root.getChildren().add(scoreText);

        Text message = new Text();
        message.setLayoutX(width/2);
        message.setLayoutY(50);
        message.setFont(Font.font("Arial", 20));
        root.getChildren().add(message);

        Button startButton = new Button("Start");
        startButton.setLayoutX(0);
        startButton.setLayoutY(0);
        startButton.setOnAction(value -> {
            if(state==States.STARTUP) {
                state = States.START;
            }
        });
        root.getChildren().add(startButton);

        Button restartButton = new Button("restart");
        restartButton.setLayoutX(60);
        restartButton.setLayoutY(0);
        restartButton.setOnAction(value -> {
            if(state==States.END) {
                state = States.STARTUP;
            }
        });
        root.getChildren().add(restartButton);

        lastIteration = System.currentTimeMillis();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (System.currentTimeMillis() > lastIteration + 1000 / frameRate) {
                    lastIteration = System.currentTimeMillis();
                    switch(state) {
                        case STARTUP:
                            leftPaddle.reset();
                            rightPaddle.reset();
                            ball.reset();
                            draw(gc);
                            message.setText("Welcome");
                            countdown = 3;
                            countdownTime = System.currentTimeMillis();
                            break;
                        case START:
                            if(countdown > 0) {
                                message.setText("Starting in " + countdown);
                                if(System.currentTimeMillis()-countdownTime>1000) {
                                    --countdown;
                                    countdownTime = System.currentTimeMillis();
                                }
                            } else {
                                ball.addVelocity(new Point2D(6, 6));
                                state = States.PLAYING;
                            }
                            break;
                        case PLAYING:
                            message.setText("");
                            draw(gc);
                            if(ball.getOutOfBounds() != 0) {
                                state = States.END;
                            }
                            break;
                        case END:
                            message.setText("Game over");
                            break;
                    }
                }
            }
        }.start();
    }
    public void draw(GraphicsContext gc) {
        gc.clearRect(0,0,width,height);
        for (UIObject o : UIObject.uiObjects) {
            o.draw(gc);
        }
    }
}
