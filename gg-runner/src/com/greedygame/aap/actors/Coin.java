package com.greedygame.aap.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.greedygame.Assets;

public class Coin extends Actor {
    private Vector2 initialpos = new Vector2();
	private Rectangle bounds = new Rectangle();
	private Texture texture;
	
	public Coin(Vector2 position, float speed){
		
		this.initialpos = position;
		this.texture = Assets.Coin;
		this.setPosition(initialpos.x, initialpos.y);  
		this.setSize(40, 40); 
        addAction(forever(Actions.moveBy(-initialpos.x, 0, speed)));
        //addAction(forever(Actions.rotateBy(100, speed)));
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
		batch.draw(this.texture, getX(), getY(), getWidth(), getHeight());

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
