package com.greedygame.aap.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greedygame.AbstractActor;
import com.greedygame.BodyData;
import com.greedygame.Constant;
import com.greedygame.aap.RunnerTable;
import com.greedygame.aap.screens.GameScreen;
import com.greedygame.assets.AssetsLevel2;

public class Coin extends AbstractActor {
    private Vector2 initialpos = new Vector2();
	private TextureRegion texture;
    World world = null;
    Camera camera = null;   
	protected Body body;
	private BodyData bodyData;
	public Coin(RunnerTable table, Vector2 position, int level){		
		this.initialpos = new Vector2(position.x/GameScreen.SCALE, position.y/GameScreen.SCALE);		
		this.world = table.gameScreen.getWorld();
		this.camera = table.gameScreen.getCamera(); 
		this.texture = new TextureRegion(AssetsLevel2.Coin);
		this.setPosition(initialpos.x, initialpos.y);  
		this.setSize(40/GameScreen.SCALE, 40/GameScreen.SCALE); 
		createSolidBody();
	}

	private void createSolidBody() {
        float bodyx = initialpos.x;
		float bodyy = initialpos.y;
		float BodyHalfw = 40/(2*GameScreen.SCALE);
		float BodyHalfh = 40/(2*GameScreen.SCALE);
		
        BodyDef bodyDef =new BodyDef();   
        bodyDef.type = BodyType.KinematicBody;  
        bodyDef.position.set(bodyx +BodyHalfw, bodyy+BodyHalfh);
        
        body = this.world.createBody(bodyDef);  
        PolygonShape groundBox = new PolygonShape();  
        body = this.world.createBody(bodyDef);  
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0;
        fixtureDef.density = 0;
        fixtureDef.shape = groundBox;
        fixtureDef.isSensor = true;
        groundBox.setAsBox(BodyHalfw, BodyHalfh);  
        body.createFixture(fixtureDef);	
        body.setBullet(true);
        
        this.bodyData = new BodyData();
        this.bodyData.setActor(this);
        this.bodyData.setId(Constant.BODY_COIN);
        body.setUserData(this.bodyData);
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		this.body.setLinearVelocity(RunnerTable.SPEED, 0);	
	}

	public void onCollisionWith(BodyData a){
		
	}
	
	public void onCollisionEndWith(BodyData a){
		
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		BodyData bd = (BodyData) this.body.getUserData();
		if(bd.isKilled() == false){
			batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);			
			float x = body.getPosition().x - getWidth()/2;
			float y = body.getPosition().y - getHeight()/2;
			batch.draw(this.texture, x, y, 
					0, body.getPosition().y, getWidth(), getHeight(), 
					1, 1,0);
		}
	}

	@Override
	public void init() {
		
	}

	public Body getBody() {
		return this.body;
	}

	public void kill() {
		if(this.bodyData.isKilled() == false){
			this.bodyData.setKilled(true);
			this.body.setUserData(this.bodyData);
			this.body.setActive(false);
		}		
	}

}
