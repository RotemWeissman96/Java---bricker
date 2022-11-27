package src.gameobjects;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

import static src.BrickerGameManager.BALL_SPEED;

/**
 * this class handles the mock balls from the extra balls strategy, same ass regular ball but does not
 * drain lives and when exiting the screen the mock balls get removed
 */
public class ExtraBall extends Ball{

    private final Vector2 windowDimension;
    private final GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound the sound on collision
     * @param gameObjects used to remove the mock ball from the game
     * @param windowDimension the window dimension
     */
    public ExtraBall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound
            , GameObjectCollection gameObjects, Vector2 windowDimension) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        randomBallVelocity();
        this.gameObjects = gameObjects;
        this.windowDimension = windowDimension;
    }

    /**
     * generate a random velocity for the ball
     */
    private void randomBallVelocity(){
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) {
            ballVelX *=-1;
        }
        this.setVelocity(new Vector2(ballVelX, BALL_SPEED));
    }

    /**
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     *  beside a regular ball, this function checks if the mock ball is outside of the screen and deletes
     *                  it if so
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getCenter().y() >= this.windowDimension.y()) {
            this.gameObjects.removeGameObject(this);
        }
    }
}
