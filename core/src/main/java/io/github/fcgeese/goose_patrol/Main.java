package io.github.fcgeese.goose_patrol;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    // Keep 16:9 aspect ratio for the game world, so that it looks the same on all devices.
    public static final float WORLD_WIDTH = 32f; // Shown world width in tiled units
    public static final float WORLD_HEIGHT = 18f; // Shown world height in tiled units
    public static final float UNIT_SCALE = 1f / 16f; // 1 unit = 16 pixels
    private io.github.EngOnePrototype.asset.AssetService assetService;

    private SpriteBatch batch;
    private Texture image;

    private Player player;
    private List<Goose> geese = new ArrayList<>();;
    private List<Element> gooseColliders = new ArrayList<>();
    private List<Element> targets = new ArrayList<>();
    private TargetManager targetManager;

    private Camera playerCamera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        player = new Player(new Vector2(100,100));

        targets.add(player);

        for(int x = 0; x<10; x++) {
            geese.add(new Goose(new Vector2(300+200, 1000*x)));
        }

        gooseColliders.addAll(geese);

        for (Goose g : geese){
            g.setColliderList(gooseColliders);
            g.setTargetList(targets);
        }

        playerCamera = new OrthographicCamera(
            player.getPositionVector().x-32,
            player.getPositionVector().y-64);
        playerCamera.viewportWidth = 600;
        playerCamera.viewportHeight = 600;
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        player.move();
        player.draw(batch);
        playerCamera.position.set(player.getPositionVector().x, player.getPositionVector().y, 0);
        playerCamera.update();
        batch.setProjectionMatrix(playerCamera.combined);

        for ( Goose g : geese){
            g.move();
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
