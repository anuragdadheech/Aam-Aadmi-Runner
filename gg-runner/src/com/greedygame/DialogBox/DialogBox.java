package com.greedygame.DialogBox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.greedygame.aap.RunnerGame;
import com.greedygame.app.buttons.Button;

public class DialogBox extends Table {
	private AtlasRegion texture;
	private boolean visible = false;
	private DialogBoxListener dialogListener;
	private String title="";
	
	private Button button1, button2, button3;
	
	public DialogBox(DialogBoxListener dbl, AtlasRegion texture, String t){
		this.dialogListener = dbl;
		this.texture = texture;
		this.title = t;
		this.setSize(texture.getRegionWidth(), texture.getRegionHeight()); 
		float x = (RunnerGame.VIRTUAL_WIDTH - texture.getRegionWidth())/2;
		float y = (RunnerGame.VIRTUAL_HEIGHT - texture.getRegionHeight())/2;
		this.setPosition(x, y);  
	
		this.button1 = new Button(x-83, y-7, 128, 128, 0);
		this.button2 = new Button(x+117, y-7, 128, 128, 1);
		this.button3 = new Button(x+312, y-7, 128, 128, 2);
		
		this.addActor(button1);
		this.addActor(button2);
		this.addActor(button3);

		addButtonListener();
	}
	
	public void centerAlign(float width, float height){
		float x = (width - texture.getRegionWidth())/2;
		float y = (height - texture.getRegionHeight())/2;
		this.setPosition(x, y);  
	}

	private  InputProcessor restoreinput;
	
	public void show(){
		if(visible == false){
			restoreinput = Gdx.input.getInputProcessor();
			Gdx.input.setInputProcessor(this.getStage());		
			visible = true;
		}
	}
	
	public void hide(){
		if(visible == true){
			Gdx.input.setInputProcessor(restoreinput);
			visible = false;
		}
	}
	@Override
	public boolean isVisible(){
		return visible;		
	}
	
	@Override
	public void act(float delta){
		super.act(delta);

	}

public void addButtonListener(){
	button1.addCaptureListener(new  ClickListener(){
       public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button){
    	   dialogListener.onButtonClicked(button1.getId());
           return false;
       }
   });
	button2.addCaptureListener(new  ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button){
        	dialogListener.onButtonClicked(button2.getId());                
            return false;
        }
    });
	
	button3.addCaptureListener(new  ClickListener(){
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int    button){
        	dialogListener.onButtonClicked(button3.getId());
            return false;
        }
    });
}

	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(visible){
			batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a);	
			batch.draw(this.texture, getX(), getY(), getWidth(), getHeight());	
			
			//TextBounds TextBound = Assets.Font.getBounds(this.title);
			
			
			//Assets.Font.draw(batch, this.title, Gdx.graphics.getWidth()/2-TextBound.width/2, this.getX()+this.getHeight());
			super.draw(batch, parentAlpha);
		}
	}


}
