package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * a game object representing a brick
 */
public class Brick extends GameObject {
    private final CollisionStrategy collisionStrategy;
    private final Counter brickCounter; // counts how many bricks are currently alive
    private boolean alive; // saves if this brick is alive

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.brickCounter = counter;
        this.alive = true;
        counter.increment();
    }

    /**
     *  upon collision act according to the brick strategy
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (!this.alive){ // avoid 2 collides at the same brick in close time skip
            return;
        }
        alive = false;
        collisionStrategy.onCollision(this, other, brickCounter);
    }
}
