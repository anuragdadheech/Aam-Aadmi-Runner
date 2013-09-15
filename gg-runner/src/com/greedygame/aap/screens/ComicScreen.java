package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.greedygame.AbstractScreen;
import com.greedygame.Constant;
import com.greedygame.aap.RunnerGame;
import com.greedygame.assets.AssetsComic1;
import com.greedygame.assets.AssetsComic2;
import com.greedygame.assets.AssetsCommon;

public class ComicScreen extends AbstractScreen implements GestureListener {
	private Stage stage;
	private Image screenBg;
	private Image instruction1,instruction2,instruction3, meter;
	private int level;
	public ComicScreen(RunnerGame myGame, int level){
		super(myGame);		
		this.level = level;
	}
	
    @Override
    public void show() {    	
        stage = new Stage();
        screenBg = new Image(AssetsCommon.BACK_DROP); 
        if(level == 1){
	        instruction1 = new Image(AssetsComic1.COMIC.get(0));         
	        instruction2 = new Image(AssetsComic1.COMIC.get(1));         
	        instruction3 = new Image(AssetsComic1.COMIC.get(2)); 
	        meter = new Image(AssetsComic1.METER);
        }else if(level == 2){
	        instruction1 = new Image(AssetsComic2.COMIC.get(0));         
	        instruction2 = new Image(AssetsComic2.COMIC.get(1));         
	        instruction3 = new Image(AssetsComic2.COMIC.get(2)); 
	        meter = new Image(AssetsComic2.METER);       	
        }
        stage.addActor(screenBg);
        stage.addActor(instruction1);
        stage.addActor(instruction2);
        stage.addActor(instruction3);
        stage.addActor(meter);
        instruction2.setVisible(false);
        instruction3.setVisible(false);
        meter.setVisible(false);
        init();
    }
    
	@Override
	public void init() {
		Gdx.input.setInputProcessor(new GestureDetector(this));
        game.analyticsEngine.sendView("InstructionScreen");		
	}

        
    @Override
    public void resize(int width, int height) {
		// Set our screen to always be 960 x XXX in size
		height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
		width = (int) RunnerGame.VIRTUAL_WIDTH;
        stage.setViewport(width , height, true);
        instruction1.setPosition( (width - instruction1.getWidth())/2, (height - instruction1.getHeight())/2);
        instruction2.setPosition( (width - instruction2.getWidth())/2, (height - instruction2.getHeight())/2);
        instruction3.setPosition( (width - instruction3.getWidth())/2, 0);
        meter.setPosition(0, height - meter.getHeight());
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        // Show the loading screen
        stage.act();
        stage.draw();        
        if(number==1){
        	instruction1.setVisible(false);
            instruction2.setVisible(true);
        }else if(number==2){
        	instruction1.setVisible(false);
            instruction2.setVisible(false);
            instruction3.setVisible(true);
            meter.setVisible(true);
        }else if(number>2){
        	if(level == 1){
        		game.setScreen(new LoadingScreen(game, Constant.SCREEN_LEVEL1));
        	}else if(level == 2){
        		game.setScreen(new LoadingScreen(game, Constant.SCREEN_LEVEL2));
        	}
        }        
    }

    @Override
    public void hide() {
    	if(level == 1){
    		AssetsComic1.unload();
    	}else if(level == 2){
    		AssetsComic2.unload();
    	}
    }

    private float number = 0;
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		number++;
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
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
		
	}





}
