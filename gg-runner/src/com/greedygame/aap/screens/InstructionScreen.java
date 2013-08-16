package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.greedygame.AbstractScreen;
import com.greedygame.Assets;
import com.greedygame.aap.RunnerGame;

public class InstructionScreen extends AbstractScreen implements GestureListener {
	private Stage stage;
	private Image screenBg;
	private Image instruction1,instruction2,instruction3;

	public InstructionScreen(RunnerGame myGame){
		super(myGame);		
        Assets.init();
	}
	
    @Override
    public void show() {    	
        stage = new Stage();
        screenBg = new Image(Assets.BACK_DROP); 
 
        instruction1 = new Image(Assets.INSTRUCTION.get(0)); 
        instruction2 = new Image(Assets.INSTRUCTION.get(1)); 
        instruction3 = new Image(Assets.INSTRUCTION.get(2)); 
        

        stage.addActor(screenBg);
        stage.addActor(instruction1);
        stage.addActor(instruction2);
        stage.addActor(instruction3);
        
        instruction2.setVisible(false);
        instruction3.setVisible(false);
        
		Gdx.input.setInputProcessor(new GestureDetector(this));
    }

        
    @Override
    public void resize(int width, int height) {
		// Set our screen to always be 960 x XXX in size
		height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
		width = (int) RunnerGame.VIRTUAL_WIDTH;
        stage.setViewport(width , height, true);
        instruction1.setPosition( (width - instruction1.getWidth())/2, (height - instruction1.getHeight())/2);
        instruction2.setPosition( (width - instruction2.getWidth())/2, (height - instruction2.getHeight())/2);
        instruction3.setPosition( (width - instruction3.getWidth())/2, (height - instruction3.getHeight())/2);
        
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
            instruction3.setVisible(false);
        }
        if(number==2){
        	instruction1.setVisible(false);
            instruction2.setVisible(false);
            instruction3.setVisible(true);
        }
        
        if(number>2){
        	game.setScreen(new MenuScreen(game));
        }        
    }

    @Override
    public void hide() {
    	//Gdx.input.setInputProcessor(null);    	
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



}
