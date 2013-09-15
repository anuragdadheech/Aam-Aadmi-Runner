package com.greedygame.aap.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greedygame.AbstractActor;
import com.greedygame.BodyData;
import com.greedygame.Constant;
import com.greedygame.aap.RunnerGame;
import com.greedygame.aap.RunnerTable;
import com.greedygame.aap.screens.GameScreen;
import com.greedygame.assets.AssetsCommon;

public class CommonMan extends AbstractActor {

    private enum Stat {
        RUN, JUMP, DUCK, SUPER, DIE, PAUSE, TODIE
    }
    
    private Animation	runAnimation;  
    private Animation	rollAnimation; 
    private Animation	cleanAnimation; 
    
    private float		stateTime;
    private Vector2 initialPosition;    
    private TextureRegion currentFrame = null;
    
    private boolean superPower;
    private Stat manStat;
    private float hitTimer;
    
    World world = null;
    Camera camera = null;
    protected float bodyx;
	protected float bodyy;
	protected float bodyw;
	protected float bodyh;
	protected Body lowerBody;
	private BodyData bodyData;
	RunnerTable table;

	
	PolygonShape groundBox;
	private Fixture fixture;
	public CommonMan(RunnerTable table, Vector2 position){
		this.table = table;
		runAnimation = new Animation(0.09f, AssetsCommon.MAN_RUN); 
		rollAnimation = new Animation(0.09f, AssetsCommon.MAN_ROLL);
		cleanAnimation = new Animation(0.075f, AssetsCommon.MAN_ATTACK);
		
		
		initialPosition = new Vector2(position.x/GameScreen.SCALE, position.y/GameScreen.SCALE);
		this.setPosition(initialPosition.x,  initialPosition.y);
		stateTime = 0f; 		
		currentFrame = runAnimation.getKeyFrame(0, true);		

		this.world = table.gameScreen.getWorld();
		this.camera = table.gameScreen.getCamera();
		
		bodyx = initialPosition.x;
		bodyy = initialPosition.y;
		bodyw = 75/(2*GameScreen.SCALE);
		bodyh = 113/(2*GameScreen.SCALE);
				
        BodyDef bodyDef =new BodyDef();   
        bodyDef.type = BodyType.DynamicBody;        
        bodyDef.position.set(bodyx +bodyw, bodyy+bodyh);  
        
        groundBox = new PolygonShape();  
        groundBox.setAsBox(bodyw, bodyh); 
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0;
        fixtureDef.shape = groundBox; 
        
        lowerBody = this.world.createBody(bodyDef);
        fixture = lowerBody.createFixture(fixtureDef);
        
        this.bodyData = new BodyData();
        this.bodyData.setActor(this);
        this.bodyData.setId(Constant.BODY_MAN);
        this.lowerBody.setUserData(this.bodyData);
        init();
	}
	
	private float super_time;
	private boolean _onBus;
	
	@Override
	public void init() {
		this.super_time = 0f;
		this._onBus = false;
	    currentFrame = runAnimation.getKeyFrame(0, true);
	    this.superPower = false;
	    this.manStat = Stat.RUN;
	}
	
	public Body getBody(){
		return this.lowerBody;
	}
	

	
	public boolean hasPower(){
		return this.superPower;
	}
	
	public void reset(){
		init();
		fixture.setSensor(false);
		this.setPosition(initialPosition.x,  initialPosition.y);
		lowerBody.setTransform(bodyx +bodyw, bodyy+bodyh, 0);
		lowerBody.setLinearVelocity(0, 0);
	}
	
	public void jump(){
		if(manStat != Stat.JUMP && onGroud() == true){
			manStat = Stat.JUMP;
			lowerBody.setType(BodyType.DynamicBody);
			lowerBody.setLinearVelocity(0, 50);
		}
	}
	
	float rollStarted = 0f;
	private boolean onRoad;
	public void duck(){
		if(manStat != Stat.DUCK ){
			manStat = Stat.DUCK;
			lowerBody.setLinearVelocity(0, -50);
			lowerBody.setTransform(lowerBody.getPosition().x, lowerBody.getPosition().y - bodyw/2, (11.0f/7));
			rollStarted = 0f;
		}
	}
	
	public void run(){
		if(manStat != Stat.RUN){
			manStat = Stat.RUN;
			lowerBody.setTransform(lowerBody.getPosition().x, lowerBody.getPosition().y + bodyw/2, 22.0f/7);
		}
	}
	
	public boolean onGroud(){
		return (this.onBus() == true || this.onRoad() == true);
	}
	
	
	@Override
	public void act(float delta){
		super.act(delta);
		
		if(lowerBody.getPosition().x < 0 || lowerBody.getPosition().y < 0){
			manStat = Stat.DIE;
		}else if(manStat == Stat.PAUSE){
			
		}
		else{		
			if(manStat == Stat.DUCK){
				duck();
				if(rollStarted >= 0.75f){		
					run();
				}else{
					rollStarted += delta;
				}
			}else if(manStat == Stat.RUN || onGroud() == true){
				run();
			}else if(manStat == Stat.JUMP){
				jump();
			}
			
			if(this.hasPower()){
				if(super_time<0){
					this.superPower = false;
				}else{
					super_time -= delta;
				}
				hitTimer -= delta;
			}
			
			if(manStat != Stat.JUMP && Gdx.input.isKeyPressed(Keys.SPACE)){
				jump();
			}else if(manStat != Stat.DUCK && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)){
				duck();
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
		super_time = 10f;
		superPower = true;
		manStat = Stat.SUPER;
		Gdx.input.vibrate(200);
		
	}

	public float powerleft(){
		return super_time/5;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(manStat == Stat.PAUSE){
			return;
		}

        stateTime += Gdx.graphics.getDeltaTime();        
        if(manStat != Stat.DIE){
        if(manStat == Stat.DUCK){
        	currentFrame = rollAnimation.getKeyFrame(stateTime, true);
        }else if(manStat == Stat.JUMP) {
        	currentFrame = runAnimation.getKeyFrame(stateTime, true);
        }else if(this.hasPower() && hitTimer>0) {
        	currentFrame = cleanAnimation.getKeyFrame(stateTime, true);        	
        }else{
        	currentFrame = runAnimation.getKeyFrame(stateTime, true);
        }
        
        this.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());  

	    batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);	
	    if(manStat == Stat.DUCK){
	    	batch.draw(currentFrame, lowerBody.getPosition().x - bodyw , lowerBody.getPosition().y - bodyh + 20/GameScreen.SCALE, this.getWidth()/GameScreen.SCALE, this.getHeight()/GameScreen.SCALE); 
        
        }else if(this.hasPower() && hitTimer>0){
        	batch.draw(currentFrame, lowerBody.getPosition().x - bodyw - 50/GameScreen.SCALE, lowerBody.getPosition().y - bodyh, this.getWidth()/GameScreen.SCALE, this.getHeight()/GameScreen.SCALE); 
        }else{
        	batch.draw(currentFrame, lowerBody.getPosition().x - bodyw, lowerBody.getPosition().y - bodyh, this.getWidth()/GameScreen.SCALE, this.getHeight()/GameScreen.SCALE); 
        }
        }
     }

	
	public void onBus(boolean s){		
		_onBus = s;
	}

	public boolean onBus(){
		return _onBus;
	}
	
	public void onRoad(boolean s){
		onRoad = s;
	}

	public boolean onRoad(){
		return onRoad;
	}
	
	public void pause() {
		manStat = Stat.PAUSE;		
	}
	
	public void resume(){
		manStat = Stat.RUN;
	}
	
	public void onCollisionWith(BodyData a){
		if(a.getId() == Constant.BODY_BUS){
			if(this.superPower){
				manStat = Stat.SUPER;
				hitTimer = .5f;
			}
			this.onBus(true);
		}else if(a.getId() == Constant.BODY_POLICE){
			if(this.superPower){
				manStat = Stat.SUPER;
				hitTimer = .5f;
			}
			this.onBus(true);
		}else if(a.getId() == Constant.BODY_ROAD){
			this.onRoad(true);
		}else if(a.getId() == Constant.BODY_COIN){
			a.setKilled(true);
			if(this.hasPower() == false){
        		RunnerGame.score.incCoin();
        		AssetsCommon.TICK_SOUND.play(0.7f);
        	}
		}else{
			if(this.superPower){
				manStat = Stat.SUPER;
				hitTimer = .5f;
			}else{
				lowerBody.setLinearVelocity(20, 20);
				fixture.setSensor(true);
				manStat = Stat.TODIE;
			}
		}
	}
	
	public void onCollisionEndWith(BodyData a){
		if(a.getId() == Constant.BODY_BUS || a.getId() == Constant.BODY_POLICE){
			this.onBus(false);
		}else if(a.getId() == Constant.BODY_ROAD){
			this.onRoad(false);
		}		
	}
}