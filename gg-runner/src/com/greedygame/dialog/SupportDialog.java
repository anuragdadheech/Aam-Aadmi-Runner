package com.greedygame.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.greedygame.assets.AssetsCommon;

public class SupportDialog implements InputProcessor {

	private Button donateButton, voteButton, shareButton;
	private Window window;
	private Stage stage;
	private WindowDialogListener dialogListener;
	
	public SupportDialog(WindowDialogListener dL, Stage s){
		dialogListener = dL;
		stage = s;
		donateButton = createButton("donate", AssetsCommon.IconDonate);
		voteButton = createButton("vote", AssetsCommon.IconVote);
        shareButton = createButton("share", AssetsCommon.IconShare);
        
        window = new Window("Show Your Support", AssetsCommon.SKIN);
        window.setMovable(false);
        window.setModal(true);
        window.setSize(680, 350);
		window.padTop(100);
		window.row().expandX();
		window.padBottom(50);
		window.add(donateButton);
		window.add(voteButton);
		window.add(shareButton);

		stage.addActor(window);


		window.setVisible(false);

		setButtonListener();
		


		
	}
	
	private void setButtonListener(){
		donateButton.addCaptureListener(new  ClickListener(){
		     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
		    	   dialogListener.onButtonClicked(donateButton.getName());
		           return false;
		      }
		});
		
		voteButton.addCaptureListener(new  ClickListener(){
		     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
		    	 dialogListener.onButtonClicked(voteButton.getName());
		    	 return false;
		 	}
		});
		
		shareButton.addCaptureListener(new  ClickListener(){
		     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
		    	 dialogListener.onButtonClicked(shareButton.getName());
		    	 return false;
		 	}
		});
	}
	
	private InputProcessor parentInputProcessor;
	public void show(){
		if(window.isVisible() == false){
			float x = (stage.getWidth()-this.getWidth())/2;
			float y = (stage.getHeight()-this.getHeight())/2;
	        this.setPosition(x, y);
			window.setVisible(true);
			
			parentInputProcessor = Gdx.input.getInputProcessor();
			InputMultiplexer im = new InputMultiplexer(this);
			im.addProcessor(stage);
			Gdx.input.setInputProcessor(im);	
			
	        Gdx.input.setCatchBackKey(true);
		}
	}
	
	public void hide(){
		if(window.isVisible()){
			window.setVisible(false);
			Gdx.input.setInputProcessor(parentInputProcessor);
		}
	}
	
	private Button createButton(String name,TextureRegion texture){
		Button button = new Button(new Image(texture), AssetsCommon.SKIN);
        button.center();
        button.setSize(128, 128);
        button.setName(name);
        return button;
	}

	public void setPosition(float x,float y){
		window.setPosition(x, y);
	}

	public float getHeight() {
		return window.getHeight();
	}

	public float getWidth() {
		return window.getWidth();
	}

	public boolean isVisible() {
		return window.isVisible();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.ESCAPE || keycode == Keys.BACK){
			dialogListener.onCancelClicked("support");
			this.hide();
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
