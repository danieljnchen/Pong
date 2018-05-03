import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    public static final double width = 1000;
    public static final double height = 800;
    public static final double yBound = 20;
    public static final double xBound = 20;
    public static final double xOutBound = 10;
    public static Paddle leftPaddle;
    public static Paddle rightPaddle;
    private static long lastIteration;
    private static int countdown = 3;
    private static final int speed = 10;
    private static long countdownTime;
    private static double frameRate = 50; // frames per second
    private static int[] score = new int[2];
    private Text scoreText;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    public enum States {
        STARTUP,
        START,
        STARTING,
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
        leftPaddle = new Paddle(false);
        rightPaddle = new Paddle(true);
        Ball ball = new Ball();

        updateScore(0,0);
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
            if(state == States.STARTUP) {
                state = States.STARTING;
            }
        });
        root.getChildren().add(startButton);

        Button restartButton = new Button("Restart");
        restartButton.setLayoutX(60);
        restartButton.setLayoutY(0);
        restartButton.setOnAction(value -> {
            if(state == States.END) {
                state = States.STARTUP;
            }
        });
        root.getChildren().add(restartButton);

        Button newPointButton = new Button("New Point");
        newPointButton.setLayoutX(135);
        newPointButton.setLayoutY(0);
        newPointButton.setOnAction(value -> {
            if(state == States.END) {
                state = States.STARTING;
            }
        });
        root.getChildren().add(newPointButton);
        root.addEventHandler(KeyEvent.KEY_PRESSED, KeyEvent->{
             switch(KeyEvent.getCode()) {
                 case UP:
                     upPressed = true;
                     break;
                 case DOWN:
                     downPressed = true;
                     break;
                 case S:
                     sPressed = true;
                     break;
                 case W:
                     wPressed = true;
                     break;
             }
        });
        root.addEventHandler(KeyEvent.KEY_RELEASED, KeyEvent->{
            switch(KeyEvent.getCode()) {
                case UP:
                    upPressed = false;
                    break;
                case DOWN:
                    downPressed = false;
                    break;
                case S:
                    sPressed = false;
                    break;
                case W:
                    wPressed = false;
                    break;
            }
        });

        lastIteration = System.currentTimeMillis();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (System.currentTimeMillis() > lastIteration + 1000 / frameRate) {
                    lastIteration = System.currentTimeMillis();
                    switch(state) {
                        case STARTUP:
                            updateScore(0,0);
                            draw(gc);
                            message.setText("Welcome");
                            break;
                        case STARTING:
                            reset();
                            countdown = 3;
                            countdownTime = System.currentTimeMillis();
                            state = States.START;
                            break;
                        case START:
                            if(countdown > 0) {
                                message.setText("Starting in " + countdown);
                                if(System.currentTimeMillis()-countdownTime>1000) {
                                    --countdown;
                                    countdownTime = System.currentTimeMillis();
                                }
                            } else {
                                /*int side = (Math.random()>.5)?-1:1;
                                System.out.println("Side: " + side);
                                double angle = 2*Math.PI/4*(1/2 - Math.random());
                                System.out.println("Angle:" + angle);
                                ball.addVelocity(new Point2D(speed*side*Math.cos(angle),speed*Math.sin(angle)));*/
                                ball.addVelocity(new Point2D(5,0));
                                state = States.PLAYING;
                            }
                            break;
                        case PLAYING:
                            message.setText("");
                            if(wPressed) {
                                leftPaddle.move(false);
                            }
                            if(sPressed) {
                                leftPaddle.move(true);
                            }
                            if(upPressed) {
                                rightPaddle.move(false);
                            }
                            if(downPressed) {
                                rightPaddle.move(true);
                            }
                            draw(gc);
                            if(ball.getOutOfBounds() > -1) {
                                score[ball.getOutOfBounds()] += 1;
                                updateScore();
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
    public void updateScore() {
        if(scoreText == null) {
            scoreText = new Text();
        }
        scoreText.setText(score[0] + " : " + score[1]);
    }
    public void updateScore(int left, int right) {
        score[0] = left;
        score[1] = right;
        updateScore();
    }
    public void reset() {
        for(UIObject o : UIObject.uiObjects) {
            o.reset();
        }
    }
    public void draw(GraphicsContext gc) {
        gc.clearRect(0,0,width,height);
        for (UIObject o : UIObject.uiObjects) {
            o.draw(gc);
        }
    }
}
