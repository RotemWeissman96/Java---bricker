package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.ExtraBall;

/**
 * a strategy to activate when a brick was hit. adds 3 mock balls to the game
 */
public class ExtraBallsStrategy extends ExtraStrategy {
    private static final int NUMBER_OF_BALLS = 3;
    private final Vector2 dimensions;
    private final Renderable renderable;
    private final Sound sound;
    private final Vector2 windowDimension;

    /**
     *
     * @param gameObjects all the objects in the game,
     * @param strategy if there are any more strategies for the current brick
     * @param dimensions size of the mock balls
     * @param windowDimension the window dimension
     * @param renderable  image of the mock ball
     * @param sound sound of a ball collide
     */
    public ExtraBallsStrategy(GameObjectCollection gameObjects, CollisionStrategy strategy,
                              Vector2 dimensions, Renderable renderable, Sound sound,
                              Vector2 windowDimension) {
        super(gameObjects, strategy);
        this.dimensions = dimensions;
        this.renderable = renderable;
        this.sound = sound;
        this.windowDimension = windowDimension;
    }

    /**
     * change the balls velocity
     * @param collidedObj the object being colided with
     * @param colliderObj the object coliding with the colider
     * @param bricksCounter the counter of the bricks
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);
        createBalls(collidedObj);
    }


    /**
     * create upon collision 3 mock balls at the brick position
     * @param collidedObj the object being colided with
     */
    public void createBalls(GameObject collidedObj) {
        for (int i = 0; i < NUMBER_OF_BALLS; i++){
            Vector2 position = collidedObj.getCenter().add(dimensions.mult(-0.5F)); // calculate center
            ExtraBall extraBall = new ExtraBall(position.add(new Vector2(i,i)), dimensions, renderable,
                    sound, super.gameObjects, windowDimension); // added i so the balls won't overlap
            this.gameObjects.addGameObject(extraBall);
        }
    }
}
