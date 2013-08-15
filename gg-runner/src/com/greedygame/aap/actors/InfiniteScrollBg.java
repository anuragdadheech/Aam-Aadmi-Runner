package com.greedygame.aap.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class InfiniteScrollBg extends Actor {
	
	private TextureRegion texture,texture2;
	private float speed;
	private float posX, posY;
	private Action scrollAction;
	
	public InfiniteScrollBg(float x, float y, TextureRegion img, float s) {
		this.texture = img;
		this.texture2 = img;
		this.posX = x;
		this.posY = y;
		this.speed = s;
		
		setWidth(img.getRegionWidth());
		setHeight(img.getRegionHeight());
		
		scrollAction = forever(sequence(moveTo(0, posY, speed), moveTo(getWidth(), posY)));
		
		setPosition(posX, posY);
		addAction(scrollAction);
	}
	
	
	public InfiniteScrollBg(float x, float y, TextureRegion img,TextureRegion img2, float s) {
		this.texture = img;
		this.texture2 = img2;
		this.posX = x;
		this.posY = y;
		this.speed = s;
		
		setWidth(img.getRegionWidth());
		setHeight(img.getRegionHeight());
		
		scrollAction = forever(sequence(moveTo(0, posY, speed), moveTo(getWidth(), posY)));
		
		setPosition(posX, posY);
		addAction(scrollAction);
	}
	
	
	public void reset(){
		setPosition(posX, posY);
		addAction(forever(sequence(moveTo(0, posY, speed), moveTo(getWidth(), posY))));
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(this.texture, getX()-getWidth(), getY(), getWidth(), getHeight());
		batch.draw(this.texture2, getX(), getY(), getWidth(), getHeight());
		
		
	}
}
