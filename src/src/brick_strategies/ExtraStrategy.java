package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * decorator - abstract class for the special strategies (not including the remove brick)
 */
public abstract class ExtraStrategy implements CollisionStrategy{
    private final CollisionStrategy extraStrategy;
    protected final GameObjectCollection gameObjects;

    public ExtraStrategy(GameObjectCollection gameObjects, CollisionStrategy extraStrategy){
        this.gameObjects = gameObjects;
        this.extraStrategy = extraStrategy;
    }
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        extraStrategy.onCollision(collidedObj, colliderObj, bricksCounter);
    }
}
