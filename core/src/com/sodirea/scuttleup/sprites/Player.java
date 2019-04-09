package com.sodirea.scuttleup.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.sodirea.scuttleup.screens.PlayScreen.PIXELS_TO_METERS;

public class Player {

    private Texture sprite;
    private Vector2 position;

    private BodyDef playerBodyDef;
    private Body playerBody;
    private CircleShape playerCircle;
    private FixtureDef playerFixtureDef;
    private Fixture playerFixture;

    private BodyDef footBodyDef;
    private Body footBody;
    private PolygonShape footBox;
    private FixtureDef footFixtureDef;
    private Fixture footFixture;
    private int numberOfFootContacts;

    public Player(float x, float y, World world) {
        sprite = new Texture("player.png");
        position = new Vector2(x, y);

        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set((position.x+sprite.getWidth()/2) * PIXELS_TO_METERS, (position.y+sprite.getWidth()/2) * PIXELS_TO_METERS); // convert render coordinates to physics body coodinates
        playerBody = world.createBody(playerBodyDef);
        playerCircle = new CircleShape();
        playerCircle.setRadius((sprite.getWidth()/2) * PIXELS_TO_METERS);
        playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerCircle;
        playerFixtureDef.density = 500f;
        playerFixtureDef.friction = 0f;
        playerFixture = playerBody.createFixture(playerFixtureDef);

        footBodyDef = new BodyDef();
        footBodyDef.type = BodyDef.BodyType.DynamicBody;
        footBodyDef.position.set(playerBody.getPosition().x, playerBody.getPosition().y - playerCircle.getRadius() - playerCircle.getRadius() / 8 -  2*PIXELS_TO_METERS);
        footBody = world.createBody(footBodyDef);
        footBox = new PolygonShape();
        footBox.setAsBox(playerCircle.getRadius() / 4, playerCircle.getRadius() / 8);
        footFixtureDef = new FixtureDef();
        footFixtureDef.shape = footBox;
        footFixtureDef.density = 0;
        footFixtureDef.isSensor = true;
        footFixture = footBody.createFixture(footFixtureDef);
        footBody.setFixedRotation(true);
        footBody.setUserData(this);
        numberOfFootContacts = 0;
    }

    public void update() {
        position.set(playerBody.getPosition().x/PIXELS_TO_METERS-sprite.getWidth()/2, playerBody.getPosition().y/PIXELS_TO_METERS-sprite.getWidth()/2); // convert physics body coordinates back to render coordinates. this ensures that the rendering position is always in sync with the physics body's position
        footBody.setTransform(new Vector2(playerBody.getPosition().x, playerBody.getPosition().y - playerCircle.getRadius() - playerCircle.getRadius()/8 - 2*PIXELS_TO_METERS), 0); // update the foot sensor body's position to constantly be under the ball's body
    }

    public void render(SpriteBatch sb) {
        sb.draw(sprite, position.x, position.y);
    }

    public void dispose() {
        sprite.dispose();
        playerCircle.dispose();
    }

    public Vector2 getBodyLinearVelocity() {
        return playerBody.getLinearVelocity();
    }

    public void setBodyLinearVelocity(float x, float y) {
        playerBody.setLinearVelocity(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void addNumberOfFootContacts() {
        numberOfFootContacts++;
    }

    public void lessNumberOfFootContacts() {
        numberOfFootContacts--;
    }

    public int getNumberOfFootContacts() {
        return numberOfFootContacts;
    }
}