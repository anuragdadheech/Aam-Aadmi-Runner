package com.greedygame.aap.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greedygame.aap.RunnerTable;
import com.greedygame.aap.screens.GameScreen;

public class InfiniteScrollBg extends Actor {
	
	private TextureRegion texture,texture2;
	private float posX, posY;
	
    World world = null;
    Camera camera = null;
    protected float bodyx;
	protected float bodyy;
	protected float BodyHalfw;
	protected float BodyHalfh;
	protected Body body;
	protected BodyType bodyType;
	public InfiniteScrollBg(RunnerTable table,float x, float y, TextureRegion img) {
		this.texture = img;
		this.texture2 = img;
		
		posX = x/GameScreen.SCALE;
		posY = y/GameScreen.SCALE;
		setPosition(posX, posY);
		this.world = table.gameScreen.getWorld();
		this.camera = table.gameScreen.getCamera();
		
        BodyDef bodyDef =new BodyDef();   
        bodyDef.type = BodyType.KinematicBody;  
        bodyDef.position.set(0, 0);
        
        body = this.world.createBody(bodyDef);  
        PolygonShape groundBox = new PolygonShape();  
        body = this.world.createBody(bodyDef);  
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0;
        fixtureDef.shape = groundBox;
        groundBox.setAsBox(2/GameScreen.SCALE, 2/GameScreen.SCALE);  
        body.createFixture(fixtureDef);  
	}
	
	public void reset(){
		System.out.println("scroll"+ GameScreen.SCALE);
		setWidth(this.texture.getRegionWidth()/GameScreen.SCALE);
		setHeight(this.texture.getRegionHeight()/GameScreen.SCALE);	
		setPosition(posX, posY);
		body.setTransform(0,0,0);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		float x = this.body.getPosition().x;
		if(this.body.getPosition().x<0){
			this.body.setTransform(getWidth(), this.body.getPosition().y, 0);
		}
		batch.draw(this.texture, x-getWidth(), getY(), getWidth(), getHeight());
		batch.draw(this.texture2, x, getY(), getWidth(), getHeight());		
	}

	public Body getBody() {
		return this.body;		
	}
}
