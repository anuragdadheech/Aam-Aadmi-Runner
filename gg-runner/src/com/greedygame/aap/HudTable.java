package com.greedygame.aap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.greedygame.ScoreoidConstants;
import com.greedygame.aap.actors.CommonMan;
import com.greedygame.aap.actors.SuperBar;
import com.greedygame.aap.screens.SelectScreen;
import com.greedygame.assets.AssetsCommon;
import com.greedygame.assets.AssetsLevel1;
import com.greedygame.facebook.FacebookInterface;

public class HudTable extends Table {
	private SuperBar superBar;	
    private float width, height;
    private long highscore;
    private CommonMan man;
    private TextureRegion bar1Texture, bar2Texture, pause , offline, online;
	private TextureRegion crowdTexture;
	private Image celebration;
	private Button button;
	private boolean isPause = false;
	private int level;

	public HudTable(RunnerTable runnerTable,float width, float height) {
		level = runnerTable.level;
		man = runnerTable.getMan();		
		this.width = width;
		this.height = height;	
		Vector2 pos = new Vector2(width,height);
		superBar = new SuperBar(pos, level);
		this.addActor(superBar);
		highscore = RunnerGame.setting.getLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE);
		bar1Texture = AssetsCommon.BAR1;
		bar2Texture = AssetsCommon.BAR2;
		pause = AssetsCommon.PAUSE;
		offline = AssetsCommon.OFFLINE;
		online = AssetsCommon.ONLINE;
		button = new Button(new Image(AssetsCommon.PAUSE), AssetsCommon.SKIN);

		button.setPosition(this.width-this.pause.getRegionWidth(), this.height-this.pause.getRegionHeight()-15);
		this.addActor(button);
		this.setSize(width, height);
		button.addCaptureListener(new  ClickListener(){
		     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
		    	 return false;
		 	}
		});

		if(level==1){
			final RunnerGame game = runnerTable.gameScreen.game;
			this.celebration = new Image(AssetsLevel1.CELEBRATION);
			this.celebration.addCaptureListener(new  ClickListener(){
			     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
			    	 RunnerGame.facebook.public_action(FacebookInterface.FB_Action.LEVEL_1_COMPLETE);
			    	 AssetsCommon.BACKGROUND_MUSIC.pause();
			    	 game.setScreen(new SelectScreen(game));
			    	 return false;
			 	}
			});
		}		
		init();
	}
	
	/** Sets the width and height. */
	public void reset (float width, float height) {
		this.width = width;
		this.height = height;
		superBar.setPosition(width-superBar.getWidth()-pause.getRegionWidth()-22, height-superBar.getHeight()-24);
		button.setPosition(this.width-this.pause.getRegionWidth(), this.height-this.pause.getRegionHeight()-15);
		if(level==1){
			this.celebration.setPosition( (this.width-this.celebration.getWidth())/2, (this.height-this.celebration.getHeight())/2);
		}	
	}
	
	public void init(){
		RunnerGame.score.reset();
		superBar.reset();
		highscore = RunnerGame.setting.getLong(ScoreoidConstants.SCOREOID_BUNDLE_SCORE);
	}	
	
	@Override
	public void act(float delta) {
		if(isPause==false && man.isKilled() == false){
			super.act(delta);
			RunnerGame.score.incScore(0.1f);
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(this.bar1Texture, this.width-this.bar1Texture.getRegionWidth()+1, this.height-this.bar1Texture.getRegionHeight()-20);	
		batch.draw(this.bar1Texture, -1, this.height-this.bar1Texture.getRegionHeight()-20);
		batch.draw(this.bar2Texture, -3,  this.height-this.bar1Texture.getRegionHeight() - this.bar2Texture.getRegionHeight()-18);
		
		super.draw(batch, parentAlpha);
		
		if(this.isPause==false){
			if(level==2){
				if(man.hasPower()){
					superBar.buzz(true);
					superBar.update(man.powerleft());
				}else{		
					superBar.update(RunnerGame.score.percentCoin());
					if(RunnerGame.score.percentCoin() >= 1){
						man.startPower();
						RunnerGame.score.incCoin();  //To make it zero
					}
				}
			}else if(level==1){
				if(RunnerGame.score.percentBlock() >= 1){
					this.addActor(this.celebration);
					RunnerGame.score.setLevel(1);
					RunnerGame.setting.setInt("level", 1);
					RunnerGame.setting.saveSettings();
				}
				superBar.update(RunnerGame.score.percentBlock());				
			}
		}	
		
        TextBounds fontBound = AssetsCommon.FONT.getBounds("Score "+ (int)RunnerGame.score.getScore());

        AssetsCommon.FONT.setColor(Color.BLACK);
        if(level==1){
        	 int left = RunnerGame.score.getBlock();
             AssetsCommon.FONT.draw(batch, "Left "+ left, this.online.getRegionWidth()+10, this.height- fontBound.height + 0);
            
             if(RunnerGame.score.isConnected()){
     			batch.draw(this.online, 0, this.height-this.online.getRegionHeight()-16);
     			AssetsCommon.FONT.setColor(Color.BLUE);
     			AssetsCommon.FONT.draw(batch, "Online", 10, this.height - 80);
     			AssetsCommon.FONT.setColor(Color.CLEAR);
     		}else{
     			AssetsCommon.FONT.draw(batch, "Offline", 10, this.height - 80);
     			batch.draw(this.offline, 0, this.height-this.offline.getRegionHeight()-16);
     		}
            
             this.crowdTexture = AssetsLevel1.CROWD.get((int)(RunnerGame.score.percentBlock()*6));
 			batch.draw(this.crowdTexture, -5, 0);             
        }else{
        	AssetsCommon.FONT.draw(batch, "Score "+ (int)RunnerGame.score.getScore(), this.online.getRegionWidth()+10, this.height- fontBound.height + 0);
       		if(RunnerGame.score.isConnected()){
    			batch.draw(this.online, 0, this.height-this.online.getRegionHeight()-16);
    			AssetsCommon.FONT.setColor(Color.BLUE);
    			AssetsCommon.FONT.draw(batch, "High  "+ (int)highscore, 10, this.height - 80);
    			AssetsCommon.FONT.setColor(Color.CLEAR);
    		}else{
    			AssetsCommon.FONT.draw(batch, "Offline", 10, this.height - 80);
    			batch.draw(this.offline, 0, this.height-this.offline.getRegionHeight()-16);
    		}
        
        }
       



	}

	public void resume() {
		isPause = false;
		superBar.resume();		
	}

	public void pause() {
		isPause = true;
		superBar.pause();
		
	}

	public void restart() {
		init();
		
	}


}
