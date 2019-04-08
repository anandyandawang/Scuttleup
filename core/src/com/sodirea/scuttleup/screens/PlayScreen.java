package com.sodirea.scuttleup.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sodirea.scuttleup.Scuttleup;
import com.sodirea.scuttleup.sprites.BasicPlatform;
import com.sodirea.scuttleup.sprites.Checkpoint;
import com.sodirea.scuttleup.sprites.Platform;
import com.sodirea.scuttleup.sprites.Player;

import java.util.ArrayList;

public class PlayScreen extends ScreenAdapter {

    public static final float PIXELS_TO_METERS = 1f; // default is 0.01f
    public static final int GRAVITY = -500;
    public static final float TIME_STEP = 1 / 300f;
    public static final boolean DEBUGGING = true; // don't forget to set PIXELS_TO_METERS to 1f for this to be useful (i.e. see real-sized physics bodies)

    public static final int CHECKPOINT_INTERVALS = 2000; // each checkpoint should give a new upgrade for character
    public static final int PLATFORM_INTERVALS = 200;
    public static final int NUM_PLATFORMS_IN_ARRAY = (int) Math.ceil(Scuttleup.SCREEN_HEIGHT / PLATFORM_INTERVALS) + 1;

    Scuttleup game;

    private Texture bg;

    private Checkpoint checkpoint; // this will just move up by CHECKPOINT_INTERVALS after it goes out of screen.
    private ArrayList<Platform> platforms;
    private Player player;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    public PlayScreen(Scuttleup game) {
        this.game = game;
        bg = new Texture("catbg.png");

        world = new World(new Vector2(0, GRAVITY), true);
        debugRenderer = new Box2DDebugRenderer();

        checkpoint = new Checkpoint(0, world);
        platforms = new ArrayList<Platform>();
        for (int i = 1; i <= NUM_PLATFORMS_IN_ARRAY; i++) {
            platforms.add(new BasicPlatform(i * PLATFORM_INTERVALS, world));
        }
        player = new Player(game.cam.position.x, checkpoint.getTexture().getHeight(), world);
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.W || keyCode == Input.Keys.SPACE) {
                    player.setBodyLinearVelocity(player.getBodyLinearVelocity().x, 50);
                } else if (keyCode == Input.Keys.A) {
                    player.setBodyLinearVelocity(-20, player.getBodyLinearVelocity().y);
                } else if (keyCode == Input.Keys.S) {

                } else if (keyCode == Input.Keys.D) {
                    player.setBodyLinearVelocity(20, player.getBodyLinearVelocity().y);
                } else if (keyCode == Input.Keys.J) {

                } else if (keyCode == Input.Keys.K) {

                } else if (keyCode == Input.Keys.L) {

                }
                return true;
            }

            @Override
            public boolean keyUp(int keyCode) {
                if (keyCode == Input.Keys.W) {

                } else if (keyCode == Input.Keys.A) {
                    player.setBodyLinearVelocity(0, 0);
                } else if (keyCode == Input.Keys.S) {

                } else if (keyCode == Input.Keys.D) {
                    player.setBodyLinearVelocity(0, 0);
                } else if (keyCode == Input.Keys.J) {

                } else if (keyCode == Input.Keys.K) {

                } else if (keyCode == Input.Keys.L) {

                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        // LOGIC UPDATES
        checkpoint.update(game.cam.position.y - game.cam.viewportHeight / 2);
        for (Platform platform : platforms) {
            platform.update();
        }
        player.update();

        game.cam.update();

        world.step(TIME_STEP, 6, 2);


        // UI UPDATES

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.sb.setTransformMatrix(game.cam.view);
        game.sb.setProjectionMatrix(game.cam.projection);

        game.sb.begin();
        game.sb.draw(bg, 0, 0);
        checkpoint.render(game.sb);
        for (Platform platform : platforms) {
            platform.render(game.sb);
        }
        player.render(game.sb);
        game.sb.end();
        if (DEBUGGING) {
            debugRenderer.render(world, game.cam.combined);
        }
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        bg.dispose();
        checkpoint.dispose();
        for (Platform platform : platforms) {
            platform.dispose();
        }
        player.dispose();
    }
}