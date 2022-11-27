package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * draws the number of lives at the screen with graphic hearts
 */
public class GraphicLifeCounter extends GameObject {
    private final int maxLifes;
    private final GameObjectCollection gameObjects;
    private final GameObject[] heartArray;
    private final Counter liveCounter;
    private int currentNumberOfHearts;

    /**
     * Construct a new GameObject instance.
     *
     * @param widgetRenderable Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param widgetRenderable   The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param gameObjectsCollection all the objects in the game
     * @param numOfLives current number of lives
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                               Vector2 widgetDimensions,
                               Counter livesCounter,
                               Renderable widgetRenderable,
                               GameObjectCollection gameObjectsCollection,
                               int numOfLives) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.currentNumberOfHearts = numOfLives;
        this.liveCounter = livesCounter;
        this.maxLifes = 4;
        this.heartArray = new GameObject[maxLifes];
        gameObjects = gameObjectsCollection;

        createHearts(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
    }

    //TODO: added a new constructor with max number of lives
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 widgetDimensions,
                              Counter livesCounter,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectsCollection,
                              int numOfLives, int maxLifes) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.currentNumberOfHearts = numOfLives;
        this.liveCounter = livesCounter;
        this.maxLifes = maxLifes;
        this.heartArray = new GameObject[maxLifes];
        gameObjects = gameObjectsCollection;
        createHearts(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
    }

    /**
     * create and add all the hearts to the game objects
     * @param topLeftCorner the top left corner
     * @param dimensions the size of a heart
     * @param renderable the image of a heart
     */
    private void createHearts(Vector2 topLeftCorner,
                              Vector2 dimensions,
                              Renderable renderable) {

        for (int i = 0; i < maxLifes; i++) {
            int x = (int)(topLeftCorner.x() + (1+ dimensions.x())*i);
            GameObject currHeart =new GameObject(new Vector2(x, topLeftCorner.y()), dimensions, renderable);
            this.heartArray[i] = currHeart;
        }
        for (int i = 0; i < currentNumberOfHearts; i++) {
            gameObjects.addGameObject(heartArray[i], Layer.UI);
        }
    }

    /**
     *
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     *  this function gets called every frame
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (liveCounter.value() > 0 && liveCounter.value() < currentNumberOfHearts){
            gameObjects.removeGameObject(heartArray[liveCounter.value()], Layer.UI);
            currentNumberOfHearts --;
        }
        if (liveCounter.value() <= maxLifes && liveCounter.value() > currentNumberOfHearts){
            gameObjects.addGameObject(heartArray[liveCounter.value()-1], Layer.UI);
            currentNumberOfHearts++;
        }
    }
}
