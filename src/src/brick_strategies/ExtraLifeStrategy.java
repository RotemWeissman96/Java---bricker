package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.FallingHeart;

/**
 * a strategy to activate when a brick was hit. an extra life is falling from the brick
 */
public class ExtraLifeStrategy extends ExtraStrategy {

    private static final int FALLING_SPEED = 100;
    private final Renderable heartImage;
    private final Vector2 heartDimensions;
    private final Counter heartCounter;
    private final Vector2 windowDimension;

    /**
     *
     * @param gameObjects all the objects in the game,
     * @param extraStrategy if there are any more strategies for the current brick
     * @param heartDimensions size of the mock balls
     * @param windowDimension the window dimension
     * @param heartImage  image of the mock ball
     * @param heartCounter counts how many lives does the player has
     */
    public ExtraLifeStrategy(GameObjectCollection gameObjects, CollisionStrategy extraStrategy,
                             Renderable heartImage, Vector2 heartDimensions, Counter heartCounter,
                             Vector2 windowDimension) {
        super(gameObjects, extraStrategy);
        this.heartImage = heartImage;
        this.heartDimensions = heartDimensions;
        this.heartCounter = heartCounter;
        this.windowDimension = windowDimension;
    }

    /**
     * drops a heart from the brick
     * @param collidedObj the brick
     * @param colliderObj a ball
     * @param bricksCounter counts the number of bricks in thee game
     */
    @Override
    public void onCollision(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollision(collidedObj, colliderObj, bricksCounter);

        FallingHeart heart = new FallingHeart(collidedObj.getCenter().add(heartDimensions.mult(-0.5F)),
                heartDimensions, heartImage, windowDimension, gameObjects, heartCounter);
        gameObjects.addGameObject(heart);
        heart.setVelocity(new Vector2(0,FALLING_SPEED));
    }


}
