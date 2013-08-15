package com.greedygame.aap;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.greedygame.Assets;
import com.greedygame.aap.actors.Bar;
import com.greedygame.aap.actors.Block;
import com.greedygame.aap.actors.Coin;
import com.greedygame.aap.actors.CommonMan;
import com.greedygame.aap.actors.InfiniteScrollBg;
import com.greedygame.aap.screens.GameScreen;

public class RunnerTable extends Table {
	
	private InfiniteScrollBg sky, road;
	private CommonMan man;
	private Bar bar;	
	private long lastBlockTime = 0;
	private long lastCoinTime = 0;
	private enum Stat {
		PAUSE, PLAY, OVER
    }
	private Array<Block> blocks;
	private Array<Coin> coins;	
	private Vector2 manPosition;
    private float width, height;    
    private int coin_collected = 0;
    private GameScreen gameScreen;
	
	public static Stat gameStat = Stat.PLAY;	
	public static float SPEED = 4.0f;    
	
	public RunnerTable(GameScreen g,float width, float height) {
		gameScreen = g;
		setBounds(0, 0, width, height);
		setClip(true);
			
		this.width = width;
		this.height = height;
		
		sky = new InfiniteScrollBg(Assets.DelhiSky.getRegionWidth(),0, Assets.DelhiSky, SPEED*4);
		this.addActor(sky);				
		
		road = new InfiniteScrollBg(Assets.DelhiHouse.getRegionWidth(),0, Assets.DelhiHouse, SPEED);
		this.addActor(road);	
		
		Vector2 pos = new Vector2(width,height);
		bar = new Bar(pos);
		this.addActor(bar);		
				
		manPosition = new Vector2(200, 130);		
		man = new CommonMan(manPosition);
		this.addActor(man);
		
		man.setZIndex(2);		
		
		blocks = new Array<Block>();
		coins = new Array<Coin>();
	}
	
	/** Sets the width and height. */
	public void setSize (float width, float height) {
		super.setSize(width, height);
		this.width = width;
		this.height = height;
		bar.setPosition(width-bar.getWidth()-5, height-bar.getHeight()-5);
	}
	
	public void restart(){
		gameScreen.SCORE = 0;
		coin_collected = 0;
		gameStat = Stat.PLAY;
		man.reset();
		sky.reset();
		road.reset();
		bar.reset();
		resetBlocks();		
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
		
		if(man.isKilled()){
			this.over();
			if(man.getActions().size==0){
				this.gameScreen.showRetry();
			}
		}else{
			
			gameScreen.SCORE += 0.1f;

			float r = MathUtils.random(1, 4);
			if (TimeUtils.nanoTime() - lastBlockTime > 1500000000*r) spawnBlock();
			
			r = MathUtils.random(1, 3);
			if (TimeUtils.nanoTime() - lastCoinTime > 1000000000*r) spawnCoin();
			

			Iterator<Block> iter = blocks.iterator();
			while (iter.hasNext()) {
				Block block = iter.next();
				if (block.getBounds().x + block.getWidth() <= 0) {
					iter.remove();
				}				
				block.addHitContactListner(man);			
			
				Iterator<Coin> coin_iter = coins.iterator();
				while (coin_iter.hasNext()) {
					Coin coin = coin_iter.next();
					
					if(coin.getBounds().overlaps(block.getBounds())){
						coin_iter.remove();
						removeActor(coin);
					}else{					
						if (coin.getBounds().x + coin.getWidth() <= 0) {
							coin_iter.remove();
							removeActor(coin);
						}
						
						if ( coin.getBounds().overlaps(man.getBounds())) {
							coin_iter.remove();	
							coin.remove();
			                Assets.Tick.play(0.1f);
			                if(man.hasPower()==false){
				                coin_collected++;
				                bar.update(coin_collected/10.0f);
				                if(coin_collected>=10){
				                	man.startPower();
					                bar.update(1);
				                	coin_collected = 0;
				                }	  
			                }	                
						}
					}
				}
			}
			
		}
	}
	

	private void spawnBlock() {
		Vector2 blockPos = new Vector2(Assets.DelhiHouse.getRegionWidth() ,this.manPosition.y);
		Block block = new Block(blockPos, 4.0f);
		addActor(block);		
		blocks.add(block);

		block.setZIndex(2);
		lastBlockTime = TimeUtils.nanoTime();
		
	}
	
	private void spawnCoin() {
		Vector2 blockPos = new Vector2(Assets.DelhiHouse.getRegionWidth() ,this.manPosition.y+20);
		Coin coin = new Coin(blockPos, 4.0f );
		addActor(coin);		
		coins.add(coin);
		lastCoinTime = TimeUtils.nanoTime();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {		
		super.draw(batch, parentAlpha);
        bar.buzz(man.hasPower());        
		Assets.Font.draw(batch, ""+ (int)gameScreen.SCORE, 10, this.height-5);
		
	}
	
	public void resetBlocks(){
		bar.clearActions();
		Iterator<Coin> coin_iter = coins.iterator();
		while (coin_iter.hasNext()) {
			Coin coin = coin_iter.next();
			coin.clearActions();
			coin_iter.remove();
			removeActor(coin);
		}
		
		Iterator<Block> block_iter = blocks.iterator();
		while (block_iter.hasNext()) {
			Block block = block_iter.next();
			block.clearActions();
			block_iter.remove();
			removeActor(block);
		}
	}
	
	public void over(){
		if(gameStat != Stat.OVER){
		gameStat = Stat.OVER;	
		Gdx.input.setInputProcessor(null);
		sky.clearActions();
		road.clearActions();		
		bar.clearActions();
		Iterator<Coin> coin_iter = coins.iterator();
		while (coin_iter.hasNext()) {
			Coin coin = coin_iter.next();
			coin.clearActions();
			removeActor(coin);
		}
		
		Iterator<Block> block_iter = blocks.iterator();
		while (block_iter.hasNext()) {
			Block block = block_iter.next();
			block.clearActions();
			//removeActor(block);
		}

		}
	}
	

}
