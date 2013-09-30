package com.greedygame.aap;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.greedygame.BodyData;
import com.greedygame.Constant;
import com.greedygame.aap.actors.Block;
import com.greedygame.aap.actors.Coin;
import com.greedygame.aap.actors.CommonMan;
import com.greedygame.aap.actors.InfiniteScrollBg;
import com.greedygame.aap.actors.Platform;
import com.greedygame.aap.screens.GameScreen;
import com.greedygame.assets.AssetsLevel1;
import com.greedygame.assets.AssetsLevel2;

public class RunnerTable extends Table {
	
	public static float SPEED = 0;
	public static float ACCELERATOR = 0;
	private InfiniteScrollBg sky, road;
	private CommonMan man;
	private Platform platform;
	private long lastBlockTime;
	private long lastCoinTime;
	private enum Stat {
		PAUSE, PLAY, OVER
    }
	
	private LinkedList<Block> blocks;
	private LinkedList<Coin> coins;	
	private Vector2 manPosition;  
    public GameScreen gameScreen;
	float blockleft;
	
	private Stat gameStat;	
	TextureRegion skyTexture = null,roadTexture = null;

    private World world;
    public int level;

    public int getLevel(){
    	return this.level;
    }
	public RunnerTable(GameScreen g,float width, float height , int level) {
		gameScreen = g;
		setBounds(0, 0, width, height);
		setClip(true);
			
		this.world = g.getWorld();
		
		this.level = level;
		if(level == 1){
			skyTexture = AssetsLevel1.DelhiSky;
			roadTexture =  AssetsLevel1.DelhiHouse;
		}else if(level == 2){
			skyTexture = AssetsLevel2.DelhiSky;
			roadTexture =  AssetsLevel2.DelhiHouse;
		}
		
		sky = new InfiniteScrollBg(this, skyTexture.getRegionWidth(),(RunnerGame.VIRTUAL_HEIGHT-skyTexture.getRegionHeight()), skyTexture);
		road = new InfiniteScrollBg(this, roadTexture.getRegionWidth(),0, roadTexture);
		this.addActor(sky);	
		this.addActor(road);		
		platform = new Platform(this);		
		platform.setName("road");
		manPosition = new Vector2(200, 130);		
		man = new CommonMan(this, manPosition);
		this.addActor(man);		
		man.setZIndex(2);		
		blocks = new LinkedList<Block>();
		coins = new LinkedList<Coin>();	
		createCollisionListener();	
		
		init();
	}
	
	private void init() {
		RunnerTable.SPEED = Constant.GAME_INIT_SPEED;
		RunnerTable.ACCELERATOR = Constant.GAME_INIT_ACCELERAION;
		this.lastBlockTime = 0;
		this.lastCoinTime = 0;
		RunnerGame.score.resetScore();
		RunnerGame.score.resetBlock();
		gameStat = Stat.PLAY;
		blockleft = Constant.BLOCK_TO_CLEAR_L1;
		blockcreated = 0;
	}
	
	/** Sets the width and height. */
	public void reset (float width, float height) {
		road.reset();
		sky.reset();
	}
	
	public void restart(){	
		man.reset();
		sky.reset();
		road.reset();
		resetBlocks();		 
		init();
		
	}
	
	float saveSpeed;
	float saveAcc;
	
	public void pause(){
		saveSpeed = SPEED;
		SPEED = 0;
		saveAcc = RunnerTable.ACCELERATOR;
		saveAcc = 0;
		gameStat = Stat.PAUSE;
		man.pause();
	}
	
	public void resume(){
		SPEED = saveSpeed;
		RunnerTable.ACCELERATOR = saveAcc;
		lastBlockTime = TimeUtils.nanoTime();
		gameStat = Stat.PLAY;
		man.resume();
	}

	public boolean isOver(){
		return gameStat == Stat.OVER;
	}

	public void jumpMan() {
		this.man.jump();
	}

	public void duckMan() {
		this.man.duck();	
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		road.getBody().setLinearVelocity(SPEED, 0);
		sky.getBody().setLinearVelocity(SPEED/4, 0);
		
		if(gameStat == Stat.PAUSE){
			return;
		}
		
		if(man.onBus()){
			Vector2 v = man.getBody().getLinearVelocity();
			man.getBody().setLinearVelocity(0, v.y);
		}
		
		if(man.isKilled()){
			this.over();
			this.gameScreen.showRetry();
		}else{
			
			SPEED += ACCELERATOR;
			
			float r = 1;
			
			if( (level == 1 && RunnerGame.score.percentBlock() < 1) || level==2){
				r = MathUtils.random(1, 4);
				if (TimeUtils.nanoTime() - lastBlockTime > 1500000000*r) spawnBlock();
			}
			
			if(level==2){
				r = MathUtils.random(1, 3);
				if (TimeUtils.nanoTime() - lastCoinTime > 1000000000*r) spawnCoin();
			}
			
		}
		
		Iterator<Block> iter = blocks.iterator();
		while (iter.hasNext()) {
			Block block = iter.next();
			if(block.getBodyPosition().x + block.getWidth()<0 || man.isKilled()){
				block.kill();
				iter.remove();
				blocks.remove(block);
				
				if(level==1 && man.isKilled() == false){
					RunnerGame.score.decBlock();
				}
			}else{
				block.setSensor(man.hasPower() && level != 1);
			}
		}
			
	}
	
	private void createCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
        		BodyData a = (BodyData) fixtureA.getBody().getUserData();
        		BodyData b = (BodyData) fixtureB.getBody().getUserData();
                if( a!= null && b!=null){
                	if(a.getActor()!=null && b.getActor()!=null){
                		a.getActor().onCollisionWith(b);
                		b.getActor().onCollisionWith(a);
                	}
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                BodyData a = (BodyData) fixtureA.getBody().getUserData();
                BodyData b = (BodyData) fixtureB.getBody().getUserData();
                
                if( a!= null && b!=null){
                	if(a.getActor()!=null && b.getActor()!=null){
                		a.getActor().onCollisionEndWith(b);
                		b.getActor().onCollisionEndWith(a);
                	}
                }

            }

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
        });
    }
	
	private int blockcreated;

	private void spawnBlock() {
		if(blockcreated < Constant.BLOCK_TO_CLEAR_L1 || level==2){
			Vector2 blockPos = new Vector2(roadTexture.getRegionWidth() ,this.manPosition.y);
			Block block = new Block(this, blockPos);
			addActor(block);		
			blocks.add(block);	
			block.setZIndex(2);
			lastBlockTime = TimeUtils.nanoTime();	
			blockcreated++;
		}
		
	}
	
	private void spawnCoin() {
		Vector2 blockPos = new Vector2(roadTexture.getRegionWidth() ,this.manPosition.y+20);
		Coin coin = new Coin(this, blockPos, level);
		addActor(coin);		
		coins.add(coin);
		lastCoinTime = TimeUtils.nanoTime();
	}
		
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {		
		super.draw(batch, parentAlpha);
	}
	
	public void resetBlocks(){
		Iterator<Coin> coin_iter = coins.iterator();
		while (coin_iter.hasNext()) {
			Coin coin = coin_iter.next();
			coin_iter.remove();
			coin.getBody().setActive(false);
			removeActor(coin);
		}
		
		Iterator<Block> block_iter = blocks.iterator();
		while (block_iter.hasNext()) {
			Block block = block_iter.next();
			block.clearActions();
			block_iter.remove();
			block.getBody().setActive(false);
			removeActor(block);
		}
	}
	
	public void over(){
		if(gameStat != Stat.OVER){
			gameStat = Stat.OVER;
			RunnerTable.ACCELERATOR = 0;
			RunnerTable.SPEED = -5f;
			
			Iterator<Coin> iter = coins.iterator();
			while (iter.hasNext()) {
				Coin coin = iter.next();
				coin.kill();
				iter.remove();
				coins.remove(coin);
			}
		}
	}
	
	public CommonMan getMan(){
		return this.man;
	}
	
	

}
