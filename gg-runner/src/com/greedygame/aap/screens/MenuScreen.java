package com.greedygame.aap.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.greedygame.AbstractScreen;
import com.greedygame.Assets;
import com.greedygame.aap.RunnerGame;
import com.greedygame.app.dialog.MainDialog;
import com.greedygame.app.dialog.SupportDialog;
import com.greedygame.app.dialog.WindowDialogListener;

public class MenuScreen extends AbstractScreen implements WindowDialogListener {
	private Stage stage;
	private Image screenBg;
	
	private MainDialog menuWindow;
	private SupportDialog supportWindow;
	public MenuScreen(RunnerGame myGame){
		super(myGame);		
        Assets.init();
	}
	
    @Override
    public void show() {    	
        stage = new Stage();
        screenBg = new Image(Assets.BACK_DROP);          
		/*menuDialog =  new DialogBox(this, Assets.MENUBOX, "Main Menu"); 
		supportDialog = new DialogBox(this, Assets.SUPPORTBOX, "Support Menu"); */
        stage.addActor(screenBg);
        //stage.addActor(menuDialog); 
        //stage.addActor(supportDialog); 
        
        menuWindow = new MainDialog(this, stage);
        supportWindow = new SupportDialog(this, stage);
        Gdx.input.setCatchBackKey(true);  


        menuWindow.show();

    }

    
  
    
    @Override
    public void resize(int width, int height) {
		// Set our screen to always be 960 x XXX in size
		height = (int) (RunnerGame.VIRTUAL_WIDTH * height/width);
		width = (int) RunnerGame.VIRTUAL_WIDTH;
		//menuDialog.centerAlign(width, height);
		//supportDialog.centerAlign(width, height);
        stage.setViewport(width , height, true);
        //dialogTable.setSize(width, height);
        menuWindow.setPosition((width-menuWindow.getWidth())/2, (height-menuWindow.getHeight())/2);
        supportWindow.setPosition((width-menuWindow.getWidth())/2, (height-menuWindow.getHeight())/2);
    }

    boolean keyDown = false;
    
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        // Show the loading screen
        stage.act();
        stage.draw();
        
        
        if( (Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK))){
        	if(keyDown == false){
	        	keyDown = true;
	        	if(supportWindow.isVisible()){
	               	supportWindow.hide();
	               	menuWindow.show();
	        	}else if(menuWindow.isVisible()){
	        		 Gdx.app.exit();
	        	}
        	}
        }else{
        	keyDown = false;
        }
    }

    @Override
    public void hide() {
    	Gdx.input.setInputProcessor(null);    	
    }

	@Override
	public void onButtonClicked(String name) {
		if(name == "play"){
			//play
			game.setScreen(new GameScreen(game)); 
		}else if(name == "support"){
			//support
			menuWindow.hide();
			supportWindow.show();				

		}else if(name == "instruction"){
			//instructions
			game.setScreen(new InstructionScreen(game)); 
		}else if(name == "donate"){
			//donate
			Gdx.net.openURI("https://donate.aamaadmiparty.org");
		}else if(name == "vote"){
			//vote
			Gdx.net.openURI("http://greedygame.com/aaprunner");
		}else if(name == "share"){
			//share
			Gdx.net.openURI("http://m.facebook.com/sharer.php?u=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dcom.greedygame.runner");
		}		
	}

	@Override
	public void onHide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		
	}

}
