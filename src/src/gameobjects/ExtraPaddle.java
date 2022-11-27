package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * this class implement the paddle that appears at the middle of the screen when extra paddle strategy
 * is invoked
 */
public class ExtraPaddle extends Paddle{
    private final Counter collisionCounter;
    private static final int COLLISIONS_TILL_DELETE = 3;
    private final GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    The user input is received
     * @param windowDimensions the window dimensions for the position of the paddle
     * @param minDistFromEdge idk
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge,
                        GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);
        this.collisionCounter = new Counter(COLLISIONS_TILL_DELETE);
        this.gameObjects = gameObjects;
    }

    /**
     * count the number of collision in order to remove the paddle after 3 times
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball) {
            collisionCounter.increment();
            if (collisionCounter.value() >= 3) {
                gameObjects.removeGameObject(this);
            }
        }
    }

    public int getCollisionCount() {
        return collisionCounter.value();
    }

    public void collisionCounterReset(){
        collisionCounter.reset();
    }
}
