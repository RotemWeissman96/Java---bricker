package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * a basic strategy that removes the brick after being hit
 */
public class RegularCollisionStrategy implements CollisionStrategy {

    protected final GameObjectCollection gameObjects;

    /**
     *
     * @param gameObjects all the game objects
     */
    public RegularCollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }


    /**
     * changes the camera, and resets the balls collides counter
     * @param collidedObj the brick
     * @param colliderObj a ball
     * @param bricksCounter counts the number of bricks in the game
     */
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        bricksCounter.decrement();
        gameObjects.removeGameObject(collidedObj);
    }

}
