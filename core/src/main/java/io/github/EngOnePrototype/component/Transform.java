package io.github.EngOnePrototype.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents the position, dimensions, scaling and rotation of an entity in the game world.
 * Also provides information about the entity's rendering order through its z-index.
 */
public class Transform implements Component, Comparable<Transform> {

    // ComponentMapper for efficient retrieval of Transform components from entities.
    public static final ComponentMapper<Transform> MAPPER = ComponentMapper.getFor(Transform.class);

    private final Vector2 position;
    private final int z;
    private final Vector2 size;
    private final Vector2 scaling;
    private final float rotationDeg;

    /**
     * Constructs a new Transform component using the provided properties.
     * 
     * @param position The position of the entity in the game world.
     * @param z The rendering order of the entity; higher values are rendered on top of lower values.
     * @param size The width and height of the entity in world units.
     * @param scaling The horizontal and vertical scaling factors for the entity.
     * @param rotationDeg The rotation of the entity in degrees.
     */
    public Transform(
        Vector2 position,
        int z, Vector2 size,
        Vector2 scaling,
        float rotationDeg
    ) {
        this.position = position;
        this.z = z;
        this.size = size;
        this.scaling = scaling;
        this.rotationDeg = rotationDeg;
    }

    /**
     * Compares this Transform component with another to determine their rendering order.
     * Entities are sorted first by their z-index, followed by their y and x coordinates.
     * 
     * @param other The other Transform component to compare against.
     * @return A negative value if this Transform comes before the other, 
     *         zero if they are equal in order or a positive value otherwise.
     */
    @Override
    public int compareTo(Transform other) {
        // Compare z-values first; higher z-values are rendered first.
        if (this.z != other.z) {
            return Float.compare(this.z, other.z);
        }

        // If z-values match, compare y coordinates; higher y-values are rendered first.
        if (this.position.y != other.position.y) {
            return Float.compare(other.position.y, this.position.y);
        }

        // If both z and y values match, compare x coordinates; higher x-values are rendered first.
        return Float.compare(this.position.x, other.position.x);
    }

    /**
     * Returns the position of the entity in the game world.
     * 
     * @return A Vector2 representing the entity's position.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Returns the entity's dimensions.
     * 
     * @return A Vector2 of the width and height of the entity.
     */
    public Vector2 getSize() {
        return size;
    }

    /**
     * Returns the entity's scaling factors.
     * 
     * @return A Vector2 of the horizontal and vertical scaling factors.
     */
    public Vector2 getScaling() {
        return scaling;
    }

    /**
     * Returns the entity's rotation.
     * 
     * @return The entity's rotation in degrees.
     */
    public float getRotationDeg() {
        return rotationDeg;
    }
}