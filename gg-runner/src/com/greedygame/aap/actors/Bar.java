package com.greedygame.aap.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greedygame.Assets;

public class Bar extends Actor {
    private Vector2 initialpos = new Vector2();
	private Rectangle bounds = new Rectangle();
	private AtlasRegion fullTexture;
	private AtlasRegion emptyTexture;
	private float percent = 0f;
	
	private Rectangle original = new Rectangle();
	public Bar(Vector2 position){
		
		this.initialpos = position;
		this.emptyTexture = Assets.BAR.get(0);
		this.fullTexture = Assets.BAR.get(1);
		
		this.emptyTexture.setRegionWidth(0);
		
		this.setPosition(initialpos.x, initialpos.y);  
		this.setSize(this.fullTexture.getRegionWidth(), this.fullTexture.getRegionHeight()); 
		
		original.x = this.fullTexture.getRegionX();
		original.y = this.fullTexture.getRegionY();
		original.width = this.fullTexture.getRegionWidth();
		original.height = this.fullTexture.getRegionHeight();
		
	}
	
		
	public void reset(){
		this.emptyTexture = Assets.BAR.get(0);
		this.fullTexture = Assets.BAR.get(1);
		this.fullTexture.setRegionX((int) original.x);
		this.update(0);
	}

	@Override
	public void act(float delta){
		super.act(delta);
		updateBounds();
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
		this.percent = p;
		clip(1-p);		
		show(1-p);
	}
	
	private void updateBounds() {
		bounds.set(getX(), getY(), getWidth(), getHeight());
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
		//batch.draw(Assets.Debug, bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	CommonMan _man;
	
	public void addContactListner(CommonMan man){
		this._man = man;		
	}
	
	
}
	
