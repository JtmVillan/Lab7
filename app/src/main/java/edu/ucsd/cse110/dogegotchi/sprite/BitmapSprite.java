package edu.ucsd.cse110.dogegotchi.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.common.base.Preconditions;

/**
 * Helper base class for drawing a sprite on a canvas.
 */
public abstract class BitmapSprite implements ISprite {

    private static final String PRECONDITION_ERROR_MESSAGE =
            "%s coordinate for sprite must be greater than or equal to 0.";

    /**
     * Sprite to be drawn on canvas. Must be set by child class.
     */
    protected Bitmap sprite;

    /**
     * Coordinates where sprite will be drawn on canvas. Must be set by child class.
     */
    protected Coord coord;

    /**
     * Draws bitmap onto canvas.
     *
     * @param canvas The target canvas.
     */
    public void draw(Canvas canvas) {
        Preconditions.checkNotNull(sprite);
        Preconditions.checkArgument(coord.getX()>=0, String.format(PRECONDITION_ERROR_MESSAGE, "x"));
        Preconditions.checkArgument(coord.getY()>=0, String.format(PRECONDITION_ERROR_MESSAGE, "y"));

        canvas.drawBitmap(sprite, coord.getX(), coord.getY(), null);
    }

    public void setCoord(final Coord coord) {
        Preconditions.checkNotNull(coord);
        this.coord = coord;
    }

    public void setSprite(final Bitmap sprite) {
        Preconditions.checkNotNull(sprite);
        this.sprite = sprite;
    }
}
