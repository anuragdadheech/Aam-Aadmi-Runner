package com.greedygame.aap.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greedygame.assets.AssetsCommon;

public class SuperBar extends Actor {
    private Vector2 initialpos = new Vector2();
	private TextureRegion fullTexture;
	private TextureRegion emptyTexture;	
	
	private Rectangle original = new Rectangle();
	int level;
	public SuperBar(Vector2 position, int level){
		this.level = level;
		this.initialpos = position;
		if(level==1){
			this.emptyTexture = AssetsCommon.SUPER_ON_L1;
			this.fullTexture = AssetsCommon.SUPER_OFF_L1;
		}else if(level==2){
			this.emptyTexture = AssetsCommon.SUPER_ON_L2;
			this.fullTexture = AssetsCommon.SUPER_OFF_L2;		
		}
		
		this.emptyTexture.setRegionWidth(0);
		
		this.setPosition(initialpos.x, initialpos.y);  
		this.setSize(this.fullTexture.getRegionWidth(), this.fullTexture.getRegionHeight()); 
		
		original.x = this.fullTexture.getRegionX();
		original.y = this.fullTexture.getRegionY();
		original.width = this.fullTexture.getRegionWidth();
		original.height = this.fullTexture.getRegionHeight();
		
	}
	
		
	public void reset(){
		if(level==1){
			this.emptyTexture = AssetsCommon.SUPER_ON_L1;
			this.fullTexture = AssetsCommon.SUPER_OFF_L1;
		}else if(level==2){
			this.emptyTexture = AssetsCommon.SUPER_ON_L2;
			this.fullTexture = AssetsCommon.SUPER_OFF_L2;		
		}
		this.fullTexture.setRegionX((int) original.x);
		this.update(0);
	}

	@Override
	public void act(float delta){
		super.act(delta);
	}

	public void clip(float p){
		int w = (int) (p*original.width);
		this.fullTexture.setRegionX((int) (original.x + original.width - w));
		this.fullTexture.setRegionWidth(w);		
	}
	
	public void show(float p){
		int w = (int) ( (1-p)*original.width);
		this.emptyTexture.setRegionWidth(w);		
	}
	
	public void update(float p){
		clip(1-p);		
		show(1-p);
	}
	
	private float randx = 0;
	private float randy = 0;
	
	public void buzz(boolean on){
		if(on){
			randx = MathUtils.random(-6, 6);
			randy = MathUtils.random(-6, 6);
		}else{
			randx = 0;
			randy = 0;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
			batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);	
			batch.draw(this.fullTexture, getX() + getWidth()-this.fullTexture.getRegionWidth() +  randx, getY() + randy, this.fullTexture.getRegionWidth(), this.fullTexture.getRegionHeight());	
			batch.draw(this.emptyTexture, getX() +  randx, getY() + randy, this.emptyTexture.getRegionWidth(), this.emptyTexture.getRegionHeight());	
	}


	boolean isPause = false;
	public void resume() {
		isPause = false;
	}


	public void pause() {
		isPause = true;
		
	}
	
	
}
	
