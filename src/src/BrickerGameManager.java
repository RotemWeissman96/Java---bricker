package src;

import src.brick_strategies.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;

import java.awt.*;
import java.util.Random;

import static java.lang.Math.floor;

/**
 * the main game class, the rules and the game object are defined here
 */
public class BrickerGameManager extends GameManager {
    // border dimensions
    private static final int BORDER_WIDTH = 50;
    private static final Renderable BORDER_RENDERABLE = null;
           // new RectangleRenderable(new Color(80, 140, 250));
    // paddle dimension
    public static final int PADDLE_HEIGHT = 20;
    private static final int PADDLE_WIDTH = 100;
    public static final int PADDLE_FLOAT = 10;
    private static final int BALL_RADIUS = 30;
    public static final float BALL_SPEED = 200;
    // BRICK AND BRICKS dimensions
    private static final int SIDE_BUFFER = 15; // first and last column relative to the side wall
    private static final int TOP_BUFFER = 40;  // first row relative to the ceiling
    private static final int BRICK_COL = 7;  // number of bricks columns
    private static final int BRICK_ROW = 8; // number of bricks rows
    private static final float BRICKS_PORTION = 0.3F; // how much screen the bricks should occupy vertically

    //  lives display and init
    private static final int INITIAL_LIVES = 3;
    private static final float HEART_WIDTH_RATIO = 0.05F;
    private static final float HEART_HEIGHT_RATIO = 0.05F;
    private static final int MAX_LIVES = 8;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;


    private enum ExtraStrategies {BALLS, PADDLE, CAMERA, LIFE, DOUBLE, REGULAR;}
    private Counter ballCollidesCounter;
    private Counter brickCounter;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter livesCounter;



    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.ball = null;
        this.brickCounter = null;

    }

    /**
     * init all the necessary game objects to start a game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.brickCounter = new Counter(0);
        this.livesCounter = new Counter(INITIAL_LIVES);
        this.ballCollidesCounter = new Counter(4);
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.ball = createBall();
        createPaddle();
        createBorders();
        createBricks();
        createBackground();
        createGraphicLifeCounter();
        createNumericLifeCounter();
    }

    /**
      * creates a numeric life counter and adds to game objects
      */
    private void createNumericLifeCounter() {
        int heartX = (int)floor((windowDimensions.x() * HEART_HEIGHT_RATIO + 1) * (INITIAL_LIVES + 1));
        int heartY = (int)floor(windowDimensions.y() - windowDimensions.y()*(HEART_WIDTH_RATIO));
        Vector2 topLeftCorner = new Vector2(heartX, heartY);
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(livesCounter, topLeftCorner,
                new Vector2(HEART_WIDTH_RATIO * windowDimensions.x(), HEART_HEIGHT_RATIO * windowDimensions.y()),
                gameObjects());
        gameObjects().addGameObject(numericLifeCounter, Layer.UI);

    }

    /**
     * create the graphic life counter and adds it to the game objects
     */
    private void createGraphicLifeCounter() {
        Renderable hearImage = imageReader.readImage("assets/heart.png", true);
        int heartX = (int)floor(windowDimensions.x()*HEART_WIDTH_RATIO);
        int heartY = (int)floor(windowDimensions.y()*HEART_HEIGHT_RATIO);
        Vector2 topLeft = new Vector2(0,windowDimensions.y() - heartY);

        GraphicLifeCounter hearts = new GraphicLifeCounter(topLeft, new Vector2(heartX, heartY),
                this.livesCounter, hearImage, this.gameObjects(), INITIAL_LIVES, MAX_LIVES);
        gameObjects().addGameObject(hearts);
    }

    /**
     * create the background image and adding it to the game objects
     */
    private void createBackground() {
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * create the borders to limit the ball from moving outside the screen
     */
    private void createBorders() {
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(-BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        BORDER_RENDERABLE)
        );
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(windowDimensions.x(), 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        BORDER_RENDERABLE)
        );
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(0, -BORDER_WIDTH),
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),
                        BORDER_RENDERABLE)
        );
    }

    /**
     * create a new ball and add to the gameObjects
     * @return the ball object
     */
    private Ball createBall() {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Ball ball = new Ball(new Vector2(0,0), new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage,
                collisionSound, ballCollidesCounter);
        ball.setCenter(windowDimensions.mult(0.5F));
        this.gameObjects().addGameObject(ball);
        randomBallVelocity(ball);
        return ball;
    }

    /**
     * generate a random velocity for the ball
     * @param ball the current ball
     */
    private void randomBallVelocity(Ball ball){
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *=-1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * create and add a new paddle
     */
    private void createPaddle() {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", false);
        Paddle paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                inputListener,
                windowDimensions, 1);
        paddle.setCenter(new Vector2(windowDimensions.x()/2,
                (int)windowDimensions.y()-PADDLE_HEIGHT - PADDLE_FLOAT));
        gameObjects().addGameObject(paddle);
    }



    /**
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     *  this function gets called every frame
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    /**
     * check if the game ended and control the behavior when the ball falls
     */
    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if(ballHeight > windowDimensions.y()) {
            livesCounter.decrement();
            ball.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2));
            randomBallVelocity(this.ball);
            if (livesCounter.value() == 0) {
                prompt = "You Lose!";
            }
        }
        if (brickCounter.value() == 0) {
            prompt = "You Win!";
        }
        if(!prompt.isEmpty()) {
            prompt += " Play again?";
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    /**
     * create and add all bricks to the game
     */
    private void createBricks() {
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Vector2 dimensions = new Vector2(
                (int)floor((windowDimensions.x() - 2*SIDE_BUFFER - BRICK_COL -1) / BRICK_COL),
                (int)floor(windowDimensions.y()*BRICKS_PORTION/BRICK_ROW));
        for (int x = SIDE_BUFFER; x < windowDimensions.x() - dimensions.x(); x += dimensions.x()+1) {
            for (int y = TOP_BUFFER; y < TOP_BUFFER+BRICK_ROW*dimensions.y(); y+=dimensions.y()+1){
                CollisionStrategy collisionStrategy = createCollisionStrategy(null,
                        ExtraStrategies.values().length, 0);
                GameObject brick = new Brick(new Vector2(x, y), dimensions, brickImage, collisionStrategy,
                        brickCounter);
                gameObjects().addGameObject(brick);
            }
        }
    }

    /**
     * generate a random strategy for the current brick
     * @param strategy the current strategy, starts as null
     * @param numberOfStrategiesToChoose indicate which strategies are available this time
     * @param doubleChosen how many doubles where already choosen
     * @return the randomly constructed strategy
     */
    public CollisionStrategy createCollisionStrategy(CollisionStrategy strategy,
                                                     int numberOfStrategiesToChoose,
                                                     int doubleChosen){
        CameraChangeController cameraChangeObject = new CameraChangeController(this, ballCollidesCounter);
        gameObjects().addGameObject(cameraChangeObject, Layer.UI);
        Random rand = new Random();
        switch (ExtraStrategies.values()[rand.nextInt(numberOfStrategiesToChoose)]) {
            case BALLS:
                return createExtraBallsStrategy(strategy);
            case LIFE:
                return createExtraLifeStrategy(strategy);
            case CAMERA:
                return createChangeCameraStrategy(strategy);
            case PADDLE:
                return createExtraPaddleStrategy(strategy);
            case DOUBLE:
                doubleChosen++;
                return doubleStrategy(strategy, doubleChosen);
            case REGULAR:
                return new CollisionStrategy(this.gameObjects(), strategy);
        }

        return null;
    }

    /**
     * help create the ExtraBallsStrategy
     * @param strategy   strategy the current strategy
     * @return the strategy after assigning the ExtraBallsStrategy
     */
    private ExtraBallsStrategy createExtraBallsStrategy(CollisionStrategy strategy) {
        Vector2 dimensions = new Vector2(1, 1).mult((int)floor(
                (windowDimensions.x() - 2*SIDE_BUFFER - BRICK_COL -1) / (3*BRICK_COL)));
        Renderable mockBallImage = imageReader.readImage("assets/mockBall.png", true);
        Sound ballCollisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        return new ExtraBallsStrategy(this.gameObjects(), strategy, dimensions,
                mockBallImage, ballCollisionSound, windowDimensions);
    }

    /**
     * help create the ExtraLifeStrategy
     * @param strategy   strategy the current strategy
     * @return the strategy after assigning the ExtraLifeStrategy
     */
    private ExtraLifeStrategy createExtraLifeStrategy(CollisionStrategy strategy) {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        int heartX = (int)floor(windowDimensions.x()*HEART_WIDTH_RATIO);
        int heartY = (int)floor(windowDimensions.y()*HEART_HEIGHT_RATIO);
        Vector2 dimensions = new Vector2(heartX, heartY);
        return new ExtraLifeStrategy(gameObjects(), strategy, heartImage, dimensions, livesCounter,
                windowDimensions);
    }

    /**
     * help create the ExtraPaddleStrategy
     * @param strategy   strategy the current strategy
     * @return the strategy after assigning the ExtraPaddleStrategy
     */
    private ExtraPaddleStrategy createExtraPaddleStrategy(CollisionStrategy strategy){
        Renderable extraPaddleImage = imageReader.readImage("assets/botGood.png", false);
        return new ExtraPaddleStrategy(gameObjects(), strategy, windowDimensions,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), extraPaddleImage, inputListener);
    }

    /**
     * help create the ChangeCameraStrategy
     * @param strategy   strategy the current strategy
     * @return the strategy after assigning the ChangeCameraStrategy
     */
    private ChangeCameraStrategy createChangeCameraStrategy(CollisionStrategy strategy) {
        return new ChangeCameraStrategy(gameObjects(), strategy, ball, windowDimensions, this,
                ballCollidesCounter);
    }

    /**
     * handles the recursion calls for createCollisionStrategy when double strategies is chosen
     * @param strategy the current strategy
     * @param doubleChosen number of time double strategy was chosen for the current brick
     * @return the strategy after assigning the randomized behaviour
     */
    private CollisionStrategy doubleStrategy(CollisionStrategy strategy, int doubleChosen) {
        int numberOfStrategiesToChooseFrom = ExtraStrategies.values().length - 1;
        if (doubleChosen == 1) {
            strategy = createCollisionStrategy(strategy, numberOfStrategiesToChooseFrom, doubleChosen);
            if (strategy == null) // if double was chosen last line, don't choose double again
                return createCollisionStrategy(strategy, numberOfStrategiesToChooseFrom - 1, doubleChosen);
            else
                return createCollisionStrategy(strategy, numberOfStrategiesToChooseFrom, doubleChosen);
        }
        // if double chosen = 2 then double was chosen twice already, don't choose it again
        strategy = createCollisionStrategy(strategy, numberOfStrategiesToChooseFrom-1, doubleChosen);
        return createCollisionStrategy(strategy, numberOfStrategiesToChooseFrom-1, doubleChosen);

    }

    public static void main(String[] args) {
        new BrickerGameManager("Bouncing Ball", new Vector2(1000, 700)).run();
    }

}