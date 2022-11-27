package src.gameobjects;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * this game object is made to bring the camera back to normal after a number of times the ball collides
 */
public class CameraChangeController extends GameObject {
    private static final int COLLIDES_TO_RESET = 4;
    private final GameManager gameManager;
    private final Counter ballCollisionCounter;

    /**
     *
     * @param gameManager the game manager - used to access the camera
     * @param ballCollisionCounter counter of the ball coli
     */
    public CameraChangeController(GameManager gameManager, Counter ballCollisionCounter) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.gameManager = gameManager;
        this.ballCollisionCounter = ballCollisionCounter;
    }

    /**
     * if the ball collided 4 times, return camera to normal
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
        if (ballCollisionCounter.value() >= COLLIDES_TO_RESET && gameManager.getCamera() != null) {
            gameManager.setCamera(null);
        }
    }
}
