package src.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

/**
 * part of the decorator design - used for polymorphism with all kinds of collision strategies
 */
public interface CollisionStrategy {
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter);
}
