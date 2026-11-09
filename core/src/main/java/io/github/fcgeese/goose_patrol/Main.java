package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;

    private Player player;
    private ArrayList<Goose> geese = new ArrayList<>();;
    private CollisionManager collisionManager;
    private TargetManager targetManager;

    private Camera playerCamera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        player = new Player(new Vector2(10,10));
        geese.add(new Goose(new Vector2(300,100)));
        collisionManager = new CollisionManager();
        targetManager = new TargetManager();
        targetManager.addTarget(player);
        playerCamera = new OrthographicCamera(player.getPositionVector().x-32, player.getPositionVector().y-64);
        playerCamera.viewportWidth = 600;
        playerCamera.viewportHeight = 600;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        player.move(collisionManager);
        player.draw(batch);
        playerCamera.position.set(player.getPositionVector().x, player.getPositionVector().y, 0);
        playerCamera.update();
        batch.setProjectionMatrix(playerCamera.combined);

        for ( Goose g : geese){
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
