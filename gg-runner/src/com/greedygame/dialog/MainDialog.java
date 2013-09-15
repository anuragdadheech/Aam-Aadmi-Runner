package com.greedygame.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.greedygame.assets.AssetsCommon;

public class MainDialog {

	private Button playButton, instructionButton, supportButton;
	private Window window;
	private Stage stage;
	private WindowDialogListener dialogListener;
	
	public MainDialog(WindowDialogListener dL, Stage s){
		dialogListener = dL;
		stage = s;
		playButton = createButton("play", AssetsCommon.IconPlay);
		instructionButton = createButton("instruction", AssetsCommon.IconInstruction);
        supportButton = createButton("support", AssetsCommon.IconSupport);
        
        window = new Window("Main Menu", AssetsCommon.SKIN);
        window.setMovable(false);
        window.setModal(true);
        window.setSize(680, 350);
		window.padTop(100);
		window.row().expandX();
		window.padBottom(50);
		window.add(playButton);
		window.add(instructionButton);
		window.add(supportButton);

		stage.addActor(window);


		window.setVisible(false);
				
	}
	
	private void setButtonListener(){
		playButton.addCaptureListener(new  ClickListener(){
		     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
		    	   dialogListener.onButtonClicked(playButton.getName());
		           return false;
		      }
		});
		
		instructionButton.addCaptureListener(new  ClickListener(){
		     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
		    	   dialogListener.onButtonClicked(instructionButton.getName());
		           return false;
		      }
		});
		
		supportButton.addCaptureListener(new  ClickListener(){
		     public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button1){
		    	   dialogListener.onButtonClicked(supportButton.getName());
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
			setButtonListener();
			Gdx.input.setInputProcessor(stage);
			setButtonListener();
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
}
