package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * this class responsible for the numeric representation of lives left.
 */
public class NumericLifeCounter extends GameObject {
    private final Counter liveCounter;
    private final TextRenderable textRenderable;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     *
     **/
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner,
                       Vector2 dimensions, GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.liveCounter = livesCounter;
        this.textRenderable = new TextRenderable(Integer.toString(livesCounter.value()));
        textRenderable.setColor(Color.GREEN);
        this.renderer().setRenderable(textRenderable);
    }


    /**
     * choose the correct color and number string to display
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
        int lives = liveCounter.value();
        switch (lives){
            case 4:
                textRenderable.setColor(Color.GREEN);
                textRenderable.setString("4");
                break;
            case 3:
                textRenderable.setColor(Color.GREEN);
                textRenderable.setString("3");
                break;
            case 2:
                textRenderable.setColor(Color.YELLOW);
                textRenderable.setString("2");
                break;
            case 1:
                textRenderable.setColor(Color.RED);
                textRenderable.setString("1");
                break;
        }
    }
}
