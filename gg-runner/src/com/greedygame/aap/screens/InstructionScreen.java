package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
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
import com.greedygame.assets.AssetsComicAll;
import com.greedygame.assets.AssetsCommon;

public class InstructionScreen extends AbstractScreen implements GestureListener, InputProcessor  {
	private Stage stage;
	private Image screenBg;
	private Image instruction1,instruction2,instruction3, meter, instruction4,instruction5,instruction6, meter2;
	private int level;
    private float number = 0;
	public InstructionScreen(RunnerGame myGame){
		super(myGame);
	}
	
    @Override
    public void show() {    	
        stage = new Stage();
        screenBg = new Image(AssetsCommon.BACK_DROP);
        instruction1 = new Image(AssetsComicAll.COMIC.get(0));        
        instruction2 = new Image(AssetsComicAll.COMIC.get(1));       
        instruction3 = new Image(AssetsComicAll.COMIC.get(2));
        instruction4 = new Image(AssetsComicAll.COMIC2.get(0));        
        instruction5 = new Image(AssetsComicAll.COMIC2.get(1));       
        instruction6 = new Image(AssetsComicAll.COMIC2.get(2));
        meter = new Image(AssetsComicAll.METER); 
        meter2 = new Image(AssetsComicAll.METER2); 
        stage.addActor(screenBg);
        stage.addActor(instruction1);
        stage.addActor(instruction2);
        stage.addActor(instruction3);
        stage.addActor(meter);
        stage.addActor(instruction4);
        stage.addActor(instruction5);
        stage.addActor(instruction6);
        stage.addActor(meter2);

        instruction2.setVisible(false);
        instruction3.setVisible(false);
        meter.setVisible(false);
        instruction4.setVisible(false);
        instruction5.setVisible(false);
        instruction6.setVisible(false);
        meter2.setVisible(false);
        
        init();

    }

	@Override
	public void init() {
        InputMultiplexer im = new InputMultiplexer(this);
		im.addProcessor(new GestureDetector(this));
		Gdx.input.setInputProcessor(im);	
        Gdx.input.setCatchBackKey(true);
        game.analyticsEngine.sendView("InstructionScreen");
        number = 0;		
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

        instruction4.setPosition( (width - instruction4.getWidth())/2, (height - instruction4.getHeight())/2);
        instruction5.setPosition( (width - instruction5.getWidth())/2, (height - instruction5.getHeight())/2);
        instruction6.setPosition( (width - instruction6.getWidth())/2, 0);
        meter2.setPosition(0, height - meter2.getHeight());
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        // Show the loading screen
        stage.act();
        stage.draw(); 
        if(number==0){
        	instruction1.setVisible(true);
            instruction2.setVisible(false);
        }else if(number==1){
        	instruction1.setVisible(false);
            instruction2.setVisible(true);
            instruction3.setVisible(false);
            meter.setVisible(false);
        }else if(number==2){
            instruction2.setVisible(false);
            instruction3.setVisible(true);
            instruction4.setVisible(false);
            meter.setVisible(true);
        }else if(number==3){
            instruction3.setVisible(false);
            instruction4.setVisible(true);
            meter.setVisible(false);

            instruction5.setVisible(false);
        }else if(number==4){
        	instruction4.setVisible(false);
            instruction5.setVisible(true);
            instruction6.setVisible(false);
            meter2.setVisible(false);
        }else if(number==5){
            instruction5.setVisible(false);
            instruction6.setVisible(true);
            meter2.setVisible(true);
        }
        else if(number>5){
        	game.setScreen(new LoadingScreen(game, Constant.SCREEN_MENU));        	
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.ESCAPE || keycode == Keys.BACK){
			number--;
			if(number<0){
				this.game.setScreen(new MenuScreen(game));
			}
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
