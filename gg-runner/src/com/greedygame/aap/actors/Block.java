package com.greedygame.aap.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.greedygame.Assets;
import com.greedygame.aap.RunnerGame;

public class Block extends Actor {

    private Vector2 initialpos = new Vector2();
	private Rectangle bounds = new Rectangle();
	private Rectangle solid = null;
	private TextureRegion texture;	
	private Array<AtlasRegion> textureArray;	
	private Animation	blastAnimation;  
	private Vector2 offset = new Vector2(0,0);	
	private int type;
	private float		stateTime = 0f;
	
	public Block(Vector2 position, float speed) {
		this.type = MathUtils.random(0, 3);
		this.initialpos = position;
		
		blastAnimation = new Animation(0.03f, Assets.BLAST); 

		//this.type = 0;
		if(type == 0){
			this.initialpos.y -= 120;
			this.textureArray = Assets.BUS;
			this.texture = this.textureArray.get(0);
			this.offset.x = 70;
			this.offset.y = 100;
			this.solid = new Rectangle();
		}else if(type == 1){
			this.initialpos.y -= 100;
			this.textureArray = Assets.CORRUPT;
			this.texture = this.textureArray.get(0);
			this.offset.x = 25;
			this.offset.y = 180;
		}else if(type == 2){
			this.initialpos.y += 80;	
			this.textureArray = Assets.PILLAR;
			this.texture = this.textureArray.get(0);
			this.offset.x = 80;
			this.offset.y = 60;
		}else if(type == 3){
			this.initialpos.y += 60;
			this.textureArray = Assets.FLYOVER;
			this.texture = this.textureArray.get(0);
			this.offset.x = 120;
			this.offset.y = 170;
		}
		this.setSize(this.texture.getRegionWidth(), this.texture.getRegionHeight());
		
		this.setPosition(initialpos.x, initialpos.y);  
        addAction(forever(Actions.moveBy(-initialpos.x, 0, speed)));         
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		updateBounds();
	}

	private void updateBounds() {
		if(this.type == 0){
			bounds.set(getX()+this.offset.x, getY(), getWidth()-this.offset.x-this.offset.y, getHeight()-50);
			solid.set(getX()+this.offset.x+5, getY()+getHeight()-30, getWidth()-this.offset.x-this.offset.y-5, 20);
		}else{
			bounds.set(getX()+this.offset.x, getY(), getWidth()-this.offset.x-this.offset.y, getHeight());
		}
	}
	
	public int gettype(){
		return this.type;
	}
	
	private boolean _isContactOn = false;
	private boolean _isHitOn = false;
	public boolean _isContact, _isHit;
	private CommonMan _man = null;
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		this._isContact = false;
		this._isHit = false;
        
		if(this._man!=null){
			if(solid != null){
				this._isContact = this._man.getBounds().overlaps(solid);
			}
			
			if(this.bounds != null){
				this._isHit = this._man.getBounds().overlaps(bounds);
			}
						
			if(this._isHit == true){
				if(this._isHitOn == false){			
					this._man.onHitBegin(this);
					if(this._man.hasPower()){
						this.texture = this.textureArray.get(1);
					}
					
					this._isHitOn = true;
				}
			}else if(this._isHitOn == true){
				this._man.onHitEnd(this);
				this._isHitOn = false;
			}
			
			if(this._isContact == true){				
				if(type == 0){
					this.setY(this.initialpos.y- MathUtils.random(2, 3));
				}				
				if(this._isContactOn == false){
					this._man.onContactBegin(this);
					this._isContactOn = true;	
				}
			}else if(this._isContactOn == true){
				this._man.onContactEnd(this);
				this._isContactOn = false;
			}			

		}

		batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);	
		batch.draw(this.texture, getX(), getY());	
		
		if(_isHitOn && this._man.hasPower()){
			stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion currentFrame = blastAnimation.getKeyFrame(stateTime, false);
			batch.draw(currentFrame,this.getX()+bounds.width/2,this.getY(), 200, 200);			
		}
		
		if(RunnerGame.DEBUG){
			if(bounds != null){
				batch.draw(Assets.Debug, bounds.x, bounds.y, bounds.width, bounds.height);
			}
			if(solid != null){
				batch.draw(Assets.Debug, solid.x, solid.y, solid.width, solid.height);
			}
		}
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public Rectangle getSolid() {
		return solid;
	}
	
	public boolean setContact(Rectangle bound){
		this._isContact = this.bounds.overlaps(bound);
		return this._isContact;
	}
	
	public void addHitContactListner(CommonMan man){
		this._man = man;		
	}
	
	public void removeHitContactListner(){
		this._man = null;
	}
}
