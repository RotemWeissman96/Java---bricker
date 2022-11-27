package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.ExtraPaddle;

/**
 * a strategy to activate when a brick was hit. an extra paddle in the middle of the screen appears
 */
public class ExtraPaddleStrategy extends CollisionStrategy{

    private static final int COLLISIONS_TILL_DELETE = 3;
    private static Counter collisionCounter;
    private static ExtraPaddle paddle;
    private final Vector2 setCenter;

    /**
     *
     * @param gameObjects all the objects in the game,
     * @param extraStrategy if there are any more strategies for the current brick
     * @param windowDimension the window dimension
     * @param paddleImage  image of the mock ball
     * @param userInputListener cthe user input listener
     */
    public ExtraPaddleStrategy(GameObjectCollection gameObjects, CollisionStrategy extraStrategy,
                               Vector2 windowDimension, Vector2 dimension, Renderable paddleImage,
                               UserInputListener userInputListener) {
        super(gameObjects, extraStrategy);
        int paddleX = (int)Math.floor(windowDimension.x()/2 - dimension.x()/2);
        int paddleY = (int)Math.floor(windowDimension.y()/2 - dimension.y()/2);
        this.setCenter = new Vector2(paddleX, paddleY);
        collisionCounter = new Counter(COLLISIONS_TILL_DELETE);
        paddle = new ExtraPaddle(new Vector2(paddleX, paddleY), dimension, paddleImage,
                userInputListener, windowDimension, 0, collisionCounter, gameObjects);
    }

    /**
     * counts the collision the paddle is part of in order to delete in time
     * @param collidedObj the brick
     * @param colliderObj a ball
     * @param bricksCounter counts the number of bricks in the game
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        if (collisionCounter.value() >= COLLISIONS_TILL_DELETE) {
            collisionCounter.reset();
            paddle.setCenter(setCenter);
            gameObjects.addGameObject(paddle);
        }
    }
}
