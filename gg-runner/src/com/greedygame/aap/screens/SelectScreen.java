package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.greedygame.AbstractScreen;
import com.greedygame.Constant;
import com.greedygame.aap.RunnerGame;
import com.greedygame.assets.AssetsCommon;

import com.greedygame.facebook.FacebookInterface;

public class SelectScreen extends AbstractScreen implements InputProcessor {
   
	private Stage stage;
    private Image ButtonLevel1,ButtonLevel2,screenBg, lock;
    
    public SelectScreen(RunnerGame myGame) {
        super(myGame);
    }

    @Override
    public void show() {
    	stage = new Stage();
    	screenBg = new Image(AssetsCommon.BACK_DROP);  
        stage.addActor(screenBg);        
        ButtonLevel2 = new Image(AssetsCommon.IconLevel2);
        ButtonLevel1 = new Image(AssetsCommon.IconLevel1);
       
        stage.addActor(ButtonLevel1);
        stage.addActor(ButtonLevel2);

        game.analyticsEngine.sendView("selectLevelScreen");
                
        ButtonLevel1.addCaptureListener(new  ClickListener(){
		     public boolean touchDown(InputEvent event, float x, float y, int pointer, int button1){
		    	 RunnerGame.facebook.public_action(FacebookInterface.FB_Action.LEVEL_1);
		         game.setScreen(new LoadingScreen(game, Constant.SCREEN_COMIC1));
		    	 return false;
		      }
		});
        
        if(RunnerGame.score.getLevel()==1){
	        ButtonLevel2.addCaptureListener(new  ClickListener(){
			     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
			    	 RunnerGame.facebook.public_action(FacebookInterface.FB_Action.LEVEL_2);
			    	 game.setScreen(new LoadingScreen(game, Constant.SCREEN_COMIC2));
			         return false;
			      }
			});
        }else{
            lock = new Image(AssetsCommon.IconLock);
            stage.addActor(lock);
            lock.addCaptureListener(new  ClickListener(){
			     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
			    	 RunnerGame.facebook.inviteDialogLevel2();
			         return false;
			      }
			});
        }
        
        InputMultiplexer im = new InputMultiplexer(this);
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);	
        Gdx.input.setCatchBackKey(true);
    }
    
    @Override
    public void resize(int width, int height) {
		// Set our screen to always be 960 x XXX in size
		height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
		width = (int) RunnerGame.VIRTUAL_WIDTH;       
		stage.setViewport(width , height, true);
		
		float x = 40;
		float y = (stage.getHeight()-ButtonLevel1.getHeight())/2;
		ButtonLevel1.setPosition(x, y);
		
		x = stage.getWidth()-ButtonLevel2.getWidth()-40;
		y = (stage.getHeight()-ButtonLevel1.getHeight())/2;
		if(RunnerGame.score.getLevel()==0){
			lock.setPosition(x + (ButtonLevel2.getWidth() - lock.getWidth())/2 , y + (ButtonLevel2.getHeight() - lock.getHeight())/2);
		}
		ButtonLevel2.setPosition(stage.getWidth()-ButtonLevel2.getWidth()-40, (stage.getHeight()-ButtonLevel1.getHeight())/2);
	}
    
    boolean keyDown = false;
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();   
    
        if( (Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK))){
        	if(keyDown == false){
	        	keyDown = true;
	        	
        	}
        }else{
        	keyDown = false;
        }
        
        if(RunnerGame.score.getLevel() == 1 && lock!= null && lock.isVisible()){
        	lock.remove();
	        ButtonLevel2.addCaptureListener(new  ClickListener(){
			     public boolean touchDown(InputEvent event, float x, float y, int pointer, int button1){
			    	 RunnerGame.facebook.public_action(FacebookInterface.FB_Action.LEVEL_2);
			    	 game.setScreen(new LoadingScreen(game, Constant.SCREEN_COMIC2));
			         return false;
			      }
			});
        }
    }

    @Override
    public void hide() {
        // Dispose the loading AssetsCommon as we no longer need them
    }

	@Override
	public void pause() {
		// TODO Auto-generated method stub		
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.ESCAPE || keycode == Keys.BACK){
			this.game.setScreen(new MenuScreen(game));
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
