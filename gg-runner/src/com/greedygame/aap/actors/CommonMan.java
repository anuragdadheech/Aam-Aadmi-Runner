package com.greedygame.aap.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.TimeUtils;
import com.greedygame.Assets;
import com.greedygame.aap.RunnerGame;

public class CommonMan extends Actor {

    private Animation	runAnimation;  
    private Animation	rollAnimation; 
    private Animation	cleanAnimation; 
    
    private float		stateTime;
    private Rectangle bounds = new Rectangle();
    private Vector2 initialPosition;
    private Block groundBlock = null;
    
    private TextureRegion currentFrame = null;
    
    private enum Stat {
        RUN, JUMP, DUCK, SUPER, DIE
    }
    
    private boolean superPower = false;
    public static Stat manStat = Stat.RUN;

	public CommonMan(Vector2 position){
		runAnimation = new Animation(0.09f, Assets.MAN_RUN); 
		rollAnimation = new Animation(0.09f, Assets.MAN_ROLL);
		cleanAnimation = new Animation(0.075f, Assets.MAN_ATTACK);
		
		
		initialPosition = position;
		this.setPosition(initialPosition.x,  initialPosition.y);
		stateTime = 0f; 		
		currentFrame = runAnimation.getKeyFrame(0, true);
	}
	
	public boolean hasPower(){
		return this.superPower;
	}
	
	public void reset(){
		this.clearActions();
		
		if(manStat == Stat.DIE){
			currentFrame = runAnimation.getKeyFrame(0, true);
	        currentFrame.flip(false, true);
		}
		manStat = Stat.RUN;
		this.setPosition(initialPosition.x,  initialPosition.y);		
	}
	
	public void jump(){
		float height = 170;
		float duration = 0.3f;
		if(manStat != Stat.JUMP){
			reset();
				manStat = Stat.JUMP;
				Action up = Actions.sequence(Actions.moveBy(0, height, duration), Actions.moveBy(0, -height, duration));
				this.addAction(up);
		}
	}
	
	public void duck(){
		float height = -10;
		float duration = 0.25f;
		if(manStat != Stat.DUCK){
			reset();
			manStat = Stat.DUCK;
			Action down = Actions.sequence(Actions.moveBy(0, height, 0.0f),Actions.delay(2*duration), Actions.moveBy(0, -height, 0.0f));
			this.addAction(down);
		}		
	}
	
	float super_time = 0f;
	boolean isLong = false;
	int count = 0;
	
	@Override
	public void act(float delta){
		super.act(delta);		
		updateBounds();
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			jump();
		}else if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)){
			 duck();
		}
		
		if (manStat != Stat.DIE && this.getActions().size == 0) {
        	manStat = Stat.RUN;
			if(this.groundBlock != null){
	        	Rectangle gbound = this.groundBlock.getBounds();
	        	if(gbound.x+gbound.width < this.getX()){
	    			this.groundBlock.removeHitContactListner();
	    			this.groundBlock = null;	
	    			falltoground();
	    		}
			}
		}
	
	}
	
	public boolean isKilled(){  
		if(manStat == Stat.DIE){
			return true;
		}else{
			return false;
		}
	}
	
	public void startPower(){
		super_time = TimeUtils.nanoTime()*1000000000;
		left = 500;
		superPower = true;
		Gdx.input.vibrate(200);
	}

	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
        stateTime += Gdx.graphics.getDeltaTime();
        
        if(manStat == Stat.DUCK){
        	currentFrame = rollAnimation.getKeyFrame(stateTime, true);
        }else if(manStat == Stat.JUMP) {
        	currentFrame = runAnimation.getKeyFrame(stateTime, true);
        }else if(manStat == Stat.SUPER) {
        	currentFrame = cleanAnimation.getKeyFrame(stateTime, true);
        }else  if(manStat == Stat.RUN) {
        	currentFrame = runAnimation.getKeyFrame(stateTime, true);
        }
        
        this.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());        

	    batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);	
	    batch.draw(currentFrame,this.getX(),this.getY()); 
	    
	    if(RunnerGame.DEBUG){
	    	batch.draw(Assets.Debug, bounds.x, bounds.y, bounds.width, bounds.height);
	    }
    }

	int left = 0;
	private void updateBounds() {
		if(superPower){
			if(left<0){
				superPower = false;
			}
			left--;
			bounds.set(getX(), getY(), getWidth(), getHeight());
		}else{
			bounds.set(getX(), getY(), getWidth(), getHeight());
		}
	}
	
	public void dieAction(){
		this.clearActions();
		currentFrame = runAnimation.getKeyFrame(0, true);
        currentFrame.flip(false, true);
        Action die = Actions.sequence(Actions.moveBy(0, 100f, .25f), Actions.moveBy(0, -400f, .5f));
		this.addAction(die);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void falltoground(){
		Action down = Actions.moveTo(this.getX(), this.initialPosition.y, 0.05f);
		this.addAction(down);
	}
	
	public void onHitBegin(Block block){
		if(manStat == Stat.DIE){
			return;
		}
     	if(superPower){
     		this.clearActions();
    		manStat = Stat.SUPER;
    		Action down = Actions.sequence(Actions.rotateBy(100,0f),Actions.rotateBy(-100,.5f) );
    		this.addAction(down);
    		falltoground();
    	}else{
    		Gdx.input.vibrate(100);
    		manStat = Stat.DIE;
			this.dieAction();
         }
     	
	}
	
	public void onContactBegin(Block block){
		if(manStat != Stat.DIE && this.superPower == false){
			this.clearActions();
	   		this.groundBlock = block;
			Rectangle bound = groundBlock.getSolid();
			this.setY(bound.y + bound.height-20);
		}
	}
	
	public void onContactEnd(Block block){
		if(manStat != Stat.DIE){
			Rectangle blockBound = block.getBounds();
			if(blockBound.x + blockBound.width < this.getX()){
				this.clearActions();
				block.removeHitContactListner();
				this.groundBlock = null;	
				falltoground();
			}
		}
	}

	public void onHitEnd(Block block) {
		// TODO Auto-generated method stub		
	}

}