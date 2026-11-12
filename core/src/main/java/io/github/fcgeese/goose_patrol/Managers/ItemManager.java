package io.github.fcgeese.goose_patrol.Managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Elements.BreadItem;
import io.github.fcgeese.goose_patrol.Elements.CoffeeItem;
import io.github.fcgeese.goose_patrol.Elements.Element;
import io.github.fcgeese.goose_patrol.Elements.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemManager extends Element{
    private static int itemSize = 16;
    private static float minimumSpawnDelay = 3f;
    private static float maximumSpawnDelay = 8f;
    private static float spawnDistanceFromEdge = 32f;
    private float worldWidth;
    private float worldHeight;

    private Item current;

    private List<Item> activeEffects = new ArrayList<>();


    private static Texture PlaceholderTexture(Color colour, int width, int height){
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(colour);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }


    private Texture coffeeTexture = PlaceholderTexture(Color.BROWN, itemSize, itemSize);
    private Texture breadTexture = PlaceholderTexture(Color.TAN, itemSize, itemSize);


    private static float randomDelay(){
        return MathUtils.random(minimumSpawnDelay, maximumSpawnDelay);
    }

    private float spawnTimer = randomDelay();


    public ItemManager(float worldWidth, float worldHeight){
        setPositionVector(new Vector2(0,0));
        setHitbox(new Rectangle(0,0,worldWidth,worldHeight));
    }


    private void SpawnRandomItem(){
        if(MathUtils.randomBoolean()){
            current = new CoffeeItem(new TextureRegion(coffeeTexture));
        }
        else{
            current = new BreadItem(new TextureRegion(breadTexture));
        }
        Rectangle area = getHitbox();
        float x = MathUtils.random(area.x + spawnDistanceFromEdge,area.x + area.width - spawnDistanceFromEdge - itemSize);
        float y = MathUtils.random(area.y + spawnDistanceFromEdge,area.y + area.height - spawnDistanceFromEdge - itemSize);
    }




    public void update(float delta, Element player, Vector2 aimTarget){
        Iterator<Item> effectsIterator = activeEffects.iterator();
        while (effectsIterator.hasNext()){
            Item item = effectsIterator.next();
            item.update(delta);
            if (item.ItemEffectEnded()){
                effectsIterator.remove();
            }
        }

        if (current == null){
            spawnTimer = spawnTimer - delta;
            if (spawnTimer <= 0f){
                SpawnRandomItem();
            }
            return;
        }
        current.update(delta);
        if (current.IsOnGround()){
            current.TryPickup(player);
        }
        else if (current.IsInInventory()){
            current.tryUse(player, aimTarget);
        }

        if (current.IsUsed()){
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

    public float GetSpeedMultiplier(){
        float multiplier = 1;
        for (Item item:activeEffects){
            multiplier = Math.max(multiplier, item.GetSpeedMultiplier());
        }
        if (current != null){
            multiplier = Math.max(multiplier, current.GetSpeedMultiplier());
        }
        return multiplier;
    }


    public Item GetHeldItem(){
        if (current != null && current.IsInInventory()) {
            return current;
        }
        else{
            return null;
        }
    }

    public void dispose(){
        coffeeTexture.dispose();
        breadTexture.dispose();
    }
}
