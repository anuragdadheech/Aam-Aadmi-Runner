package com.greedygame.aap.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greedygame.AbstractActor;
import com.greedygame.BodyData;
import com.greedygame.Constant;
import com.greedygame.aap.RunnerTable;
import com.greedygame.aap.screens.GameScreen;

public class Platform extends AbstractActor{
    World world = null;
    Camera camera = null;   
	protected Body body;
	private BodyData bodyData;
	private Fixture fixture;
	public Platform(RunnerTable table) {
		
		this.world = table.gameScreen.getWorld();
		this.camera = table.gameScreen.getCamera();	
		this.bodyData = new BodyData();
		
		this.setSize(table.getWidth()/GameScreen.SCALE, table.getHeight()/GameScreen.SCALE);		
		createSolidBody(); 
		
		this.bodyData.setId(Constant.BODY_ROAD);
		this.bodyData.setActor(this);
		this.body.setUserData(this.bodyData);
	}
	
	private void createSolidBody() {		
		 BodyDef groundBodyDef =new BodyDef();  
	     groundBodyDef.position.set(new Vector2(0, 100/GameScreen.SCALE - 10));  
	     body = this.world.createBody(groundBodyDef);  
	     PolygonShape groundBox = new PolygonShape();  
	     groundBox.setAsBox(getWidth() * 2, 10.0f);  
	     body.createFixture(groundBox, 0.0f); 
	        
	        this.bodyData = new BodyData();
	        this.bodyData.setActor(null);
	        this.bodyData.setId(Constant.BODY_ROAD);
	        body.setUserData(this.bodyData);	
	}

	public Body getBody(){
		return this.body;
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		this.body.setLinearVelocity(RunnerTable.SPEED, 0);	
	}


	
	public void setSensor(boolean b){
		this.fixture.setSensor(b);
	}
	
	public void onCollisionWith(AbstractActor a){
	}
	
	public void onCollisionEndWith(AbstractActor a){
	}
	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);
		
		//batch.draw(this.texture, body.getPosition().x, body.getPosition().y-BodyHalfh, getWidth(), getHeight());	

	}

	@Override
	public void onCollisionWith(BodyData a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCollisionEndWith(BodyData a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
