package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.gameobjects.CameraChangeController;

/**
 * a strategy to activate when a brick was hit. changes the camera, makes it follow the ball
 */
public class ChangeCameraStrategy extends CollisionStrategy {
    private static final int MAX_COLLIDES = 4;

    private final Counter ballCollisionCounter;
    private final Ball ball;
    private final Vector2 windowDimensions;
    private final GameManager gameManager;

    /**
     *
     * @param gameObjects all the objects in the game,
     * @param extraStrategy if there are any more strategies for the current brick
     * @param ball the main ball of the game
     * @param windowDimensions the window dimension
     * @param gameManager  used to change the camera
     * @param ballCollisionCounter counts the balls collisions
     */
    public ChangeCameraStrategy(GameObjectCollection gameObjects, CollisionStrategy extraStrategy,
                                Ball ball, Vector2 windowDimensions, GameManager gameManager,
                                Counter ballCollisionCounter) {
        super(gameObjects, extraStrategy);
        this.ball = ball;
        this.windowDimensions = windowDimensions;
        this.gameManager = gameManager;
        this.ballCollisionCounter = ballCollisionCounter;
        CameraChangeController cameraChangeObject = new CameraChangeController(gameManager, ballCollisionCounter);
        gameObjects.addGameObject(cameraChangeObject, Layer.UI);
    }

    /**
     * changes the camera, and resets the balls collides counter
     * @param collidedObj the brick
     * @param colliderObj a ball
     * @param bricksCounter counts the number of bricks in thee game
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);

        if (colliderObj.equals(ball) && ballCollisionCounter.value() >= MAX_COLLIDES) {
            gameManager.setCamera(new Camera(ball, Vector2.ZERO, windowDimensions.mult(1.2F),
                                    windowDimensions));
            ballCollisionCounter.reset();
        }
    }
}
