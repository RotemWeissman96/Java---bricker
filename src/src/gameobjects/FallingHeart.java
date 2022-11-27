package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import static src.BrickerGameManager.PADDLE_FLOAT;
import static src.BrickerGameManager.PADDLE_HEIGHT;

/**
 * the class is responsible for the hearts that fall in the extra life strategy
 */
public class FallingHeart extends GameObject {

    private final Vector2 windowDimension;
    private final GameObjectCollection gameObjects;
    private final Counter hearCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        Vector2 windowDimension, GameObjectCollection gameObjects, Counter hearCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.windowDimension = windowDimension;
        this.gameObjects = gameObjects;
        this.hearCounter = hearCounter;
    }

    /**
     * let the heart fall downwards
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() >= windowDimension.y()) {
           gameObjects.removeGameObject(this);
        }
    }

    /**
     * add life when colide with paddle
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjects.removeGameObject(this);
        if (hearCounter.value() < 4) {
            hearCounter.increment();
        }
    }

    /**
     * collide only with the main paddle
     * @param other The other GameObject.
     * @return if the hearts should collide
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return (other instanceof Paddle) && (other.getCenter().y() ==
                windowDimension.y() - PADDLE_HEIGHT - PADDLE_FLOAT);
    }
}
