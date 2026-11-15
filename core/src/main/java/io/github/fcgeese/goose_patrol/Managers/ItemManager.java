package io.github.fcgeese.goose_patrol.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.fcgeese.goose_patrol.Settings;
import io.github.fcgeese.goose_patrol.Elements.BreadItem;
import io.github.fcgeese.goose_patrol.Elements.CoffeeItem;
import io.github.fcgeese.goose_patrol.Elements.Element;
import io.github.fcgeese.goose_patrol.Elements.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemManager extends Element{

    private static float itemSize = 1f;
    private static float minimumSpawnDelay = 3f;
    private static float maximumSpawnDelay = 8f;
    private static float spawnDistanceFromEdge = 2f;
    private List<Element> spawnColliders = new ArrayList<>();

    private Item current;

    private List<Item> activeEffects = new ArrayList<>();

    public static TextureRegion coffeeTexture = Settings.coffeeTexture;

    public static TextureRegion breadTexture = Settings.breadTexture;

    private static float randomDelay(){
        return MathUtils.random(minimumSpawnDelay, maximumSpawnDelay);
    }

    private float spawnTimer = randomDelay();


    public ItemManager(float worldWidth, float worldHeight){
        setPositionVector(new Vector2(0,0));
        setHitbox(new Rectangle(0,0,worldWidth,worldHeight));
    }


    private void spawnRandomItem() {
        Rectangle area = getHitbox();

        // Try to find a valid spawning position
        for (int attempts = 0; attempts < 100; attempts++) {

            float x = MathUtils.random(
                area.x + spawnDistanceFromEdge,
                area.x + area.width - spawnDistanceFromEdge - itemSize
            );
            float y = MathUtils.random(
                area.y + spawnDistanceFromEdge,
                area.y + area.height - spawnDistanceFromEdge - itemSize
            );

            // Temporary hitbox for potential spawn position
            Rectangle candidate = new Rectangle(x, y, itemSize, itemSize);

            boolean blocked = false;

            // Check against all map colliders
            for (Element collider : spawnColliders) {
                if (candidate.overlaps(collider.getHitbox())){
                    blocked = true;
                    break;
                }
            }

            // Spawn if the position is valid
            if (!blocked) {
                if(MathUtils.randomBoolean()) {
                    current = new CoffeeItem(new TextureRegion(coffeeTexture));
                }
                else{
                    current = new BreadItem(new TextureRegion(breadTexture));
                }

                current.spawnAt(x, y);

                Gdx.app.log("ItemManager", 
                "Spawned item at: " + x + ", " + y + 
                ", took " + (attempts + 1) + " attempts");

                return;
            }
        }

        // No valid spawning position found after 100 attempts
        Gdx.app.log("ItemManager", "Could not find a valid item spawning position");

        spawnTimer = randomDelay();
    }

    public void update(float delta, Element player, Vector2 aimTarget){
        Iterator<Item> effectsIterator = activeEffects.iterator();
        while (effectsIterator.hasNext()){
            Item item = effectsIterator.next();
            item.update(delta);
            if (item.itemEffectEnded()){
                effectsIterator.remove();
            }
        }

        if (current == null){
            spawnTimer = spawnTimer - delta;
            if (spawnTimer <= 0f){
                spawnRandomItem();
            }
            return;
        }
        current.update(delta);
        if (current.isOnGround()){
            current.tryPickup(player);
        }
        else if (current.isInInventory()){
            current.tryUse(player, aimTarget);
        }

        if (current.isUsed()){
            activeEffects.add(current);
            current = null;
            spawnTimer = randomDelay();
        }
    }

    @Override
    public void draw(SpriteBatch batch){
        if(current != null){
            current.draw(batch);
        }
        for (Item item : activeEffects) {
            item.draw(batch);
        }
    }

    public void setSpawnColliders(List<Element> colliders) {
        this.spawnColliders = colliders;
    }

    public float getSpeedMultiplier(){
        float multiplier = 1;
        for (Item item:activeEffects){
            multiplier = Math.max(multiplier, item.getSpeedMultiplier());
        }
        if (current != null){
            multiplier = Math.max(multiplier, current.getSpeedMultiplier());
        }
        return multiplier;
    }

    public Item getHeldItem(){
        if (current != null && current.isInInventory()) {
            return current;
        }
        else{
            return null;
        }
    }

    public void dispose(){
        coffeeTexture.getTexture().dispose();
        breadTexture.getTexture().dispose();
    }
}
