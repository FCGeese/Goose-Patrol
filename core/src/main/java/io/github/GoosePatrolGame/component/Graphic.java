package io.github.GoosePatrolGame.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Stores the visual properties used to render a game entity.
 * Contains the texture region and color tint for the entity's sprite.
 */
public class Graphic implements Component{

    // ComponentMapper for efficient retrieval of Graphic components from entities.
    public static final ComponentMapper<Graphic> MAPPER = ComponentMapper.getFor(Graphic.class);

    private TextureRegion region;
    private final Color color;

    /**
     * Constructs a new Graphic component using the specified color and texture region.
     * 
     * @param color The color tint applied to the entity's sprite.
     * @param region The texture region for the entity's sprite.
     */
    public Graphic(Color color, TextureRegion region) {
        this.color = color;
        this.region = region;
    }

    /**
     * Returns the texture region used to render the entity.
     * 
     * @return The texture region for the entity's sprite.
     */
    public TextureRegion getRegion() {
        return region;
    }

    /**
     * Returns the colour applied to the entity's sprite when rendered.
     * 
     * @return The color tint for the entity's sprite.
     */
    public Color getColor() {
        return color;
    }
}