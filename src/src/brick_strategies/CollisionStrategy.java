package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * responsible for the strategies to activate when a brick is hit
 */
public class CollisionStrategy {

    protected final GameObjectCollection gameObjects;
    private final CollisionStrategy extraStrategy;

    /**
     *
     * @param gameObjects all the game objects
     */
    public CollisionStrategy(GameObjectCollection gameObjects) {
        this.extraStrategy = null;
        this.gameObjects = gameObjects;
    }

    /**
     *
     * @param gameObjects all the game objects
     * @param extraStrategy if there is another strategy
     */
    public CollisionStrategy(GameObjectCollection gameObjects, CollisionStrategy extraStrategy) {
        this.extraStrategy = extraStrategy;
        this.gameObjects = gameObjects;
    }

    /**
     * changes the camera, and resets the balls collides counter
     * @param collidedObj the brick
     * @param colliderObj a ball
     * @param bricksCounter counts the number of bricks in thee game
     */
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        if (extraStrategy != null) {
           extraStrategy.onCollision(collidedObj, colliderObj, bricksCounter);
        }
        else {
            bricksCounter.decrement();
            gameObjects.removeGameObject(collidedObj);
        }
    }

}
