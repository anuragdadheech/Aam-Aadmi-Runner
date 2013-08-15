package com.greedygame.app.buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greedygame.Assets;

public class Button extends Actor {

	private Rectangle bounds = new Rectangle();
	private Texture texture;
	private int id;
	
	public Button(float x, float y, float w, float h, int id){
		this.texture = Assets.Debug;
		this.setPosition(x, y);  
		this.setSize(w, h); 
		this.id = id;
		

	}
	
	public int getId(){
		return this.id;
	}

	@Override
	public void act(float delta){
		super.act(delta);
		updateBounds();
	}

	private void updateBounds() {
		bounds.set(getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {

		batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);	
		//batch.draw(this.texture, getX(), getY(), getWidth(), getHeight());

		//batch.draw(Assets.Debug, bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	
	
	
}
