package io.github.fcgeese.goose_patrol.Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.fcgeese.goose_patrol.Managers.CollisionManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class BreadItem extends Item {
    private static int maxShots = 5;
    private TextureRegion breadTexture;
    private List<BreadProjectile> loaves = new ArrayList<>();
    private int shotsLeft = maxShots;

    public BreadItem(TextureRegion breadTexture){
        super(breadTexture);
        this.breadTexture = breadTexture;
    }



    protected int getTriggerButton(){
        return Input.Buttons.LEFT;
    }

    protected void onUse(Vector2 userCentre, Vector2 aimTarget) {
        Vector2 direction = new Vector2(aimTarget).sub(userCentre);
        if (direction.isZero()){
            direction.set(1,0);
        }
        loaves.add(new BreadProjectile(userCentre, direction, breadTexture));
        shotsLeft--;
        if (shotsLeft <= 0){
            consume();
        }
    }


    @Override
    public void update(float delta){
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        Iterator<BreadProjectile> loafPositionIterator = loaves.iterator();
        while (loafPositionIterator.hasNext()){
            BreadProjectile loaf = loafPositionIterator.next();
            loaf.move(delta);
            if(loaf.isOffScreen(screenW,screenH)){
                loafPositionIterator.remove();
            }
        }
    }



    @Override
    public void draw(SpriteBatch batch){
        super.draw(batch);
        for (BreadProjectile loaf : loaves){
            loaf.draw(batch);
        }
    }

    public boolean itemEffectEnded() {
        return super.itemEffectEnded() && loaves.isEmpty();
    }

    public static class BreadProjectile extends MovingElement {
        public static float speed = 400f;
        private CollisionManager collider;


        BreadProjectile(Vector2 centre, Vector2 direction, TextureRegion texture){
            float w = texture.getRegionWidth();
            float h = texture.getRegionHeight();
            setTexture(texture);
            setPositionVector(new Vector2(centre.x - w /2f, centre.y - h / 2f));
            setHitbox(new Rectangle(getPositionVector().x,getPositionVector().y,w,h));
            setDisplacementVector(new Vector2(direction).nor().scl(speed));
        }

        public void setCollider(CollisionManager collider) {
            this.collider = collider;
        }

        public CollisionManager getCollider() {
            return collider;
        }

        boolean isOffScreen(float screenW, float screenH){
            Rectangle box = getHitbox();
            return (box.x + box.width < 0) || (box.x > screenW) || (box.y + box.height < 0) || (box.y > screenH);
        }

        void collisionHandling(){
            if (!collider.canMove(this, getDisplacementVector().x, getDisplacementVector().y)){
                Element hitElement = collider.hitObj(this);
                if(hitElement instanceof Goose){
                    ((MovingElement) hitElement).changeHealth(-50);// MAGIC DAMAGE NUMBER FOR TESTING
                    ((MovingElement) hitElement).setState(State.HIT);
                }
            }
        }
    }
}
