package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    ArrayList<Goose> geese = new ArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        geese.add(new Goose( new Vector2(140,210) ) );
        targetManager targetManager = new targetManager() {
            @Override
            public boolean isInRange(Goose goose, Element target) {
                return false;
            }

            @Override
            public Element getTarget(Goose goose) {
                return null;
            }
        };
        collisionManager collisionManager = new collisionManager() {
            @Override
            public boolean canMove(MovingElement e) {
                return false;
            }
        };
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
    //    batch.begin();
    //    batch.draw(image, 140, 210);
        player.move(targetManager, collisionManager);
        player.draw(batch);
        for(Goose g : geese) {
            g.move(targetManager, collisionManager);
            g.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
